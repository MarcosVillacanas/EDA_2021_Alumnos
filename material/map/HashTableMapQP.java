package material.map;

public class HashTableMapQP<K, V> extends AbstractHashTableMap<K, V> {

    final int c1 = 3;
    final int c2 = 5;

    public HashTableMapQP() { super(); }

    public HashTableMapQP(int capacity) { super(capacity); }

    public HashTableMapQP(int prime, int capacity) { super(prime, capacity); }

    @Override
    protected int offset(int retry, K key) {
        return ((this.c1 * retry) + (this.c2 * retry * retry));
    }
}