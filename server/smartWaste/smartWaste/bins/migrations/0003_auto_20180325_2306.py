# -*- coding: utf-8 -*-
# Generated by Django 1.11.7 on 2018-03-25 23:06
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('bins', '0002_auto_20180325_2230'),
    ]

    operations = [
        migrations.AlterField(
            model_name='bin',
            name='binStatus',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='bin',
            name='geoLocation',
            field=models.TextField(default='None', max_length=420),
        ),
    ]