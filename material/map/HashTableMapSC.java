package material.map;

import java.util.*;
import java.util.stream.Collectors;

public class HashTableMapSC<K, V> implements Map<K, V> {

    protected class HashEntry<K, V> implements Entry<K, V> {

        protected K key;
        protected V value;

        public HashEntry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        @Override
        public V getValue() { return this.value; }

        @Override
        public K getKey() { return this.key; }

        public V setValue(V val) {
            V oldVal = this.value;
            this.value = val;
            return oldVal;
        }

        @Override
        public boolean equals(Object o) {
            HashEntry<K, V> toBeCompared = (HashEntry<K, V>) o;
            return this.key == toBeCompared.getKey() && this.value == toBeCompared.getValue();
        }

        @Override
        public String toString() {
            return "(" + key + "," + value + ")";
        }
    }

    private int n;
    private long scale, shift; // A y B en MAD
    private int prime;
    private int capacity;
    private List<HashEntry<K, V>> [] buckets;

    public HashTableMapSC() {
        this.n = 0;
        this.prime = 109345121;
        this.capacity = 11;
        this.buckets = (List<HashEntry<K, V>> []) new List[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
    }

    public HashTableMapSC(int cap) {
        this.n = 0;
        this.prime = 109345121;
        this.capacity = cap;
        this.buckets = (List<HashEntry<K, V>> []) new List[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
    }

    public HashTableMapSC(int p, int cap) {
        this.n = 0;
        this.prime = p;
        this.capacity = cap;
        this.buckets = (List<HashEntry<K, V>> []) new List[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
    }

    protected int hashValue(K key) {
        return (int) ( ( Math.abs(key.hashCode() * this.scale + this.shift) & this.prime ) % this.capacity );
    }

    @Override
    public int size() { return this.n; }

    @Override
    public boolean isEmpty() { return this.n == 0; }

    @Override
    public V get(K key) {
        this.checkKey(key);
        int index = this.hashValue(key);
        if (this.buckets[index] != null) {
            for (HashEntry<K, V> e : this.buckets[index]) {
                if (e.getKey().equals(key)) {
                    return e.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        this.checkKey(key);
        int index = this.hashValue(key);

        if (this.n >= this.capacity * 0.75) {
            this.rehash(this.capacity * 2);
            index = this.hashValue(key);
        }

        if (this.buckets[index] == null) {
            this.buckets[index] = new LinkedList<>();
        }
        else {
            for (HashEntry<K, V> e : this.buckets[index]) {
                if (e.getKey() == key) {
                    V oldValue = e.getValue();
                    e.setValue(value);
                    return oldValue;
                }
            }
        }
        this.buckets[index].add(new HashEntry<>(key, value));
        this.n++;
        return null;
    }

    @Override
    public V remove(K key) {
        this.checkKey(key);
        int index = this.hashValue(key);
        if (this.buckets[index] != null) {
            for (HashEntry<K, V> e : this.buckets[index]) {
                if (e.getKey().equals(key)) {
                    this.buckets[index].remove(e);
                    this.n--;
                    return e.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<K> keys() {
        List<K> keys = new LinkedList<>();
        for (int i = 0; i < this.capacity; i++) {
            if (this.buckets[i] != null) {
                keys.addAll(this.buckets[i].stream().map(HashEntry::getKey).collect(Collectors.toList()));
            }
        }
        return keys;
    }

    @Override
    public Iterable<V> values() {
        List<V> values = new LinkedList<>();
        for (int i = 0; i < this.capacity; i++) {
            if (this.buckets[i] != null) {
                values.addAll(this.buckets[i].stream().map(HashEntry::getValue).collect(Collectors.toList()));
            }
        }
        return values;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        List<Entry<K, V>> entries = new LinkedList<>();
        for (int i = 0; i < this.capacity; i++) {
            if (this.buckets[i] != null) {
                entries.addAll(this.buckets[i]);
            }
        }
        return entries;
    }

    protected void checkKey(K key) throws RuntimeException{
        if (key == null) {
            throw new RuntimeException("Invalid key: null");
        }
    }

    protected void rehash(int newCap) {
        List<HashEntry<K, V>> [] newBuckets = new List[newCap];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        this.capacity = newCap;

        for (List<HashEntry<K, V>> bucket : this.buckets) {
            if (bucket != null) {
                for (HashEntry<K, V> e : bucket) {
                    int index = this.hashValue(e.getKey());
                    if (newBuckets[index] == null) {
                        newBuckets[index] = new LinkedList<>();
                    }
                    newBuckets[index].add(e);
                }
            }
        }
        this.buckets = newBuckets;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() { return new HashTableMapIterator<K, V>(this.buckets, this.capacity); }

    private class HashTableMapIterator<T, U> implements Iterator<Entry<T, U>> {
        private int globalIndex;
        private int bucketIndex;
        private List<HashEntry<T, U>>[] map;
        private int numElements;

        public HashTableMapIterator(List<HashEntry<T, U>>[] map, int numElements) {
            this.globalIndex = this.goToNextBucket(0);
            this.bucketIndex = 0;
            this.map = map;
            this.numElements = numElements;
        }

        @Override
        public boolean hasNext() { return this.globalIndex < numElements; }

        @Override
        public Entry<T, U> next() {
            if (this.globalIndex >= this.numElements) {
                throw new RuntimeException("The map hasn't more elements");
            }
            else if (this.bucketIndex == this.map[this.globalIndex].size()) {
                this.globalIndex = this.goToNextBucket(this.globalIndex);
                return next();
            }
            else {
                this.bucketIndex++;
                return this.map[this.globalIndex].get(this.bucketIndex - 1);
            }
        }

        @Override
        public void remove() { throw new UnsupportedOperationException("Not implemented."); }

        private int goToNextBucket(int index) {
            for (int i = index; i < this.numElements; i++) {
                if (this.map[i] != null && this.map[i].size() > 0) {
                    return i;
                }
            }
            return this.numElements; // out of range
        }
    }

    private class HashTableMapKeyIterator<T, U> implements Iterator<T> {
        public HashTableMapIterator<T, U> it;

        public HashTableMapKeyIterator(HashTableMapIterator<T, U> it) { this.it = it; }

        @Override
        public T next() { return it.next().getKey(); }

        @Override
        public boolean hasNext() { return it.hasNext(); }

        @Override
        public void remove() { throw new RuntimeException("Not implemented"); }
    }

    private class HashTableMapValueIterator<T, U> implements Iterator<U> {
        public HashTableMapIterator<T, U> it;

        public HashTableMapValueIterator(HashTableMapIterator<T, U> it) { this.it = it; }

        @Override
        public U next() { return it.next().getValue(); }

        @Override
        public boolean hasNext() { return it.hasNext(); }

        @Override
        public void remove() { throw new UnsupportedOperationException("Not implemented."); }
    }
}
