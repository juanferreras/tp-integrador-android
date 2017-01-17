package dam.isi.frsf.utn.edu.ar.pedime.Restaurante;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import dam.isi.frsf.utn.edu.ar.pedime.R;
import dam.isi.frsf.utn.edu.ar.pedime.model.Plato;
import dam.isi.frsf.utn.edu.ar.pedime.model.Restaurante;

public class RestauranteActivity extends AppCompatActivity {

    private ListView listViewPlatos;

    private Restaurante restaurante;
    private PlatosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        Intent i = getIntent();

        restaurante = (Restaurante) i.getSerializableExtra("restaurante");

        listViewPlatos = (ListView) findViewById(R.id.listview_platos);
        adapter = new PlatosAdapter(this, restaurante.getPlatos());
        listViewPlatos.setAdapter(adapter);


    }
}
