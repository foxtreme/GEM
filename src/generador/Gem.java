/*
 * tomado de https://www.seas.gwu.edu/~drum/java/lectures/appendix/examples/uniform_random.java
 */
package generador;

import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chris
 */
public class Gem {

    static final long m = 2147483647L;
    static final long a = 48271L;
    static final long q = 44488L;
    static final long r = 3399L;

    // variable global, inicializar la semilla con algun valor
    // valor arbitrario diferente de 0
    static long r_seed = 12345678L;

    public Gem(){}
    
    //generador entre 0,1
    public double generarAleatorio() {
        //esto se hace para trucar el overflow de multiplicar numeros tan grandes
        long hi = r_seed / q;
        long lo = r_seed - q * hi;
        long t = a * lo - r * hi;
        if (t > 0) {
            r_seed = t;
        } else {
            r_seed = t + m;
        }
        return ((double) r_seed / (double) m);
    }
    
    /**
     * Distribucion Normal N(10,2)
     * @param ri numero aleatorio N()
     * @return 
     */
    public List<Double> convolucion(int cant){
        List<Double> numerosConvolucion = new ArrayList<Double>();
        double sum;
        for(int i=0;i<cant;i++){
            sum=0;
            for(int j=0;j<12;j++){
                sum = sum+generarAleatorio();
            }
            sum=sum-6;
            numerosConvolucion.add(sum);
            System.out.println("numero_"+i+": "+numerosConvolucion.get(i));
        }
        return numerosConvolucion;
        
    }
    

    public static void main(String[] args) {
        //generando 10000 numeros aleatorios
        double num=0;
        Gem gem = new Gem();
        gem.convolucion(1000);
    }
}
