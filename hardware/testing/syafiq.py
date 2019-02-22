import urllib.request as urllib2
import urllib
import json
import urllib.parse
import base64






def convert():

    # print('convert')
    image = open('weh.jpg', 'rb')  # open binary file in read mode
    image_read = image.read()
    image_64_encode = base64.b64encode(image_read)
    strdata = str(image_64_encode).strip('b')
    trimdata = ''
    for line in strdata:
        trimdata += line.strip("'")
    send(trimdata)
    print('data:image/jpeg;base64,' + trimdata)


def send(dataa):
    url = 'http://smartsys.ml/upload.php'


    #prepare raw json data
    raw = {'deviceID':'frompyth','image_data':dataa}

    #encode raw data with json encoding
    data = json.dumps(raw).encode('utf8')

    #init post request
    req = urllib2.Request(url, data, {'Content-Type': 'application/json','User-Agent': 'Mozilla/5.0'})
    f = urllib2.urlopen(req)

    #read response from api 
    response = f.read()
    f.close()


#print (response)



convert()
