import schedule
import time
import urllib3
import json
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)

GPIO.setup(4, GPIO.OUT)
GPIO.setup(17, GPIO.OUT)

p = GPIO.PWM(4, 50)
p.start(2.5)
t = GPIO.PWM(17, 50)
t.start(2.5)

op = {"FUSE OPEN"}
cl = {"FUSE CLOSE"}

o = json.dumps(op)
c = json.dumps(cl)

print("ACTIVATION SCRIPT SERVER")

def stopcheck(self,stat):

    self.test = stat

def checkactivation():

    print("CHECK ACTIVATION")
    http = urllib3.PoolManager()
    r = http.request('GET', 'http://192.168.43.135/smartdbbox/api/public/api/device/check')
    stat = json.loads(r.data)

    if stat['status'] == '1':
        p.ChangeDutyCycle(7.5)  
        time.sleep(1)
        fuse1up = 1
        p.ChangeDutyCycle(2.5)
        print(o) 

    elif stat['status'] == '2':
        p.ChangeDutyCycle(12.5)
        time.sleep(1)
        fuse1down = 0
        p.ChangeDutyCycle(2.5)
        print(c)

    elif stat['status'] == '3':
        t.ChangeDutyCycle(7.5)  
        time.sleep(1)
        fuse2up = 1
        t.ChangeDutyCycle(2.5)
        print(o)

    elif stat['status'] == '4':
        t.ChangeDutyCycle(12.5) 
        time.sleep(1)
        fuse2down = 0
        t.ChangeDutyCycle(2.5)
        print(c)
    
    else:
        print(stat['status'])    

fuse1 = json.dumps({
    "Up":"1"
    
})

fuse2 = json.dumps({
    "Up":"1"
    
})

s = http.request('POST', 'http://192.168.43.135/smartdbbox/api/public/api/device/check', headers={'Content-Type': 'application/json'}, body=fuse1)

job = schedule.every(2).seconds.do(checkactivation)

while True:

    schedule.run_pending()
    if(checkactivation() == 1):
        schedule.cancel_job(checkactivation)
        schedule.clear
        print(job)
        print('JOB CANCEL')

   
    
    

   



    
