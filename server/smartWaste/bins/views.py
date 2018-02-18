# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse, Http404
from bins.models import *
# from bins.forms import *
from django.core import serializers

def getSensorInfo(requests):
	if (requests.method != 'GET'):
		raise Http404
	geoLocation = requests.get('geo')
	binStatus = requests.get('status')
	pk = requests.get('pk')

	newBin = Bin.objects.create(binID=pk,
		geoLocation=geoLocation,
		binStatus=binStatus)
	newBin.save()
	return HttpResponse('well', content_type='text/plain')

def sendBackToPhone(requests):
	allBins = Bin.objects.all()
	response_text = serializers.serialize("json", allBins)
	return HttpResponse(response_text, content_type="application/json")
