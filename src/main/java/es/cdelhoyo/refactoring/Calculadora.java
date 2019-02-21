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

        List<Producto> productos2 = new ArrayList(); // productos con el segundo a mitad de precio aplicado
        for (List<Producto> productosConNombre: productosMap.values()){
            for(int i=0; i< productosConNombre.size(); i++){
                Producto p = productosConNombre.get(i);
                if(i%2==0){ // Un producto que está en posicines pares nunca va a ser el segundo y nunca tendra rebaja de segundo a mitad de precio
                    productos2.add(p);
                }else{
                    if(productosConNombre.get(i).getSegundoAMitadDePrecio()){
                        p.setPrecio(p.getPrecio()/2);
                    }
                    productos2.add(p);
                }
            }
        }

        // En este momento ya tenemos la lista de productos igual que la que entra,
        // pero ya se ha aplicado la rebaja de mitad de precio a los productos que incluian la oferta
        // Esta lista la hemos dejado en productos2

        double precioTotal = 0.0;
        for (Producto p: productos2){
            double precioDelProductoConRebaja = p.getPrecio() * p.getRebaja();
            double decuentoDeEdad = calcularDecuentoDeEdad(comprador, p);
            double precioDelProductoConRebajaYDescuentoDeEdad = precioDelProductoConRebaja * decuentoDeEdad;
            double precioAñadidoPorIVA;
            if(p.getTipo().equals("comida")){
                precioAñadidoPorIVA = p.getPrecio() * p.getRebaja()*.06; // Añadimos IVA del 6% por tipo comida
            }else if(p.getTipo().equals("drogueria")){
                precioAñadidoPorIVA = p.getPrecio() * p.getRebaja()*.09; // Añadimos IVA del 9% por tipo drogueria
            }else if(p.getTipo().equals("transporte")){
                precioAñadidoPorIVA = p.getPrecio() * p.getRebaja()*.12; // Añadimos IVA del 12% (la rebaja de jubilado no aplica al IVA)
            }else if(p.getTipo().equals("vivienda")){
                precioAñadidoPorIVA = p.getPrecio() * p.getRebaja()*.18; // Añadimos IVA del 18% (la rebaja de joven no aplica al IVA)
            }else{
                precioAñadidoPorIVA = p.getPrecio() * p.getRebaja() *.21; // Añadimos IVA del 21% por tipo general
            }
            double precioTotalDelProducto = precioDelProductoConRebajaYDescuentoDeEdad + precioAñadidoPorIVA;
            precioTotal+=precioTotalDelProducto;
        }
        return redondearADosDecimales(precioTotal);
    }

    private double calcularDecuentoDeEdad(Comprador comprador, Producto p) {
        double decuentoDeEdad = 1;
        if(comprador.getEdad()>65 && p.getTipo().equals("transporte")){
            decuentoDeEdad = 0.8;
        }else if(comprador.getEdad()<35 && p.getTipo().equals("vivienda")){
            decuentoDeEdad = 0.8;
        }
        return decuentoDeEdad;
    }

    private double redondearADosDecimales(double result) {
        BigDecimal bd = new BigDecimal(result);
        bd = bd.setScale(2, RoundingMode.HALF_UP); // Redondeamos a dos decimales
        return bd.doubleValue();
    }
}
