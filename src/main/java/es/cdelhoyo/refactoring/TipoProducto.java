package es.cdelhoyo.refactoring;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public enum TipoProducto {
    COMIDA(0.06),
    DROGUERIA(0.09),
    TRANSPORTE(0.12, new Pair(GrupoEdad.JUBILADO, 0.80)),
    VIVIENDA(0.18, new Pair(GrupoEdad.JOVEN, 0.80)),
    GENERAL(0.21);

    private final double iva;
    private final Map<GrupoEdad, Double> rebajaPorGrupoDeEdad = new HashMap();

    TipoProducto(double iva, Pair<GrupoEdad, Double>... pairRebajaPorGrupoDeEdadArray) {

        this.iva = iva;
        for(Pair<GrupoEdad, Double> pairRebajaPorGrupoDeEdad:pairRebajaPorGrupoDeEdadArray)
            rebajaPorGrupoDeEdad.put(pairRebajaPorGrupoDeEdad.getKey(), pairRebajaPorGrupoDeEdad.getValue());
    }

    public double getIva() {
        return iva;
    }

    public double getRebajaPorGrupoDeEdad(GrupoEdad grupoDeEdad){
        return rebajaPorGrupoDeEdad.containsKey(grupoDeEdad)? rebajaPorGrupoDeEdad.get(grupoDeEdad).doubleValue() : 1;
    }

}

