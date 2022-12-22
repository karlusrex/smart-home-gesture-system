# Smart Home Gesture System IOT

Software to control multiple sensors in a smart home environment

## Hardware

- Raspberry Pi 4
- Webcam 
- Temperature Sensor
- Tellstick Znet
- Actuator
- Bluetooth speaker (or any kind of speaker)

## Software

- OpenCV
- gTTS (Google Text To Speech)
- Mediapipe 

## Setup

- Python 3.9.X

Dependencies:
- pip install opencv-python
- pip install mediapipe
- pip install gTTS
- pip install mpg321
- pip install python-dotenv

## .env file setup:

- 1. Create a file in root directory called .env
- 2. In the file create four variables called PUBKEY, PRIVKEY, TOKEN, SECRET - You can find these values at http://api.telldus.com/keys/generatePrivate

The .env file will look like the following:
PUBKEY =  "YOUR_PUBLIC_KEY"
PRIVKEY = "YOUR_PRIVKEY_KEY"
TOKEN =   "YOUR_TOKEN_KEY"
SECRET =  "YOUR_SECRET_KEY"
