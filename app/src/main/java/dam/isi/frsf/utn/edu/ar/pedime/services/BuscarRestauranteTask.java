package dam.isi.frsf.utn.edu.ar.pedime.services;

import android.content.res.Resources;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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
 * Created by arielkohan on 1/17/17.
 */

public class BuscarRestauranteTask extends AsyncTask<String, Integer, Restaurante> {

    //TODO: ver donde poner esta constante.
    private final String API_URL = "http://pedime.herokuapp.com";

    private BusquedaRestauranteListener listener;


    public BuscarRestauranteTask(BusquedaRestauranteListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Restaurante restaurante) {

        listener.OnResultadoRestauranteListener(restaurante);
    }

    @Override
    protected Restaurante doInBackground(String... args) {
        Restaurante restauranteResultado = null;
        String idRestaurante = args[0];

        JSONObject restauranteData = getRestaurante(idRestaurante);
        restauranteResultado = parseJSONToRestaurante(restauranteData);

        return restauranteResultado;
    }

    private Restaurante parseJSONToRestaurante(JSONObject restauranteData)  {


        Restaurante resultado = new Restaurante();

        try{
            resultado.set_id(restauranteData.getString("_id"));
            resultado.setDireccion(restauranteData.getString("direccion"));
            resultado.setNombre(restauranteData.getString("nombre"));

            List<Plato> platos = new ArrayList<>();

            JSONArray platosJSON = restauranteData.getJSONArray("platos");
            for(int i = 0 ; i < platosJSON.length(); i++){
                Plato nuevoPlato = new Plato();
                JSONObject temp = platosJSON.getJSONObject(i);
                nuevoPlato.setNombre(temp.getString("nombre"));
                nuevoPlato.setDescripcion(temp.getString("descripcion"));
                nuevoPlato.setFoto(temp.getString("foto"));
                nuevoPlato.setPrecio(temp.getDouble("precio"));

                platos.add(nuevoPlato);
            }

            resultado.setPlatos(platos);

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return resultado;
        }

    }


    private JSONObject getRestaurante(String id) {
        JSONObject restauranteData = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(API_URL + "/api/restaurantes/" + id);

            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                sb.append(current);
                data = isw.read();
            }
            restauranteData = new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return restauranteData;
    }

}
