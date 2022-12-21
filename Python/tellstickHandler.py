# -*- coding: utf-8 -*-
#!/usr/bin/env python
# This is where to insert your generated API keys (http://api.telldus.com/keys)

import os
from dotenv import load_dotenv
import requests, json, hashlib, uuid, time
from tts import speak

load_dotenv()

pubkey  = os.environ.get('PUBKEY')  # Public Key
privkey = os.environ.get('PRIVKEY') # Private Key
token   = os.environ.get('TOKEN')   # Token
secret  = os.environ.get('SECRET')  # Token Secret

localtime = time.localtime(time.time())
timestamp = str(time.mktime(localtime))
nonce = uuid.uuid4().hex
oauthSignature = (privkey + "%26" + secret)

debugging = False

#Plan to merge getResponse and deviceAction into one function
def getResponse(method):
    response = requests.get(
        url=f"https://pa-api.telldus.com/json/{method}",

        params={

            "includeValues": "1",

        },
            headers = {"Authorization": 'OAuth oauth_consumer_key="{pubkey}",oauth_nonce="{nonce}", oauth_signature="{oauthSignature}", oauth_signature_method="PLAINTEXT", oauth_timestamp="{timestamp}", oauth_token="{token}", oauth_version="1.0"'.format(pubkey=pubkey, nonce=nonce, oauthSignature=oauthSignature, timestamp=timestamp, token=token),
        },
    )
    responseData = response.json()
    if debugging:
        print(json.dumps(responseData, indent=4, sort_keys=True))

    return responseData

def deviceAction(action, deviceid):
    response = requests.get(
        url=f"https://pa-api.telldus.com/json/{action}",
        params={
            "includeValues": "1",
            "id": deviceid,
        },
            headers = {"Authorization": 'OAuth oauth_consumer_key="{pubkey}",oauth_nonce="{nonce}", oauth_signature="{oauthSignature}", oauth_signature_method="PLAINTEXT", oauth_timestamp="{timestamp}", oauth_token="{token}", oauth_version="1.0"'.format(pubkey=pubkey, nonce=nonce, oauthSignature=oauthSignature, timestamp=timestamp, token=token),
        },
    )
    responseData = response.json()
    if debugging:  
        print(json.dumps(responseData, indent=4, sort_keys=True))
    return responseData

def getDevices(): 
    deviceList = {}
    for device in getResponse("devices/list")["device"]:
        #Mapping device name to ID
        deviceList[device["id"]] = device["name"]
    return json.dumps(deviceList)

def getSensors():
    sensorDict = {}
    for sensor in getResponse("sensors/list")["sensor"]:
        #Mapping sensor ID to temperature
        sensorDict[sensor["id"]] = {
            "temp": sensor["temp"],
            "humidity": sensor["humidity"]
        }
    return json.dumps(sensorDict)