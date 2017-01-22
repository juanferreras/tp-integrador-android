package dam.isi.frsf.utn.edu.ar.pedime.services;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.pedime.model.Plato;
import dam.isi.frsf.utn.edu.ar.pedime.model.Restaurante;
import dam.isi.frsf.utn.edu.ar.pedime.utils.BusquedaRestauranteListener;

/**
 * Created by Lucas on 21/01/2017.
 */

public class RealizarPedidoTask {


    //TODO: ver donde poner esta constante.
    private final String API_URL = "http://pedime.herokuapp.com";

    private BusquedaRestauranteListener listener;


}
