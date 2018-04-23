import datetime

from django import forms
from django.forms import ModelForm

from django.contrib.auth.models import User
from bins.models import *

import re  # regex matching


class BinStatusForm(forms.Form):
    geo = forms.CharField(max_length=100,
                                  required=True)
    status = forms.IntegerField(required=True,
                                   min_value=0, max_value=1)
    pk = forms.IntegerField(required=True)
    request = forms.IntegerField(required=True,
                                       min_value=0, max_value=1)
    lastPickUpTime = forms.CharField(max_length=200, required=True)

    def clean(self):
        cleaned_data = super(BinStatusForm, self).clean()

        pattern = r"(\-?\d+(\.\d+)?),\s*(\-?\d+(\.\d+)?)"
        sequence = self.cleaned_data.get('geo', '')
        # print 'coordinate: ', sequence
        if not re.match(pattern, sequence):
            # print 'invalid geoLocation'
            raise forms.ValidationError('Invalid geographic location.')
        # print 'valid geoLocation'
        print cleaned_data

        return cleaned_data

    # def clean_geoLocation(self):
    #     print 'validating geoLocation'

    #     pattern = r"^(\-?\d+(\.\d+)?),\s*(\-?\d+(\.\d+)?)$"
    #     sequence = self.cleaned_data['geo']
    #     if re.match(pattern, sequence):
    #         print 'valid geoLocation'
    #         return sequence
    #     else:
    #         print 'invalid geoLocation'
    #         raise forms.ValidationError('Invalid geographic location.')

    def clean_lastPickUpTime(self):
        def validateTime(date_text):
            try:
                if datetime.datetime.strptime(date_text, '%I:%M%p on %B %d, %Y'):
                    print 'regex does match'
                    return True
                else:
                    return False
            except ValueError:
                print 'regex does not match'
                return False

        sequence = self.cleaned_data['lastPickUpTime']
        if (sequence == 'None' or validateTime(sequence)):
            print 'valid lastPickUpTime'
            return sequence
        else:
            print 'invalid lastPickUpTime'
            raise forms.ValidationError('Invalid pickup time.')



class ReportDamageForm(forms.Form):
    binID = forms.IntegerField()
    geoLocation = forms.CharField(max_length=100)
    description = forms.CharField(max_length=400)
    def clean(self):
        cleaned_data = super(ReportDamageForm, self).clean()
        print 'cleaning', cleaned_data
        return cleaned_data

    def clean_binID(self):
        try:
            print 'cleaning binID'
            binID = self.cleaned_data['binID']
            return binID
        except KeyError:
            raise ValidationError('The binID field was blank.')
        

    def clean_description(self):
        try:
            print 'cleaning description'
            description = self.cleaned_data['description']
            return description
        except KeyError:
            raise ValidationError('The description field was blank.')

    def clean_geoLocation(self):
        try:
            print 'cleaning geoLocation'
            description = self.cleaned_data['geoLocation']
            pattern = r"^(\-?\d+(\.\d+)?),\s*(\-?\d+(\.\d+)?)$"
            if re.match(pattern, description):
                return description
            else:
                raise forms.ValidationError('Invalid geographic location.')
        except KeyError:
            raise ValidationError('The description field was blank.')