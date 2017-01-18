package dam.isi.frsf.utn.edu.ar.pedime.utils;

import dam.isi.frsf.utn.edu.ar.pedime.model.Plato;

/**
 * Created by arielkohan on 1/18/17.
 */

public interface PlatoAccionadoListener {

    public void onPlatoAgregadoListener(Plato p);
    public void onPlatoRemovidoListener(Plato p);
    public boolean platoAgregado(Plato p);
}
