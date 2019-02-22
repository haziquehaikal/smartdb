import serial
import RPi.GPIO as GPIO
import os, time

GPIO.setmode(GPIO.BOARD)

port = serial.Serial("/dev/ttyAMA0", baudrate=9600, timeout=1)

port.write('AT'+'\r\n')
rcv = port.read(10)
print rcv
time.sleep(1)

port.write('ATE0'+'\r\n')
rcv = port.read(10)
print rcv
time.sleep(1)

port.write('AT+CMGF=1'+'\r\n')
rcv = port.read(10)
print rcv
time.sleep(1)

port.write('AT+CNMI=2,1,0,0,0'+'\r\n')
rcv = port.read(10)
print rcv
time.sleep(1)

port.write('AT+CMGS="0122475447"'+'\r\n')
rcv = port.read(10)
print rcv
time.sleep(1)

port.write('Test'+'\r\n')
rcv = port.read(10)
print rcv

port.write("\x1A")
for i in range(10):
    rcv = port.read(10)
    print rcv