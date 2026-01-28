package com.softscanner.soft.dto;

public class RegistroDTO {

    private String serial;
    private String comprador;
    private String dataSaida;
    private String dataRetorno;

    // Construtor usado pelo JPQL
    public RegistroDTO(String serial, String comprador, String dataSaida, String dataRetorno) {
        this.serial = serial;
        this.comprador = comprador;
        this.dataSaida = dataSaida;
        this.dataRetorno = dataRetorno;
    }

    public String getSerial() {
        return serial;
    }

    public String getComprador() {
        return comprador;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public String getDataRetorno() {
        return dataRetorno;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public void setDataRetorno(String dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }
}
