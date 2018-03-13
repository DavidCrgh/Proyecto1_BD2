package Controladores;

import Gestores.GestorBD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Interfaz/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        GestorBD gestorBase = new GestorBD();
        gestorBase.establecerConexionSuperUsuario();

        try{
            String sql = "SELECT DESCRIPCION FROM SYS.CATEGORIA";
            PreparedStatement prueba = gestorBase.getConexion().prepareStatement(sql);
            ResultSet tuples = prueba.executeQuery();

            while(tuples.next()){
                System.out.println(tuples.getString("DESCRIPCION"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }



        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
