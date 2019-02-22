import time
import schedule
import json
import urllib3
# import RPi.GPIO as GPIO


class deviceActivation:

    # GPIO.setmode(GPIO.BCM)
    # GPIO.setwarnings(False)
    # GPIO.setup(18, GPIO.OUT)
    # GPIO.setup(4, GPIO.OUT)
    # GPIO.output(4, GPIO.HIGH)
    # GPIO.output(18, GPIO.LOW)

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
        
        r = http.request('POST', 'http://192.168.0.56/smarthome/public/api/device/check', headers={'Content-Type': 'application/json'},
                         body=data)
        stat = json.loads(r.data)
        if(stat['error'] == False):

            if(stat['status'] == 1):

                # GPIO.output(4, GPIO.LOW)
                # time.sleep(1)
                # GPIO.output(18, GPIO.HIGH)
                self.setCurrentState(False)
                print("Device is activated..")
                print("\nDevice checking task will be ended")
                schedule.clear()
            else:
                print("Device is not activated..")
                pass
        else:
            print(stat['message'])
