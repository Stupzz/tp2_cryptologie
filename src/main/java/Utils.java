import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;


public class Utils {

    public static String md5Hexa(String str) throws NoSuchAlgorithmException {
        byte[] byteStr = str.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] bytes = messageDigest.digest(byteStr);

        return getHexaStrFromByteArray(bytes);
    }

    public static String sha1Hexa(String str) throws NoSuchAlgorithmException {
        byte[] byteStr = str.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] bytes = messageDigest.digest(byteStr);

        return getHexaStrFromByteArray(bytes);
    }

    public static String getHexaStrFromByteArray(byte[] bytes){
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<bytes.length;i++) {
            String hex=Integer.toHexString(0xff & bytes[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static byte[] md5Bytes(String str) throws NoSuchAlgorithmException {
        byte[] byteStr = str.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] bytes = messageDigest.digest(byteStr);
        return bytes;
    }

    public static byte[] sha1Bytes(String str) throws NoSuchAlgorithmException {
        byte[] byteStr = str.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] bytes = messageDigest.digest(byteStr);
        return bytes;
    }

    public static byte[] getXFirstByte(byte[] bytes, int x){
        byte[] res = new byte[x];
        for (int i = 0; i < x; i++){
            res[i] = bytes[i];
        }
        return res;
    }

    public static BigInteger getIntFromByte(byte[] bytes){
        BigInteger res = BigInteger.ZERO;
        BigInteger _256 = BigInteger.valueOf(256);
        for (int i = 0; i < 8; i++){
            res = res.add(BigInteger.valueOf(bytes[i] & 0xFF).multiply(_256.pow(i)));
        }
        return res;
    }

    public static int[] getIntArrayFromByte(byte[] bytes){
        int[] res = new int[8];
        for (int i = 0; i < 8; i++){
            res[i] = bytes[i] & 0xFF ;
        }
        return res;
    }

    public static Map<Integer, Integer> mapSortByValue(Map<Integer, Integer> map){// a test
        /*
        trie le dictionnaire en fonction de ses valeurs
         */
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
