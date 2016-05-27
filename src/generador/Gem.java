/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generador;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;


/**
 *
 * @author chris
 */
public class Gem {

    int a, c, m;
    long seed;
    String randomDevice;
    Integer BUFFER_SIZE;
    

    /**
     * inicializa el gem con los valores propuestos por Park y Miller
     */
    public Gem() {
        //valores propuestos por Park y Miller
        this.a = 16807;
        this.c = 0;
        this.m = 2147483641;
        this.randomDevice = "/dev/urandom";
        this.BUFFER_SIZE = 1024;
    }

    /**
     * Genera la semilla para usar con el random
     */
    private void generarSeed() {
        int nBytes=8;
        InputStream is;
        ByteArrayOutputStream data;
        int read, offset;
        byte buffer[];
        BigInteger seed;
        try {
            is = new FileInputStream(randomDevice);
            data = new ByteArrayOutputStream();
            buffer = new byte[BUFFER_SIZE];
            while (nBytes > 0 && (read = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
                offset = read < nBytes ? read : nBytes;
                nBytes -= offset;
                data.write(buffer, 0, offset);
            }
            seed = new BigInteger(1, data.toByteArray());
            data.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            seed = null;
        }
        this.seed = seed.longValue();
        System.out.println("seed: "+this.seed);
    }
    
    public void generarNumeroAleatorio(){
        double num = (((this.a*this.seed)+this.c)%this.m);
        System.out.println("num = "+num);
    }
    
    public static void main(String args[]){
        Gem g = new Gem();
        g.generarSeed();
        g.generarNumeroAleatorio();
    }

}
