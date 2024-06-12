import eps32
from time import sleep
while True:
    temperatura_f = esp32.raw_temperature()
    print("Temperatura em F é", temperatura_f)