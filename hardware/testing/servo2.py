import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)

GPIO.setup(18, GPIO.OUT)

p = GPIO.PWM(18, 50)

p.start(7.5)
p.ChangeDutyCycle(7.5) 
time.sleep(1)
p.ChangeDutyCycle(2.5) 
time.sleep(1)
p.ChangeDutyCycle(12.5) 
p.stop()
GPIO.cleanup()