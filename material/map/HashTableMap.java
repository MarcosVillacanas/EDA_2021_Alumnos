package material.map;

import java.util.Iterator;
import java.util.Random;

public class HashTableMap<K,V> implements Map<K,V> {

    private class HashEntry<K,V> implements Entry<K,V> {

        protected K key;
        protected V value;
        protected HashTableMap<K,V> myTable;

        // Es protected porque

        public HashEntry(K key, V value, HashTableMap<K, V> myTable) {
            this.key = key;
            this.value = value;
            this.myTable = myTable;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public V getValue() {
            return null;
        }
    }

    private int n;
    private long scale, shift; // A y B en MAD
    private int prime, capacity;
    private HashEntry<K,V> [] bucket;
    private HashEntry<K,V> AVAILABLE;

    public HashTableMap(int prime, int capacity) {
        this.n = 0;
        this.prime = prime;
        this.capacity = capacity;
        this.bucket = (HashEntry<K, V>[]) new HashEntry[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt( prime - 1) + 1;
        // +1 para que no sea 0, -1 para que no desborde
        this.shift = rand.nextInt(prime);
        // Se evite que escale multiplicando por 0, pero se permite que desplaze sumando 0
        this.AVAILABLE = new HashEntry<>(null, null, this);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public V put(K key, V value) throws RuntimeException {
        return null;
    }

    @Override
    public V get(K key) throws RuntimeException {
        return null;
    }

    @Override
    public V remove(K key) throws RuntimeException {
        return null;
    }

    @Override
    public Iterable<K> keys() {
        return null;
    }

    @Override
    public Iterable<V> value() {
        return null;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        return null;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }
}
