package Modelo;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Javier on 3/18/2018.
 */
public class Subasta {

    SimpleStringProperty id;
    SimpleStringProperty vendedor;
    SimpleStringProperty precioBase;
    SimpleStringProperty subCategoria;

    public Subasta(String id, String vendedor, String precioBase, String subCategoria){
        this.id = new SimpleStringProperty(id);
        this.vendedor = new SimpleStringProperty(vendedor);
        this.precioBase = new SimpleStringProperty(precioBase);
        this.subCategoria = new SimpleStringProperty(subCategoria);

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

    public String getVendedor() {
        return vendedor.get();
    }

    public SimpleStringProperty vendedorProperty() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor.set(vendedor);
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

    public String getSubCategoria() {
        return subCategoria.get();
    }

    public SimpleStringProperty subCategoriaProperty() {
        return subCategoria;
    }

    public void setSubCategoria(String subCategoria) {
        this.subCategoria.set(subCategoria);
    }


}
