package com.github.typingtanuki.mail;

import java.util.*;

public class ChainCounter {
    private static final double MIN_SUPPORT = 0.1d;

    private final int length;
    private final Map<String, Long> counts = new LinkedHashMap<>();

    public ChainCounter(int length) {
        this.length = length;
    }

    public void countFor(StringBuilder chain, String s) {
        if (length == 1) {
            count(counts, s);
            return;
        }
        if (chain.length() > length) {
            count(counts, chain.substring(chain.length() - length + 1) + s);
        }
    }

    private static void count(Map<String, Long> counts, String s) {
        Long count = counts.getOrDefault(s, 0L);
        count++;
        counts.put(s, count);
    }

    public void consolidate(ChainCounter b) {
        removeLowOccurrence();

        if (b == null) {
            return;
        }

        Set<Map.Entry<String, Long>> entries = new LinkedHashSet<>(counts.entrySet());
        Set<String> bKeys = b.keys();

        for (Map.Entry<String, Long> entry : entries) {
            String key = entry.getKey();
            long value = entry.getValue();

            long accum = 0;
            for (String bKey : bKeys) {
                if (bKey.contains(key)) {
                    accum += b.get(bKey);
                }
            }
            if (accum == value) {
                counts.remove(key);
            }
        }
    }

    public void display() {
        if (isEmpty()) {
            return;
        }
        System.out.println("Length " + length + ":");
        showTop();
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    private Long get(String key) {
        return counts.get(key);
    }

    private Set<String> keys() {
        return new LinkedHashSet<>(counts.keySet());
    }

    private void removeLowOccurrence() {
        if (counts.isEmpty()) {
            return;
        }

        long boing = 500;

        Set<Map.Entry<String, Long>> entries = new LinkedHashSet<>(counts.entrySet());
        for (Map.Entry<String, Long> entry : entries) {
            long val = entry.getValue();
            if (val < boing) {
                counts.remove(entry.getKey());
            }
        }
    }

    private List<Long> computeValues() {
        Set<Long> noDup = new LinkedHashSet<>(counts.values());
        List<Long> values = new ArrayList<>(noDup);
        values.sort(Long::compareTo);
        Collections.reverse(values);
        return values;
    }

    private void showTop() {
        List<Long> values = computeValues();
        Long top = null;

        for (Long v : values) {
            if (top == null) {
                top = v;
            } else if (v < top * MIN_SUPPORT) {
                return;
            }
            System.out.print(v + ": ");
            for (Map.Entry<String, Long> entry : counts.entrySet()) {
                if (entry.getValue().equals(v)) {
                    System.out.print(entry.getKey() + " ");
                }
            }
            System.out.println();
        }
    }
}
