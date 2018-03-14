package Controladores;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Javier on 3/13/2018.
 */
public class ControladorInicioAdministrador implements Initializable{

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
    public Spinner porcentajeMejora;

    @FXML
    public Spinner incrementoMinimo;

    public void initialize(URL fxmlLocations, ResourceBundle resources){

    }



}
