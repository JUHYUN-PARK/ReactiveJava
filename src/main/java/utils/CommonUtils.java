package utils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

public class CommonUtils {
    public static long startTime;

    public static final String GITHUB_ROOT = "https://raw.githubusercontent.com/yudong80/reactivejava/master/";

    public static final String ERROR_CODE = "-500";

    public static void exampleStart() {
        startTime = System.currentTimeMillis();
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getShape(String obj) {
        if(obj == null || obj.equals("")) return "NO_SHAPE";
        if(obj.endsWith("-H")) return "HEXAGON";
        if(obj.endsWith("-O")) return "OCTAGON";
        if(obj.endsWith("-R")) return "RECTANGLE";
        if(obj.endsWith("-T")) return "TRIANGLE";
        if(obj.endsWith("-<>")) return "DIAMOND";
        return "BALL";
    }

    public static void doSomething() {
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUWXYZ";

    public static String numberToAlphabet(long x) {
        return Character.toString(ALPHABET.charAt((int) x % ALPHABET.length()));
    }

    public static boolean isNetworkAvailable() {
        try {
            return InetAddress.getByName("www.google.com").isReachable(1000);
        } catch (IOException e) {
            Log.v("Network is not available");
        }
        return false;
    }

    public static int toInt(String val) {
        return Integer.parseInt(val);
    }
}
