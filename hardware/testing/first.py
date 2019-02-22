import sys
# import RPi.GPIO as GPIO
import time
import schedule
import json
import urllib3
# import RPi.GPIO as GPIO


class deviceActivation:

    # GPIO.setmode(GPIO.BCM)
    # GPIO.setwarnings(False)
    # GPIO.setup(24, GPIO.OUT)
    # GPIO.setup(23, GPIO.OUT)
    # GPIO.output(24, GPIO.HIGH)
    # GPIO.output(23, GPIO.LOW)

    def __init__(self,deviceid):
        # 1  = ACTIVE , 0  = NOTACTIVE

        self.state = 0
        self.name = True
        self.deviceID = deviceid

    def checkDeviceState(self):
        schedule.every(3).seconds.do(self.checkServerJob)
        # print(self.getCurrentState())
        while self.getCurrentState():
            schedule.run_pending()
            time.sleep(1)

    def setCurrentState(self, status):
        self.name = status

    def getCurrentState(self):
        return self.name

    def checkServerJob(self):
        http = urllib3.PoolManager()
        print("Checking device activation status..")
        data = json.dumps({
            "error": False,
            "deviceid": self.deviceID
        })
        # https://staging.uni10smarthome.com/api/device/check
        # 192.168.43.84/smarthome/public/api/device/check
        r = http.request('POST', 'https://staging.uni10smarthome.com/api/device/check', headers={'Content-Type': 'application/json'},
                         body=data)
        stat = json.loads(r.data)
        if(stat['error'] == False):

            if(stat['status'] == 1):

                # GPIO.output(24, GPIO.LOW)
                # time.sleep(1)
                # GPIO.output(23, GPIO.HIGH)
                self.setCurrentState(False)
                print("Device is activated..")
                print("\nDevice checking task will be ended")
                schedule.clear()
            else:
                print("Device is not activated..")
                pass
        else:
            print(stat['message'])


from pusherNotify import pusherNotify
from deviceActivation import deviceActivation
from motorControl import motorControl
import time
import logging
# root = logging.getLogger()
# root.setLevel(logging.INFO)
# ch = logging.StreamHandler(sys.stdout)
# root.addHandler(ch)


try:

    # #init classes
    pushernotify = pusherNotify('fd3407f791a1d14b21f3', 'ap1', 'device123')
    activationserver = deviceActivation('device123')

    # check device activation status
    activationserver.checkDeviceState()

    while activationserver.getCurrentState() == True:

        if(activationserver.getCurrentState == False):
            break

    pushernotify.notifyServer()

    while True:
        time.sleep(1)


except KeyboardInterrupt:
    print("Program exit")
    # GPIO.cleanup()
