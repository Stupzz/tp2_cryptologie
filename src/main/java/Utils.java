import com.google.common.hash.Hashing;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class Utils {

    public static String md5Hexa(String str){
        byte[] bytes = Hashing.md5().hashString(str, StandardCharsets.UTF_8).asBytes();
        return getHexaStrFromByteArray(bytes);
    }

    public static String sha1Hexa(String str){
        byte[] bytes = Hashing.sha1().hashString(str, StandardCharsets.UTF_8).asBytes();
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

    public static byte[] md5Bytes(String str){
        return Hashing.md5().hashString(str, StandardCharsets.UTF_8).asBytes();
    }

    public static byte[] sha1Bytes(String str){
        return Hashing.sha1().hashString(str, StandardCharsets.UTF_8).asBytes();
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
}
