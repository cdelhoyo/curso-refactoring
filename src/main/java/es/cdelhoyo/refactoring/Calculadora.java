package es.cdelhoyo.refactoring;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Calculadora {

    public double calcular(List<Producto> productos, Comprador comprador){
        Map<Producto, Long> mapaDeCadaProductoConLaCantidadDeVecesRepetido = productos.stream().collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        double precioTotal = mapaDeCadaProductoConLaCantidadDeVecesRepetido.keySet().stream()
                .mapToDouble(producto -> calcularPrecioFinalDeTodasLasUnidadesDelProducto(comprador, producto, mapaDeCadaProductoConLaCantidadDeVecesRepetido.get(producto)))
                .sum();
        return redondearADosDecimales(precioTotal);
    }

    private double calcularPrecioFinalDeTodasLasUnidadesDelProducto(Comprador comprador, Producto producto, Long cantidad) {
        double precioDelProductoConRebaja = producto.getPrecio() * producto.getRebaja() * cantidad;
        double decuentoDeEdad = calcularDecuentoDeEdad(comprador, producto);
        double precioDelProductoConRebajaYDescuentoDeEdad = precioDelProductoConRebaja * decuentoDeEdad;
        double porcentajeDeIVAPorIVA = calcularPorcentajeDeIVAPorIVA(producto);
        double precioAñadidoPorIVA = precioDelProductoConRebaja * porcentajeDeIVAPorIVA;
        return precioDelProductoConRebajaYDescuentoDeEdad + precioAñadidoPorIVA;
    }

    private double calcularPorcentajeDeIVAPorIVA(Producto producto) {
        double porcentajeDeIVAPorIVA;
        if(producto.getTipo().equals("comida")){
            porcentajeDeIVAPorIVA = 0.06;
        }else if(producto.getTipo().equals("drogueria")){
            porcentajeDeIVAPorIVA = 0.09;
        }else if(producto.getTipo().equals("transporte")){
            porcentajeDeIVAPorIVA = 0.12;
        }else if(producto.getTipo().equals("vivienda")){
            porcentajeDeIVAPorIVA = 0.18;
        }else{
            porcentajeDeIVAPorIVA =  0.21;
        }
        return porcentajeDeIVAPorIVA;
    }

    private double calcularDecuentoDeEdad(Comprador comprador, Producto producto) {
        double decuentoDeEdad = 1;
        if(comprador.getEdad()>65 && producto.getTipo().equals("transporte")){
            decuentoDeEdad = 0.8;
        }else if(comprador.getEdad()<35 && producto.getTipo().equals("vivienda")){
            decuentoDeEdad = 0.8;
        }
        return decuentoDeEdad;
    }

    private double redondearADosDecimales(double numero) {
        BigDecimal bd = new BigDecimal(numero);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
