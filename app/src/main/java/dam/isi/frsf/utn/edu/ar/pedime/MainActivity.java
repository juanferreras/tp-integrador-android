package dam.isi.frsf.utn.edu.ar.pedime;

import android.app.NotificationManager;
import android.content.Context;
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

    private TextView mResultTextView;

    private String idMesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NotificationManager notifManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = (TextView) findViewById(R.id.result_textview);
        mResultTextView.setText(R.string.no_barcode_captured);

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
                    try {
                        final String idRestaurante = barcode.displayValue.split("\\|")[0];
                        idMesa = barcode.displayValue.split("\\|")[1];
                        new BuscarRestauranteTask(this).execute(idRestaurante);
                        mResultTextView.setText(R.string.buscando_restaurante);
                    } catch (Exception e) {
                        Toast.makeText(this, "Ocurrio un error al leer el código QR.", Toast.LENGTH_SHORT).show();
                        mResultTextView.setText(R.string.no_barcode_captured);
                    }
                } else mResultTextView.setText(R.string.no_barcode_captured);
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnResultadoRestauranteListener(Restaurante restaurante) {
        if(restaurante == null) {
            Toast.makeText(this, "No se encontró el restaurante.", Toast.LENGTH_SHORT).show();
            mResultTextView.setText(R.string.no_barcode_captured);
        } else{
            Log.i("RESTAURANTE: ", restaurante.toString());
            Toast.makeText(this, "Restaurante encontrado.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, RestauranteActivity.class);
            i.putExtra("restaurante", restaurante);
            i.putExtra("idMesa", idMesa);
            startActivity(i);
        }
    }

}
