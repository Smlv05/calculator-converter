package io.github.smlv05.calculator;

import java.util.ArrayList;
import java.util.List;

public class History {

    private static final int MAX_SIZE = 10;

    private final List<String> records = new ArrayList<>();

    public void add(String record) {
        if (record == null || record.isBlank()) {
            throw new IllegalArgumentException("Record cannot be empty");
        }
        if (records.size() == MAX_SIZE) {
            records.remove(0);
        }
        records.add(record);
    }

    public List<String> getAll() {
        return new ArrayList<>(records);
    }

    public void clear() {
        records.clear();
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }
}