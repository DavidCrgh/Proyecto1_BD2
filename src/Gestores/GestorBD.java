package Gestores;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import jdk.nashorn.internal.codegen.CompilerConstants;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleSQLException;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import java.io.IOException;
import java.math.BigDecimal;
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
            invocarAlerta("El usuario o cedula seleccionados ya han sido elegidos. Intente de nuevo.");
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
            invocarAlerta("La nueva cedula ya existe. Intente de nuevo.");
            e.printStackTrace();
        }
    }

    public ArrayList<String> devolverUsuarios(String alias,int atributo){
        ArrayList<String> aliasTelefonosUsuarios = new ArrayList<>();
        try{
            String sqlUsuarios = "{call C##PRINCIPALSCHEMA.retornarUsuarios(?,?,?)}";
            CallableStatement retornarUsuarios = conexion.prepareCall(sqlUsuarios);

            retornarUsuarios.setString(1,alias);
            retornarUsuarios.setInt(2,atributo);
            retornarUsuarios.registerOutParameter(3, OracleTypes.CURSOR);

            retornarUsuarios.executeUpdate();
            ResultSet aliasTelefonosDevueltos = (ResultSet)retornarUsuarios.getObject(3);

            switch(atributo){
                case 0: // Devuelve todos los alias
                    while (aliasTelefonosDevueltos.next()) {
                            aliasTelefonosUsuarios.add(aliasTelefonosDevueltos.getString("ALIAS"));
                    }
                    break;

                case 1: //Devuelve todos los telefonos
                    while (aliasTelefonosDevueltos.next()) {
                        aliasTelefonosUsuarios.add(aliasTelefonosDevueltos.getString("TELEFONO"));
                    }
                    break;

                default: //Devuelve una lista de toda la info de un solo usuario
                    while (aliasTelefonosDevueltos.next()) {
                        aliasTelefonosUsuarios.add(aliasTelefonosDevueltos.getString("CEDULA"));
                        aliasTelefonosUsuarios.add(aliasTelefonosDevueltos.getString("NOMBRE_APELLIDOS"));
                        aliasTelefonosUsuarios.add(aliasTelefonosDevueltos.getString("DIRECCION"));
                    }

            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return aliasTelefonosUsuarios;
    }

    public void eliminarTelefonoUsuario(String aliasUsuario, String telefonoEliminar){
        String procTelefono = "{call C##PRINCIPALSCHEMA.eliminarTelefonoUsuario(?,?)}";

        try{
            CallableStatement eliminarTelefono = conexion.prepareCall(procTelefono);
            eliminarTelefono.setString(1,aliasUsuario);
            eliminarTelefono.setString(2,telefonoEliminar);

            eliminarTelefono.executeUpdate();


        }catch(SQLException e){
            e.printStackTrace();

        }
    }

    public void modificarTelefonoUsuario(String alias, String numeroTelefonoNuevo, String numeroTelefonoViejo){
        String procModificarTelefono = "{call C##PRINCIPALSCHEMA.modificarTelefonoUsuario(?,?,?)}";

        try{
            CallableStatement modificarTelefono = conexion.prepareCall(procModificarTelefono);
            modificarTelefono.setString(1,alias);
            modificarTelefono.setString(2,numeroTelefonoNuevo);
            modificarTelefono.setString(3,numeroTelefonoViejo);

            modificarTelefono.executeUpdate();
        }catch(SQLException e){
            invocarAlerta("El nuevo telefono ya existe en la base de datos. Intente de nuevo.");
            e.printStackTrace();
        }
    }

    public void agregarNuevoTelefonoUsuario(String aliasUsuario,String nuevoTelefono){
        String procNuevoTelefono = "{call C##PRINCIPALSCHEMA.agregarNuevoTelefonoUsuario(?,?)}";
        try{
            CallableStatement agregarTelefono = conexion.prepareCall(procNuevoTelefono);
            agregarTelefono.setString(1,aliasUsuario);
            agregarTelefono.setString(2,nuevoTelefono);

            agregarTelefono.executeUpdate();


        }catch(SQLException e){
            invocarAlerta("El telefono ingresadoya existe en la base de datos. Intente de nuevo.");
            e.printStackTrace();
        }
    }

    public void agregarNuevasVariables(String aliasAdministrador, BigDecimal porcentajeMejora, BigDecimal incrementoMinimo){

        String procedimientoVaribles = "{call C##PRINCIPALSCHEMA.agregarNuevasVariables(?,?,?)}";

        try{
            CallableStatement agregarVariables = conexion.prepareCall(procedimientoVaribles);
            agregarVariables.setString(1,aliasAdministrador);
            agregarVariables.setBigDecimal(2,incrementoMinimo);
            agregarVariables.setBigDecimal(3,porcentajeMejora);

            agregarVariables.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}