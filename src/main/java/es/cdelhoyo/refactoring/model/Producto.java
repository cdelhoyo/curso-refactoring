package es.cdelhoyo.refactoring.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Producto {
    private String nombre;
    private double precio;
    private TipoProducto tipo;
    private double rebaja; // Entre 0 y 1 (en porcentaje)
    private boolean segundoAMitadDePrecio; // Todas los productos con el mismo nombre tendran este boolean igual
}
