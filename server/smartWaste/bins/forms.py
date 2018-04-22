import datetime

from django import forms
from django.forms import ModelForm

from django.contrib.auth.models import User
from bins.models import *

import re  # regex matching


class BinStatusForm(forms.Form):
    geoLocation = forms.CharField(max_length=100,
                                  label="geoLocation",
                                  required=True)
    binStatus = forms.IntegerField(required=True,
                                   min_value=0, max_value=1)
    binID = forms.IntegerField(required=True)
    requestUrgent = forms.IntegerField(required=True,
                                       min_value=0, max_value=1)
    lastPickUpTime = forms.CharField(max_length=200, required=True)

    def clean(self):
        cleaned_data = super(BinStatusForm, self).clean()
        return cleaned_data

    def clean_geoLocation(self):
        pattern = r"^(\-?\d+(\.\d+)?),\s*(\-?\d+(\.\d+)?)$"
        sequence = self.cleaned_data['geo']
        if re.match(pattern, sequence):
            return sequence
        else:
            raise forms.ValidationError('Invalid geographic location.')

    def validateTime(date_text):
        try:
            datetime.datetime.strptime(date_text, '%I:%M%p on %B %d, %Y')
            return True
        except ValueError:
            return False

    def clean_lastPickUpTime(self):
        sequence = self.cleaned_data['lastPickUpTime']
        if (sequence == 'None' or self.validateTime(sequence)):
            return sequence
        else:
            raise forms.ValidationError('Invalid pickup time.')



class ReportDamageForm(forms.Form):
    binID = forms.IntegerField(required=True)
    geoLocation = forms.CharField(max_length=100,
                                  label="geoLocation",
                                  required=True)
    description = forms.CharField(max_length=400,
                                  label="description",
                                  required=True)
    def clean(self):
        cleaned_data = super(ReportDamageForm, self).clean()
        return cleaned_data

    def clean_geoLocation(self):
        pattern = r"^(\-?\d+(\.\d+)?),\s*(\-?\d+(\.\d+)?)$"
        sequence = self.cleaned_data['geoLocation']
        if re.match(pattern, sequence):
            return sequence
        else:
            raise forms.ValidationError('Invalid geographic location.')