package com.wq.concurrency.data.struct.cache;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/28 9:29
 * @Description:
 **/
public class LfuCache<K, V> {

    private final Map<K, Entry<K, V>> dataContainer;

    private final Map<Integer, Entry<K, V>> frequencyMap;

    private final int fullSize;

    private int minIndex = 0;

    public LfuCache(int initSize) {
        this.dataContainer = new HashMap<>(initSize);
        this.frequencyMap = new HashMap<>(initSize);
        this.fullSize = initSize;
    }

    public static void main(String[] args) {
        LfuCache<String, String> lfuCache = new LfuCache<>(5);
        lfuCache.put("one", "1");
        lfuCache.put("two", "2");
        lfuCache.put("three", "3");
        lfuCache.put("four", "4");
        lfuCache.put("five", "5");

        lfuCache.put("six", "6");
        System.out.println(lfuCache.get("one") == null);

        lfuCache.put("two", "2-2");// two顶上去，并且frequency=2
        lfuCache.put("seven", "7");
        System.out.println(lfuCache.get("three") == null);

        System.out.println(lfuCache.get("five"));// five -> 2; (7,6,4) -> 1 ;(5,2) -> 2
        lfuCache.put("eight","8");
        System.out.println(lfuCache.get("four") == null);// 8,7,6 -> 1 ; 5,2 -> 2

        System.out.println(lfuCache.get("seven"));
        System.out.println(lfuCache.get("six")); // 8 -> 1; 6,7,5,2 -> 2
        System.out.println(lfuCache.get("eight"));// 8,6,7,5,2 -> 2

        lfuCache.put("nine","9");
        System.out.println(lfuCache.get("two") == null);// 9 -> 1; 8,6,7,5 -> 2

    }

    public V get(K key) {
        if (key == null || !dataContainer.containsKey(key)) {
            return null;
        }

        // find
        Entry<K, V> entry = this.dataContainer.get(key);
        Entry<K, V> target = lookFor(this.frequencyMap.get(entry.frequency), key);

        removeFromCurrentList(target);
        addToListHead(target);

        return entry.value;
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("empty cache key");
        }

        Entry<K, V> target;
        if (dataContainer.containsKey(key)) {
            Entry<K, V> oldEntry = this.dataContainer.get(key);
            oldEntry.value = value;

            // add frequency
            Entry<K, V> listHead = this.frequencyMap.get(oldEntry.frequency);
            target = lookFor(listHead, key);

            if (target == null) {
                throw new IllegalArgumentException("key:" + key + " 不存在");
            }

            // 原先链表中移除
            removeFromCurrentList(target);
            // 然后加入下一级列表
            addToListHead(target);
        } else {
            // 先判断容器是否已经饱和
            if (this.fullSize == this.dataContainer.size()) {
                // 淘汰一个
                eliminatedEntry();
            }
            target = new Entry<>(key, value);
            addToListHead(target);
            this.minIndex = target.frequency;
        }

        target.value = value;
        this.dataContainer.put(key, target);
    }

    private void eliminatedEntry() {
        Entry<K, V> header = this.frequencyMap.get(this.minIndex);
        Entry<K, V> tail = lookTailEntry(header);

        // 从频次表中移除
        removeFromCurrentList(tail);
        // 从数据存储表中移除
        this.dataContainer.remove(tail.key);

    }

    private Entry<K, V> lookTailEntry(Entry<K, V> header) {
        Entry<K, V> trail = header.next;
        while (trail.next != null) {
            trail = trail.next;
        }
        return trail;
    }

    private void addToListHead(Entry<K, V> entry) {
        int expect = ++entry.frequency;
        if (this.frequencyMap.containsKey(expect)) {
            Entry<K, V> header = this.frequencyMap.get(expect);
            entry.next = header.next;
            entry.pre = header;
            header.next.pre = entry;
            header.next = entry;
            return;
        }

        Entry<K, V> header = new Entry<>(null, null);
        header.frequency = expect;
        header.next = entry;
        entry.pre = header;
        this.frequencyMap.put(expect, header);
    }


    private void removeFromCurrentList(Entry<K, V> entry) {
        int index = entry.frequency;

        // only header left ?
        if (entry.pre == this.frequencyMap.get(index) && entry.next == null) {
            entry.pre.next = null;
            entry.pre = null;
            this.frequencyMap.remove(index);
            if (entry.frequency == this.minIndex) {
                // 最小的访问次数列表中已经没有元素，此时指向访问次数次大的列表
                this.minIndex++;
            }
            return;
        }

        entry.pre.next = entry.next;

        // end of list
        if (entry.next != null) {
            entry.next.pre = entry.pre;
        }
        entry.pre = null;
        entry.next = null;
    }

    private Entry<K, V> lookFor(Entry<K, V> head, K tar) {
        Entry<K, V> trail = head;
        do {
            if (trail.key == tar) {
                break;
            }
        } while (!endOf(trail) && (trail = trail.next) != null);

        // = null的时候，代表已经是结尾了
        return trail;
    }

    private boolean endOf(Entry<K, V> item) {
        return item.next == null;
    }

    private static class Entry<K, V> extends WeakReference<V> {
        private K key;
        private V value;
        private volatile int frequency;

        private Entry<K, V> pre;
        private Entry<K, V> next;

        public Entry(K key, V referent) {
            super(referent);
            this.key = key;
            this.value = referent;
            this.frequency = 0;
        }

        public Entry(Entry<K, V> entry) {
            super(entry.value);
            this.key = entry.key;
            this.value = entry.value;
            this.frequency = 0;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    ", frequency=" + frequency +
                    '}';
        }
    }
}
