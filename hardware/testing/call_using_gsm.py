import serial
import RPi.GPIO as GPIO
import os, time

GPIO.setmode(GPIO.BOARD)

port = serial.Serial("/dev/ttyAMA0", baudrate=9600, timeout=1)

port.write('AT'+'\r\n')
rcv = port.read(10)
print rcvtime.sleep(1)

port.write('AT+CMGF=1'+'\r\n')
rcv = port.read(10)
print rcv
time.sleep(1)

port.write('AT+CMGF=1'+'\r\n')
rcv = port.read(10)
print rcv
time.sleep(1)

port.write('AT+CMGF=1'+'\r\n')
rcv = port.read(10)
print rcv
time.sleep(1)

port.write('ATD0122475447;'+'\r\n')
rcv = port.read(10)
print rcvtime.sleep(1)
while(1):
    rcv = port.read(10)
    print rcv