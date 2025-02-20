from langchain_core.pydantic_v1 import BaseModel, Field
from typing import List

class CVDataExtraction(BaseModel):
    name: str = Field(description="Name (Camel case)")
    email: str = Field(description="Email")
    birthday: str = Field(description="Birthday")
    phone: str = Field(description="Phone number (No spacing)")
    address: str = Field(description="Address")
    nation: str = Field(description="Nation (Guess based on Address)")
    profession: str = Field(description="Profession")
    socialNetworks: List[str] = Field(description="Social networks")
    aboutMe: str = Field(description="About me/Profile")
    education: str = Field(description="Education")
    skills: List[str] = Field(description="Skills")
    certifications: List[str] = Field(description="Certifications")
