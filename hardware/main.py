import sys
# import RPi.GPIO as GPIO

from pusherNotify import pusherNotify
from deviceActivation import deviceActivation
from motorControl import motorControl
import time
import logging
root = logging.getLogger()
root.setLevel(logging.INFO)
ch = logging.StreamHandler(sys.stdout)
root.addHandler(ch)


try:

    # #init classes
    pushernotify = pusherNotify('fd3407f791a1d14b21f3', 'ap1', 'device124')
    activationserver = deviceActivation('device124')
    # updatemotor = motorControl()

    # check device activation status
    activationserver.checkDeviceState()

    while activationserver.getCurrentState() == True:

        if(activationserver.getCurrentState == False):
            break

    pushernotify.notifyServer()

    while True:

        # updatemotor.obses()
        
        time.sleep(1)


except KeyboardInterrupt:
    print("Program exit")
    GPIO.cleanup()
