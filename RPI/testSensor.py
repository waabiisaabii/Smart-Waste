import RPi.GPIO as GPIO
import time
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
		time.sleep(0.1)
	else:
		print 'interrupt', i
		GPIO.output(14, False)
		GPIO.output(15, True)
		time.sleep(0.1)
GPIO.cleanup()
