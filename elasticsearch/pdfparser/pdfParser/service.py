import os
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain_core.output_parsers import JsonOutputParser
from langchain_core.prompts import PromptTemplate
from langchain_community.document_loaders import PyPDFLoader


from pdfparser.pdfParser.model import CVDataExtraction

GOOGLE_API_KEY = "AIzaSyAYcFU4L_rOJf10MvyetmUgrg40KZpduGc"
os.environ["GOOGLE_API_KEY"] = GOOGLE_API_KEY


class PDFParserService:
    _instance = None

    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    llm = ChatGoogleGenerativeAI(model="gemini-1.5-flash")
    parser = JsonOutputParser(pydantic_object=CVDataExtraction)
    prompt = PromptTemplate(
        input_variables=["context"],
        partial_variables={"format_instructions": parser.get_format_instructions()},
        template="""Extract the information as specified, correct for good format, and replace new line with spacing.
            Format: {format_instructions}
            Context: {context}
            """
    )
    chain = prompt | llm | parser

    def parseCV(self, path):
        loader = PyPDFLoader(path)
        pages = loader.load()
        return self.chain.invoke({"context": pages})
