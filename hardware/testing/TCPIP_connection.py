#Note: Chane APN depending your GSM operator. In my case, I am using tunisiana.tunisiana
import serial
import RPi.GPIO as GPIO
import os, time

GPIO.setmode(GPIO.BOARD)

#Enable Serial Communication
ser = serial.Serial("\dev\ttyAMA0", baudrate=9600, timeout=1)

#Transmitting AT Commands to the Modem '\r\n' indicates the Enter key

ser.write('AT'+'\r\n')
rcv = ser.read(10)
print rcv
time.sleep(1)
#Verify whether the SIM card is ready
ser.write("AT+CPIN?\r")
msg=ser.read(128)
print(msg)
time.sleep(3)
#SHUT all existing Communication
ser.write("AT+CIPSHUT\r")
msg=ser.read(128)
print(msg)
time.sleep(3)
#configure the device for a single IP connection
ser.write("AT+CIPMUX=0\r")
msg=ser.read(128)
print(msg)
time.sleep(3)
#Activate GPRS service
ser
#sets the PDP context parameters
command="AT+CGDCONT=1"+","+'"IP"'+","'"internet.ooredoo.tn"\r'
ser.write(command.encode())
msg=ser.read(128)
print(msg)
time.sleep(3)
#connect to GPRS with APN=orange.fr username="" and password=""
ser.write('AT+CSTT="internet.ooredoo.tn":,"",""\r')
