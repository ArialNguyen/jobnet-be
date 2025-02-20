from django.urls import re_path
from .views import (
    PDFParserAPIView
)

urlpatterns = [
    re_path(r'^/(?P<filename>[^/]+)$', PDFParserAPIView.as_view())
]