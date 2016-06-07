/*
 * generador tomado de https://www.seas.gwu.edu/~drum/java/lectures/appendix/examples/uniform_random.java
 * adaptado para que evitar el overflow de acuerdo a https://en.wikipedia.org/wiki/Lehmer_random_number_generator
 */
package generador;

import java.io.IOException;
import static java.lang.Math.*;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
        System.out.println("--------Convolucion----------");
        System.out.println("cantidad de datos: "+cant);
        List<Double> numerosConvolucion = new ArrayList<Double>();
        double sum;
        for (int i = 0; i < cant; i++) {
            sum = 0;
            for (int j = 0; j < 12; j++) {
                sum = sum + generarAleatorio();
            }
            sum = ((sum - 6) * 2) + 10;
            numerosConvolucion.add(sum);
            System.out.println(""+sum);
        }
        return numerosConvolucion;

    }

    /**
     * Genera una cantidad dada de numeros en distribucion de Poisson con
     * landa = 0.35
     *
     * @param cant cantidad de numeros aleatorios a ser transformados a
     * distribucion poisson
     * @return Lista de numeros Poisson
     */
    public List<Integer> poisson(int cant) {
        System.out.println("--------Poisson----------");
        System.out.println("cantidad de datos: "+cant);
        List<Integer> numerosPoisson = new ArrayList<Integer>();
        double expo = 0, tiempo = 0, ri;
        int numPoisson = 0, count=0;
        System.out.println("\tri\t\t tiempo_entre_llegadas\t\t tiempo_de_llegada\t\t\t n(Poisson)");
        //crea los numeros en distribucion exponencial
        for (int i = 0; i < cant; i++) {
            ri = generarAleatorio();
            expo = (1 / 0.35) * (-1) * Math.log(ri);
            tiempo = tiempo + expo;
            if(tiempo>=1) {
                numerosPoisson.add(numPoisson);
                System.out.println(ri + "\t" + expo + "\t\t\t" + tiempo + "\t\t\t" + numPoisson);
                numPoisson = 0;
                tiempo = 0;
            }
            else {
                numPoisson++;
                System.out.println(ri + "\t" + expo + "\t\t\t" + tiempo + "\t\t\t");
            } 
        }
        return numerosPoisson;
    }

    /**
     * Genera una cantidad dada de numeros en distribucion binomial con
     * p=0.3 y n=100
     *
     * @param cant cantidad de numeros aleatorios a ser transformados a
     * distribucion binomial
     */
    public List<Integer> binomial(int cant) {
        System.out.println("--------Binomial----------");
        System.out.println("cantidad de datos: "+cant);
        int n = 100, numBinomial = 0, cantNumerosBinomial = cant / n;
        double p = 0.3;
        List<Integer> numerosBinomial = new ArrayList<Integer>();
        for (int j = 0; j < cantNumerosBinomial; j++) {
            System.out.println("r1,r2...r100:");
            System.out.println("-----------------------");
            for (int i = 0; i < n; i++) {
                double ri = generarAleatorio();
                
                if(ri>p){//el numero es un fracaso
                    System.out.print("0, ");
                }
                if (ri <= p) {//el numero es un exito
                    System.out.print("1, ");
                    numBinomial++;
                    numerosBinomial.add(numBinomial);
                }
                if (i % 20 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println("-----------------------");
            System.out.println("numero Binomial: " + numBinomial);
            System.out.println(" #####################################");
            numBinomial = 0;
        }
        return numerosBinomial;
    }

    /**
     * Aplica la prueba de chi cuadrado a una lista de numeros pseudoaleatorios
     * @param lista con los numeros pseudoaleatorios
     * @return true si pasa la prueba y false si la lista de numeros es rechazada
     */
    public boolean pruebachi2(List<Double> lista) {
        System.out.println("----------Prueba Chi cuadrado----------");
        double chicrit = 0;
        double cant = lista.size();//cantidad de datos
        if (cant == 1000) {
            chicrit = 41.4217;//de acuerdo a la tabla y al numero de clases que determinan los grados de libertad
        }
        if (cant == 10000) {
            chicrit = 118.4980;//de acuerdo a la tabla y al numero de clases que determinan los grados de libertad
        }
        double clases = ceil(sqrt(cant));//numero de clases
        double gl = clases - 1;//numero de grados de libertad
        double FE = cant / clases; //frecuencia esperada en cada intervalo
        System.out.println("clases: " + clases);
        System.out.println("gl: " + gl);
        System.out.println("Rango \t\t\t\t FO \t\t FE \t\t (FE-FO)^2/FE");
        double chicalc = 0;
        int FOsum = 0;
        for (double i = 0; i < 1; i = i + (1 / clases)) {
            int FO = countOnRange(i, (i + (1 / clases)), lista);
            FOsum += FO;
            double chicalci = pow((FE - FO), 2) / FE;
            chicalc += pow((FE - FO), 2) / FE;//chi calculado
            System.out.println("[" + i + " - " + (i + (1 / clases)) + ")" + "\t\t" + FO + "\t\t" + FE + "\t\t" + chicalci);
        }
        System.out.println("Chi critico: " + chicrit);
        System.out.println("Chi calculado: " + chicalc);
        System.out.println("FO Total: " + FOsum);
        System.out.println("Pasa la prueba?: ");
        return chicalc <= chicrit;
    }

    /**
     * Aplica la prueba de poker con 3 decimales a una lista de numeros pseudoaleatorios
     * @param lista con los numeros pseudoaleatorios
     * @return true si pasa la prueba, false si la lista de numeros es rechazada
     */
    public boolean poker3(List<Double> lista) {
        System.out.println("----------Prueba de Poker 3 decimales----------");
        List<Double> listan = formatList(lista, "#.###");//define el formato para 3 decimales
        System.out.println("Clase \t\t\t\t FO \t\t FE \t\t x2");
        int Iguales3 = 0;
        int Iguales2 = 0;
        int Iguales0 = 0;
        double chicrit = 4.6052;//de acuerdo a la tabla

        for (Double number : listan) {
            double num = ceil(number * 1000);
            double num1 = (int) num / 100;
            double num2 = ((int) num / 10) % 10;
            double num3 = num % 10;
            if (num1 == num2 && num1 == num3) {//los 3 numeros son iguales
                Iguales3++;
            } else if (num1 != num2 && num1 != num3 && num2 != num3) {//los 3 numeros son diferentes
                Iguales0++;
            } else {//hay dos numeros iguales
                Iguales2++;
            }
        }
        double FE = listan.size();
        double chi21 = (pow((FE * 0.01) - Iguales3, 2)) / (FE * 0.01);
        double chi22 = (pow((FE * 0.27) - Iguales2, 2)) / (FE * 0.27);
        double chi23 = (pow((FE * 0.72) - Iguales0, 2)) / (FE * 0.72);
        double chicalc = chi21 + chi22 + chi23;//se calcula el chi cuadrado
        System.out.println("3 Iguales                \t " + Iguales3 + " \t\t " + FE * 0.01 + " \t\t " + chi21);
        System.out.println("2 Iguales, 1 Diferente   \t " + Iguales2 + " \t\t " + FE * 0.27 + " \t\t " + chi22);
        System.out.println("3 Diferentes             \t " + Iguales0 + " \t\t " + FE * 0.72 + " \t\t " + chi23);
        System.out.println("\t\t\t\t\t\t chicalc:        " + chicalc);
        System.out.println("Pasa la prueba?: ");
        return chicalc <= chicrit;
    }

    /**
     * Aplica la prueba de poker con 2 decimales a una lista de numeros pseudoaleatorios
     * @param lista con los numeros pseudoaleatorios
     * @return true si pasa la prueba, false si la lista de numeros es rechazada
     */
    public boolean poker2(List<Double> lista) {
        System.out.println("----------Prueba de Poker 2 decimales----------");
        List<Double> listan = formatList(lista, "#.##");
        System.out.println("Clase \t\t\t\t FO \t\t FE \t\t x2");
        int Iguales2 = 0;
        int Iguales0 = 0;
        double chicrit =  2.7055;//de acuerdo a la tabla

        for (Double number : listan) {
            double num = ceil(number * 100);
            double num1 = ((int) num / 10);
            double num2 = num % 10;
            if (num1 == num2) {//los dos decimales son iguales
                Iguales2++;
            } else {//los dos decimales son diferentes
                Iguales0++;
            }
        }
        double FE = listan.size();
        double chi21 = (pow((FE * 0.1) - Iguales2, 2)) / (FE * 0.1);
        double chi22 = (pow((FE * 0.9) - Iguales0, 2)) / (FE * 0.9);
        double chicalc = chi21 + chi22;//se calcula el chi cuadrado
        System.out.println("2 Iguales      \t\t\t " + Iguales2 + " \t\t " + FE * 0.1 + " \t\t " + chi21);
        System.out.println("2 Diferentes   \t\t\t " + Iguales0 + " \t\t " + FE * 0.9 + " \t\t " + chi22);
        System.out.println("\t\t\t\t\t\t chicalc:        " + chicalc);
        System.out.println("Pasa la prueba?: ");
        return chicalc <= chicrit;
    }

    private int countOnRange(double ini, double fin, List<Double> lista) {
        int cant = 0;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) >= ini && lista.get(i) < fin) {
                cant++;
            }
        }
        return cant;
    }

    /**
     * Genera los archivos con los numeros pseudoaleatorios
     * @return los numeros generados en la lista
     * @throws IOException 
     */
    public List<Double> generarLista() throws IOException {
        List<Double> lines = new LinkedList<>();
        List<String> linesS = new LinkedList<>();

        for (int i = 0; i < 10000; i++) {
            if (i == 1000) {
                Path file = Paths.get("GEM1000.txt");
                Files.write(file, linesS, Charset.forName("UTF-8"));
            }
            double num = generarAleatorio();
            linesS.add(String.valueOf(num));
            lines.add(num);
        }
        Path file = Paths.get("GEM10000.txt");
        Files.write(file, linesS, Charset.forName("UTF-8"));
        return lines;
    }

    /**
     * Formatea los numeros a un determinado numero de decimales
     * @param lista lista de numeros pseudoaleatorios
     * @param decimales cantidad de decimales aceptados
     * @return Lista de numeros pseudoaleatorios formateados
     */
    private List<Double> formatList(List<Double> lista, String decimales) {
        List<Double> newList = new LinkedList<>();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat decimal = new DecimalFormat(decimales, otherSymbols);
        decimal.setRoundingMode(RoundingMode.UP);
        //genera la lista de numeros formateados
        for (int i = 0; i < lista.size(); i++) {
            newList.add(Double.parseDouble(decimal.format(lista.get(i))));
        }
        return newList;
    }
    
    public double[] NAleatorios(int cantidad){
        double data[] = new double[cantidad];
        double num=0;
        for(int i=0; i<cantidad;i++){
            num =generarAleatorio();
            data[i] = num;
        }
        return data;
    }

    public static void main(String[] args) throws IOException {
               
        //generando 10000 numeros aleatorios
        double num = 0;
        Gem gem = new Gem();
        //trabajo con otras distribuciones - convolucion
        //gem.convolucion(1000);
        //gem.convolucion(10000);
        //trabajo con otras distribuciones - binomial
        //gem.binomial(1000);
        //trabajo con otras distribuciones - poisson
        //gem.poisson(1000);
        //List<Double> num10000 = gem.generarLista();
        //List<Double> num1000 = num10000.subList(0, 1000);
        /*
        Pruebas de bondad 1000 numeros
        */
        //System.out.println(gem.pruebachi2(num1000));
        //System.out.println(gem.poker2(num1000));
        //System.out.println(gem.poker3(num1000));
        /*
        Pruebas de bondad 10000 numeros
        */
        //System.out.println(gem.pruebachi2(num10000));
        //System.out.println(gem.poker2(num10000));
        //System.out.println(gem.poker3(num10000));
    }

}
