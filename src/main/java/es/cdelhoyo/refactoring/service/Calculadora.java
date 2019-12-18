package es.cdelhoyo.refactoring.service;

import es.cdelhoyo.refactoring.model.Comprador;
import es.cdelhoyo.refactoring.model.GrupoDeEdad;
import es.cdelhoyo.refactoring.model.Producto;
import es.cdelhoyo.refactoring.model.TipoProducto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Calculadora {

    public double calcular(List<Producto> productos, Comprador comprador) {
        Map<Producto, Long> mapaDeProductosConSuCantidad = productos.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.<Producto>counting()));

        double precioTotal = mapaDeProductosConSuCantidad.keySet().stream()
                .mapToDouble(producto -> calcularPrecioDelProducto(producto, comprador, mapaDeProductosConSuCantidad.get(producto)))
                .sum();

        return redondearADosDecimales(precioTotal);
    }

    private double calcularPrecioDelProducto(Producto producto, Comprador comprador, Long cantidad) {
        double precioDelProductoConRebaja = producto.getPrecio() * producto.getRebaja();
        double descuentoPorMitad = getDescuentoPorMitad(producto, cantidad, precioDelProductoConRebaja);
        double precioDelProductoConRebajaYMitad = precioDelProductoConRebaja * cantidad - descuentoPorMitad;

        double descuentoPorEdad = producto.getTipo().getRebajaPorGrupoDeEdad(GrupoDeEdad.createGrupoDeEdad(comprador.getEdad()));
        double precioDelProductoConRebajaYdescuentoDeEdad = precioDelProductoConRebajaYMitad * descuentoPorEdad;

        double iva = producto.getTipo().getIva();
        double precioAñadidoPorIva = precioDelProductoConRebajaYMitad * iva;

        return precioDelProductoConRebajaYdescuentoDeEdad + precioAñadidoPorIva;
    }

    private double getDescuentoPorMitad(Producto producto, Long cantidad, double precioDelProductoConRebaja) {
        return producto.isSegundoAMitadDePrecio() ? precioDelProductoConRebaja * Math.floor(cantidad / 2) * 0.5 : 0;
    }

    private double redondearADosDecimales(double result) {
        BigDecimal bd = new BigDecimal(result);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
