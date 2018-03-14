package Controladores;

import Gestores.GestorBD;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    public Spinner porcentajeMejora;

    @FXML
    public Spinner incrementoMinimo;

    @FXML
    public PasswordField contrasennaRegistro;

    @FXML
    public ComboBox tipoUsuario;

    @FXML
    public Button variablesSistema;

    GestorBD gestorAdministrador;

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


}
