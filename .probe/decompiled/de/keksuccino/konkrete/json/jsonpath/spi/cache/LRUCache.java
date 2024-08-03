package de.keksuccino.konkrete.json.jsonpath.spi.cache;

import de.keksuccino.konkrete.json.jsonpath.JsonPath;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache implements Cache {

    private final ReentrantLock lock = new ReentrantLock();

    private final Map<String, JsonPath> map = new ConcurrentHashMap();

    private final Deque<String> queue = new LinkedList();

    private final int limit;

    public LRUCache(int limit) {
        this.limit = limit;
    }

    @Override
    public void put(String key, JsonPath value) {
        JsonPath oldValue = (JsonPath) this.map.put(key, value);
        if (oldValue != null) {
            this.removeThenAddKey(key);
        } else {
            this.addKey(key);
        }
        if (this.map.size() > this.limit) {
            this.map.remove(this.removeLast());
        }
    }

    @Override
    public JsonPath get(String key) {
        JsonPath jsonPath = (JsonPath) this.map.get(key);
        if (jsonPath != null) {
            this.removeThenAddKey(key);
        }
        return jsonPath;
    }

    private void addKey(String key) {
        this.lock.lock();
        try {
            this.queue.addFirst(key);
        } finally {
            this.lock.unlock();
        }
    }

    private String removeLast() {
        this.lock.lock();
        String var2;
        try {
            String removedKey = (String) this.queue.removeLast();
            var2 = removedKey;
        } finally {
            this.lock.unlock();
        }
        return var2;
    }

    private void removeThenAddKey(String key) {
        this.lock.lock();
        try {
            this.queue.removeFirstOccurrence(key);
            this.queue.addFirst(key);
        } finally {
            this.lock.unlock();
        }
    }

    private void removeFirstOccurrence(String key) {
        this.lock.lock();
        try {
            this.queue.removeFirstOccurrence(key);
        } finally {
            this.lock.unlock();
        }
    }

    public JsonPath getSilent(String key) {
        return (JsonPath) this.map.get(key);
    }

    public void remove(String key) {
        this.removeFirstOccurrence(key);
        this.map.remove(key);
    }

    public int size() {
        return this.map.size();
    }

    public String toString() {
        return this.map.toString();
    }
}