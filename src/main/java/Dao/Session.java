package Dao;

import io.swagger.models.auth.In;
import juego.Inventario;
import juego.Objeto;
import juego.Usuario;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Session {

    Connection session = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    protected Session() {

        try {
            this.session = getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private Connection getConnection() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");

           session = DriverManager.getConnection("jdbc:mysql://localhost/dsa", "root", "Mazinger72");

            return session;

        } catch (Exception e) {

            throw e;
        }


    }


    public void close() throws Exception {
        //session.close();
    }


    public void save(Object o) throws Exception {


        statement = session.createStatement();
        String preparedQuery = null;
        String query = null;
        // Result set get the result of the SQL query
        String table;
        int contador = 1;
        table = o.getClass().getSimpleName();

        System.out.println(table);


        Field[] fields = o.getClass().getDeclaredFields();


        for (int i = 1; i <= fields.length; i++) {

            if (i == 1) {

                preparedQuery = "?,";

            }
            if (i != fields.length & i != 1) {

                preparedQuery += "?,";
            }
            if (i == fields.length)
                preparedQuery += "?";
        }


        query = "INSERT INTO " + table + " VALUES (" + preparedQuery + ")";
        System.out.println(query);
        for (Field f : fields) {
            System.out.println(fields.length);
        }

        PreparedStatement ps = this.session.prepareStatement(query);

        for (Field f : fields) {
            ps.setString(contador, new PropertyDescriptor(fields[contador - 1].getName(), o.getClass()).getReadMethod().invoke(o).toString());
            contador++;

        }

        ps.executeUpdate();
        ps.close();
    }


    public void update ( Object o, String id) throws Exception {

        statement = session.createStatement();
        String preparedQuery = null;
        String query = null;
        // Result set get the result of the SQL query
        String table;
        int contador = 1;
        table = o.getClass().getSimpleName();

        System.out.println(table);


        Field[] fields = o.getClass().getDeclaredFields();


        for (int i = 1; i <= fields.length; i++) {

            if (i == 1) {

                preparedQuery = "? = ?,";

            }
            if (i != fields.length & i != 1) {

                preparedQuery += "? = ?,";
            }
            if (i == fields.length)
                preparedQuery += "? =? ";
        }


        query = "UPDATE " + table + " SET (" + preparedQuery + ") WHERE id =" + id;
        System.out.println(query);
        for (Field f : fields) {
            System.out.println(fields.length);
        }

        PreparedStatement ps = this.session.prepareStatement(query);
        contador = (fields.length-1)*2;
        for (int cosa = 0; cosa < contador; cosa ++) {


            if (contador%2 == 0)
                ps.setString(contador, new PropertyDescriptor(fields[contador +1].getName(), o.getClass()).getReadMethod().invoke(o).toString());

            if (contador%2 != 0){
                ps.setString(contador, new PropertyDescriptor(fields[contador +1].getName(), o.getClass()).getName().toString());
            }
            contador = contador +1;


            ps.executeUpdate();
            ps.close();


        }
    }

    public Object get( String id, Class clase) throws Exception {

        Object o = clase.newInstance();
        String table = o.getClass().getSimpleName();
        statement = session.createStatement();
        String preparedQuery = null;
        String query = null;
        int contador = 1;

        Field[] fields = clase.getDeclaredFields();

        query = "SELECT * FROM " + table + " WHERE id =?";

        System.out.println("query :"+query);


        PreparedStatement ps = this.session.prepareStatement(query);
        ps.setString(1, id);

        System.out.println("1");


        resultSet = ps.executeQuery();

        System.out.println("2");

        if (resultSet!=null) {
            if (resultSet.next()) {
                o = writeResultSet(resultSet, o);
            }
            else {
                System.out.println("resulset VACIO!!!!!!");
            }
        }
        System.out.println("3");

        ps.close();


        return  o;
    }

    private Object writeResultSet(ResultSet resultSet, Object instancia) throws Exception {
        try{
            int contador = 1 ;
            int p = 0;

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int nCols = rsmd.getColumnCount();
            int i =1;
            String property = null;

            Method m = null;
            while (i <= nCols) {

                property = rsmd.getColumnName(i);
                m = findMethod(instancia.getClass(), property);
                int type = rsmd.getColumnType(i);
                Object result = null;
                switch (type) {
                    case Types.VARCHAR:
                        result = resultSet.getString(i);
                        break;
                    case Types.INTEGER:
                        result = resultSet.getInt(i);
                        System.out.println("INT "+result);
                        break;
                    case Types.DATE:
                        result = resultSet.getDate(i);
                        break;
                }

                    setter(instancia, m, result);
                i++;
            }

            return instancia;

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("no va");
            throw e;
        }
    }

    private Method findMethod(Class theClass, String property) {
        Method[] methods = theClass.getDeclaredMethods();
        String nombreMetodo = "set"+property.substring(0,1)
        .toUpperCase()+property.substring(1);

        System.out.println("metodo a buscar "+nombreMetodo);

        for (Method m: methods) {
            if (m.getName().equals(nombreMetodo)) return m;
        }
        return null;
    }

    public Object setter (Object a, Method m, Object result) throws Exception{

        if (m!=null) System.out.println(m.getName()+" result"+ result);
        else System.out.println("método no encontrado!!");
        m.invoke(a, result);
        return a;
    }

    public Statement getStatement () throws Exception{


        Statement st = this.session.createStatement();
        return st;

    }
}







