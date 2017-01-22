package dam.isi.frsf.utn.edu.ar.pedime.Restaurante;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import dam.isi.frsf.utn.edu.ar.pedime.R;
import dam.isi.frsf.utn.edu.ar.pedime.model.Plato;
import dam.isi.frsf.utn.edu.ar.pedime.model.Restaurante;
import dam.isi.frsf.utn.edu.ar.pedime.utils.FirebaseInstanceService;
import dam.isi.frsf.utn.edu.ar.pedime.utils.PlatoAccionadoListener;
import dam.isi.frsf.utn.edu.ar.pedime.utils.RestClient;

public class RestauranteActivity extends AppCompatActivity implements PlatoAccionadoListener,View.OnClickListener{

    //TODO: ver donde poner esta constante.
    private final String API_URL = "http://pedime.herokuapp.com";

    private ListView listViewPlatos;
    private Button buttonPedir;
    private Button buttonLlamarMozo;

    private Restaurante restaurante;
    private PlatosAdapter adapter;

    private List<Plato> pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        Intent i = getIntent();

        restaurante = (Restaurante) i.getSerializableExtra("restaurante");

        listViewPlatos = (ListView) findViewById(R.id.listview_platos);
        buttonPedir = (Button) findViewById(R.id.button_pedir_comida);
        buttonLlamarMozo = (Button) findViewById(R.id.button_llamar_mozo);

        adapter = new PlatosAdapter(this, restaurante.getPlatos());
        adapter.setPlatoAccionadoListener(this);
        listViewPlatos.setAdapter(adapter);

        buttonLlamarMozo.setOnClickListener(this);
        buttonPedir.setOnClickListener(this);


    }

    @Override
    public void onPlatoAgregadoListener(Plato p) {
        if(pedido == null)
            pedido = new ArrayList<Plato>();

        pedido.add(p);
        Toast.makeText(this, "El Plato " + p.getNombre() + " fue agregado al pedido.", Toast.LENGTH_SHORT).show();
        actualizarBotonPedido();
    }


    @Override
    public void onPlatoRemovidoListener(Plato p) {
        pedido.remove(p);
        Toast.makeText(this, "El Plato " + p.getNombre() + " fue removido del pedido.", Toast.LENGTH_SHORT).show();
        actualizarBotonPedido();
    }

    @Override
    public boolean platoAgregado(Plato p) {
        if(pedido == null) return false;

        return pedido.contains(p);
    }


    private void actualizarBotonPedido() {
        Double precioFinal = 0.0;
        for(Plato p: pedido){
            precioFinal += p.getPrecio();
        }
        buttonPedir.setText(getString(R.string.pedir) + " - $" + precioFinal.toString());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_llamar_mozo: {
                Toast.makeText(this, "El mozo irá a su mesa en un instante.", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.button_pedir_comida: {
                realizarPedido();
                break;
            }

        }
    }

    private void realizarPedido(){
        if(pedido == null || pedido.isEmpty())
            Toast.makeText(this, "El pedido no tiene ningun plato. Por favor seleccione uno.", Toast.LENGTH_SHORT).show();
        else {
            if (restaurante.get_id() == null)
                Toast.makeText(this, "Ha ocurrido un problema con su pedido. Por favor llame al mozo.", Toast.LENGTH_SHORT).show();
            else {
                HttpURLConnection urlConnection = null;
                try {
                    JSONObject objeto = new JSONObject();
                    Double precioFinal = 0.0;
                    for (Plato p : pedido) {
                        precioFinal += p.getPrecio();
                    }

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    String token = preferences.getString("registration_id", null);

                    objeto.put("precioTotal", precioFinal);
                    objeto.put("platos", pedido);
                    objeto.put("token", token);
                    objeto.put("mesa", 1);

                    String str = objeto.toString();
                    byte[] datosAEnviar = str.getBytes("UTF-8");

                    URL url = new URL(API_URL + "/api/restaurantes/" + restaurante.get_id() + "/pedidos");

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setFixedLengthStreamingMode(datosAEnviar.length);
                    urlConnection.setRequestProperty("Content-Type", "application/json");

                    DataOutputStream flujoSalida = new DataOutputStream(urlConnection.getOutputStream());
                    flujoSalida.write(datosAEnviar);
                    flujoSalida.flush();
                    flujoSalida.close();

                    String reply;
                    InputStream in = urlConnection.getInputStream();
                    StringBuffer sb = new StringBuffer();
                    int chr;
                    while ((chr = in.read()) != -1) {
                        sb.append((char) chr);
                    }
                    reply = sb.toString();


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }

                Toast.makeText(this, "Su pedido llegará a su mesa en cualquier instante.", Toast.LENGTH_SHORT).show();
            }
        }
        }
}
