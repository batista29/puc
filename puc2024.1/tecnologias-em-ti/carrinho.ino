#include <Servo.h> 
const int SensorE = A4;
//PINO SENSOR ESQUERDO (ANALÓGICO)
const int SensorD = A5; 
//PINO SENSOR DIREITO (ANALÓGICO)
const int ServoE = 6; 
//PINO PWM SERVO ESQUERDO 
const int ServoD = 5; 
//PINO PWM SERVO DIREITO
int Linha = 200;
//VALOR DA LINHA(???)
Servo RodaE;
//SERVO ESQUERDO
Servo RodaD;
//SERVO DIREITO
void setup() {
  RodaE.attach(ServoE); 
  RodaD.attach(ServoD); 
  Serial.begin(9600); 
  } 
  void loop()  { 
    int LidoE = analogRead(SensorE); 
    int LidoD = analogRead(SensorD);
    if (LidoE >= Linha && LidoD >= Linha) { 
      //AMBOS DETECTAM A LINHA == VAI PARA FRENTE 
      RodaE.write(90);
      RodaD.write(90); }
      else if (LidoE >= Linha) 
      { //ESQUERDA DETECTA A LINHA == VAI PARA DIREITA
        RodaE.write(120); RodaD.write(120); 
        } else if (LidoD >= Linha) {
          //DIREITA DETECTA A LINHA == VAI PARA ESQUERDA
          RodaE.write(120); RodaD.write(120); }
          else { 
            //NENHUM DETECTA A LINHA == FICA PARADO 
            RodaE.write(90); 
            RodaD.write(90);
  }
}
