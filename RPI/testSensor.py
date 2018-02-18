import RPi.GPIO as GPIO
import time
import requests

url = 'https://peaceful-island-64716.herokuapp.com/iot/sensor'

GPIO.setmode(GPIO.BCM)
GPIO.setup(3, GPIO.IN)
GPIO.setup(14, GPIO.OUT)
GPIO.setup(15, GPIO.OUT)

while True:
	i = GPIO.input(3)
	if i == 0:
		print 'no interrupt', i
		GPIO.output(14, True)
		GPIO.output(15, False)
		r = requests.get(url, params={'geo': 'geolocation', 
									'status': 1, 
									'pk': 2})
		print r.status_code
		time.sleep(0.1)
	else:
		print 'interrupt', i
		GPIO.output(14, False)
		GPIO.output(15, True)
		r = requests.get(url, params={'geo': 'geolocation', 
									'status': 1, 
									'pk': 2})
		print r.status_code
		time.sleep(0.1)
GPIO.cleanup()