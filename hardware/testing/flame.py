#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import json
import urllib3


# GPIO SETUP
channel = 17
GPIO.setmode(GPIO.BCM)
GPIO.setup(channel, GPIO.IN)


def calluser():
    data = {'error': False, 'fire_detect': True}
    encoded_data = json.dumps(data).encode('utf-8')
    http = urllib3.PoolManager()
    r = http.request('GET', 'http://192.168.0.56/smartdbbox/api/public/api/notify/trigger')
    stat = json.loads(r.data)
    print(stat)
    #r = http.request(
    #'POST',
    #'http://192.168.0.56/smartdbbox/api/public/api/notify/trigger',
    #body=encoded_data,
    #headers={'Content-Type': 'application/json'})

def callback(channel):
    
    print ('flame detect')
    #print(json.loads(r.data))
 
#GPIO.add_event_detect(channel, GPIO.BOTH, bouncetime=300)  # let us know when the pin goes HIGH or LOW
#GPIO.add_event_callback(channel, callback)  # assign function to GPIO PIN, Run function on change
 

# infinite loop
while True:
    flag = GPIO.input(channel)
    if(flag == 0):
        print('fire alert')
        calluser()

    print(flag)
    time.sleep(1)
