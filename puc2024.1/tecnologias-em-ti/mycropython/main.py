import machine
import time
from time import sleep
from machine import Pin

led=Pin(2, Pin.OUT)

for y in range(5):
    led.value(1)
    sleep(20)
    led.value(0)
    sleep(20) 