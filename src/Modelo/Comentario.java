package Modelo;

import javafx.beans.property.SimpleStringProperty;

/**
 * Creado por David Valverde Garro - 2016034774
 * Fecha: 24-Mar-18 Tiempo: 7:03 PM
 */
public class Comentario {
    private SimpleStringProperty autor;
    private SimpleStringProperty item;
    private SimpleStringProperty comentario;

    public Comentario(String autor, String item, String comentario) {
        this.autor = new SimpleStringProperty(autor);
        this.item = new SimpleStringProperty(item);
        this.comentario = new SimpleStringProperty(comentario);
    }

    public String getAutor() {
        return autor.get();
    }

    public SimpleStringProperty autorProperty() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor.set(autor);
    }

    public String getItem() {
        return item.get();
    }

    public SimpleStringProperty itemProperty() {
        return item;
    }

    public void setItem(String item) {
        this.item.set(item);
    }

    public String getComentario() {
        return comentario.get();
    }

    public SimpleStringProperty comentarioProperty() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario.set(comentario);
    }
}
