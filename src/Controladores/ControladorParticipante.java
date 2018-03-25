package Controladores;

import Gestores.GestorBD;
import Modelo.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
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
    public ComboBox filtroCategoria;

    @FXML
    public ComboBox filtroSubCategoria;

    @FXML
    public Button sinFiltro;

    @FXML
    public Button filtrar;

    //Consultas

    @FXML
    public TableView tablaSubastasHistorialPart;

    @FXML
    public TableView tablaPujasHistorialPart;

    @FXML
    public TableColumn idSubastasHistorialPart;

    @FXML
    public TableColumn vendedorSubastasHistorialPart;

    @FXML
    public TableColumn precioBaseSubastasHistorialPart;

    @FXML
    public TableColumn subCategoriaSubastasHistorialPart;

    @FXML
    public TableColumn idPujasPart;

    @FXML
    public TableColumn compradorPujasPart;

    @FXML
    public TableColumn fechaHoraPujasPart;

    @FXML
    public TableColumn montoPujasPart;

    @FXML
    public Button actualizarSubastasHistorialPart;

    @FXML
    public Button mostrarDetallesSubastasHistorialPart;

    @FXML
    public Button mostrarHistorialPujasPart;

    //Historial de Usuarios
    @FXML
    public ComboBox listaUsuarios;

    @FXML
    public TableView historialUsuarios;

    @FXML
    public TableColumn itemHistorial;

    @FXML
    public TableColumn precioBaseHistorial;

    @FXML
    public TableColumn precioFinalHistorial;

    @FXML
    public TableView comentariosUsuarios;

    @FXML
    public TableColumn autorComentario;

    @FXML
    public TableColumn itemComentario;

    @FXML
    public TableColumn comentarioComentario;

    @FXML
    public ToggleGroup grupoRadioButtons;

    @FXML
    public RadioButton opcionVentas;

    @FXML
    public RadioButton opcionPujas;

    @FXML
    public Button verHistorial;

    @FXML
    public Button actualizar;

    GestorBD gestorParticipante;

    String participanteLogueado;

    File imagenSeccionada = new File("Imagenes/defecto.jpg");

    public void  initialize(URL fxmlLocations, ResourceBundle resources){

        configurarColumnas();

        subastarItem.setOnAction(event -> {
            if(fechaInicio.getValue() == null || fechaFin.getValue() == null || horaInicio.getText().equals("") || minutoInicio.getText().equals("") ||
                    segundoInicio.getText().equals("") || horaFin.getText().equals("") || minutoFin.getText().equals("") || segundoFin.getText().equals("") ||
                    descripcionItem.getText().equals("") || detallesEntrega.getText().equals("") || precioBaseSubasta.getText().equals("") || subCategoriaSubasta.getSelectionModel().getSelectedItem() == null){
                gestorParticipante.invocarAlerta("Debe ingresar todos los datos para subastar un ítem.");

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
                limpiarControles();
            }
        });

        pujarPuja.setOnAction(event->{
            if(tablaPuja.getSelectionModel().getSelectedItem() == null || nuevaOfertaPuja.getText().equals(""))
                gestorParticipante.invocarAlerta("Se debe seleccionar una puja y una oferta válidas");

            else {
                Subasta subastaSeleccionada = (Subasta) tablaPuja.getSelectionModel().getSelectedItem();
                if (fechaPujaNoValida(subastaSeleccionada.getId())) {
                    gestorParticipante.invocarAlerta("El tiempo para pujar ha finalizado");
                    nuevaOfertaPuja.clear();
                } else {


                    BigDecimal precioBase = new BigDecimal(subastaSeleccionada.getPrecioBase());
                    BigDecimal oferta = new BigDecimal(nuevaOfertaPuja.getText());

                    //TODO validar que puja sea hecha en periodo valido

                    if (precioBase.compareTo(oferta) == 1) {
                        gestorParticipante.invocarAlerta("La oferta debe ser mayor al precio base");

                    } else {
                        Date fechaSystem = gestorParticipante.obtenerFecha();


                        int idItem = gestorParticipante.buscarIdItem(Integer.parseInt(subastaSeleccionada.getId()));

                        gestorParticipante.pujarPuja(participanteLogueado, idItem, oferta, new java.sql.Date(fechaSystem.getTime()));
                    }
                }
            }
            nuevaOfertaPuja.clear();
        });

        categoriaSubasta.setOnAction(event -> {
            if(categoriaSubasta.getSelectionModel().getSelectedItem() != null) {
                String idCategoria = categoriaSubasta.getSelectionModel().getSelectedItem().toString().substring(0, categoriaSubasta.getSelectionModel().getSelectedItem().toString().indexOf("-"));
                setSubCategoriaSubasta(gestorParticipante.filtrarSubCategorias(Integer.parseInt(idCategoria)));
            }

            });

        filtroCategoria.setOnAction(event -> {
            if(filtroCategoria.getSelectionModel().getSelectedItem() != null) {
                String idCategoria = filtroCategoria.getSelectionModel().getSelectedItem().toString().substring(0, filtroCategoria.getSelectionModel().getSelectedItem().toString().indexOf("-"));
                setSubCategoriaSubasta(gestorParticipante.filtrarSubCategorias(Integer.parseInt(idCategoria)));
            }
        });

        cargarImagen.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Imagen");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imagenes JPG","*.jpg"));
            imagenSeccionada = fileChooser.showOpenDialog(cargarImagen.getScene().getWindow());
            Image imagen = new Image(imagenSeccionada.toURI().toString());
            imagenItem.setImage(imagen);
        });

        sinFiltro.setOnAction(event -> {
            Date fechaSystem = gestorParticipante.obtenerFecha();
            ArrayList<Subasta> subastasValidas = gestorParticipante.getSubastas(new java.sql.Date(fechaSystem.getTime()),participanteLogueado);
            tablaPuja.setItems(FXCollections.observableArrayList(subastasValidas));

            filtroCategoria.getSelectionModel().clearSelection();
            filtroSubCategoria.getSelectionModel().clearSelection();
        });

        filtrar.setOnAction(event -> {
            if(filtroCategoria.getSelectionModel().getSelectedItem() == null)
                gestorParticipante.invocarAlerta("Debe seleccionarse una categoria para filtrar");
            else{
                Date fechaSystem = gestorParticipante.obtenerFecha();
                ArrayList<Subasta> subastasPorCategoriaoSubCategoria = null;
                if(filtroCategoria.getSelectionModel().getSelectedItem() != null && filtroSubCategoria.getSelectionModel().getSelectedItem() == null) {

                    String idCategoria = filtroCategoria.getSelectionModel().getSelectedItem().toString().substring(0, filtroCategoria.getSelectionModel().getSelectedItem().toString().indexOf("-"));
                    subastasPorCategoriaoSubCategoria = gestorParticipante.getSubastasPorCategoria(new java.sql.Date(fechaSystem.getTime()), participanteLogueado, Integer.parseInt(idCategoria), 0);

                }
                else{
                   String idSubCategoria = filtroSubCategoria.getSelectionModel().getSelectedItem().toString().substring(0, filtroSubCategoria.getSelectionModel().getSelectedItem().toString().indexOf("-"));
                   subastasPorCategoriaoSubCategoria = gestorParticipante.getSubastasPorCategoria(new java.sql.Date(fechaSystem.getTime()), participanteLogueado, Integer.parseInt(idSubCategoria),1);
                }
                tablaPuja.setItems(FXCollections.observableArrayList(subastasPorCategoriaoSubCategoria));
            }
        });

        mostrarDetallesPuja.setOnAction(event -> {
            Subasta subastaSeleccionada = (Subasta) tablaPuja.getSelectionModel().getSelectedItem();
            Item informacionItem = gestorParticipante.extraerInformacionItem(subastaSeleccionada.getId());
            abrirVentanaDetallesItem(informacionItem);
        });

        actualizarSubastasHistorialPart.setOnAction(event -> {
            ArrayList<Subasta> todasLasSubastas = gestorParticipante.getSubastasSinRestriccion();
            tablaSubastasHistorialPart.setItems(FXCollections.observableArrayList(todasLasSubastas));
        });

        mostrarDetallesSubastasHistorialPart.setOnAction(event -> {
            Subasta subastaSeleccionadaHistorial = (Subasta) tablaSubastasHistorialPart.getSelectionModel().getSelectedItem();
            Item informacionItemHistorial = gestorParticipante.extraerInformacionItem(subastaSeleccionadaHistorial.getId());
            abrirVentanaDetallesItem(informacionItemHistorial);
        });

        mostrarHistorialPujasPart.setOnAction(event -> {
            if(tablaSubastasHistorialPart.getSelectionModel().getSelectedItem() == null)
                gestorParticipante.invocarAlerta("Debe seleccionar una subasta");
            else{
                Subasta subastaHistorialSeleccionada = (Subasta) tablaSubastasHistorialPart.getSelectionModel().getSelectedItem();
                ArrayList<Puja> pujasAsociadas = gestorParticipante.getPujas(Integer.parseInt(subastaHistorialSeleccionada.getId()));

                tablaPujasHistorialPart.setItems(FXCollections.observableArrayList(pujasAsociadas));
            }
        });

        actualizar.setOnAction(event -> {
            ArrayList<String> usuarios = gestorParticipante.devolverUsuarios("",0);
            listaUsuarios.setItems(FXCollections.observableArrayList(usuarios));
        });

        verHistorial.setOnAction(event -> {
            if(listaUsuarios.getSelectionModel().getSelectedItem() == null)
                gestorParticipante.invocarAlerta("Se debe seleccionar un usuario");
            else{
                String aliasUsuario = listaUsuarios.getSelectionModel().getSelectedItem().toString();
                if(opcionVentas.isSelected()){
                    ArrayList<ConsultasHistorial> consultasObtenidas = gestorParticipante.obtenerHistorialSubastas(aliasUsuario);
                    historialUsuarios.setItems(FXCollections.observableArrayList(consultasObtenidas));
                    ArrayList<Comentario> comentariosObtenidos = gestorParticipante.obtenerComentariosSobreUsuario(aliasUsuario, "Vendedor");
                    comentariosUsuarios.setItems(FXCollections.observableArrayList(comentariosObtenidos));
                }
                else{
                    ArrayList<ConsultasHistorial> consultasObtenidas =  gestorParticipante.obtenerHistorialPujas(aliasUsuario);
                    historialUsuarios.setItems(FXCollections.observableArrayList(consultasObtenidas));
                    ArrayList<Comentario> comentariosObtenidos = gestorParticipante.obtenerComentariosSobreUsuario(aliasUsuario, "Comprador");
                    comentariosUsuarios.setItems(FXCollections.observableArrayList(comentariosObtenidos));
                }
            }
        });
    }

    public void datosDefecto(){
        Date fechaSystem = gestorParticipante.obtenerFecha();

        ArrayList<Subasta> todasLasSubastas = gestorParticipante.getSubastasSinRestriccion();
        ArrayList<Subasta> subastasValidas = gestorParticipante.getSubastas(new java.sql.Date(fechaSystem.getTime()),participanteLogueado);
        ArrayList<String> categoriasElegir = gestorParticipante.getCategorias();
        ArrayList<String> usuarios = gestorParticipante.devolverUsuarios("",0);

        tablaSubastasHistorialPart.setItems(FXCollections.observableArrayList(todasLasSubastas));
        categoriaSubasta.setItems(FXCollections.observableArrayList(categoriasElegir));
        filtroCategoria.setItems(FXCollections.observableArrayList(categoriasElegir));
        tablaPuja.setItems(FXCollections.observableArrayList(subastasValidas));
        listaUsuarios.setItems(FXCollections.observableArrayList(usuarios));
    }

    public void setSubCategoriaSubasta(ArrayList<String> subCategorias){
        subCategoriaSubasta.setItems(FXCollections.observableArrayList(subCategorias));
        filtroSubCategoria.setItems(FXCollections.observableArrayList(subCategorias));
    }

    public void configurarColumnas(){
        columnaIdPuja.setCellValueFactory(new PropertyValueFactory<Subasta,String>("id"));
        columnaVendedorPuja.setCellValueFactory(new PropertyValueFactory<Subasta,String>("vendedor"));
        columnaPrecioBasePuja.setCellValueFactory(new PropertyValueFactory<Subasta,String>("precioBase"));
        columnaSubCategoriaPuja.setCellValueFactory(new PropertyValueFactory<Subasta,String>("subCategoria"));

        idSubastasHistorialPart.setCellValueFactory(new PropertyValueFactory<Subasta,String>("id"));
        vendedorSubastasHistorialPart.setCellValueFactory(new PropertyValueFactory<Subasta,String>("vendedor"));
        precioBaseSubastasHistorialPart.setCellValueFactory(new PropertyValueFactory<Subasta,String>("precioBase"));
        subCategoriaSubastasHistorialPart.setCellValueFactory(new PropertyValueFactory<Subasta,String>("subCategoria"));

        idPujasPart.setCellValueFactory(new PropertyValueFactory<Puja,String>("id"));
        compradorPujasPart.setCellValueFactory(new PropertyValueFactory<Puja,String>("comprador"));
        fechaHoraPujasPart.setCellValueFactory(new PropertyValueFactory<Puja,String>("fechaHora"));
        montoPujasPart.setCellValueFactory(new PropertyValueFactory<Puja,String>("monto"));

        itemHistorial.setCellValueFactory(new PropertyValueFactory<ConsultasHistorial, String>("descripcionItem"));
        precioBaseHistorial.setCellValueFactory(new PropertyValueFactory<ConsultasHistorial, String>("precioBase"));
        precioFinalHistorial.setCellValueFactory(new PropertyValueFactory<ConsultasHistorial, String>("precioFinal"));

        autorComentario.setCellValueFactory(new PropertyValueFactory<Comentario, String>("autor"));
        itemComentario.setCellValueFactory(new PropertyValueFactory<Comentario, String>("item"));
        comentarioComentario.setCellValueFactory(new PropertyValueFactory<Comentario, String>("comentario"));
    }

    public void abrirVentanaDetallesItem(Item infoItem){
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("../Interfaz/DetallesItem.fxml").openStream());
            ControladorDetallesItem controladorItem = loader.getController();
            controladorItem.itemActual = infoItem;
            controladorItem.llenarInformacion();
            Stage escenario = new Stage();
            escenario.setTitle("Detalles Del Item");
            escenario.setScene(new Scene(root,600,438));
            escenario.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void limpiarControles(){
        fechaInicio.getEditor().clear();
        fechaFin.getEditor().clear();
        horaInicio.clear();
        minutoInicio.clear();
        segundoInicio.clear();
        horaFin.clear();
        minutoFin.clear();
        segundoFin.clear();
        descripcionItem.clear();
        detallesEntrega.clear();
        imagenItem.setImage(null);
        categoriaSubasta.getSelectionModel().clearSelection();
        subCategoriaSubasta.getSelectionModel().clearSelection();
        precioBaseSubasta.clear();
    }

    public boolean fechaPujaNoValida(String idSubasta){
        Date fechaSistema = gestorParticipante.obtenerFecha();
        Date tiempoFinSubasta = gestorParticipante.obtenerTiempoFin(Integer.parseInt(idSubasta));

        return fechaSistema.after(tiempoFinSubasta) ;
    }
}
