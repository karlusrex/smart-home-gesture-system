import json
from tellstickHandler import getSensors, getDevices, deviceAction
from tts import speak

def handleGestureRecognition(boolArray):
    gestures = getGestures()
    for savedgesture in gestures:
        if boolArray == gestures[savedgesture]["array"]:
            deviceid = gestures[savedgesture]["device"]
            sensors = getSensors()
            for sensor in sensors:
                if deviceid == sensor:
                    temp = sensors[sensor]
                    speak("The temperature is" + temp + "degrees celsius")

            devices = getDevices()
            for device in devices:
                if device == deviceid:
                    action = gestures[savedgesture]["action"]
                    deviceAction(action, deviceid)

def getGestures():
    with open('./smart-home-gesture-system/Python/data/data.json') as file:
        gestures = json.load(file)
        return gestures

def saveGesture(boolarray, name, action, device):
    gestures = getGestures()
    # Conflicting is true if you want to save the same gesture
    conflict = False
    savedslots = []
    for savedgesture in gestures:
        # Get all the keys in the data.json file
        savedslots = gestures.keys()
        # Checks if the boolarray is saved, meaning the gesture is taken
        if boolarray == gestures[savedgesture]["array"]:
            conflict = True

    # Converts string values to integers
    intsavedslots = [int(stringint) for stringint in savedslots]
    # Sort the keys to be able to find if there is a slot missing
    intsavedslots = sorted(intsavedslots)

    empty = 0
    # Checks if there is missing slot by counting from 1 to len of gestures
    for n in range(1, len(gestures)):
        if n not in intsavedslots:
            # Found empty slot
            empty = n
            break

    if not conflict:
        # Default index
        index = 0
        if empty > 0:
            index = empty
        else:
            index = len(gestures) + 1
        gestures[index] = {"array": boolarray, "name": name, "action": action, "device": device}
        with open('./smart-home-gesture-system/Python/data/data.json', "w") as file:
            json.dump(gestures, file)

def editGesture(oldname, boolarray, newname, action, device):
    gestures = getGestures()
    editgesture = 0
    conflict = False
    for gesture in gestures:
        if gestures[gesture]["name"] == oldname:
            editgesture = gesture
        if gestures[gesture]["array"] == boolarray:
            conflict = True

    if not conflict:
        edit = gestures[editgesture]

        edit["array"] = boolarray
        edit["name"] = newname
        edit["action"] = action
        edit["device"] = device

        with open('./smart-home-gesture-system/Python/data/data.json', "w") as file:
            json.dump(gestures, file)

def removeGesture(name):
    gestures = getGestures()
    removegesture = 0
    for gesture in gestures:
        if gestures[gesture]["name"] == name:
            removegesture = gesture
    
    gestures.pop(removegesture)
    
    with open('./smart-home-gesture-system/Python/data/data.json', "w") as file:
        json.dump(gestures, file)