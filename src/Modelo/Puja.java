package Modelo;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Javier on 3/21/2018.
 */
public class Puja {

    SimpleStringProperty id;
    SimpleStringProperty comprador;
    SimpleStringProperty fechaHora;
    SimpleStringProperty monto;

    public Puja(String id, String comprador, String fechaHora, String monto){
        this.id = new SimpleStringProperty(id);
        this.comprador = new SimpleStringProperty(comprador);
        this.fechaHora = new SimpleStringProperty(fechaHora);
        this.monto = new SimpleStringProperty(monto);
    }


    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getComprador() {
        return comprador.get();
    }

    public SimpleStringProperty compradorProperty() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador.set(comprador);
    }

    public String getFechaHora() {
        return fechaHora.get();
    }

    public SimpleStringProperty fechaHoraProperty() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora.set(fechaHora);
    }

    public String getMonto() {
        return monto.get();
    }

    public SimpleStringProperty montoProperty() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto.set(monto);
    }


}
