from pydantic import BaseModel

class ThesisRequest(BaseModel):
    report_url: str

class TopicRelevanceRequest(BaseModel):        
    report_url: str
    topic_title: str

class ImprovementRequest(BaseModel):
    report_url: str
    focus_area: str = "all"