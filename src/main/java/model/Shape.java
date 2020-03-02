package model;

public class Shape {
    public static final String BALL = "BALL";
    public static final String RECTANGLE = "RECTANGLE";
    public static final String PENTAGON = "PENTAGON";

    public static String getString(String color, String shape) {
        return color + "-" + shape;
    }
}
