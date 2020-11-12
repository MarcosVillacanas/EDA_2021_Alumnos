package material.map;

import java.util.Iterator;

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
    private long scale, shift;
    private int prime, capacity;
    private HashEntry<K,V> [] bucket;

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
