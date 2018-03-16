package Controladores;

import Gestores.GestorBD;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
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

    GestorBD gestorAdministrador;

    String administradorLogueado;

    ArrayList<String> listaTemporalTelefonos = new ArrayList<>();

    public void initialize(URL fxmlLocations, ResourceBundle resources){


        datosDefecto();

        registrarUsuario.setOnAction(event -> {
            if(listaTemporalTelefonos.size()== 0 || aliasRegistro.getText().equals("")||contrasennaRegistro.getText().equals("")
                    ||cedulaRegistro.getText().equals("")|| nombreApellidosRegistro.getText().equals("")
                    ||direccionRegistro.getText().equals("")||tipoUsuario.getSelectionModel().getSelectedItem() == null)

                invocarAlerta("Se deben ingresar todos los datos del nuevo usuario");
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
                invocarAlerta("Debe ingresarse un número de teléfono");
            else{
                listaTemporalTelefonos.add(nuevoTelefonoRegistro.getText());
                nuevoTelefonoRegistro.clear();
            }
        });

        eliminarTelefonoRegistro.setOnAction(event -> {
            if(telefonosRegistro.getSelectionModel().getSelectedItem()==null)
                invocarAlerta("Debe ingresarse un teléfono para eliminar de la lista");
            else{
                listaTemporalTelefonos.remove(telefonosRegistro.getSelectionModel().getSelectedItem().toString());
            }
        });

        telefonosRegistro.setOnMouseClicked(event->{
            telefonosRegistro.setItems(FXCollections.observableArrayList(listaTemporalTelefonos));
        });

        modificarUsuario.setOnAction(event -> { //TODO que pueda dejar varas vacias a la hora de modificar
            if(aliasModificar.getSelectionModel().getSelectedItem() == null)
                invocarAlerta("Se debe seleccionar un usuario");
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
                invocarAlerta("Se debe incluir el alias y un telefono");
            else{
                gestorAdministrador.eliminarTelefonoUsuario(aliasModificar.getSelectionModel().getSelectedItem().toString(),
                        telefonosModificar.getSelectionModel().getSelectedItem().toString());
                cargarTelefonos();
            }
        });

        modificarTelefonoUsuario.setOnAction(event ->{
            if(aliasModificar.getSelectionModel().getSelectedItem()== null ||  nuevoTelefonoModificar.getText().equals("")
                    || telefonosModificar.getSelectionModel().getSelectedItem() ==null)
                invocarAlerta("Se debe ingresar el antiguo telefono y el nuevo telefono para realizar la modificación");
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
                invocarAlerta("Debe ingresar un número de teléfono y un usuario");
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
                invocarAlerta("El incremento mínimo y porcentaje de mejora deben ser número enteros");
            }
            limpiarVariables();
        });
    }

    public void invocarAlerta(String mensaje) {

        Alert nuevaAlerta = new Alert(Alert.AlertType.WARNING);
        nuevaAlerta.setTitle("Error");
        nuevaAlerta.setContentText(mensaje);
        nuevaAlerta.showAndWait();

    }

    public void datosDefecto(){
        tipoUsuario.getItems().addAll("Administrador","Participante");
    }

    public void limpiarCamposRegistro(){
        aliasRegistro.clear();
        contrasennaRegistro.clear();
        cedulaRegistro.clear();
        nombreApellidosRegistro.clear();
        nuevoTelefonoRegistro.clear();
        telefonosRegistro.getItems().clear();
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


}
