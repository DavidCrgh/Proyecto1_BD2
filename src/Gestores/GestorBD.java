package Gestores;


import Modelo.Subasta;
import com.opencsv.CSVReader;
import com.sun.org.apache.regexp.internal.RE;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleSQLException;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            agregarVariables.setBigDecimal(2,porcentajeMejora);
            agregarVariables.setBigDecimal(3,incrementoMinimo);

            agregarVariables.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void crearSubasta(String aliasVendedor, Date tiempoInicio, Date tiempoFin, String descripcionItem,
                             String nombreImagen, BigDecimal precioBase, String detallesEntrega, int idSubcategoria){ // El id del item se obtiene en el stored procedure

        String subastaSQL = "{call C##PRINCIPALSCHEMA.crearSubasta(?,?,?,?,?,?,?,?)}"; //INSERT INTO ITEM(DESCRIPCION,FOTO,PRECIO_BASE,DETALLESENTREGA,IDSUBCATEGORIA) VALUES(?,?,?,?,?);
        try {
            FileInputStream imagen = new FileInputStream("Imagenes/"+nombreImagen);

            CallableStatement nuevaSubasta = conexion.prepareCall(subastaSQL);


            nuevaSubasta.setString(1,aliasVendedor);
            nuevaSubasta.setDate(2, tiempoInicio);
            nuevaSubasta.setDate(3,tiempoFin);
            nuevaSubasta.setString(4,descripcionItem);
            nuevaSubasta.setBinaryStream(5,imagen,imagen.available());

            nuevaSubasta.setBigDecimal(6,precioBase);
            nuevaSubasta.setString(7,detallesEntrega);
            nuevaSubasta.setInt(8,idSubcategoria);

            nuevaSubasta.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getCategorias(){ //Con la modalidad devuelvo la categoria o subcategorias asciadas
        ArrayList<String> categorias = new ArrayList<>();
        String sqlCategorias = "{call C##PRINCIPALSCHEMA.obtenerCategorias(?)}";

        try{
            CallableStatement ejecutarCat = conexion.prepareCall(sqlCategorias);
            ejecutarCat.registerOutParameter(1,OracleTypes.CURSOR);

            ejecutarCat.executeUpdate();

            ResultSet catObtenidas = (ResultSet) ejecutarCat.getObject(1);
            while(catObtenidas.next()){

                    categorias.add(catObtenidas.getString("ID")+"-"+catObtenidas.getString("DESCRIPCION"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return categorias;
    }

    public ArrayList<String> filtrarSubCategorias(int idCategoria){
        ArrayList<String> subCategorias = new ArrayList<String>();
        String sqlFiltro = "{call C##PRINCIPALSCHEMA.filtrarSubCategorias(?,?)}";
        try{
            CallableStatement filtrar = conexion.prepareCall(sqlFiltro);
            filtrar.setInt(1,idCategoria);
            filtrar.registerOutParameter(2,OracleTypes.CURSOR);
            filtrar.executeUpdate();

            ResultSet subCategoriasObtenidas = (ResultSet) filtrar.getObject(2);
            while(subCategoriasObtenidas.next()){
                subCategorias.add(subCategoriasObtenidas.getString("IDSUB")+"-"+subCategoriasObtenidas.getString("DESCRIPCION"));
            }
            subCategoriasObtenidas.close();
            filtrar.close();
        }catch(SQLException e){
            e.printStackTrace();
        }

        return subCategorias;
    }

    public ArrayList<Subasta> getSubastas(Date fechaSistema){
        String sqlSubastasBuenas = "{call C##PRINCIPALSCHEMA.getSubastasValidas(?,?)}";
        ArrayList<Subasta> subastas = new ArrayList<>();
        try{
            CallableStatement subastasBuenas = conexion.prepareCall(sqlSubastasBuenas);
            subastasBuenas.setDate(1,fechaSistema);
            subastasBuenas.registerOutParameter(2,OracleTypes.CURSOR);

            subastasBuenas.executeUpdate();

            ResultSet subastasDevueltas = (ResultSet) subastasBuenas.getObject(2);

            while(subastasDevueltas.next()){
                Subasta subastaAuxiliar = new Subasta(subastasDevueltas.getString("ID"),subastasDevueltas.getString("ALIASVENDEDOR"),
                        subastasDevueltas.getString("PRECIO_BASE"),subastasDevueltas.getString("DESCRIPCION"));

                subastas.add(subastaAuxiliar);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return subastas;
    }

    public void pujarPuja(String aliasComprador, int idItem, BigDecimal ofertaComprador, Date fechaPuja){
       String sqlPujar = "{call C##PRINCIPALSCHEMA.crearPuja(?,?,?,?)}";
       try{
           CallableStatement pujar = conexion.prepareCall(sqlPujar);
           pujar.setString(1,aliasComprador);
           pujar.setInt(2,idItem);
           pujar.setBigDecimal(3,ofertaComprador);
           pujar.setDate(4,fechaPuja);
           pujar.executeUpdate();
       }catch(SQLException e){
           e.printStackTrace();
       }
    }

    public int buscarIdItem(int idSubasta){
        String sqlItem = "{call C##PRINCIPALSCHEMA.buscarIdItem(?,?)}";
        int idItemDevuelto = 0;

        try{
            CallableStatement buscarIdItem = conexion.prepareCall(sqlItem);
            buscarIdItem.setInt(1,idSubasta);
            buscarIdItem.registerOutParameter(2,OracleTypes.CURSOR);
            buscarIdItem.executeUpdate();

            ResultSet idDevuelto = (ResultSet) buscarIdItem.getObject(2);

            while(idDevuelto.next()){
                idItemDevuelto = Integer.parseInt(idDevuelto.getString("IDITEM"));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return idItemDevuelto;
    }

    public void cargarImagen(){
        establecerConexionSuperUsuario();
        try{
            PreparedStatement cargar = conexion.prepareStatement("SELECT * FROM ITEM");
            ResultSet rs = cargar.executeQuery();

            while(rs.next()){
                Blob image = rs.getBlob("FOTO");
                byte barr[]=image.getBytes(1,(int)image.length());
                FileOutputStream fout = new FileOutputStream("Imagenes/caca.jpg");
                fout.write(barr);
                fout.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    } //FIXME
/*
    public void cargarCat() {
        establecerConexionSuperUsuario();

        String csvFile = "C:/Users/User/Desktop/Proyecto1_BD2/categorias.csv";
  //      BufferedReader reader = null;
        String [] line = null;
        String cvsSplitBy = ",";
        String categoriaActual = "";
        int idCategoria = 0;
        CSVReader reader = null;
        int cont = 0;
        try {
             reader = new CSVReader(new FileReader(csvFile), ',', '"');
          //  reader= new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"));
                  //  br = new BufferedReader(new FileReader(csvFile, ));
           // br.readLine();
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                if(!line[0].equals(""))
                    System.out.println(line[0] + "    " +  line[1]);

                if(cont ==1){
                    categoriaActual= line[0]; //Agarra la primer categoria
                    String sql = "INSERT INTO CATEGORIA(DESCRIPCION) VALUES(?)";
                    String returnCols[] = { "ID" };
                    PreparedStatement caca = conexion.prepareStatement(sql, returnCols);
                    caca.setString(1,categoriaActual);
                    caca.execute();
                    ResultSet llave = caca.getGeneratedKeys();

                    while(llave.next()){
                        idCategoria = llave.getInt(1);
                    }
                    llave.close();
                    caca.close();
                    insertarSubcategoria(idCategoria, line[1]);
                }

                String[] country = line;

                if(!country[0].equals("") && cont!=1){
                    if(categoriaActual.equals(country[0])){
                       insertarSubcategoria(idCategoria,country[1]);
                    }
                    else{
                        categoriaActual= line[0];
                        String sql2 = "INSERT INTO CATEGORIA(DESCRIPCION) VALUES(?)";
                        String returnCols[] = { "ID" };
                        PreparedStatement caca2 = conexion.prepareStatement(sql2, returnCols);
                        caca2.setString(1,categoriaActual);
                        caca2.execute();
                        ResultSet llave2 = caca2.getGeneratedKeys();

                        while(llave2.next()){
                            idCategoria = llave2.getInt(1);
                        }
                        llave2.close();
                        caca2.close();
                        insertarSubcategoria(idCategoria, line[1]);
                    }

                }

                cont++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void  insertarSubcategoria(int idCategoria, String descripcionSub){
        if(conexion==null)
            establecerConexionSuperUsuario();
        String s = "{call C##PRINCIPALSCHEMA.pruebaImagen(?,?)}";


        try{
            CallableStatement c = conexion.prepareCall(s);
            c.setInt(1,idCategoria);
            c.setString(2,descripcionSub);
            c.executeUpdate();

            c.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    */
}