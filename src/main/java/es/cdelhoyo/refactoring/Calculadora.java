package es.cdelhoyo.refactoring;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculadora {

    public double calcular(List<Producto> productos, Comprador comprador){
        List<String> nombres = new ArrayList(); // nombres diferentes de los productos
        for (Producto p: productos){
            if(!nombres.contains(p.getNombre())) {
                nombres.add(p.getNombre());
            }
        }

        Map<String, List<Producto>> productosMap = new HashMap(); // mapa con la lista de productos que tienen el mismo nombre
        for(String n:nombres){
            List<Producto> productosConNombre = new ArrayList();
            for (Producto p: productos){
                if(p.getNombre().equals(n)){
                    productosConNombre.add(p);
                }
            }
            productosMap.put(n, productosConNombre);
        }

        List<Producto> productosConElSegundoAMitadDePrecioAplicado = new ArrayList();
        for (List<Producto> productosConNombre: productosMap.values()){
            for(int i=0; i< productosConNombre.size(); i++){
                Producto p = productosConNombre.get(i);
                if(i%2==0){ // Un producto que está en posicines pares nunca va a ser el segundo y nunca tendra rebaja de segundo a mitad de precio
                    productosConElSegundoAMitadDePrecioAplicado.add(p);
                }else{
                    if(productosConNombre.get(i).getSegundoAMitadDePrecio()){
                        p.setPrecio(p.getPrecio()/2);
                    }
                    productosConElSegundoAMitadDePrecioAplicado.add(p);
                }
            }
        }

        // En este momento ya tenemos la lista de productos igual que la que entra,
        // pero ya se ha aplicado la rebaja de mitad de precio a los productos que incluian la oferta
        // Esta lista la hemos dejado en productosConElSegundoAMitadDePrecioAplicado

        double precioTotal = 0.0;
        for (Producto producto: productosConElSegundoAMitadDePrecioAplicado){
            double precioTotalDelProducto = calcularPrecioTotalDelProducto(comprador, producto);
            precioTotal+=precioTotalDelProducto;
        }
        return redondearADosDecimales(precioTotal);
    }

    private double calcularPrecioTotalDelProducto(Comprador comprador, Producto producto) {
        double precioDelProductoConRebaja = producto.getPrecio() * producto.getRebaja();
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
