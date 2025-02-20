from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.conf import settings
import json
from datetime import datetime 
from elasticsearch import Elasticsearch

from langchain_community.vectorstores import ElasticsearchStore
from langchain.retrievers.multi_query import MultiQueryRetriever
from langchain_core.prompts import PromptTemplate
from langchain_community.retrievers import BM25Retriever
from langchain.retrievers import EnsembleRetriever
from langchain.schema import Document
from langchain_google_genai import GoogleGenerativeAI, GoogleGenerativeAIEmbeddings


class PostApiView(APIView):
    elastic_url = "https://my-elasticsearch-project-ce2d6d.es.ap-southeast-1.aws.elastic.cloud:443"
    elastic_index = "posts"
    elastic_client = Elasticsearch(elastic_url, api_key=settings.ELASTIC_API_KEY)
    llm = GoogleGenerativeAI(
        model="gemini-1.5-flash-latest",
        google_api_key=settings.GEMINI_API_KEY  
    )
    embedding = GoogleGenerativeAIEmbeddings(model="models/embedding-001", google_api_key=settings.GEMINI_API_KEY)
    store = ElasticsearchStore(es_connection=elastic_client, index_name=elastic_index, embedding=embedding)

    # def __init__(self): 
    #     elastic_client = Elasticsearch(self.elastic_url)
    #     elastic_client.indices.delete(index=self.elastic_index)
    def get(self, request, *args, **kwargs):
        results = []
        try:
            query = request.GET.get("search", "")
            id = request.GET.get("id") 
            categoryId = request.GET.get("categoryId")
            professionId = request.GET.get("professionId")
            professions = request.GET.get("professions")
            minSalary = request.GET.get("minSalary") 
            maxSalary = request.GET.get("maxSalary")
            provinceName = request.GET.get("provinceName") # Need to change from client
            workingFormat = request.GET.get("workingFormat")

            print(type(str(professions).split(",")), str(professions).split(","))
            filter = []
            if id is not None:
                filter.append({"term": {"metadata.id": id}})
            if categoryId is not None:
                filter.append({"term": {"metadata.category.id": categoryId}})
            if professionId is not None:
                filter.append({"term": {"metadata.profession.id": professionId}})
            if professions is not None:
                filter.append({
                    "terms": {
                        "metadata.profession.name.keyword": str(professions).split(","),
                    }
                })
            if minSalary is not None:
                filter.append({"range": {"metadata.minSalary": {"gte": minSalary}}})
            if maxSalary is not None:
                filter.append({"range": {"metadata.maxSalary": {"lte": maxSalary}}})
            if workingFormat is not None:
                filter.append({"match": {"metadata.workingFormat": {"query": workingFormat, "operator": "and"}}})
            if provinceName is not None:
                filter.append({"term": { "metadata.locations.provinceName.keyword": provinceName }})
            # filter.append({"range": {"metadata.applicationDeadline": {"gte": datetime.now().date()}}})
            print(filter)
            vector_retriever = self.store.as_retriever(
                search_kwargs={
                    'k': 15, # Need to passing specific numberOfPost from user
                    'fetch_k': 3000,
                    'filter': filter
                }
            )
            
            docs = self._get_all_data(filter=filter) 
            # docs = vector_retriever.invoke(" ") # Can be used if numberOfPost was Passed
            bm25_retriever = BM25Retriever.from_documents(docs)
            bm25_retriever.k = 15
            hybrid_retriever = EnsembleRetriever(
                retrievers=[bm25_retriever, vector_retriever], weights=[0.5, 0.5]
            )
            multi_query_retriever = MultiQueryRetriever.from_llm(
                retriever=hybrid_retriever,
                llm=self.llm,
                prompt=PromptTemplate(
                    input_variables=["question"],
                    template="""You are a helpful assistant that generates 5 job post titles based on a single job post title related to: {question}
                    Requirements:
                    1. Your job post titles must have the same language with input i've passed.
                    2. Find the job title and change it to a number of different but closely related names (within the same field of that job) without significantly changing the meaning of the original job title.
                    Return the list of job post title ( each element contains only alphabet and no bullet point, ...), no comments (Here are), no ordinal number.
                    Example:
                    If i pass `Lập trình viên` then your result like `['Developer', 'Lập trình viên backend JAVA', 'IT frontend', 'Lập trình nhúng', 'Lập trình Android']`
                    """,
                ),
                include_original=True
            )
            results = multi_query_retriever.invoke(query)
        except Exception as error:
            print('Caught this error: ' + repr(error))
        posts = list(map(lambda x: self._format_date_fields(x.metadata), results))
        return Response(posts, status=status.HTTP_200_OK)

    def post(self, request, *args, **kwargs):
        body_unicode = request.body.decode('utf-8')
        post = json.loads(body_unicode)
        post['applicationDeadline'] = datetime.strptime(post['applicationDeadline'], "%d/%m/%Y").date()
        post['createdAt'] = datetime.strptime(post['createdAt'], "%d/%m/%Y").date()
        docs = [Document(page_content=self._combine(post), metadata=post)]
        self.store.add_documents(docs)
        return Response(post, status=status.HTTP_201_CREATED) 

    def _combine(self, post):
        if "Cạnh tranh" in post['minSalaryString']: 
            return "{} {} {} {}".format(
                post['title'],
                post['profession']['name'],
                post['level']['name'],
                ", ".join([location['provinceName'] for location in post['locations']])
            )
        
        return "{} {} {} {}".format(
                post['title'],
                post['profession']['name'],
                post['level']['name'],
                ", ".join([location['provinceName'] for location in post['locations']])
            )


    

    def _get_all_data(self, filter):
        response = self.elastic_client.search(
            index=self.elastic_index,
            query={
                "bool": {
                    "filter": filter
                }
            },
            size=10000,
            scroll="1m"
        )
        scroll_id = response['_scroll_id']
        hits = response['hits']['hits']
        all_data = hits

        while True:
            scroll_response = self.elastic_client.scroll(scroll_id=scroll_id, scroll='1m')
            
            # Check if there are more results
            if not scroll_response['hits']['hits']:
                break
            
            # Append hits to the list
            all_data.extend(scroll_response['hits']['hits'])
        
        return [
            Document(
                page_content=data['_source']['text'],
                metadata=data['_source']['metadata']
            ) for data in all_data
        ]

    def _format_date_fields(self, post):
        date_fields = ['applicationDeadline', 'createdAt']
        for field in date_fields:
            if field in post:
                post[field] = datetime.strptime(post[field], '%Y-%m-%d').strftime('%d/%m/%Y')
        return post
