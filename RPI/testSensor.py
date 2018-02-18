import RPi.GPIO as GPIO
import time
import requests

url = 'https://peaceful-island-64716.herokuapp.com/iot/sensor'
FULL = 1
EMPTY = 0

GPIO.setmode(GPIO.BCM)
GPIO.setup(3, GPIO.IN)
GPIO.setup(14, GPIO.OUT)
GPIO.setup(15, GPIO.OUT)

# dead loop
while True:
	i = GPIO.input(3)
	if i == 0:
		print 'empty', i
		GPIO.output(14, True)
		GPIO.output(15, False)
		r = requests.post(url, data={'geo': 'geolocation', 'status': EMPTY, 'pk': 2,})
		print r.status_code
		time.sleep(0.1)
	else:
		print 'full', i
		GPIO.output(14, False)
		GPIO.output(15, True)
		r = requests.post(url, data={'geo': 'geolocation', 'status': FULL, 'pk': 2,})
		print r.status_code
		time.sleep(0.1)
GPIO.cleanup()
