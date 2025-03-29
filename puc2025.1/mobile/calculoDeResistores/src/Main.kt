// Ler na documentação sobre null-safety
fun showInstructions(){
    println("Calculadora de resistores <<<<")
    println("Informe as 4 cores separadas por vírgula")
}

fun getColorsFromUser():String?{
        return readLine()
}

fun cleanResistorString(s: String):String{
    return s.replace(" ", "").uppercase()
}

fun getColorsList(cleanedString:String):List<String>{
    return cleanedString.split(",")
}

fun firstAndSecondResistorBand(colors:List<String>):Double{
    var mapOfColors = mapOf("PRETO" to 0, "MARROM" to 1, "VERMELHO" to 2, "LARANJA" to 3, "AMARELO" to 4, "VERDE" to 5, "AZUL" to 6, "VIOLETA" to 7, "CINZA" to 8, "BRANCO" to 9)
    var colorsTemp:List<String> = colors

    var sum:Double = 0.0

    for(color in colors.take(2)) {
        sum = mapOfColors[color]!! + (sum * 10)
    }

    return sum
}

fun multiplier(sum:Double, colors: List<String>): Double {
    var mapOfColors = mapOf("PRETO" to 0, "MARROM" to 1, "VERMELHO" to 2, "LARANJA" to 3, "AMARELO" to 4, "VERDE" to 5, "AZUL" to 6, "VIOLETA" to 7, "DOURADO" to 0.1, "PRATEADO" to 0.01)

    val colorName:String = colors[2]
    val valueColor = mapOfColors[colorName]

    var newSum = sum

    for(i in 1..valueColor as Int){
        newSum *= 10
    }

    return newSum
}

fun tolerance(sum:Double, colors:List<String>){
    var mapOfColors = mapOf("MARROM" to 1, "VERMELHO" to 2, "VERDE" to 0.5, "AZUL" to 0.25, "VIOLETA" to 0.1, "CINZA" to 0.05 ,"DOURADO" to 5, "PRATEADO" to 10)

    var tolerance = mapOfColors[colors[3]]
    println(tolerance)

    println("Resistor de $sum Ohms e $tolerance% de tolerancia")
}

fun main(){
    var resistorColors: List<String>

    showInstructions()
    var colors = getColorsFromUser()
    if (colors != null){
        resistorColors = getColorsList(cleanResistorString(colors))
        tolerance(multiplier(firstAndSecondResistorBand(resistorColors), resistorColors), resistorColors)
    }
}

// Strings são imutáveis por definição em java e kotlin