package usecase.practica4;

public class PearStore {
    String id;
    int units;
    double score;

    public PearStore(String name, int serial) {
        this.id = name + "-" + serial;
        this.units = 0;
    }

    public String getId () { return this.id; }

    public int getUnits() { return units; }

    public void setUnits(int units) { this.units = units; }

    public double getScore() { return score; }

    public void setScore(double score) { this.score = score; }

    @Override
    public boolean equals (Object o){
        PearStore toBeCompared = (PearStore) o;
        return this.id.equals(toBeCompared.id);
    }
}