import pysher
import sys
import json
import time
# Add a logging handler so we can see the raw communication data
import logging
# root = logging.getLogger()
# root.setLevel(logging.INFO)
# ch = logging.StreamHandler(sys.stdout)
# root.addHandler(ch)

pusher = pysher.Pusher('fd3407f791a1d14b21f3','ap1',)

def  my_func(*args, **kwargs):
    print("processing Args:", args)
    print("processing Kwargs:", kwargs)
    message_dict = json.loads(args[0])
    # Now we have a dict of our message we can access the values by:
    print(message_dict)

# We can't subscribe until we've connected, so we use a callback handler
# to subscribe when able
def connect_handler(data):
    channel = pusher.subscribe('event_device123')
    channel.bind('event_device999', my_func)

pusher.connection.bind('pusher:connection_established', connect_handler)
pusher.connect()

while True:
    # Do other things in the meantime here...
    time.sleep(1)