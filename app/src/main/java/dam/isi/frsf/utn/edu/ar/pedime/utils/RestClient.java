package dam.isi.frsf.utn.edu.ar.pedime.utils;

import android.os.StrictMode;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dam.isi.frsf.utn.edu.ar.pedime.model.Plato;

/**
 * Created by Lucas on 21/01/2017.
 */

public class RestClient {

    private final String API_URL = "http://pedime.herokuapp.com";

    public void realizarPedido(String idRestaurante, JSONObject objeto){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

            HttpURLConnection urlConnection = null;
            try {

                String str = objeto.toString();
                byte[] datosAEnviar = str.getBytes("UTF-8");

                URL url = new URL(API_URL + "/api/restaurantes/" + idRestaurante + "/pedidos");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(datosAEnviar.length);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                DataOutputStream flujoSalida = new DataOutputStream(urlConnection.getOutputStream());
                flujoSalida.write(datosAEnviar);
                flujoSalida.flush();
                flujoSalida.close();

                urlConnection.getResponseMessage();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
        }

}
