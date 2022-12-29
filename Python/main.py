import argparse
import dataHandler
import tellstickHandler
import gestureTracker

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--function", type=str, help="Name of the function to call")
    parser.add_argument("--args", type=str, nargs="*", help="Arguments to pass to the function")
    parser.add_argument("--start", help="Starts the gesture tracker", action="store_true")
    args = parser.parse_args()

    if args.start:
        gestureTracker.startGestureTracker()
    if args.function == "getDevices":
        devices = tellstickHandler.getDevices()
        print(devices)
    elif args.function == "getSensors":
        sensors = tellstickHandler.getSensors()
        print(sensors)
    elif args.function == "getGestureBoolArray":
        boolarray = gestureTracker.getGestureBoolArray()
        print(boolarray)
    elif args.function == "saveGesture":
        boolarray = [int(x) for x in args.args[0].strip('[]').split(',')]
        name = args.args[1]
        action = args.args[2]
        device = int(args.args[3])
        dataHandler.saveGesture(boolarray, name, action, device)
    elif args.function == "editGesture":
        oldname = args.args[0]
        boolarray = [int(x) for x in args.args[1].strip('[]').split(',')]
        newname = args.args[2]
        action = args.args[3]
        device = int(args.args[4])
        dataHandler.editGesture(oldname, boolarray, newname, action, device)
    elif args.function == "removeGesture":
        name = args.args[0]
        dataHandler.removeGesture(name)
        
if __name__ == "__main__":
    main()