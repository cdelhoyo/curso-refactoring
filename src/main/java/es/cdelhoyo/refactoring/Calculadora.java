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
                if(i%2==0){
                    productos2.add(p);
                }else{
                    if(productosConNombre.get(i).getSegundoAMitadDePrecio()){
                        p.setPrecio(p.getPrecio()/2);
                    }
                    productos2.add(p);
                }
            }
        }

        double result = .0;
        for (Producto p: productos2){
            if(p.getTipo().equals("comida")){
                result += p.getPrecio() * p.getRebaja();
                result += p.getPrecio() * p.getRebaja()*.06;
            }else if(p.getTipo().equals("drogueria")){
                result += p.getPrecio() * p.getRebaja();
                result += p.getPrecio() * p.getRebaja()*.09;
            }else if(p.getTipo().equals("transporte")){
                if(comprador.getEdad()>65){
                    result += p.getPrecio() * p.getRebaja() *0.8;
                    result += p.getPrecio() * p.getRebaja()*.12;
                }else{
                    result += p.getPrecio() * p.getRebaja();
                    result += p.getPrecio() * p.getRebaja()*.12;
                }
            }else if(p.getTipo().equals("vivienda")){
                if(comprador.getEdad()<35){
                    result += p.getPrecio() * p.getRebaja() *0.8;
                    result += p.getPrecio() * p.getRebaja()*.18;
                }else{
                    result += p.getPrecio() * p.getRebaja();
                    result += p.getPrecio() * p.getRebaja()*.18;
                }
            }else{
                result += p.getPrecio() * p.getRebaja();
                result += p.getPrecio() * p.getRebaja() *.21;
            }
        }
        BigDecimal bd = new BigDecimal(result);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
