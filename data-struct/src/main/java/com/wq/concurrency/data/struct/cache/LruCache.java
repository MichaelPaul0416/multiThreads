package com.wq.concurrency.data.struct.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/27 10:31
 * @Description: 线程不安全容器
 **/
public class LruCache<K, V> {

    private final Map<K, V> cacheMap = new ConcurrentHashMap<>(8);
    private final Entry<K, V> head;
    private final Entry<K, V> tail;

    private final int cacheSize;

    public static void main(String[] args) {
        LruCache<String, Integer> lruCache = new LruCache<>(5);
        lruCache.put("one", 1);
        lruCache.put("two", 2);
        lruCache.put("three", 3);
        lruCache.put("four", 4);
        lruCache.put("five", 5);

        // get two
        System.out.println(lruCache.get("two"));

        lruCache.put("six", 6);
        System.out.println(lruCache.get("one") == null);

    }


    public LruCache(int cacheSize) {
        this.cacheSize = cacheSize;
        this.head = new Entry<>(null, null);
        this.tail = new Entry<>(null, null);
        this.head.next = this.tail;
        this.tail.pre = this.head;
    }

    public LruCache(int cacheSize, K key, V value) {
        this(cacheSize);
        Entry<K, V> entry = new Entry<>(key, value);
        entry.next = this.tail;
        this.head.next = entry.next;
        entry.pre = this.head;
        this.tail.pre = entry;
    }

    private static final class Entry<K, V> {
        private K key;
        private V value;

        private Entry<K, V> pre;
        private Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    private void addCache(K key, V value) {
        this.cacheMap.put(key, value);
    }

    public V get(K key) {
        if (key == null) {
            return null;
        }

        Entry<K, V> target = lookForKey(key);

        if (target == null) {
            return null;
        }

        // move to head
        remove(target);
        addAtHead(target);

        return target.value;
    }

    public void put(K key, V value) {
        if (this.cacheMap.size() == this.cacheSize) {
            // 主动触发清理
            cleanExpiredKey();
        }

        // contain key
        if (key == null) {
            throw new IllegalArgumentException("cache key must not be null");
        }

        if (this.cacheMap.containsKey(key)) {
            Entry<K, V> cached = lookForKey(key);
            if (cached == null) {
                throw new IllegalMonitorStateException("");
            }
            if (cached.value != value) {
                cached.value = value;
            }
            remove(cached);
            addAtHead(cached);
        } else {
            addAtHead(new Entry<>(key, value));
        }
        addCache(key, value);
    }

    private boolean fullCache() {
        return this.cacheSize == this.cacheMap.size();
    }

    private void cleanExpiredKey() {
        Entry<K, V> end = this.tail.pre;
        end.pre.next = this.tail;
        this.tail.pre = end.pre;

        end.next = null;
        end.pre = null;
        this.cacheMap.remove(end.key);
    }

    private void addAtHead(Entry<K, V> entry) {
        entry.pre = this.head;
        entry.next = this.head.next;
        this.head.next.pre = entry;
        this.head.next = entry;
    }

    private void remove(Entry<K, V> entry) {
        entry.pre.next = entry.next;
        entry.next.pre = entry.pre;

        //
        entry.pre = null;
        entry.next = null;
    }

    private Entry<K, V> lookForKey(K key) {
        if (emptyList()) {
            return null;
        }

        Entry<K, V> trail = this.head.next;
        do {
            if (trail.key == key) {
                break;
            }
        } while (!atTail(trail) && (trail = trail.next) != null);

        if (trail.next == this.tail && trail.key != key) {
            return null;
        }

        return trail;
    }

    private boolean atTail(Entry<K, V> entry) {
        return entry.next == this.tail;
    }

    private boolean emptyList() {
        return this.head.next == this.tail && this.tail.pre == this.head && this.cacheMap.size() == 0;
    }
}
