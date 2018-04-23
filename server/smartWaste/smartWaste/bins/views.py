# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse, Http404
from bins.models import *
from bins.forms import *
# from bins.forms import *
from django.core import serializers
from django.views.decorators.csrf import csrf_exempt


@csrf_exempt
def getSensorInfo(request):
    if (request.method != 'POST'):
        # raise Http404
        return HttpResponse('Not a POST method', content_type='text/plain')

    form = BinStatusForm(data=request.POST)
    print 'valid???: ', form.is_valid()
    if not form.is_valid():
        # raise Http404
        return HttpResponse('#### Invalid data', content_type='text/plain')


    # geoLocation = request.POST.get('geo')
    # binStatus = request.POST.get('status')
    # pk = request.POST.get('pk')
    # requestUrgent = request.POST.get('request')
    # lastPickUpTime = request.POST.get('lastPickUpTime')

    geoLocation = form.cleaned_data.get('geo')
    binStatus = form.cleaned_data.get('status')
    pk = form.cleaned_data.get('pk')
    requestUrgent = form.cleaned_data.get('request')
    lastPickUpTime = form.cleaned_data.get('lastPickUpTime')

    newBin, created = Bin.objects.get_or_create(binID=pk, )
    newBin.geoLocation = geoLocation
    newBin.binStatus = binStatus
    newBin.requestUrgent = requestUrgent
    newBin.lastPickUpTime = lastPickUpTime
    newBin.save()

    return HttpResponse('well', content_type='text/plain')


def sendBackToPhone(request):
    allBins = Bin.objects.all()
    response_text = serializers.serialize("json", allBins)
    return HttpResponse(response_text, content_type="application/json")

@csrf_exempt
def getDamageReportItem(request):
    if (request.method != 'POST'):
        return HttpResponse('Not a POST method', content_type='text/plain')

    form = ReportDamageForm(data=request.POST)

    if not form.is_valid():
        return HttpResponse('#### Invalid data', content_type='text/plain')

    geoLocation = form.cleaned_data.get('geoLocation')
    description = form.cleaned_data.get('description')
    binID = form.cleaned_data.get('binID')

    newReport = DamageReportModel.objects.create(binID=binID,
                                     geoLocation=geoLocation,
                                     description=description,
                                     status=0)
    newReport.save()
    return HttpResponse('Damage report saved', content_type='text/plain')

def returnAllDamageReports(request):
    allReports = DamageReportModel.objects.all()
    response_text = serializers.serialize("json", allReports)
    return HttpResponse(response_text, content_type="application/json")
