package model;

public class Shape {
    public static final String HEXAGON = "HEXAGON";
    public static final String OCTAGON = "OCTAGON";
    public static final String BALL = "BALL";
    public static final String RECTANGLE = "RECTANGLE";
    public static final String PENTAGON = "PENTAGON";
    public static final String TRIANGLE = "TRIANGLE";
    public static final String STAR = "STAR";
    public static final String DIAMOND = "DIAMOND";

    public static String getString(String color, String shape) {
        return color + "-" + shape;
    }

    public static String getColor(String shape) {
        if(shape.endsWith("<>"))
            return shape.replace("<>", "").trim();

        int hyphen = shape.indexOf("-");
        if(hyphen > 0) {
            return shape.substring(0, hyphen);
        }
        return shape;
    }

    public static String getSuffix(String shape) {
        if(HEXAGON.endsWith(shape)) return "-H";
        if(OCTAGON.endsWith(shape)) return "-O";
        if(RECTANGLE.endsWith(shape)) return "-R";
        if(TRIANGLE.endsWith(shape)) return "-T";
        if(DIAMOND.endsWith(shape)) return "<>";
        if(PENTAGON.endsWith(shape)) return "-P";
        if(STAR.endsWith(shape)) return "-S";
        return "";
    }
}
