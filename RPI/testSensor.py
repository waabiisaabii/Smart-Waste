import RPi.GPIO as GPIO
import time
import requests
import datetime

url = 'https://peaceful-island-64716.herokuapp.com/iot/sensor'
FULL = 1
EMPTY = 0
GEO_LOCATION = "40.442877, -79.943236" # cmu address (lat, lon)

INFRARED_SENSOR_PIN = 3
ACTIVATE_REQUEST = 23
DEACTIVATE_REQUEST = 2
RED_LED = 14
GREEN_LED = 15
YELLOW_LED = 18

SLEEP_TIME_IN_SECOND = 0.5

GPIO.setmode(GPIO.BCM)
GPIO.setup(INFRARED_SENSOR_PIN, GPIO.IN) # infrared sensor
GPIO.setup(ACTIVATE_REQUEST, GPIO.IN, pull_up_down=GPIO.PUD_UP) # activating urgent request
GPIO.setup(DEACTIVATE_REQUEST, GPIO.IN, pull_up_down=GPIO.PUD_UP) # deactivating urgent request
GPIO.setup(RED_LED, GPIO.OUT) # bin status: red LED
GPIO.setup(15, GPIO.OUT) # bin status: green LED
GPIO.setup(18, GPIO.OUT) # urgent request: yellow LED

activateRequest = False
lastPickUpTime = 'None'

def sendAndDisplayData(green, red, status, pk, requestUrgent, sleep, lastPickUpTime):
	GPIO.output(RED_LED, red)
	GPIO.output(GREEN_LED, green)
	r = requests.post(url, data={'geo': GEO_LOCATION, 
								'status': status, 
								'pk': pk,
								'request': requestUrgent,
								'lastPickUpTime': lastPickUpTime})
	status = 'empty' if status == 1 else 'full'
	print status, i, r.status_code, 'activate', activateRequest, lastPickUpTime
	time.sleep(sleep)

while True:
	# if activated, yellow LED is on
	isNotCollected = GPIO.input(DEACTIVATE_REQUEST)
	activateRequest |= (~GPIO.input(ACTIVATE_REQUEST))
	activateRequest &= isNotCollected
	GPIO.output(YELLOW_LED, activateRequest)

	if (isNotCollected == 0):
		print 'isCollected!'
		lastPickUpTime = datetime.datetime.now().strftime("%I:%M%p on %B %d, %Y")

	i = GPIO.input(INFRARED_SENSOR_PIN)
	
	if i == 1:
		sendAndDisplayData(green = True,
							red = False,
							status = EMPTY,
							pk = 2,
							requestUrgent = activateRequest,
							sleep = SLEEP_TIME_IN_SECOND,
							lastPickUpTime = lastPickUpTime)
	else:
		sendAndDisplayData(green = False,
							red = True,
							status = FULL,
							pk = 2,
							requestUrgent = activateRequest,
							sleep = SLEEP_TIME_IN_SECOND,
							lastPickUpTime = lastPickUpTime)

GPIO.cleanup()