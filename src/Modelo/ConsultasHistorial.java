package Modelo;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Javier on 3/21/2018.
 */
public class ConsultasHistorial {

    SimpleStringProperty descripcionItem;
    SimpleStringProperty precioBase;
    SimpleStringProperty precioFinal;

    public ConsultasHistorial(String descripcionItem, String precioBase, String precioFinal){
        this.descripcionItem = new SimpleStringProperty(descripcionItem);
        this.precioBase = new SimpleStringProperty(precioBase);
        this.precioFinal = new SimpleStringProperty(precioFinal);
    }

    public String getDescripcionItem() {
        return descripcionItem.get();
    }

    public SimpleStringProperty descripcionItemProperty() {
        return descripcionItem;
    }

    public void setDescripcionItem(String descripcionItem) {
        this.descripcionItem.set(descripcionItem);
    }

    public String getPrecioBase() {
        return precioBase.get();
    }

    public SimpleStringProperty precioBaseProperty() {
        return precioBase;
    }

    public void setPrecioBase(String precioBase) {
        this.precioBase.set(precioBase);
    }

    public String getPrecioFinal() {
        return precioFinal.get();
    }

    public SimpleStringProperty precioFinalProperty() {
        return precioFinal;
    }

    public void setPrecioFinal(String precioFinal) {
        this.precioFinal.set(precioFinal);
    }
}
