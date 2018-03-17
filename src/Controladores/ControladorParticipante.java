package Controladores;

import Gestores.GestorBD;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Javier on 3/17/2018.
 */
public class ControladorParticipante implements Initializable {

    //Del Vendedor
    @FXML
    public DatePicker fechaInicio;

    @FXML
    public DatePicker fechaFin;

    @FXML
    public TextArea descripcionItem;

    @FXML
    public TextArea detallesEntrega;

    @FXML
    public TextField precioBaseSubasta;

    @FXML
    public ImageView imagenItem;

    @FXML
    public Button cargarImagen;

    @FXML
    public ComboBox categoriaSubasta;

    @FXML
    public ComboBox subCategoriaSubasta;

    @FXML
    public Button subastarItem;

    //Del Comprador

    @FXML
    public TableView tablaPuja;

    @FXML
    public TableColumn columnaIdPuja;

    @FXML
    public TableColumn columnaVendedorPuja;

    @FXML
    public TableColumn columnaPrecioBasePuja;

    @FXML
    public TableColumn columnaSubCategoriaPuja;

    @FXML
    public TextField nuevaOfertaPuja;

    @FXML
    public Button mostrarDetallesPuja;

    @FXML
    public Button pujarPuja;

    GestorBD gestorParticipante;

    String participanteLogueado;

    File imagenSeccionada = new File("Imagenes/defecto.jpg");
    public void  initialize(URL fxmlLocations, ResourceBundle resources){

        categoriaSubasta.setOnAction(event -> {
            if(categoriaSubasta.getSelectionModel().getSelectedItem() != null) {
                String idCategoria = categoriaSubasta.getSelectionModel().getSelectedItem().toString().substring(0, categoriaSubasta.getSelectionModel().getSelectedItem().toString().indexOf("-"));
                setSubCategoriaSubasta(gestorParticipante.filtrarSubCategorias(Integer.parseInt(idCategoria)));
            }

            });

        cargarImagen.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Imagen");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imagenes JPG","*.jpg"));
            imagenSeccionada = fileChooser.showOpenDialog(cargarImagen.getScene().getWindow());

        });

        subastarItem.setOnAction(event -> {

        });


    }

    public void datosDefecto(){

        ArrayList<String> categoriasElegir = gestorParticipante.getCategorias();
        categoriaSubasta.setItems(FXCollections.observableArrayList(categoriasElegir));

    }

    public void setSubCategoriaSubasta(ArrayList<String> subCategorias){
        subCategoriaSubasta.setItems(FXCollections.observableArrayList(subCategorias));
    }

}
