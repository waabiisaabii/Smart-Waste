from django.conf.urls import url
from django.contrib import admin
from . import views

urlpatterns = [
    url(r'sensor', views.getSensorInfo),
    url(r'returnJSON', views.sendBackToPhone),
]
