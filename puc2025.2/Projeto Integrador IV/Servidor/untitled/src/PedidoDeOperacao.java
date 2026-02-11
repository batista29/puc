package com.example.petcare.network;

public class PedidoDeOperacao extends Comunicado{
    private byte   comando;

    public PedidoDeOperacao (byte comando)
    {
        this.comando = comando;
    }

    public byte getOperacao(){

        return this.comando;
    }


    public String toString ()
    {
        return (""+this.comando);
    }
}
