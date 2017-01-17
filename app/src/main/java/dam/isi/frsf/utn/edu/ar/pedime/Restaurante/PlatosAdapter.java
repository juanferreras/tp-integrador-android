package dam.isi.frsf.utn.edu.ar.pedime.Restaurante;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.pedime.R;
import dam.isi.frsf.utn.edu.ar.pedime.model.Plato;
import dam.isi.frsf.utn.edu.ar.pedime.model.Restaurante;
import dam.isi.frsf.utn.edu.ar.pedime.services.DownloadImageTask;

/**
 * Created by arielkohan on 1/17/17.
 */

public class PlatosAdapter extends BaseAdapter {

    private Context context;
    private List<Plato> data;
    private String API_URL = "http://pedime.herokuapp.com";
    private static LayoutInflater inflater = null;

    NumberFormat formatter = new DecimalFormat("#0.00");


    public PlatosAdapter(Context context, List<Plato> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (data == null) return  0;

        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView ==null) {
            vi = inflater.inflate(R.layout.plato_raw, null);

            holder = new ViewHolder();
            holder.textViewPlatoDescripcion= (TextView) vi.findViewById(R.id.plato_descripcion);
            holder.textViewPlatoNombre= (TextView) vi.findViewById(R.id.plato_title);
            holder.textViewPrecio= (TextView) vi.findViewById(R.id.text_precio);
            holder.imageViewPlato = (ImageView) vi.findViewById(R.id.imagen_plato);
            holder.buttonAgregarAPedido = (Button) vi.findViewById(R.id.button_agregar_pedido);

            vi.setTag(holder);

        } else
            holder = (ViewHolder) vi.getTag();


        if(data.size() <= 0 )
            holder.textViewPlatoNombre.setText("NO DATA"); //TODO: refactorizar y poner en la actividad un textview con algun comentario si no hay datos
        else {
            Plato plato = data.get(position);

            holder.textViewPlatoNombre.setText(plato.getNombre());
            holder.textViewPlatoDescripcion.setText(plato.getDescripcion());
            String precio = formatter.format(plato.getPrecio());
            holder.textViewPrecio.setText("$ " + precio);
            holder.buttonAgregarAPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: Agregar el pedido

                    Toast.makeText(context, "Plato seleccionado para agregar", Toast.LENGTH_SHORT).show();
                }
            });

            new DownloadImageTask(holder.imageViewPlato)
                    .execute(API_URL + plato.getFoto());
        }

        return vi;
    }

    public static class ViewHolder{

        public TextView textViewPlatoNombre;
        public TextView textViewPlatoDescripcion;
        public TextView textViewPrecio;
        public ImageView imageViewPlato;
        public Button buttonAgregarAPedido;
    }
}
