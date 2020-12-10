package material.map;

public class HashTableMapDH<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapDH() {
        super();
    }

    public HashTableMapDH(int capacity) {
        super(capacity);
    }

    public HashTableMapDH(int prime, int capacity) {
        super(prime, capacity);
    }

    @Override
    protected int offset(int retry, K key) {
        return retry * this.aux(key);
    }

    private int aux(K key) {
        return 7 - (key.hashCode() % 7);
    }

}