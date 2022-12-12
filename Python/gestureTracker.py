# Import the necessary Packages for this software to run
import mediapipe
import cv2
from datetime import datetime, timedelta

drawingModule = mediapipe.solutions.drawing_utils
handsModule = mediapipe.solutions.hands

gestureBoolArray = [] #Contains 4 boolean values representing if finger is up or down. Representing [Index_Finger_Tip, Middle_Finger_Tip, Ring_Finger_Tip, Pinky_Tip]

cap = cv2.VideoCapture(0, cv2.CAP_DSHOW)
fourcc = cv2.VideoWriter_fourcc('m', 'p', '4', 'v')

with handsModule.Hands(static_image_mode=False, min_detection_confidence=0.7, min_tracking_confidence=0.7,
                       max_num_hands=1) as hands:
    pause = datetime.now() + timedelta(seconds=2.0)
    
    while True:
        ret, frame = cap.read()

        frame1 = cv2.resize(frame, (640, 480))

        results = hands.process(cv2.cvtColor(frame1, cv2.COLOR_BGR2RGB))

        if results.multi_hand_landmarks != None and datetime.now().second >= pause.second:
            for handLandmarks in results.multi_hand_landmarks:
                drawingModule.draw_landmarks(frame1, handLandmarks, handsModule.HAND_CONNECTIONS)

                for i in [8, 12, 16, 20]:
                    finger = handLandmarks.landmark[i].y
                    reference = handLandmarks.landmark[i - 2].y
                    fingerName = handsModule.HandLandmark(i).name
                    if finger < reference:
                        gestureBoolArray.append(1)
                    else: 
                        gestureBoolArray.append(0)
                print(gestureBoolArray)
                gestureBoolArray = []
        
            pause = datetime.now() + timedelta(seconds=2.0)

        cv2.imshow("Frame", frame1);
        key = cv2.waitKey(1) & 0xFF

        if key == ord("q"):
            break

def getGestureBoolArray():
    return gestureBoolArray