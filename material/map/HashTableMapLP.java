package material.map;

public class HashTableMapLP<K,V> extends AbstractHashTableMap<K,V> {

    public HashTableMapLP() { super(); }

    public HashTableMapLP(int capacity) { super(capacity); }

    public HashTableMapLP(int prime, int capacity) { super(prime, capacity); }

    @Override
    protected int offset(int retry, K key) {
        return retry;
    }
}
