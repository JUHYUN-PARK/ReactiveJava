package model;

public class Pair<String, Integer> {
    private String name;
    private Integer price;

    public Pair (String _name, Integer _price) {
        name = _name;
        price = _price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
