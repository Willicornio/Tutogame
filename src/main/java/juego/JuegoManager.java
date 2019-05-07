package juego;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

public class JuegoManager  implements Interfaz {


    private HashMap<String, Usuario> usuarios;
    private List<Objetos> listaObjetos;
    private List<Inventario> listainventarios;
    private List<Mapa> listaMapas;



    private JuegoManager(){
        this.usuarios = new HashMap<>();
        this.listaObjetos = new LinkedList<>();
        //++++++++++++++++ OBJETOS POR DEFECTO++++++++++++++++++++++++++
        this.listaObjetos.add(0, new Objetos("Linterna",0,1,5));
        this.listaObjetos.add(1, new Objetos("Ganzua",1,1,10));
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    }

    private static Interfaz instance;
    public static Interfaz getInstance(){
        if (instance == null) instance = new JuegoManager();
        return instance;
    }


    @Override
    public boolean login(String nombre, String pass) {
        return false;
    }

    @Override
    public boolean logout(String idUser, String pass) {
        return false;
    }

    @Override
    public void eliminarUsuario(String idUser) {

        this.usuarios.remove(idUser); //LO EQUIVALENTE EN DAO AL DELETE

    }

    @Override
    public Usuario crearUsuario(String nombre, String pass, List<Objetos> listaObjetos) {


        Inventario i = crearInventario(listaObjetos);

        Usuario u = new Usuario(nombre,pass,i);


        this.usuarios.put(u.idUser,u); //LO EQUIVALENTE EN DAO AL SAVE

        return u;


    }


    @Override
    public void modificarDinero(String idUser, int cantidad) {

    }

    @Override
    public boolean checkPartidaEnCurso(String idUser) {
        return false;
    }

    @Override
    public void guardarPartidaEnCurso(String idUsuario, String idPartida, Partida p) {

    }

    @Override
    public void eliminarPartidaEnCurso(String idUsuario, String idPartida) {

    }

    @Override
    public void sumarPuntuacionFinal(String idUser, int puntuacion) {

    }

    @Override
    public void iniciarPartida(String idUser) {

    }

    @Override
    public void finalizarPartida(String idUser) {

    }

    @Override
    public void construirMapa(Mapa mapa, String idUser) {

    }

    @Override
    public void guardarPartida(String idUser) {

    }

    @Override
    public void moodificarVida(String idUser) {

    }



    @Override
    public void añadirUsuario(String idUser, String pass) {

    }

    @Override
    public List<Objetos> dameObjetos(){

        return this.listaObjetos;
    }

    @Override
    public void activarmeObjeto(String idObjeto, String idUser) {





    }

    @Override
    public void desactivarmeObjeto(String idObjecto, String idUser) {

    }

    @Override
    public Inventario crearInventario(List<Objetos> listaObjetos) {

        return new Inventario(listaObjetos);

    }


    @Override
    public void crearObjetoNuevo(Objetos objeto) {
        this.listaObjetos.add(objeto);

    }

    @Override
    public void eliminarObjeto(String idObjeto) {

    }
}
