/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generador;

import static java.lang.Math.*;


/**
 *
 * @author chris
 */
public class Gem {

    double a, c, m,seed;
    
        

    /**
     * inicializa el gem con los valores propuestos por Park y Miller
     */
    public Gem(double seed) {
        //valores propuestos por Park y Miller
        this.a = 16807;
        this.c = 0;
        this.m = 2147483641;
        this.seed = seed;
    }
    
    public double generarNumeroAleatorio(double xn){
        double num = (((this.a*xn)+this.c)%this.m);
        System.out.println(num);
        return num;
    }
    
    public static void main(String args[]){
        Gem g = new Gem(3);
        g.generarNumeroAleatorio(g.seed);
    }
}
