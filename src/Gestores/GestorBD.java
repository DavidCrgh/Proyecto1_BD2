package Gestores;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Javier on 2/19/2018.
 */
public class GestorBD {

    private Connection conexion;
    private Statement estado;

    public GestorBD() {
        conexion = null;
        estado = null;
    }

    public void establecerConexionSuperUsuario() {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String connectionUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
            conexion = DriverManager.getConnection(connectionUrl,"C##PRINCIPALSCHEMA","oracleBases21698");
            estado = conexion.createStatement();

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public Statement getEstado() {
        return estado;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
                conexion = null;
                estado = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void establecerConexionUsuario(String username, String password) {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String connectionUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
            conexion = DriverManager.getConnection(connectionUrl,username,password);
            estado = conexion.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean existeConexionUsuarios(String username, String password) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String connectionUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
            conexion = DriverManager.getConnection(connectionUrl,username,password);
            conexion.createStatement();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean existeEntidad(String usuario,String tablaBuscar) {
        establecerConexionSuperUsuario(); // Para cuando se valida un supervisor
        String sqlEntidad = "";
        switch (tablaBuscar) {

            case "ADMINISTRADOR":
                sqlEntidad = "SELECT * FROM " + tablaBuscar + " WHERE ALIASADMINISTRADOR = ?";
                break;
            default:
                sqlEntidad = "SELECT * FROM " + tablaBuscar + " WHERE ALIASPARTICIPANTE = ?";
                break;
        }

        try {

            PreparedStatement obtenerEntidad = conexion.prepareStatement(sqlEntidad);
            obtenerEntidad.setString(1, usuario);
            ResultSet resultados = obtenerEntidad.executeQuery();

            if (resultados.next()) {
                cerrarConexion();
                return true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        cerrarConexion();
        return false;
    }

    public void invocarAlerta(String mensaje) {

        Alert nuevaAlerta = new Alert(Alert.AlertType.WARNING);
        nuevaAlerta.setTitle("Error");
        nuevaAlerta.setContentText(mensaje);
        nuevaAlerta.showAndWait();

    }

    public void agregarNuevoUsuario(String usuario, String contrasenna, String cedula, String nombreApellidos, String direccion,ArrayList<String> telefonos,String tipoUsuario){
        String [] telefonosUsuario = new String[telefonos.size()];
        telefonosUsuario = telefonos.toArray(telefonosUsuario);


        String procedimientoAlmacenado = "";

        switch(tipoUsuario){
            case "Administrador":
                procedimientoAlmacenado = "{call C##PRINCIPALSCHEMA.crearUsuarioAdministrador (?,?,?,?,?,?)}";
                break;
            case "Participante":
                procedimientoAlmacenado = "{call C##PRINCIPALSCHEMA.crearUsuarioParticipante (?,?,?,?,?,?)}";
                break;
        }

       try{
            ArrayDescriptor arrDesc = ArrayDescriptor.createDescriptor("C##PRINCIPALSCHEMA.LISTATELEFONOS",conexion);
            Array arregloTelefonos = new ARRAY(arrDesc,conexion,telefonosUsuario);


            CallableStatement agregarUsuario = conexion.prepareCall(procedimientoAlmacenado);
            agregarUsuario.setString(1,usuario);
            agregarUsuario.setString(2,contrasenna);
            agregarUsuario.setString(3,cedula);
            agregarUsuario.setString(4,nombreApellidos);
            agregarUsuario.setString(5,direccion);
            agregarUsuario.setArray(6,arregloTelefonos);

            agregarUsuario.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }


    }

    public void modificarUsuario(String alias,String nuevaCedula, String nuevoNombreApellidos, String nuevaDireccion){
        try{
            CallableStatement modificacionUsuario = conexion.prepareCall("{call C##PRINCIPALSCHEMA.modificarUsuario(?,?,?,?)}");
            modificacionUsuario.setString(1,alias);
            modificacionUsuario.setString(2,nuevaCedula);
            modificacionUsuario.setString(3,nuevoNombreApellidos);
            modificacionUsuario.setString(4,nuevaDireccion);

            modificacionUsuario.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> devolverUsuarios(int atributo){
        ArrayList<String> aliasTelefonosUsuarios = new ArrayList<>();
        try{
            String sqlUsuarios = "{call C##PRINCIPALSCHEMA.retornarUsuarios(?)}";
            CallableStatement retornarUsuarios = conexion.prepareCall(sqlUsuarios);

            retornarUsuarios.setInt(1,atributo);
            retornarUsuarios.registerOutParameter(2, OracleTypes.CURSOR);

            retornarUsuarios.executeUpdate();
            ResultSet aliasTelefonosDevueltos = (ResultSet)retornarUsuarios.getObject(1);

            while(aliasTelefonosDevueltos.next()){
                aliasTelefonosUsuarios.add(aliasTelefonosDevueltos.getString("ALIAS"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return aliasTelefonosUsuarios;
    }
}