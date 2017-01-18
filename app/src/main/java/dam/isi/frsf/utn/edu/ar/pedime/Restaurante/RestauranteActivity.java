package dam.isi.frsf.utn.edu.ar.pedime.Restaurante;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.pedime.R;
import dam.isi.frsf.utn.edu.ar.pedime.model.Plato;
import dam.isi.frsf.utn.edu.ar.pedime.model.Restaurante;
import dam.isi.frsf.utn.edu.ar.pedime.utils.PlatoAccionadoListener;

public class RestauranteActivity extends AppCompatActivity implements PlatoAccionadoListener,View.OnClickListener{

    private ListView listViewPlatos;
    private Button buttonPedir;
    private Button buttonLlamarMozo;

    private Restaurante restaurante;
    private PlatosAdapter adapter;

    private List<Plato> pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                //TODO: PEDIR LA COMIDA (POST)
                //VALIDAR QUE HAYA POR LO MENOS UN PLATO PEDIDO. SINO MOSTRAR ALGUN MENSAJE ACLARANDO LA SITUACION.

                Toast.makeText(this, "Su pedido llegará a su mesa en cualquier instante.", Toast.LENGTH_SHORT).show();

                break;
            }

        }
    }
}
