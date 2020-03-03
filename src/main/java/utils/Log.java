package utils;

public class Log {
    public static void it(Object obj) {
        long time = System.currentTimeMillis() - CommonUtils.startTime;
        System.out.println(getThreadName() + " | " + time + " | " + "value = " + obj);
    }

    public static void i(Object obj) {
        System.out.println(getThreadName() + " | " + obj);
    }

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }
}
