import websockets
import asyncio
import ayam
from motorControl import motorControl
import json


class pusherNotify:

    def __init__(self, appKey, clusterId, deviceId):
        self.app_key = appKey
        self.cluster_id = clusterId
        self.device_id = deviceId
        self.pusher = ayam.Pusher(appKey, clusterId)
        self.haha = ''
        self.fusecontrol = motorControl()

    def callback(self, *args):

        # print(args[0])
        data = json.loads(args[0])
        self.fusecontrol.setFuseState(data['state'])

    def connect_handler(self, data):
        channel = self.pusher.subscribe('event_' + self.device_id)
        channel.bind('App\\Events\\updateFuseEvent', self.callback)

    def notifyServer(self):
        print("listen event from server")
        self.pusher.connection.bind(
            'pusher:connection_established', self.connect_handler)
        self.pusher.connect()

    def setval(self, data):
        self.haha = data

    def getval(self):
        return self.haha
