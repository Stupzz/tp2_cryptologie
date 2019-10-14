import javax.rmi.CORBA.Util;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Config {
    private String alphabet;
    private int tailleMin, tailleMax, N, tailleAlphabet;
    private int[] T;

    public Config() {
    }

    public String getAlphabet() {
        return alphabet;
    }

    public int getTailleMin() {
        return tailleMin;
    }

    public int getTailleMax() {
        return tailleMax;
    }

    public int getN() {
        return N;
    }

    public int getTailleAlphabet() {
        return tailleAlphabet;
    }

    public int[] getT() {
        return T;
    }

    private void initN(){
        N = 0;
        if (tailleMin == tailleMax) {
            N = T[0];
        }
        else{
            int j = 0;
            for(int i = tailleMin; i <= tailleMax; i++){
                N += T[j];
                j++;
            }
        }
    }

    public void initConfig(String alphabet, int tailleMin, int tailleMax){
        this.alphabet = alphabet;
        if (tailleMin > tailleMax){
            this.tailleMax = tailleMin;
            this.tailleMin = tailleMax;
        }
        else{
            this.tailleMax = tailleMax;
            this.tailleMin = tailleMin;
        }
        tailleAlphabet = alphabet.length();
        initT();
        initN();
    }

    private void initT() {
        int step = (tailleMax - tailleMin) + 1;
        T = new int[step];
        int j = 0;
        for(int i = tailleMin; i <= tailleMax; i++){
            T[j] = (int)Math.pow(tailleAlphabet, i);
            j++;
        }
    }

    public long h2i(byte[] y, int t){
        BigInteger yInt = Utils.getIntFromByte(y);
        return yInt.add(BigInteger.valueOf(t)).mod(BigInteger.valueOf(N)).longValue();
    }

    public String i2c(int number){
        int tailleMot = tailleMin;
        for (int i = 0; i < T.length; i++){
            if (number < T[i]){
                break;
            }
            number -= T[i];
            tailleMot++;
        }
        char[] str = new char[tailleMot];
        for(int i = 0; i < tailleMot; i++){
            str[(tailleMot - 1 ) - i] = alphabet.charAt(number % tailleAlphabet); // on insère les bon char à tout les emplacement
            number = number / tailleAlphabet; // check nombre à virgule
        }

        String res = new String(str);
        return res;
    }

    public int i2iMd5(int number, int indice){
        return (int)h2i(Utils.md5Bytes(i2c(number)), indice);
    }

    public int nouvelleChaine(int indiceDepart, int largeur){
        int res = indiceDepart;
        for (int i = 1; i < largeur; i++){
            res = i2iMd5(res, i);
        }
        return res;
    }
}
