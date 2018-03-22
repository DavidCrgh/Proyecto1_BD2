package Controladores;

import Gestores.GestorBD;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Javier on 3/13/2018.
 */
public class ControladorLogin implements Initializable {

    @FXML
    public Button loguearAdministrador;
    @FXML
    public Button loguearParticipante;
    @FXML
    public TextField usuarioAdministrador;
    @FXML
    public TextField usuarioParticipante;
    @FXML
    public PasswordField contraseñaAdministrador;
    @FXML
    public PasswordField contraseñaParticipante;
    @FXML
    public RadioButton rboton_Oracle;
    @FXML
    public RadioButton rboton_PostgreSQL;
    @FXML
    public ToggleGroup grupoBotonesBD;

    public GestorBD gestorBase = new GestorBD();


    public void initialize(URL fxmlLocations, ResourceBundle resources) {

        loguearAdministrador.setOnAction(event -> {

            String usuarioAdmi = usuarioAdministrador.getText();
            String contraAdmin = contraseñaAdministrador.getText();

            loguearEntidad(usuarioAdmi,contraAdmin,"Administrador",1026,551);

        });

        loguearParticipante.setOnAction(event ->{
            String usuarioParti = usuarioParticipante.getText();
            String contraParti = contraseñaParticipante.getText();

            loguearEntidad(usuarioParti,contraParti,"Participante",605,556);
        });

    }


    public void abrirVentanaEntidad(String entidad, int width, int height) {

        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("../Interfaz/" + entidad + ".fxml").openStream());

            buscarControladorYSetGestor(entidad, loader); //Busca el controlador dependiendo de la entidad para setearle el gestor de base correspondiente
            Stage escenario = new Stage();
            escenario.setTitle(entidad);
            escenario.setScene(new Scene(root, width, height));
            escenario.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loguearEntidad(String usuario, String contra, String nombreTabla, int width, int height) {


        if (gestorBase.existeConexionUsuarios(usuario, contra) && gestorBase.existeEntidad(usuario, nombreTabla.toUpperCase())) {

            gestorBase.establecerConexionUsuario(usuario, contra);

            abrirVentanaEntidad(nombreTabla, width, height);
        } else {
            gestorBase.invocarAlerta("No existe un usuario asociado a: " + usuario);
        }


    }

    public void buscarControladorYSetGestor(String entidad, FXMLLoader loader) {

        switch (entidad) {

            case "Administrador":
                ControladorAdministrador controladorAdministrador = loader.getController();
                controladorAdministrador.gestorAdministrador = gestorBase;// Este gestor base ya vendria con la conexion asociada a un usuario, se pasa el gestor para que del otro lado se pueda hacer el llamado a las funciones de la base y asi.
                controladorAdministrador.administradorLogueado = usuarioAdministrador.getText(); // Para tener guardado el alias en la clase del controlador
                controladorAdministrador.datosDefecto();
                break;
            case "Participante":
                ControladorParticipante controladorParticipante = loader.getController();
                controladorParticipante.gestorParticipante = gestorBase;
                controladorParticipante.participanteLogueado = usuarioParticipante.getText();
                controladorParticipante.datosDefecto(); //Puede dar nulo si se pone en el initialize
                break;
        }

    }


}
