package Modelo;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Javier on 3/19/2018.
 */
public class Item {

    SimpleStringProperty id;
    SimpleStringProperty descripcion;
    SimpleStringProperty detallesEntrega;
    SimpleStringProperty nombreImagen;
    SimpleStringProperty precioBase;
    SimpleStringProperty inicioSubasta;
    SimpleStringProperty finSubasta;

    public Item(String id, String descripcion, String detallesEntrega, String nombreImagen, String precioBase, String inicioSubasta, String finSubasta){
        this.id = new SimpleStringProperty(id);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.detallesEntrega = new SimpleStringProperty(detallesEntrega);
        this.nombreImagen = new SimpleStringProperty(nombreImagen);
        this.precioBase = new SimpleStringProperty(precioBase);
        this.inicioSubasta = new SimpleStringProperty(inicioSubasta);
        this.finSubasta = new SimpleStringProperty(finSubasta);
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

    public String getDescripcion() {
        return descripcion.get();
    }

    public SimpleStringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public String getDetallesEntrega() {
        return detallesEntrega.get();
    }

    public SimpleStringProperty detallesEntregaProperty() {
        return detallesEntrega;
    }

    public void setDetallesEntrega(String detallesEntrega) {
        this.detallesEntrega.set(detallesEntrega);
    }

    public String getNombreImagen() {
        return nombreImagen.get();
    }

    public SimpleStringProperty nombreImagenProperty() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen.set(nombreImagen);
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


    public String getInicioSubasta() {
        return inicioSubasta.get();
    }

    public SimpleStringProperty inicioSubastaProperty() {
        return inicioSubasta;
    }

    public void setInicioSubasta(String inicioSubasta) {
        this.inicioSubasta.set(inicioSubasta);
    }

    public String getFinSubasta() {
        return finSubasta.get();
    }

    public SimpleStringProperty finSubastaProperty() {
        return finSubasta;
    }

    public void setFinSubasta(String finSubasta) {
        this.finSubasta.set(finSubasta);
    }

}
