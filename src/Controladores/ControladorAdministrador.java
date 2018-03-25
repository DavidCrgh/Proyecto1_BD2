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
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Javier on 3/13/2018.
 */
public class ControladorAdministrador implements Initializable{

    @FXML
    public TextField aliasRegistro;

    @FXML
    public TextField cedulaRegistro;

    @FXML
    public TextField nombreApellidosRegistro;

    @FXML
    public TextField nuevoTelefonoRegistro;

    @FXML
    public Button agregarTelefonoRegistro;

    @FXML
    public ComboBox telefonosRegistro;

    @FXML
    public Button eliminarTelefonoRegistro;

    @FXML
    public TextArea direccionRegistro;

    @FXML
    public Button registrarUsuario;

    @FXML
    public ComboBox aliasModificar;

    @FXML
    public TextField cedulaModificar;

    @FXML
    public TextField nombreApellidosModificar;

    @FXML
    public TextField nuevoTelefonoModificar;

    @FXML
    public Button agregarTelefonoModificar;

    @FXML
    public ComboBox telefonosModificar;

    @FXML
    public Button modificarTelefonoUsuario;

    @FXML
    public Button eliminarTelefonoUsuario;

    @FXML
    public TextArea direccionModificar;

    @FXML
    public Button modificarUsuario;

    @FXML
    public TextField porcentajeMejora;

    @FXML
    public TextField incrementoMinimo;

    @FXML
    public PasswordField contrasennaRegistro;

    @FXML
    public ComboBox tipoUsuario;

    @FXML
    public Button variablesSistema;

    @FXML
    public Button actualizarModificarUsuario;

    @FXML
    public TableView tablaSubastasAdmi;

    @FXML
    public TableColumn idSubastaAdmi;

    @FXML
    public TableColumn vendedorSubastaAdmi;

    @FXML
    public TableColumn precioBaseSubastaAdmi;

    @FXML
    public TableColumn subCategoriaSubastaAdmi;

    @FXML
    public Button sinFiltroAdmi;

    @FXML
    public Button mostrarDetallesAdmi;

    @FXML
    public Button filtrarAdmi;

    @FXML
    public ComboBox filtroCategoriaAdmi;

    @FXML
    public ComboBox filtroSubCategoriaAdmi;

    @FXML
    public TableView tablaSubastasHistorialAdmi;

    @FXML
    public TableView tablaPujasAdmi;

    @FXML
    public TableColumn idSubastaHistorialAdmi;

    @FXML
    public TableColumn vendedorSubastaHistorialAdmi;

    @FXML
    public TableColumn precioBaseSubastaHistorialAdmi;

    @FXML
    public TableColumn subCategoriaSubastaHistorialAdmi;

    @FXML
    public TableColumn idPujasAdmi;

    @FXML
    public TableColumn compradorPujasAdmi;

    @FXML
    public TableColumn fechaHoraPujasAdmi;

    @FXML
    public TableColumn montoPujasAdmi;

    @FXML
    public Button mostrarDetallesHistorialAdmi;

    @FXML
    public Button mostrarHistorialAdmi;

    @FXML
    public Button actualizarSubastasHistorialAdmi;

    @FXML
    public ToggleGroup grupoBotones;

    @FXML
    public RadioButton opcionVentas;

    @FXML
    public RadioButton opcionPujas;

    @FXML
    public ComboBox aliasUsuarioHistorial;

    @FXML
    public TableView tablaHistorialUsuarios;

    @FXML
    public TableColumn itemHistorialUsuarios;

    @FXML
    public TableColumn precioBaseHistorialUsuarios;

    @FXML
    public TableColumn precioFinalHistorialUsuarios;

    @FXML
    public Button mostrarHistorialUsuariosAdmi;

    @FXML
    public TableView comentariosUsuarios;

    @FXML
    public TableColumn autorComentario;

    @FXML
    public TableColumn itemComentario;

    @FXML
    public TableColumn comentarioComentario;


    GestorBD gestorAdministrador;

    String administradorLogueado;

    ArrayList<String> listaTemporalTelefonos = new ArrayList<>();

    public void initialize(URL fxmlLocations, ResourceBundle resources){

        configurarColumnas();

        registrarUsuario.setOnAction(event -> {
            if(listaTemporalTelefonos.size()== 0 || aliasRegistro.getText().equals("")||contrasennaRegistro.getText().equals("")
                    ||cedulaRegistro.getText().equals("")|| nombreApellidosRegistro.getText().equals("")
                    ||direccionRegistro.getText().equals("")||tipoUsuario.getSelectionModel().getSelectedItem() == null)

                gestorAdministrador.invocarAlerta("Se deben ingresar todos los datos del nuevo usuario");
            else{

                String usuario = aliasRegistro.getText();
                String contrasenna = contrasennaRegistro.getText();
                String cedula = cedulaRegistro.getText();
                String nombreApellidos = nombreApellidosRegistro.getText();
                String direccion = direccionRegistro.getText();
                String tipoUser = tipoUsuario.getSelectionModel().getSelectedItem().toString();

                gestorAdministrador.agregarNuevoUsuario(usuario,contrasenna,cedula,nombreApellidos,direccion,listaTemporalTelefonos,tipoUser);
                limpiarCamposRegistro();
            }
        });

        agregarTelefonoRegistro.setOnAction(event -> {
            if(nuevoTelefonoRegistro.getText().equals(""))
                gestorAdministrador.invocarAlerta("Debe ingresarse un número de teléfono");
            else{
                listaTemporalTelefonos.add(nuevoTelefonoRegistro.getText());
                nuevoTelefonoRegistro.clear();
            }
        });

        eliminarTelefonoRegistro.setOnAction(event -> {
            if(telefonosRegistro.getSelectionModel().getSelectedItem()==null)
                gestorAdministrador.invocarAlerta("Debe ingresarse un teléfono para eliminar de la lista");
            else{
                listaTemporalTelefonos.remove(telefonosRegistro.getSelectionModel().getSelectedItem().toString());
            }
        });

        telefonosRegistro.setOnMouseClicked(event->{
            telefonosRegistro.setItems(FXCollections.observableArrayList(listaTemporalTelefonos));
        });

        modificarUsuario.setOnAction(event -> {
            if(aliasModificar.getSelectionModel().getSelectedItem() == null)
               gestorAdministrador.invocarAlerta("Se debe seleccionar un usuario");
            else{
                String aliasUsuario = aliasModificar.getSelectionModel().getSelectedItem().toString();


                String [] posibleInfo = {cedulaModificar.getText(),nombreApellidosModificar.getText(),direccionModificar.getText()};

                ArrayList<String> informacionVieja = gestorAdministrador.devolverUsuarios(aliasUsuario,3);
                determinarInformacionAModificar(posibleInfo,informacionVieja);

                gestorAdministrador.modificarUsuario(aliasUsuario,posibleInfo[0],posibleInfo[1],posibleInfo[2]);// cedula,nombre,direccion
                limpiarCamposModificar();
            }
        });

        actualizarModificarUsuario.setOnAction(event -> {

            ArrayList<String> aliasUsuarios = gestorAdministrador.devolverUsuarios("",0);
            aliasModificar.setItems(FXCollections.observableArrayList(aliasUsuarios));
        });

        aliasModificar.setOnAction(event -> {
            if(aliasModificar.getSelectionModel().getSelectedItem() != null) {
                cargarTelefonos();
            }
        });

        eliminarTelefonoUsuario.setOnAction(event -> {
            if(aliasModificar.getSelectionModel().getSelectedItem() == null || telefonosModificar.getSelectionModel().getSelectedItem() == null)
                gestorAdministrador.invocarAlerta("Se debe incluir el alias y un telefono");
            else{
                gestorAdministrador.eliminarTelefonoUsuario(aliasModificar.getSelectionModel().getSelectedItem().toString(),
                        telefonosModificar.getSelectionModel().getSelectedItem().toString());
                cargarTelefonos();
            }
        });

        modificarTelefonoUsuario.setOnAction(event ->{
            if(aliasModificar.getSelectionModel().getSelectedItem()== null ||  nuevoTelefonoModificar.getText().equals("")
                    || telefonosModificar.getSelectionModel().getSelectedItem() ==null)
                gestorAdministrador.invocarAlerta("Se debe ingresar el antiguo telefono y el nuevo telefono para realizar la modificación");
            else {
                String alias = aliasModificar.getSelectionModel().getSelectedItem().toString();
                String nuevoTelefono = nuevoTelefonoModificar.getText();
                String telefonoViejo = telefonosModificar.getSelectionModel().getSelectedItem().toString();

                gestorAdministrador.modificarTelefonoUsuario(alias,nuevoTelefono,telefonoViejo);
                limpiarTelefonosModificar();
            }


        });

        agregarTelefonoModificar.setOnAction(event -> {
            if(nuevoTelefonoModificar.getText().equals("") || aliasModificar.getSelectionModel().getSelectedItem() == null)
                gestorAdministrador.invocarAlerta("Debe ingresar un número de teléfono y un usuario");
            else{
                String aliasUsuario = aliasModificar.getSelectionModel().getSelectedItem().toString();
                String nuevoTelefono = nuevoTelefonoModificar.getText();
                gestorAdministrador.agregarNuevoTelefonoUsuario(aliasUsuario,nuevoTelefono);
                limpiarTelefonosModificar();
            }
        });

        variablesSistema.setOnAction(event -> {
            try{
                BigDecimal porcentaje  = new BigDecimal(porcentajeMejora.getText());
                BigDecimal incrementoMin = new BigDecimal(incrementoMinimo.getText());
                gestorAdministrador.agregarNuevasVariables(administradorLogueado,porcentaje,incrementoMin);

            }catch(Exception e){
                gestorAdministrador.invocarAlerta("El incremento mínimo y porcentaje de mejora deben ser número enteros");
            }
            limpiarVariables();
        });

        filtrarAdmi.setOnAction(event -> {
            if(filtroCategoriaAdmi.getSelectionModel().getSelectedItem() == null)
                gestorAdministrador.invocarAlerta("Debe seleccionarse una categoria para filtrar");
            else{
                Date fechaSystem = gestorAdministrador.obtenerFecha();
                ArrayList<Subasta> subastasPorCategoriaoSubCategoria = null;
                if(filtroCategoriaAdmi.getSelectionModel().getSelectedItem() != null && filtroSubCategoriaAdmi.getSelectionModel().getSelectedItem() == null) {

                    String idCategoria = filtroCategoriaAdmi.getSelectionModel().getSelectedItem().toString().substring(0, filtroCategoriaAdmi.getSelectionModel().getSelectedItem().toString().indexOf("-"));
                    subastasPorCategoriaoSubCategoria = gestorAdministrador.getSubastasPorCategoria(new java.sql.Date(fechaSystem.getTime()), "DFG", Integer.parseInt(idCategoria), 0);

                }
                else{
                    String idSubCategoria = filtroSubCategoriaAdmi.getSelectionModel().getSelectedItem().toString().substring(0, filtroSubCategoriaAdmi.getSelectionModel().getSelectedItem().toString().indexOf("-"));
                    subastasPorCategoriaoSubCategoria = gestorAdministrador.getSubastasPorCategoria(new java.sql.Date(fechaSystem.getTime()), "DFG", Integer.parseInt(idSubCategoria),1);
                }
                tablaSubastasAdmi.setItems(FXCollections.observableArrayList(subastasPorCategoriaoSubCategoria));
            }
        });

        sinFiltroAdmi.setOnAction(event -> {
            Date fechaSystem = gestorAdministrador.obtenerFecha();
            ArrayList<Subasta> subastasValidas = gestorAdministrador.getSubastas(new java.sql.Date(fechaSystem.getTime()),"DFG");
            tablaSubastasAdmi.setItems(FXCollections.observableArrayList(subastasValidas));

            filtroCategoriaAdmi.getSelectionModel().clearSelection();
            filtroSubCategoriaAdmi.getSelectionModel().clearSelection();
        });

        filtroCategoriaAdmi.setOnAction(event -> {
            if(filtroCategoriaAdmi.getSelectionModel().getSelectedItem() != null) {
                String idCategoria = filtroCategoriaAdmi.getSelectionModel().getSelectedItem().toString().substring(0, filtroCategoriaAdmi.getSelectionModel().getSelectedItem().toString().indexOf("-"));
                filtroSubCategoriaAdmi.setItems(FXCollections.observableArrayList(gestorAdministrador.filtrarSubCategorias(Integer.parseInt(idCategoria))));
            }
        });

        mostrarDetallesAdmi.setOnAction(event -> {
            Subasta subastaSeleccionada = (Subasta) tablaSubastasAdmi.getSelectionModel().getSelectedItem();
            Item informacionItem = gestorAdministrador.extraerInformacionItem(subastaSeleccionada.getId());
            abrirVentanaDetallesItem(informacionItem);
        });

        mostrarDetallesHistorialAdmi.setOnAction(event -> {
            Subasta subastaHistorialSeleccionada = (Subasta) tablaSubastasHistorialAdmi.getSelectionModel().getSelectedItem();
            Item infoItemHistorial = gestorAdministrador.extraerInformacionItem(subastaHistorialSeleccionada.getId());
            abrirVentanaDetallesItem(infoItemHistorial);
        });

        mostrarHistorialAdmi.setOnAction(event -> {
            if(tablaSubastasHistorialAdmi.getSelectionModel().getSelectedItem() == null)
                gestorAdministrador.invocarAlerta("Debe seleccionar una subasta");
            else{
                Subasta subastaHistorialSeleccionada = (Subasta) tablaSubastasHistorialAdmi.getSelectionModel().getSelectedItem();
                ArrayList<Puja> pujasAsociadas = gestorAdministrador.getPujas(Integer.parseInt(subastaHistorialSeleccionada.getId()));

                tablaPujasAdmi.setItems(FXCollections.observableArrayList(pujasAsociadas));
            }
        });

        actualizarSubastasHistorialAdmi.setOnAction(event -> {
            ArrayList<Subasta> todasLasSubastas = gestorAdministrador.getSubastasSinRestriccion();
            tablaSubastasHistorialAdmi.setItems(FXCollections.observableArrayList(todasLasSubastas));
        });

        mostrarHistorialUsuariosAdmi.setOnAction(event -> {
            if(aliasUsuarioHistorial.getSelectionModel().getSelectedItem() == null)
                gestorAdministrador.invocarAlerta("Se debe seleccionar un usuario");
            else{
                String aliasUsuario = aliasUsuarioHistorial.getSelectionModel().getSelectedItem().toString();
                if(opcionVentas.isSelected()){
                    ArrayList<ConsultasHistorial> consultasObtenidasSubastas = gestorAdministrador.obtenerHistorialSubastas(aliasUsuario);
                    tablaHistorialUsuarios.setItems(FXCollections.observableArrayList(consultasObtenidasSubastas));
                    ArrayList<Comentario> comentariosObtenidos = gestorAdministrador.obtenerComentariosSobreUsuario(aliasUsuario, "Vendedor");
                    comentariosUsuarios.setItems(FXCollections.observableArrayList(comentariosObtenidos));
                }
                else{
                    ArrayList<ConsultasHistorial> consultasObtenidasPujas = gestorAdministrador.obtenerHistorialPujas(aliasUsuario);
                    tablaHistorialUsuarios.setItems(FXCollections.observableArrayList(consultasObtenidasPujas));
                    ArrayList<Comentario> comentariosObtenidos = gestorAdministrador.obtenerComentariosSobreUsuario(aliasUsuario, "Comprador");
                    comentariosUsuarios.setItems(FXCollections.observableArrayList(comentariosObtenidos));
                }
            }
        });

    }


    public void datosDefecto(){
        Date fechaSystem = gestorAdministrador.obtenerFecha();

        ArrayList<Subasta> todasLasSubastasActivas = gestorAdministrador.getSubastas(new java.sql.Date(fechaSystem.getTime()),"DFH");
        ArrayList<Subasta> todasLasSubastas = gestorAdministrador.getSubastasSinRestriccion();
        ArrayList<String> aliasUsuarios = gestorAdministrador.devolverUsuarios(administradorLogueado,0);

        ArrayList<String> categoriasElegir = gestorAdministrador.getCategorias();
        filtroCategoriaAdmi.setItems(FXCollections.observableArrayList(categoriasElegir));

        tablaSubastasAdmi.setItems(FXCollections.observableArrayList(todasLasSubastasActivas));
        tablaSubastasHistorialAdmi.setItems(FXCollections.observableArrayList(todasLasSubastas));

        tipoUsuario.getItems().addAll("Administrador","Participante");
        aliasUsuarioHistorial.setItems(FXCollections.observableArrayList(aliasUsuarios));

    }

    public void limpiarCamposRegistro(){
        aliasRegistro.clear();
        contrasennaRegistro.clear();
        cedulaRegistro.clear();
        nombreApellidosRegistro.clear();
        nuevoTelefonoRegistro.clear();
        listaTemporalTelefonos.clear();
        telefonosRegistro.setItems(FXCollections.observableArrayList(listaTemporalTelefonos));
        direccionRegistro.clear();
        tipoUsuario.getSelectionModel().clearSelection();
    }

    public void limpiarCamposModificar(){
        cedulaModificar.clear();
        nombreApellidosModificar.clear();
        nuevoTelefonoModificar.clear();
        telefonosModificar.getItems().clear();
        direccionModificar.clear();
        aliasModificar.getSelectionModel().clearSelection();
    }

    public void limpiarTelefonosModificar(){
        telefonosModificar.getItems().clear();
        nuevoTelefonoModificar.clear();
        aliasModificar.getSelectionModel().clearSelection();
    }

    public void limpiarVariables(){
        porcentajeMejora.clear();
        incrementoMinimo.clear();
    }

    public void cargarTelefonos(){
        String aliasEscogido = aliasModificar.getSelectionModel().getSelectedItem().toString();
        ArrayList<String> telefonosUsuario = gestorAdministrador.devolverUsuarios(aliasEscogido, 1);

        telefonosModificar.setItems(FXCollections.observableArrayList(telefonosUsuario));
    }

    public void determinarInformacionAModificar(String[] posibleInfo, ArrayList<String> infoVieja){
        for(int i =0;i<infoVieja.size();i++){
            if(posibleInfo[i].equals(""))
                posibleInfo[i] = infoVieja.get(i);
        }
    }

    public void configurarColumnas(){
        idSubastaAdmi.setCellValueFactory(new PropertyValueFactory<Subasta,String>("id"));
        vendedorSubastaAdmi.setCellValueFactory(new PropertyValueFactory<Subasta,String>("vendedor"));
        precioBaseSubastaAdmi.setCellValueFactory(new PropertyValueFactory<Subasta,String>("precioBase"));
        subCategoriaSubastaAdmi.setCellValueFactory(new PropertyValueFactory<Subasta,String>("subCategoria"));

        idSubastaHistorialAdmi.setCellValueFactory(new PropertyValueFactory<Subasta,String>("id"));
        vendedorSubastaHistorialAdmi.setCellValueFactory(new PropertyValueFactory<Subasta,String>("vendedor"));
        precioBaseSubastaHistorialAdmi.setCellValueFactory(new PropertyValueFactory<Subasta,String>("precioBase"));
        subCategoriaSubastaHistorialAdmi.setCellValueFactory(new PropertyValueFactory<Subasta,String>("subCategoria"));

        idPujasAdmi.setCellValueFactory(new PropertyValueFactory<Puja,String>("id"));
        compradorPujasAdmi.setCellValueFactory(new PropertyValueFactory<Puja,String>("comprador"));
        fechaHoraPujasAdmi.setCellValueFactory(new PropertyValueFactory<Puja,String>("fechaHora"));
        montoPujasAdmi.setCellValueFactory(new PropertyValueFactory<Puja,String>("monto"));

        itemHistorialUsuarios.setCellValueFactory(new PropertyValueFactory<ConsultasHistorial,String>("descripcionItem"));
        precioBaseHistorialUsuarios.setCellValueFactory(new PropertyValueFactory<ConsultasHistorial,String>("precioBase"));
        precioFinalHistorialUsuarios.setCellValueFactory(new PropertyValueFactory<ConsultasHistorial,String>("precioFinal"));

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




}
