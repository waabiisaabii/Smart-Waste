# import RPi.GPIO as GPIO
import time
import requests
import datetime
import re 
import csv
import numpy as np

url = 'https://peaceful-island-64716.herokuapp.com/iot/sensor'
FULL = 1
EMPTY = 0
# GEO_LOCATION = "40.442877, -79.943236" # cmu address (lat, lon)

locations = []
with open('coordinates.csv', 'rb') as csvfile:
	spamreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
	for row in spamreader:
		locations.append(row[0] + row[1])


INFRARED_SENSOR_PIN = 3
ACTIVATE_REQUEST = 23
DEACTIVATE_REQUEST = 2
RED_LED = 14
GREEN_LED = 15
YELLOW_LED = 18

SLEEP_TIME_IN_SECOND = 0.5

activateRequest = False
lastPickUpTime = 'None'

def sendAndDisplayData(green, red, status, 
	pk, requestUrgent, sleep, lastPickUpTime,
	location):
	# GPIO.output(RED_LED, red)
	# GPIO.output(GREEN_LED, green)
	r = requests.post(url, data={'geo': location, 
								'status': status, 
								'pk': pk,
								'request': requestUrgent,
								'lastPickUpTime': lastPickUpTime})
	status = 'empty' if status == 1 else 'full'
	print status, i, r.status_code, r.text, ' | activate', activateRequest, lastPickUpTime

	# pattern = r"(\-?\d+(\.\d+)?),\s*(\-?\d+(\.\d+)?)"
	# print 'a' if re.match(pattern, location) else '000'
	time.sleep(sleep)

while True:
	print '####################### next round #################'
	fullness = np.random.choice([0, 1], size=(len(locations),), p=[1./2, 1./2])
	for j in range(len(locations)):
		location = locations[j]
		i = fullness[j]

		isNotCollected = 0
		activateRequest |= (~1)
		activateRequest &= isNotCollected

		if (isNotCollected == 0):
			print 'isCollected!',
			lastPickUpTime = datetime.datetime.now().strftime("%I:%M%p on %B %d, %Y")
			print lastPickUpTime,

		print location
		sendAndDisplayData(green = True,
								red = False,
								status = i,
								pk = j,
								requestUrgent = activateRequest,
								sleep = SLEEP_TIME_IN_SECOND,
								lastPickUpTime = lastPickUpTime,
								location=location)