package material.map;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

abstract public class AbstractHashTableMap<K,V> implements Map<K,V> {

    protected class HashEntry<K,V> implements Entry<K,V> {

        protected K key;
        protected V value;
        protected AbstractHashTableMap<K,V> myTable;

        // Es protected porque

        public HashEntry(K key, V value, AbstractHashTableMap<K, V> myTable) {
            this.key = key;
            this.value = value;
            this.myTable = myTable;
        }

        @Override
        public K getKey() { return this.key; }

        @Override
        public V getValue() { return this.value; }

        public V setValue (V val) {
            V oldVal = this.value;
            this.value = val;
            return oldVal;
        }
    }

    private int n;
    private long scale, shift; // A y B en MAD
    private int prime;
    private int capacity;
    private HashEntry<K,V> [] bucket;
    private HashEntry<K,V> AVAILABLE;

    public AbstractHashTableMap() {
        this.n = 0;
        this.prime = 7;
        this.capacity = 11;
        this.bucket = (HashEntry<K, V>[]) new HashEntry[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt( prime - 1) + 1;
        this.shift = rand.nextInt(prime);
        this.AVAILABLE = new HashEntry<>(null, null, this);
    }

    public AbstractHashTableMap(int capacity) {
        this.n = 0;
        this.prime = 7;
        this.capacity = capacity;
        this.bucket = (HashEntry<K, V>[]) new HashEntry[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt( prime - 1) + 1;
        this.shift = rand.nextInt(prime);
        this.AVAILABLE = new HashEntry<>(null, null, this);
    }

    public AbstractHashTableMap(int prime, int capacity) {
        this.n = 0;
        this.prime = prime;
        this.capacity = capacity;
        this.bucket = (HashEntry<K, V>[]) new HashEntry[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt( prime - 1) + 1;
        // +1 para que no sea 0, -1 para que no desborde
        this.shift = rand.nextInt(prime);
        // Se evita que escale multiplicando por 0, pero se permite que desplaze sumando 0
        this.AVAILABLE = new HashEntry<>(null, null, this);
    }

    private void checkKey(K key) throws RuntimeException {
        if (key == null) {
            throw new RuntimeException("Invalid key: null");
        }
    }

    @Override
    public int size() { return this.n; }

    @Override
    public boolean isEmpty() { return this.n == 0; }

    private class HashEntryIndex {
        int index;
        boolean found;

        public HashEntryIndex(int index, boolean found) {
            this.index = index;
            this.found = found;
        }

        public int getIndex() { return this.index; }

        public boolean isFound() { return found; }
    }

    private int hashValue(K key) {
        // h2(y) ((y*a + b) mod p) mod N --MAD
        return (int) ( ( Math.abs(key.hashCode() * this.scale + this.shift) % this.prime ) % this.capacity );
    }

    private HashEntryIndex findEntry(K key) {
        int avail = -1;
        this.checkKey(key);
        int hashcode = this.hashValue(key), index = hashcode, retry = 0;
        do {
            Entry<K, V> e = this.bucket[index];
            if (e == null) {
                if (avail < 0) {
                    avail = index; // key is not in table
                }
                break;
            }
            else if (key.equals(e.getKey())) { // we have found our key
                return new HashEntryIndex(index, true); // key found
            }
            else if (e == AVAILABLE) { // bucket is deactivated
                if (avail < 0) {
                    avail = index; // remember that this slot is available
                }
            }
            retry++;
            index = (hashcode + offset(retry, key)) % capacity; // keep looking

        } while (retry < this.capacity);

        return new HashEntryIndex(avail, false); // first empty or available slot
    }

    abstract protected int offset(int i, K key);

    // abstract -> allows "prueba lineal","prueba cuadrática" y "doble hash" to be all implemented in the same class
    // offset method, the only lines that would change between the three different implementations, is abstract
    // so as these three options can implement their particular offsets in three small subclasses

    @Override
    public V put(K key, V value) throws RuntimeException {
        HashEntryIndex e = this.findEntry(key);

        if (e.isFound()) {
            return this.bucket[e.getIndex()].setValue(value);
        }
        else if (this.n >= this.capacity / 2) {
            this.rehash();
            e = this.findEntry(key);
        }
        this.bucket[e.getIndex()] = new HashEntry<>(key, value, this);
        this.n++;
        return null;
    }

    // exam's exercise, code a rehash also when enough elements are removed to set capacity /= 2
    // therefore in this variation, capacity is set from the method calling, capacity * 2 from put, and /2 from remove
    private void rehash() {
        this.capacity *= 2;
        HashEntry<K,V> [] old = this.bucket;
        this.bucket = (HashEntry<K, V>[]) new HashEntry[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);

        for (HashEntry<K,V> e : old) {
            // AVAILABLE don't need to be copied, since clusters are gonna change
            // e & this.AVAILABLE can be compared using != because pointers are what matters
            if (e != null && e != this.AVAILABLE) {
                HashEntryIndex entry = this.findEntry(e.getKey());
                int index = entry.getIndex();
                this.bucket[index] = e;
            }
        }
    }

    @Override
    public V get(K key) {
        HashEntryIndex i = this.findEntry(key);

        if (i.isFound()) {
            return this.bucket[i.getIndex()].getValue();
        }

        return null;
    }

    @Override
    public V remove(K key) throws RuntimeException {
        HashEntryIndex i = this.findEntry(key);

        if (i.isFound()) {
            V oldValue = this.bucket[i.getIndex()].getValue();
            this.bucket[i.getIndex()] = this.AVAILABLE;
            this.n--;
            return oldValue;
        }

        return null;
    }

    @Override
    public Iterable<K> keys() {
        LinkedList<K> keys = new LinkedList<>();
        for (int i = 0; i < this.capacity; i++) {
            if (this.bucket[i] != null && this.bucket[i] != this.AVAILABLE) {
                keys.add(this.bucket[i].getKey());
            }
        }
        return keys;
    }

    @Override
    public Iterable<V> values() {
        LinkedList<V> values = new LinkedList<>();
        for (int i = 0; i < this.capacity; i++) {
            if (this.bucket[i] != null && this.bucket[i] != this.AVAILABLE) {
                values.add(this.bucket[i].getValue());
            }
        }
        return values;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        LinkedList<Entry<K,V>> keys = new LinkedList<>();
        for (int i = 0; i < this.capacity; i++) {
            if (this.bucket[i] != null && this.bucket[i] != this.AVAILABLE) {
                keys.add(this.bucket[i]);
            }
        }
        return keys;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableMapIterator<>(this.bucket, this.AVAILABLE, this.capacity);
    }

    private class HashTableMapIterator<T,U> implements Iterator<Entry<T,U>> {
        private int pos;
        private HashEntry<T,U> [] bucket;
        private Entry<T,U> AVAILABLE;

        public HashTableMapIterator (HashEntry<T,U>[] b, Entry<T,U> av, int numElems) {
            this.bucket = b;
            if (numElems == 0) {
                this.pos = this.bucket.length;
            }
            else {
                this.pos = 0;
                this.goToNextElement(0);
                this.AVAILABLE = av;
            }
        }

        private void goToNextElement(int start) {
            final int n = this.bucket.length;
            this.pos = start;
            while (this.pos < n && (this.bucket[this.pos] == null || this.bucket[this.pos] == this.AVAILABLE)) {
                this.pos++;
            }
        }

        @Override
        public boolean hasNext() { return this.pos < this.bucket.length; }

        @Override
        public Entry<T,U> next () {
            if (this.hasNext()) {
                int currentPos = this.pos;
                this.goToNextElement(this.pos + 1);
                return this.bucket[currentPos];
            }
            throw new RuntimeException("The map hasn't more elements");
        }
    }
}
