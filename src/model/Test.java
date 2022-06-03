package model;

import dao.OdontologoDao;
import java.io.File;
import java.sql.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Test {

    private static final String SQL_CREATE = "DROP TABLE IF EXISTS ODONTOLOGOS; CREATE TABLE ODONTOLOGOS" +
            "(ID INT PRIMARY KEY NOT NULL," +
            "APELLIDO varchar(150) NOT NULL," +
            "NOMBRE varchar(150) NOT NULL," +
            "MATRICULA varchar(150) NOT NULL)";
    private static final String SQL_INSERT = "INSERT INTO ODONTOLOGOS (ID, APELLIDO, NOMBRE, MATRICULA) values (?,?,?,?)";

    private static final String SQL_LIST = "SELECT * FROM ODONTOLOGOS";

    public static void main(String[] args) throws Exception {
        File log4jProperties = new File("src/config/log4j.properties");
        PropertyConfigurator.configure(log4jProperties.getAbsolutePath());
        Logger logger = Logger.getLogger(Test.class);

        Connection connection = null;

        // Creo algunos odontologos para poder trabajar con los datos
        Odontologo odontologo1 = new Odontologo(1, "Sabrina", "Lerman", 334455);
        Odontologo odontologo2 = new Odontologo(2, "Arik", "Lerman", 223311);
        Odontologo odontologo3 = new Odontologo(3, "Pablo", "Carrascal", 998877);

        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute(SQL_CREATE);

            logger.info("Aguarde mientras se cargan los odontologos a la base de datos");
            PreparedStatement psinsert = connection.prepareStatement(SQL_INSERT);
            psinsert.setInt(1, 1);
            psinsert.setString(2, odontologo1.getApellido());
            psinsert.setString(3, odontologo1.getNombre());
            psinsert.setInt(4, odontologo1.getMatricula());
            psinsert.execute();

            PreparedStatement psinsert2 = connection.prepareStatement(SQL_INSERT);
            psinsert2.setInt(1, 2);
            psinsert2.setString(2, odontologo2.getApellido());
            psinsert2.setString(3, odontologo2.getNombre());
            psinsert2.setInt(4, odontologo2.getMatricula());
            psinsert2.execute();

            PreparedStatement psinsert3 = connection.prepareStatement(SQL_INSERT);
            psinsert3.setInt(1, 3);
            psinsert3.setString(2, odontologo3.getApellido());
            psinsert3.setString(3, odontologo3.getNombre());
            psinsert3.setInt(4, odontologo3.getMatricula());
            psinsert3.execute();
            logger.info("Odontologos cargados a la base de datos");

            //connection.setAutoCommit(false);

            //logger.info("Aguarde mientras se lista la base de datos de odontologos");
            //PreparedStatement psInsert1 = connection.prepareStatement(SQL_LIST);
            //psInsert1.execute();

            connection.setAutoCommit(true);


        } catch (Exception e) {
            logger.error(e.getMessage());
            connection.rollback();

        } finally {
            connection.close();
        }
    }



    private static void listar(Connection connection) throws Exception {
        File log4jProperties = new File("src/config/log4j.properties");
        PropertyConfigurator.configure(log4jProperties.getAbsolutePath());
        Logger logger = Logger.getLogger(Test.class);

        //Connection connection = null;


        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet listaOdontologos = statement.executeQuery(SQL_LIST);
            logger.info("Aguarde mientras se lista la base de datos de odontologos");

            while (listaOdontologos.next()) {
                System.out.println(listaOdontologos.getString(2) + ", " + listaOdontologos.getString(3) + ". MATRÍCULA: " + listaOdontologos.getString(4));
            }
        } catch(Exception e){
            logger.error(e.getMessage());
            connection.rollback();

        } finally {
            connection.close();
        }
    }
    public static Connection getConnection() throws Exception {
        Class.forName("org.h2.Driver").newInstance();
        return DriverManager.getConnection("jdbc:h2:~/test", "sa", "");

    }
}
