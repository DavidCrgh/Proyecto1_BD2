package Controladores;

import Gestores.GestorBD;
import Modelo.Subasta;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @FXML
    public TextField horaInicio;

    @FXML
    public TextField minutoInicio;

    @FXML
    public TextField segundoInicio;

    @FXML
    public TextField horaFin;

    @FXML
    public TextField minutoFin;

    @FXML
    public TextField segundoFin;

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

    @FXML
    public Button actualizarSubastas;

    GestorBD gestorParticipante;

    String participanteLogueado;

    File imagenSeccionada = new File("Imagenes/defecto.jpg");
    public void  initialize(URL fxmlLocations, ResourceBundle resources){

        configurarColumnas();

        subastarItem.setOnAction(event -> {
            if(false){ //TODO condicion para verificar que todos los campos esten llenos

            }else{
                //Obtiene todos los datos de los controles de la interfaz
                DateFormat formatoFechas = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                try {
                    String tiempoInicioSubasta = " " + horaInicio.getText() + ":" + minutoInicio.getText() + ":" + segundoInicio.getText();
                    String fechaInicioSubasta = fechaInicio.getValue().toString() + tiempoInicioSubasta;
                    Date dateInicio = formatoFechas.parse(fechaInicioSubasta);

                    String tiempoFinSubasta = " " + horaFin.getText() + ":" + minutoFin.getText() + ":" + segundoFin.getText();
                    String fechaFinSubasta = fechaFin.getValue().toString() + tiempoFinSubasta;
                    Date dateFin = formatoFechas.parse(fechaFinSubasta);

                    String descripcion = descripcionItem.getText();
                    String detalles = detallesEntrega.getText();
                    String imagen = imagenSeccionada.getName();
                    BigDecimal precioBase = new BigDecimal(precioBaseSubasta.getText());
                    int idCategoria = Integer.parseInt(
                            subCategoriaSubasta.getSelectionModel().getSelectedItem().toString().substring(
                                    0, subCategoriaSubasta.getSelectionModel().getSelectedItem().toString().indexOf("-")
                            )
                    );

                    gestorParticipante.crearSubasta(
                            participanteLogueado,
                            new java.sql.Date(dateInicio.getTime()),
                            new java.sql.Date(dateFin.getTime()),
                            descripcion,
                            imagen,
                            precioBase,
                            detalles,
                            idCategoria
                    );

                } catch (ParseException e) {
                    e.printStackTrace();
                }




            }
        });

        pujarPuja.setOnAction(event->{
            if(tablaPuja.getSelectionModel().getSelectedItem() == null || nuevaOfertaPuja.getText().equals(""))
                gestorParticipante.invocarAlerta("Se debe seleccionar una puja y una oferta válidas");

            else{
                Date fechaSystem = obtenerFecha();
                BigDecimal oferta = new BigDecimal(nuevaOfertaPuja.getText());
                Subasta subastaSeleccionada = (Subasta)tablaPuja.getSelectionModel().getSelectedItem();
                int idItem = gestorParticipante.buscarIdItem(Integer.parseInt(subastaSeleccionada.getId()));

                gestorParticipante.pujarPuja(participanteLogueado,idItem,oferta,new java.sql.Date(fechaSystem.getTime()));
            }
        });

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
            // TODO  cargar imagen en la interfaz
            // imagenItem.setImage(new Image(...)); Buscar en internet :v
        });

        actualizarSubastas.setOnAction(event -> {
            Date fechaSystem = obtenerFecha();
            ArrayList<Subasta> subastasValidas = gestorParticipante.getSubastas(new java.sql.Date(fechaSystem.getTime()));
            tablaPuja.setItems(FXCollections.observableArrayList(subastasValidas));
        });


    }

    public void datosDefecto(){

        Date fechaSystem = obtenerFecha();

        ArrayList<Subasta> subastasValidas = gestorParticipante.getSubastas(new java.sql.Date(fechaSystem.getTime()));
        ArrayList<String> categoriasElegir = gestorParticipante.getCategorias();
        categoriaSubasta.setItems(FXCollections.observableArrayList(categoriasElegir));
        tablaPuja.setItems(FXCollections.observableArrayList(subastasValidas));

    }

    public void setSubCategoriaSubasta(ArrayList<String> subCategorias){
        subCategoriaSubasta.setItems(FXCollections.observableArrayList(subCategorias));
    }

    public void configurarColumnas(){
        columnaIdPuja.setCellValueFactory(new PropertyValueFactory<Subasta,String>("id"));
        columnaVendedorPuja.setCellValueFactory(new PropertyValueFactory<Subasta,String>("vendedor"));
        columnaPrecioBasePuja.setCellValueFactory(new PropertyValueFactory<Subasta,String>("precioBase"));
        columnaSubCategoriaPuja.setCellValueFactory(new PropertyValueFactory<Subasta,String>("subCategoria"));
    }

    public Date obtenerFecha(){
        Date fechaSistemaReal = null;
        try{
            DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
            Date objetoDate = new Date();
            String fechaSistema = formatoFecha.format(objetoDate);
            fechaSistemaReal = formatoFecha.parse(fechaSistema);

        }catch(Exception e){
            e.printStackTrace();
        }

        return fechaSistemaReal;
    }

}
