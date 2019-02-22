
import RPi.GPIO as GPIO
import time


GPIO.setmode(GPIO.BCM)

GPIO.setup(4, GPIO.OUT)

p = GPIO.PWM(4, 50)
p.start(2.5)

inp = 2

if inp == 2:  # unlock
    p.ChangeDutyCycle(2.5)
    time.sleep(1)
    p.ChangeDutyCycle(12.5)
    time.sleep(1)
    p.ChangeDutyCycle(2.5)