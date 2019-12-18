package es.cdelhoyo.refactoring.model;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TipoProducto {
    COMIDA(0.06),
    DROGUERIA(0.09),
    TRANSPORTE(0.12, new Pair(GrupoDeEdad.JUBILADO, 0.80)),
    VIVIENDA(0.18, new Pair(GrupoDeEdad.JOVEN, 0.80)),

    GENERAL(0.21);

    private double iva;
    private final Map<GrupoDeEdad, Double> rebajaPorGrupoDeEdad = new HashMap();

    TipoProducto(double iva, Pair<GrupoDeEdad, Double>... pairRebajaPorGrupoDeEdadArray) {
        this.iva = iva;
        for(Pair<GrupoDeEdad, Double> pairRebajaPorGrupoDeEdad:pairRebajaPorGrupoDeEdadArray){
            rebajaPorGrupoDeEdad.put(pairRebajaPorGrupoDeEdad.getKey(), pairRebajaPorGrupoDeEdad.getValue());
        }
    }

    public double getRebajaPorGrupoDeEdad(GrupoDeEdad grupoDeEdad){
        return rebajaPorGrupoDeEdad.containsKey(grupoDeEdad)? rebajaPorGrupoDeEdad.get(grupoDeEdad).doubleValue() : 1;
    }


}
