# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse, Http404

def getSensorInfo(requests):
	if (requests.method != 'GET'):
		raise Http404
	


	return HttpResponse('well', content_type='text/plain')

def sendBackToPhone(requests):
	print 'hello'
	return HttpResponse('well', content_type='text/plain')
