import os
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.parsers import FileUploadParser
from pdfparser.pdfParser.service import PDFParserService


class PDFParserAPIView(APIView):
    parser_classes = [FileUploadParser]
    
    pdfParserService = PDFParserService()

    def post(self, request, filename, format=None):
        file = request.data.get("file", None)

        if not file:
            return Response("No file found", status=status.HTTP_400_BAD_REQUEST)
        
        file_content = file.read()
        temp_path = f'/tmp/{filename}'
        with open(temp_path, 'wb') as temp_file:
            temp_file.write(file_content)

        json = self.pdfParserService.parseCV(temp_path)
        os.remove(temp_path)
        return Response(json, status=status.HTTP_200_OK)
