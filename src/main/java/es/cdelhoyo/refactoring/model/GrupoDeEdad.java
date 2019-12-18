package es.cdelhoyo.refactoring.model;

public enum GrupoDeEdad {
    JOVEN(35),
    ADULTO(65),
    JUBILADO(100000);

    private int maxEdad;

    GrupoDeEdad(int maxEdad) {
        this.maxEdad = maxEdad;
    }

    public static GrupoDeEdad createGrupoDeEdad(int edad){
        for (GrupoDeEdad grupoDeEdad  : GrupoDeEdad.values()){
            if(edad < grupoDeEdad.maxEdad){
                return grupoDeEdad;
            }
        }
        return JUBILADO;
    }
}
