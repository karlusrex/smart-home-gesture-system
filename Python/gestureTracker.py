# Import the necessary Packages for this software to run
import mediapipe
import cv2
from datetime import datetime, timedelta
from dataHandler import handleGestureRecognition

gestureBoolArray = [] #Contains 4 boolean values representing if finger is up or down. Representing [Index_Finger_Tip, Middle_Finger_Tip, Ring_Finger_Tip, Pinky_Tip]
#set is for windows / export for mac and linux

def setGestureBoolArray(gesture_bool_array):
    with open('./data/gesture_bool_array.txt', 'w') as file:
        file.write(str(gesture_bool_array))

def getGestureBoolArray():
    try:
        with open('./data/gesture_bool_array.txt', 'r') as file:
            gesture_bool_array = file.readline()
    except IOError:
        print("Error could not find gesture_bool_array.txt")

    return gesture_bool_array

def startGestureTracker():
    global gestureBoolArray
    handsModule = mediapipe.solutions.hands
    cap = cv2.VideoCapture(0, cv2.CAP_DSHOW)
    fourcc = cv2.VideoWriter_fourcc('m', 'p', '4', 'v')
    pause = datetime.now() + timedelta(seconds=1.0)
    with handsModule.Hands(static_image_mode=False, min_detection_confidence=0.7, min_tracking_confidence=0.7,
                           max_num_hands=1) as hands:
        while True:
            ret, frame = cap.read()
            frame1 = cv2.resize(frame, (640, 480))
            if datetime.now().second >= pause.second:
                results = hands.process(cv2.cvtColor(frame1, cv2.COLOR_BGR2RGB))
                if results.multi_hand_landmarks != None:
                    for handLandmarks in results.multi_hand_landmarks:
                        for i in [8, 12, 16, 20]:
                            finger = handLandmarks.landmark[i].y
                            reference = handLandmarks.landmark[i - 2].y
                            if finger < reference:
                                gestureBoolArray.append(1)
                            else: 
                                gestureBoolArray.append(0)
                        print(gestureBoolArray)
                        setGestureBoolArray(gestureBoolArray)
                        handleGestureRecognition(gestureBoolArray)
                        gestureBoolArray = []
                pause = datetime.now() + timedelta(seconds=1.0)
            cv2.imshow("Frame", frame1);
            key = cv2.waitKey(1) & 0xFF
            if key == ord("q"):
                break
