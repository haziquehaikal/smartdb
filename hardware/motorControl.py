
import time
# import RPi.GPIO as GPIO
import json
import urllib3
import schedule
import time


# GPIO.setmode(GPIO.BCM)
# GPIO.setup(23, GPIO.OUT)
# GPIO.setup(24, GPIO.OUT)
# GPIO.setup(22, GPIO.OUT)
# GPIO.setup(27, GPIO.IN)

# p = GPIO.PWM(22, 50)
# p.start(2.5)


class motorControl():

    def __init__(self):

        self.state = ''

    def fuseStateJob(self):
        print("temporary func for checking state")
        schedule.every(3).seconds.do(self.checkFuseState)
        while True:
            schedule.run_pending()
            time.sleep(1)

    def updateMotorState(self):

        print("update")

        # def checkServerJob(self):
        http = urllib3.PoolManager()
        print("Checking device activation status..")
        data = json.dumps({
            "error": False,
            "motostate": "1",
            "deviceid": "device124"
        })

        r = http.request('POST', 'https://staging.uni10smarthome.com/api/device/deviceshut', headers={'Content-Type': 'application/json'},
                         body=data)
                         

    def setFuseState(self, data):
        self.state = data
        self.decoyMotor()

    def obses(self):

        # sensor = GPIO.input(27)

        if(sensor == 0):
            print("0")
            time.sleep(0.1)
            # p.ChangeDutyCycle(12.5)
            self.updateMotorState()
            time.sleep(1)

        elif(sensor == 1):
            # print("1")
            time.sleep(0.1)
            p.ChangeDutyCycle(2.5)

            time.sleep(1)

    def decoyMotor(self):

        gpiopins = [23, 24, 25, 5, 6]

        # while True:

        for y in range(len(self.state)):

            if(self.state[y] == "1"):
                print('on\n')
                time.sleep(0.1)
                p.ChangeDutyCycle(12.5)
                time.sleep(1)

            else:
                print('off\n')
                time.sleep(0.1)
                p.ChangeDutyCycle(2.5)
                time.sleep(1)

        for y in range(len(self.state)):

            if(self.state[y] == "1"):
                print('on\n')
                GPIO.output(gpiopins[y], GPIO.HIGH)
                time.sleep(1)
            else:
                print('off\n')
                GPIO.output(gpiopins[y], GPIO.LOW)
                time.sleep(1)

        for i in range(len(gpiopins)):

            GPIO.output(gpiopins[i], GPIO.HIGH)
            time.sleep(1)

    def checkFuseState(self):
        http = urllib3.PoolManager()
        data = json.dumps({
        "error": False,
        "deviceid":"device123"
    })

        r = http.request('POST', 'http://services.uni10smarthome.com/api/mobile/device/db/stat'
                ,headers={'Content-Type': 'application/json'},
                 body=data)
        stat = json.loads(r.data)
        # print(stat['fuse_stat'])
        temp = []
        if(stat['error'] == False):
            if(stat['fuse_stat'] == True):
                print('NO UPDATE')
            else:
                print(stat['fuse_stat'])
                temp = stat['fuse_stat']
        else:
            print(stat['message'])