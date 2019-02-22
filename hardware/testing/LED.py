import RPi.GPIO as GPIO

GPIO.setmode (GPIO.BOARD)

GPIO.setup(29,GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(31,GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

GPIO.setup(11, GPIO.OUT)
GPIO.output(11,0)
GPIO.setup(13, GPIO.OUT)
GPIO.output(13,0)

try:
        while True:
                GPIO.output(11, GPIO.input(29))
                GPIO.output(13, GPIO.input(31))

except KeyboardInterrupt:
        GPIO.cleanup()