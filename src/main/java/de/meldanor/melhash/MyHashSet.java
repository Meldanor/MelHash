/*
 * Copyright (C) 2013 Kilian Gaertner
 * 
 * This file is part of MelHash.
 * 
 * MelHash is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MelHash is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MelHash.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.meldanor.melhash;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.meldanor.melhash.probe.Probe;
import de.meldanor.melhash.probe.ProbeFactory;
import de.meldanor.melhash.probe.ProbeType;

public class MyHashSet<T> implements Iterable<T> {

    private int size = 0;
    private int capacity;
    protected T[] elements;

    private ProbeType probeType;

    public MyHashSet() {
        this(16);
    }

    public MyHashSet(ProbeType probeType) {
        this(16, probeType);
    }

    public MyHashSet(int size) {
        this(size, ProbeType.LINEAR);
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int size, ProbeType probeType) {
        this.probeType = probeType;
        this.capacity = size;
        this.elements = (T[]) new Object[capacity];
    }

    public void add(T e) {
        if (size >= capacity / 2)
            resize(capacity * 2);

        int k = hash(e);
        Probe probe = ProbeFactory.instance().createProbe(probeType);
        int p = mod(k + probe.cur(), capacity);
        while (elements[p] != null) {
            p = mod(k + probe.next(), capacity);
        }
        elements[p] = e;
        ++size;
    }

    public int getIndex(T e) {
        int k = hash(e);
        Probe probe = ProbeFactory.instance().createProbe(probeType);
        int p = mod(k + probe.cur(), capacity);
        while (elements[p] != null) {
            if (elements[p].equals(e)) {
                return p;
            }
            else {
                p = mod(k + probe.next(), capacity);
            }
        }
        System.out.println(p);
        return -1;
    }

    public boolean contains(T e) {
        return getIndex(e) != -1;
    }

    public void delete(T e) {
        int index = getIndex(e);
        if (index == -1)
            return;
        else {
            elements[index] = null;
            --size;

            if (size > 0 && size == capacity / 8) {
                resize(capacity / 2);
            }
        }
    }

    private int hash(T e) {
        return (e.hashCode() & 0x7fffffff) % capacity;
    }

    private int mod(int n, int m) {
        return (n < 0) ? (m - (Math.abs(n) % m)) % m : (n % m);
    }

    private void resize(int newSize) {

        MyHashSet<T> tmp = new MyHashSet<T>(newSize, probeType);

        for (int i = 0; i < elements.length; ++i) {
            if (elements[i] != null) {
                tmp.add(elements[i]);
            }
        }
        this.elements = tmp.elements;
        this.size = tmp.size;
        this.capacity = tmp.capacity;
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {

        int i = 0;

        @Override
        public boolean hasNext() {
            return i < elements.length - 1;
        }

        @Override
        public T next() {
            return elements[++i];
        }

        @Override
        public void remove() {

        }

    }

    private static final String FILE = "src/main/resources/dic-0294.txt";

    public static void main(String[] args) throws Exception {
        MyHashSet<String> linearSet = new MyHashSet<String>(ProbeType.LINEAR);
        MyHashSet<String> squareSet = new MyHashSet<String>(ProbeType.SQUARE);
        MyHashSet<String> alterSquareSet = new MyHashSet<String>(
                ProbeType.ALTERNATE_SQUARE);
        List<String> words = Files.readAllLines(new File(FILE).toPath(),
                Charset.defaultCharset());
        // Calculate hash code of every string once
        for (String word : words) {
            word.hashCode();
        }

        long time = 0L;

        long t1 = 0L;
        long t2 = 0L;
        long t3 = 0L;

        time = System.nanoTime();
        for (String word : words) {
            linearSet.add(word);
        }

        t1 = System.nanoTime() - time;

        time = System.nanoTime();
        for (String word : words) {
            squareSet.add(word);
        }

        t2 = System.nanoTime() - time;

        time = System.nanoTime();
        for (String word : words) {
            alterSquareSet.add(word);
        }

        t3 = System.nanoTime() - time;

        for (String word : words) {
            if (!linearSet.contains(word)) {
                System.out.println(word + " ist nicht in LinearSet!");
                return;
            }
            if (!squareSet.contains(word)) {
                System.out.println(word + " ist nicht in SquareSet!");
                return;
            }
            if (!alterSquareSet.contains(word)) {
                System.out.println(word + " ist nicht in SquareSet!");
                return;
            }
        }

        System.out.println("Words\t" + linearSet.size);
        System.out.println("Setsize\t" + linearSet.capacity);
        System.out.println("Linear\t" + TimeUnit.NANOSECONDS.toMicros(t1)
                + "\tµs");
        System.out.println("Square\t" + TimeUnit.NANOSECONDS.toMicros(t2)
                + "\tµs");
        System.out.println("ASquare\t" + TimeUnit.NANOSECONDS.toMicros(t3)
                + "\tµs");

    }

}
