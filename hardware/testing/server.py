import websockets
import time
import asyncio
import RPi.GPIO as GPIO
import time


GPIO.setmode(GPIO.BCM)

GPIO.setup(4, GPIO.OUT)

p = GPIO.PWM(4, 50)
p.start(2.5)
try:

    async def server(websocket, path):

        while True:
            state = await websocket.recv()
            print(f"< {state}")

            if state == "1":
                p.ChangeDutyCycle(7.5)  
                time.sleep(1)  
                p.ChangeDutyCycle(2.5)                  
                await websocket.send("FUSE BUKAK")
            elif state == "0":
                p.ChangeDutyCycle(12.5) 
                time.sleep(1)  
                p.ChangeDutyCycle(2.5) 
                await websocket.send("FUSE TUTUP")
                
    print("PI WEB SERVER")
    start_server = websockets.serve(server, '192.168.43.134', 8765)

    asyncio.get_event_loop().run_until_complete(start_server)
    asyncio.get_event_loop().run_forever()

except KeyboardInterrupt:
    print("Program exit")
    p.stop()
    GPIO.cleanup()
