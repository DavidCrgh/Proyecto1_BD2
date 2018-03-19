package Controladores;

import Modelo.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Javier on 3/19/2018.
 */
public class ControladorDetallesItem implements Initializable {

    @FXML
    public Label idItem;

    @FXML
    public Label precioItem;

    @FXML
    public Label inicioSubasta;

    @FXML
    public Label finSubasta;

    @FXML
    public TextArea descripcionItem;

    @FXML
    public TextArea detallesEntregaItem;

    @FXML
    public ImageView imagenItem;

    @FXML
    public Button cerrarDetalles;

    Item itemActual;

    public void initialize(URL fxmlLocations, ResourceBundle resources){
        cerrarDetalles.setOnAction(event -> {
            Stage escenario = (Stage)cerrarDetalles.getScene().getWindow();
            escenario.close();
        });
    }

    public void llenarInformacion(){
        idItem.setText(itemActual.getId());
        descripcionItem.setText(itemActual.getDescripcion());
        detallesEntregaItem.setText(itemActual.getDetallesEntrega());

        File imagenMostrar = new File(itemActual.getNombreImagen());

        imagenItem.setImage( new Image(imagenMostrar.toURI().toString()));
        precioItem.setText(itemActual.getPrecioBase());
        inicioSubasta.setText(itemActual.getInicioSubasta());
        finSubasta.setText(itemActual.getFinSubasta());
    }
}
