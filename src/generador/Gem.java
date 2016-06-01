/*
 * generador tomado de https://www.seas.gwu.edu/~drum/java/lectures/appendix/examples/uniform_random.java
 * adaptado para que evitar el overflow de acuerdo a https://en.wikipedia.org/wiki/Lehmer_random_number_generator
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

    // variable global, inicializar la semilla con algun valor arbitrario diferente de 0
    static long r_seed = 12345678L;

    public Gem() {
    }

    /**
     * *
     * Generador de numeros pseudoaleatorios entre 0 y 1
     *
     * @return numero pseudoaleatorio
     */
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
     *
     * @param ri numero aleatorio N()
     * @return
     */
    public List<Double> convolucion(int cant) {
        List<Double> numerosConvolucion = new ArrayList<Double>();
        double sum;
        for (int i = 0; i < cant; i++) {
            sum = 0;
            for (int j = 0; j < 12; j++) {
                sum = sum + generarAleatorio();
            }
            sum = ((sum - 6) * 2) + 10;
            numerosConvolucion.add(sum);
        }
        return numerosConvolucion;

    }

    /**
     * Genera una cantidad dada de numeros en distribucion de Poisson con landa
     * = 0.35
     *
     * @param cant cantidad de numeros aleatorios a ser transformados a
     * distribucion poisson
     * @return Lista de numeros Poisson
     */
    public List<Integer> poisson(int cant) {
        List<Integer> numerosPoisson = new ArrayList<Integer>();
        double expo = 0, tiempo = 0, ri;
        int numPoisson = 0;
        String strPoisson = "";
        System.out.println("\tri\t\t tiempo_entre_llegadas\t\t tiempo_de_llegada\t\t\t n(Poisson)");
        //crea los numeros en distribucion exponencial
        for (int i = 0; i < cant; i++) {
            ri = generarAleatorio();
            expo = (1 / 0.35) * (-1) * Math.log(ri);
            tiempo = tiempo + expo;
            if (tiempo < 1) {
                numPoisson++;
                strPoisson = "" + numPoisson;
                System.out.println(ri + "\t" + expo + "\t\t\t" + tiempo + "\t\t\t" + strPoisson);
            } else {
                numerosPoisson.add(numPoisson);
                numPoisson = 0;
                strPoisson = "0";
                System.out.println(ri + "\t" + expo + "\t\t\t" + tiempo + "\t\t\t" + strPoisson);
                tiempo = 0;
            }
        }
        return numerosPoisson;
    }

    
    /**
     * Genera una cantidad dada de numeros en distribucion binomial con p=0.3 y n=100
     * @param cant cantidad de numeros aleatorios a ser transformados a distribucion binomial
     */
    public List<Integer> binomial(int cant){
        int n=100,numBinomial=0,cantNumerosBinomial = cant/n;
        double p=0.3;
        List<Integer> numerosBinomial = new ArrayList<Integer>();
        for(int j=0;j<cantNumerosBinomial;j++){
            System.out.println("r1,r2...r100:");
            System.out.println("-----------------------");
            for(int i=0;i<n;i++){
                double ri=generarAleatorio();
                System.out.print(ri+" , ");
                if(ri<=p){
                    numBinomial++;
                    numerosBinomial.add(numBinomial);
                }if(i%20==0){
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println("-----------------------");
            System.out.println("numero Binomial: "+numBinomial);
            System.out.println(" #####################################");
            numBinomial=0;            
        }
        return numerosBinomial;        
    }
    
    public static void main(String[] args) {
        //generando 10000 numeros aleatorios
        double num = 0;
        Gem gem = new Gem();
        gem.binomial(1000);

    }
}
