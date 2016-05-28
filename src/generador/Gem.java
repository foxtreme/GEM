/*
 * tomado de https://www.seas.gwu.edu/~drum/java/lectures/appendix/examples/uniform_random.java
 */
package generador;

import static java.lang.Math.*;

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

    //generador entre 0,1
    public static double generarAleatorio() {
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

    public static void main(String[] argv) {
        //generando 10000 numeros aleatorios
        double num=0;
        for (int i = 1; i <= 10000; i++) {
            num= generarAleatorio();
            System.out.println("numero_"+i+": " + num);
        }
    }
}
