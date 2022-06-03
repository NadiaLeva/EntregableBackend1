package dao;

import servicio.IDao;
import model.Odontologo;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDao implements IDao<Odontologo> {

    private static final String SQL_INSERT = "INSERT INTO ODONTOLOGOS(ID, NOMBRE, APELLIDO, MATRICULA) VALUES (?,?,?,?)";
    private static final String SQL_LIST = "SELECT * FROM ODONTOLOGOS";

    private static final Logger logger = null;

    public OdontologoDao() {
        File log4jProperties = new File("src/config/log4j.properties");
        PropertyConfigurator.configure(log4jProperties.getAbsolutePath());
        Logger logger = Logger.getLogger(OdontologoDao.class);
    }

    public Odontologo guardar(Odontologo odontologo) throws SQLException {

        Connection connection = null;
        try {
            connection = getConnection();
            logger.info("Guardando un nuevo odontologo: " + odontologo.getNombre());
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, odontologo.getNombre());
            preparedStatement.setString(3, odontologo.getApellido());
            preparedStatement.setInt(4, odontologo.getMatricula());

            preparedStatement.execute();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            connection.close();
        }

        return odontologo;
    }

    public List<Odontologo> listar() throws SQLException {
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList();
        try {
            connection = getConnection();
            logger.info("Aguarde mientras se lista la base de datos de odontologos");
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_LIST);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer id = resultSet.getInt("ID");
                String nombre = resultSet.getString("NOMBRE");
                String apellido = resultSet.getString("APELLIDO");
                Integer matricula = resultSet.getInt("MATRICULA");

                Odontologo odontologo = new Odontologo(id, nombre, apellido, matricula);
                odontologo.setId(id);
                odontologo.setMatricula(matricula);
                odontologo.setNombre(nombre);
                odontologo.setApellido(apellido);

                odontologos.add(odontologo);

            }
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println("Está saltando el catch");
            logger.error(e.getMessage());
        } finally {
            connection.close();
        }

        return odontologos;
    }

    private Connection getConnection() throws Exception {
        Class.forName("org.h2.Driver").newInstance();
        return DriverManager.getConnection("jdbc:h2:~/test;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "");
    }
}
