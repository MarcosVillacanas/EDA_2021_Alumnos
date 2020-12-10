package usecase.practica4;

public class Product {
    private String id;

    public Product(String name, int serial) { this.id = name + "-" + serial; }

    public String getId () { return this.id; }

    @Override
    public boolean equals (Object o){
        Product toBeCompared = (Product) o;
        return this.id.equals(toBeCompared.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
