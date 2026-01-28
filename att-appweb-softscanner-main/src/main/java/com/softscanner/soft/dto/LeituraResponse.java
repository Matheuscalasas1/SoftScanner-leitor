package com.softscanner.soft.dto;

public class LeituraResponse {

    private final String tipo;
    private final String comprador;

    public LeituraResponse(String tipo, String comprador) {
        this.tipo = tipo;
        this.comprador = comprador;
    }

    public String getTipo() {
        return tipo;
    }

    public String getComprador() {
        return comprador;
    }
}