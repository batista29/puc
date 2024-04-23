int pino_vermelho = 4;
//int pino_verde = 
int tempo = 1000;

void setup() {
  pinMode(pino_vermelho,OUTPUT);
}

void loop() {
  digitalWrite(pino_vermelho,HIGH);

  delay(tempo);

  digitalWrite(4,LOW);

  delay(tempo);

}
