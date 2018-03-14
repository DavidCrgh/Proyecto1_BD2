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

            loguearEntidad(usuarioAdmi,contraAdmin,"Administrador",100,100);

        });

        loguearParticipante.setOnAction(event ->{
            String usuarioParti = usuarioParticipante.getText();
            String contraParti = contraseñaParticipante.getText();

            loguearEntidad(usuarioParti,contraParti,"Participante",100,100);
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
                break;
            case "Participante":

                break;
        }

    }


}
