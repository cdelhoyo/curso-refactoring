package es.cdelhoyo.refactoring;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        double precioDelProductoConRebaja = producto.getPrecio() * producto.getRebaja();
        double precioDeTodosLosProductoConRebaja = precioDelProductoConRebaja * cantidad;
        double rebajaPorMitadDePrecio = producto.getSegundoAMitadDePrecio() ? Math.floor(cantidad/2) * 0.5 * precioDelProductoConRebaja : 0;
        double precioDeLosProductoConRebajaYSegundoAMitadDePrecio = precioDeTodosLosProductoConRebaja - rebajaPorMitadDePrecio;
        GrupoEdad grupoDeEdadDelComprador = GrupoEdad.getGrupoDeEdadPorEdad(comprador.getEdad());
        double decuentoDeEdad = producto.getTipo().getRebajaPorGrupoDeEdad(grupoDeEdadDelComprador);
        double precioDeLosProductosConRebajaYDescuentoDeEdad = precioDeLosProductoConRebajaYSegundoAMitadDePrecio * decuentoDeEdad;
        double precioAñadidoPorIVA = precioDeLosProductoConRebajaYSegundoAMitadDePrecio * producto.getTipo().getIva();
        return precioDeLosProductosConRebajaYDescuentoDeEdad + precioAñadidoPorIVA;
    }

    private double redondearADosDecimales(double numero) {
        BigDecimal bd = new BigDecimal(numero);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
