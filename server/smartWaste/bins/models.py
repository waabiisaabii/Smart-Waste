# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
class Bin(models.Model):
	binID = models.IntegerField()
	geoLocation = models.TextField(max_length=420)
	binStatus = models.IntegerField()
	def __unicode__(self):
		return "{0}, {1}, {2}".format(self.binID, self.geoLocation, self.binStatus)

