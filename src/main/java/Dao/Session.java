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
            // Setup the connection with the DB
           /*
            session = DriverManager
                    .getConnection("jdbc:mysql://localhost/feedback?"
                            + "user=root&password=dsa2019");
            */
           /* PARA USAR LA BASE DE DATOS QUE TENGO CREADA UTILIZAR ESTA LINIA Y CAMBIAR LA PASS*/
           session = DriverManager.getConnection("jdbc:mysql://localhost/feedback", "root", "dsa2019");

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


    /*if ( a == "Usuario"){

        String nombre;



        e.getName();

        preparedStatement = session
                .prepareStatement("insert into  feedback." + a +"values (default, ?, ?)");
        // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
        // Parameters start with 1
        preparedStatement.setString(1, Nombre);
        preparedStatement.setString(2, apellido);
        preparedStatement.executeUpdate();
    }


}catch (Exception fatal){
    throw fatal;
}


}




}






*/
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

        query = "UPDATE " + table + " SET nombre = caracaola WHERE ( id = " + id + " )";

        // UPDATE `feedback`.`usuario` SET `nombre` = 'este' WHERE (`id` = 'Willi25');


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


        //Field[] fields = o.getClass().getDeclaredFields();
        Field[] fields = clase.getDeclaredFields();

        query = "SELECT * FROM " + table + " WHERE id =?";

        System.out.println("query :"+query);
        System.out.println(id);
        PreparedStatement ps = this.session.prepareStatement(query);
        ps.setString(1, id);
/*
        for(Field f : fields){
            ps.setString(contador, new PropertyDescriptor(fields[contador - 1].getName(),o.getClass()).getReadMethod().invoke(o).toString() );
            contador++;

        }*/

        resultSet = ps.executeQuery();
        if (resultSet!=null) {
            if (resultSet.next()) {
                o = writeResultSet(resultSet, o);
            }
            else {
                System.out.println("resulset VACIO!!!!!!");
            }
        }

        ps.close();


        return  o;


    }




    private Object writeResultSet(ResultSet resultSet, Object instancia) throws Exception {
        try{
            int contador = 1 ;
            int p = 0;

//        if (resultSet!=null && resultSet.next()) {
            //else

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int nCols = rsmd.getColumnCount();

            System.out.println(rsmd.getColumnCount());
            System.out.println(rsmd.getColumnName(1));
            System.out.println(rsmd.getColumnName(2));
            System.out.println(rsmd.getColumnTypeName(1));
            System.out.println(rsmd.getColumnTypeName(2));

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




//--------------------------------------------PRUEBAS ADRI-----------------------------------------------------

//Esto se pone directamente en el DAO de OBJETOS (TE AHORRAS MUCHO PONIENDOLO AHÍ)







//ESTO ESTA EN EL DAO DE USUARIOS (TE AHORRAS MUCHO PONIENDOLO AHI)









    //----------------------------------------PRUEBAS ADRI-----------------------------------------------------




    public Statement getStatement () throws Exception{


        Statement st = this.session.createStatement();
        return st;

    }
}







