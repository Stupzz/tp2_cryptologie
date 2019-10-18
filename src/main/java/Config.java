import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Config {
    private  String alphabet;
    private  int tailleMin, tailleMax, N, tailleAlphabet;
    private int[] T;

    public Config() {
    }

    public  String getAlphabet() {
        return alphabet;
    }

    public  int getTailleMin() {
        return tailleMin;
    }

    public  int getTailleMax() {
        return tailleMax;
    }

    public  int getN() {
        return N;
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

    public int i2iMd5(int number, int indice) throws NoSuchAlgorithmException {
        return (int)h2i(Utils.md5Bytes(i2c(number)), indice);
    }

    public int nouvelleChaine(int indiceDepart, int largeur, boolean isMD5) throws NoSuchAlgorithmException {
        int res = indiceDepart;
        for (int i = 1; i < largeur; i++){
            res = isMD5 ? i2iMd5(res, i) : i2iSha1(res, i);
        }
        return res;
    }

    public int i2iSha1(int number, int indice) throws NoSuchAlgorithmException {
        return (int)h2i(Utils.sha1Bytes(i2c(number)), indice);
    }
}
