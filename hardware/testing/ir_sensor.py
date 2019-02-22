import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)

GPIO.setup(25, GPIO.IN)
GPIO.setup(4, GPIO.OUT)

p = GPIO.PWM(4, 50)
p.start(2.5)

while True:

    sensor = GPIO.input(25)

    if sensor == 1:
        print("1")
        time.sleep(0.1)
        p.ChangeDutyCycle(12.5) 
        time.sleep(1)  
        p.ChangeDutyCycle(2.5)
        time.sleep(1)
        p.ChangeDutyCycle(12.5) 

    elif sensor == 0:
        print("0")
        time.sleep(0.1)
        p.ChangeDutyCycle(2.5)
        time.sleep(1)
        p.ChangeDutyCycle(12.5) 