package dam.isi.frsf.utn.edu.ar.pedime;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dam.isi.frsf.utn.edu.ar.pedime.Restaurante.RestauranteActivity;
import dam.isi.frsf.utn.edu.ar.pedime.barcode.BarcodeCaptureActivity;
import dam.isi.frsf.utn.edu.ar.pedime.model.Restaurante;
import dam.isi.frsf.utn.edu.ar.pedime.services.BuscarRestauranteTask;
import dam.isi.frsf.utn.edu.ar.pedime.utils.BusquedaRestauranteListener;

public class MainActivity extends AppCompatActivity implements BusquedaRestauranteListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;

    private final String API_URL = "http://pedime.herokuapp.com";

    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = (TextView) findViewById(R.id.result_textview);

        Button scanBarcodeButton = (Button) findViewById(R.id.scan_barcode_button);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;

                    // buscamos el id de restaurante en la API

                    final String idRestaurante = barcode.displayValue;
                    mResultTextView.setText(R.string.buscando_restaurante);


                    new BuscarRestauranteTask(this).execute(idRestaurante);
                    



                } else mResultTextView.setText(R.string.no_barcode_captured);
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnResultadoRestauranteListener(Restaurante restaurante) {
        if(restaurante == null)
            Toast.makeText(this, "No se encontró el restaurante.", Toast.LENGTH_SHORT).show();
        else{
            Log.i("RESTAURANTE: ", restaurante.toString());
            Toast.makeText(this, "Restaurante encontrado.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, RestauranteActivity.class);
            i.putExtra("restaurante", restaurante);
            startActivity(i);
        }
    }

}
