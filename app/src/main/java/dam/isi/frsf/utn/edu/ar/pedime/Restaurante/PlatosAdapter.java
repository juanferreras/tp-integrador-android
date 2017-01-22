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
import dam.isi.frsf.utn.edu.ar.pedime.utils.Constantes;
import dam.isi.frsf.utn.edu.ar.pedime.utils.PlatoAccionadoListener;

/**
 * Created by arielkohan on 1/17/17.
 */

public class PlatosAdapter extends BaseAdapter {

    private Context context;
    private List<Plato> data;
    private static LayoutInflater inflater = null;

    private PlatoAccionadoListener listener;

    private final NumberFormat formatter = new DecimalFormat("#0.00");


    public PlatosAdapter(Context context, List<Plato> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPlatoAccionadoListener(PlatoAccionadoListener listener){
        this.listener = listener;
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
            final Plato plato = data.get(position);

            holder.textViewPlatoNombre.setText(plato.getNombre());
            holder.textViewPlatoDescripcion.setText(plato.getDescripcion());
            String precio = formatter.format(plato.getPrecio());
            holder.textViewPrecio.setText("$ " + precio);
            holder.buttonAgregarAPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(listener.platoAgregado(plato)){
                    listener.onPlatoRemovidoListener(plato);
                    ((Button)view).setText(R.string.agregar_al_pedido);
                } else {
                    listener.onPlatoAgregadoListener(plato);
                    ((Button)view).setText(R.string.sacar_pedido);
                }
                }
            });

            new DownloadImageTask(holder.imageViewPlato)
                    .execute(Constantes.API_URL + plato.getFoto());
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
