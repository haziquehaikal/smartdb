import websockets
import RPi.GPIO as GPIO
import time
import asyncio

GPIO.setmode(GPIO.BCM)

GPIO.setup(4, GPIO.OUT)

p = GPIO.PWM(4, 50)

p.start(2.5)    

inp = 1

if inp == 1:  # lock
    p.ChangeDutyCycle(12.5)
    time.sleep(1)
    p.stop()

if inp == 2:  # unlock
    p.ChangeDutyCycle(2.5)
    time.sleep(1)
    p.stop() 


async def hello(websocket, path):
    name = await websocket.recv()
    print(f"< {name}")

    greeting = f"Hello {name}!"

    await websocket.send(greeting)
    print(f"> {greeting}")

start_server = websockets.serve(hello, 'localhost', 8765)

asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_forever()
