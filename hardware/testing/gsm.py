
import serial
import time
from threading import Thread, Lock
from curses import ascii

# Enable Serial Communication
ser = serial.Serial()
ser.port = "/dev/ttyAMA0"
ser.baudrate = 115200
ser.timeout = 1


def doRead(ser, lock):
    while True:

        lock.acquire()

        try:
            rcv = ser.readline().decode().strip('\n')
        except:
            pass
        else:
            while rcv != '':
                print(rcv)
                rcv = ser.readline().decode().strip('\n').strip('\r')
        lock.release()
        time.sleep(.15)

ser.open()
ser_lock = Lock()


th = Thread(target=doRead, args=(ser, ser_lock))
th.daemon = True
th.start()


gotlock = ser_lock.acquire()

ser.write(b'AT+CMGF=1\r')
ser.write(b'AT+CPMS="ME","SM","ME"\r')
ser_lock.release()
time.sleep(.15)


try:
    ser_lock.acquire()
except:
    time.sleep(.1)
else:
    ser.write(b'AT+CPIN?\r')
    ser_lock.release()
    time.sleep(.15)


while True:
    try:
        cmd = input()
    except:
        pass
    else:
        ser_lock.acquire()

        if '^z' in cmd:
            ser.write(bytes('{}\r'.format(ascii.ctrl('z')), 'utf-8'))
        else:
            ser.write(bytes('{}\r'.format(cmd), 'utf-8'))
        ser_lock.release()
        time.sleep(.15)