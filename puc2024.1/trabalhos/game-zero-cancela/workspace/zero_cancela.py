soma  =  0
ultimo  =  penultimo  =  antepenultimo  =  pre_antepenultimo =  0
aux_ultimo  =  aux_penultimo  =  aux_antepenultimo  = aux_pre_antepenultimo =  1
qtd_considerados  =  qtd_desconsiderados  =  qtd_numeros  =  0
print("Bem-vindo ao Jogo Zero Cancela!")
#ler número
atual  = int ( input ( "Digite um número:" ))
# laço de repetição
while ( atual  >=  0 ):
    # variáveis ​​que controlam os zeros que são digitados
    aux_pre_antepenultimo  =  aux_antepenultimo
    aux_antepenultimo  =  aux_penultimo
    aux_penultimo  =  aux_ultimo
    aux_ultimo  =  atual
  #atualiza as variaveis desse modo quando o nº atual não for zero
    if ( atual  >  0 ):
        pre_antepenultimo  =  antepenultimo
        antepenultimo  =  penultimo
        penultimo  =  ultimo
        ultimo  =  atual
        soma  =  soma  +  atual
        qtd_considerados=qtd_considerados+1
    #condicinal para retirar os números digitados errados
    elif ( atual  ==  0 ):
        qtd_desconsiderados=qtd_desconsiderados+1
        qtd_considerados=qtd_considerados-1
        #verifica se ainda tem numero para ser retirado
        if(soma == 0):
             print("Atenção: Não há números para serem descartados!")
             qtd_considerados = 0
             qtd_desconsiderados -= 1
        else:
        #condicinal analisar se o usuário digitou mais de 3 zeros
            if (aux_ultimo  ==  0  and  aux_penultimo  ==  0  and  aux_antepenultimo  ==  0 and aux_pre_antepenultimo == 0):
                    qtd_desconsiderados=qtd_desconsiderados-1
                    qtd_considerados=qtd_considerados+1
                    print ( "Atenção: NÃO é permitido inserir mais de três 0 consecutivos!" )
            else :
            #atualiza as variaveis desse modo quando o nº atual for zero
                soma  =  soma  -  ultimo
                ultimo  =  penultimo
                penultimo  =  antepenultimo
                antepenultimo  =  pre_antepenultimo  
     #recebe número atual
    atual  = int ( input ( "Digite um número:" ))              

print ( "Zero Cancela Finalizado!" )
print ( "Soma total= " , soma )
print (f"Números considerados = {qtd_considerados} \nNúmeros desconsiderados = {qtd_desconsiderados} ")
