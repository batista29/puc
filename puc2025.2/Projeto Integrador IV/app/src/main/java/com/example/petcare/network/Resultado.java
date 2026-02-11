package com.example.petcare.network;

import com.example.petcare.network.Comunicado;

public class Resultado extends Comunicado
{
    private String valorResultante;
    private byte totalopcao = 0 ;
    public Resultado (String valorResultante)
    {
        this.valorResultante = valorResultante;
    }

    public Resultado(String valorResultante, byte total){
        this.valorResultante = valorResultante;
        this.totalopcao = total;
    }

    public byte getTotalopcao() {
        return totalopcao;
    }

    public String getValorResultante ()
    {
        return this.valorResultante;
    }

    public String toString ()
    {
        return (""+this.valorResultante);
    }

}