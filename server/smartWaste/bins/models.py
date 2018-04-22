# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


# Create your models here.
class Bin(models.Model):
    binID = models.IntegerField()
    geoLocation = models.TextField(max_length=420, default='None')
    binStatus = models.IntegerField(default=0, )
    requestUrgent = models.IntegerField(default=0, )
    lastPickUpTime = models.TextField(max_length=420, default='None')

    def __unicode__(self):
        return "{0}, {1}, {2}".format(self.binID, self.geoLocation, self.binStatus)


class DamageReportModel(models.Model):
    binID = models.IntegerField()
    geoLocation = models.TextField(max_length=420, default='None')
    description = models.TextField(max_length=420, default='None')
    status = models.IntegerField()

    def __unicode__(self):
        return "{0}, ({1}): {2}, [{3}]".format(self.binID,
                                               self.geoLocation,
                                               self.description,
                                               self.status)
