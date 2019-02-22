
import time
import RPi.GPIO as GPIO
import json
import urllib3
import schedule
import time


class motorControl():

    GPIO.setmode(GPIO.BCM)
    GPIO.setup(4, GPIO.OUT)
    GPIO.setup(17, GPIO.OUT)
    GPIO.setup(27, GPIO.IN)

    def __init__(self):

        self.state = ''

    # def fuseStateJob(self):
    #     print("temporary func for checking state")
    #     schedule.every(3).seconds.do(self.checkFuseState)
    #     while True:
    #         schedule.run_pending()
    #         time.sleep(1)

    def setFuseState(self, data):
        self.state = data
        self.decoyMotor()

    def decoyMotor(self):

        gpiopins = [1, 2, 3, 4, 5]

        for i in range(len(gpiopins)):
            for y in self.state:
                if(y == "1"):
                    print ('on\n')
                    # GPIO.output(gpiopins[i], GPIO.LOW)
                    # time.sleep(1)
                else:
                    # GPIO.output(gpiopins[i], GPIO.HIGH)
                    # time.sleep(1)
                    print ('off\n')


    # def checkFuseState(self):
    #     http = urllib3.PoolManager()
    #     data = json.dumps({
    #     "error": False,
    #     "deviceid":"device123"
    # })

    #     r = http.request('POST', 'http://services.uni10smarthome.com/api/mobile/device/db/stat'
    #             ,headers={'Content-Type': 'application/json'},
    #              body=data)
    #     stat = json.loads(r.data)
    #     # print(stat['fuse_stat'])
    #     temp = []
    #     if(stat['error'] == False):
    #         if(stat['fuse_stat'] == True):
    #             print('NO UPDATE')
    #         else:
    #             print(stat['fuse_stat'])
    #             temp = stat['fuse_stat']
    #     else:
    #         print(stat['message'])

    # def motorControl(self,fuse,state):

    #     f1 = GPIO.PWM(4, 50)
    #     f2 = GPIO.PWM(17, 50)
    #     f1.start(2.5)
    #     f2.start(2.5)

    #     if(fuse == "f1"):
    #         if(state == 1):
    #             f1.ChangeDutyCycle(7.5)
    #             time.sleep(1)
    #             f1.ChangeDutyCycle(2.5)
    #         elif(state == 0):
    #             f1.ChangeDutyCycle(12.5)
    #             time.sleep(1)
    #             f1.ChangeDutyCycle(2.5)

    #     elif(fuse == "f1"):
    #         if(state == 1):
    #             f1.ChangeDutyCycle(7.5)
    #             time.sleep(1)
    #             f1.ChangeDutyCycle(2.5)
    #         elif(state == 0):
    #             f1.ChangeDutyCycle(12.5)
    #             time.sleep(1)
    #             f1.ChangeDutyCycle(2.5)
    #     else:
    #         print("ERR:FUSE NOT AVAILABLE")