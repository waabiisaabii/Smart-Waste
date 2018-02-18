# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse, Http404
from bins.models import *
# from bins.forms import *
from django.core import serializers
from django.views.decorators.csrf import csrf_exempt

@csrf_exempt
def getSensorInfo(requests):
	if (requests.method != 'POST'):
		raise Http404
	geoLocation = requests.POST.get('geo')
	binStatus = requests.POST.get('status')
	pk = requests.POST.get('pk')

	newBin = Bin.objects.get(binID=pk)
	newBin.binStatus = binStatus
	newBin.save()
	return HttpResponse('well', content_type='text/plain')

def sendBackToPhone(requests):
	allBins = Bin.objects.all()
	response_text = serializers.serialize("json", allBins)
	return HttpResponse(response_text, content_type="application/json")
