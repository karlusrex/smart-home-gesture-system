import json
def getGestures():
    with open('./smart-home-gesture-system/Python/data/data.json') as file:
        gestures = json.load(file)
        return gestures

def saveGesture(boolarray, name, action, device):
    gestures = getGestures()
    #Conflicting is true if you want to save the same gesture
    conflict = False
    for savedgesture in gestures:
        if boolarray == gestures[savedgesture]:
            conflict = True

    if not conflict:
        gestures[len(gestures) + 1] = {"array":boolarray, "name":name, "action":action, "device":device}
        with open('./smart-home-gesture-system/Python/data/data.json', "w") as file:
            json.dump(gestures, file)
