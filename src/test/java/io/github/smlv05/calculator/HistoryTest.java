package io.github.smlv05.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("History")
class HistoryTest {

    private History history;

    @BeforeEach
    void setUp() {
        history = new History();
    }

    private void addRecords(int count) {
        for (int i = 1; i <= count; i++) {
            history.add("op " + i);
        }
    }

    @Test
    @DisplayName("new history is empty")
    void newHistoryIsEmpty() {
        assertTrue(history.isEmpty());
        assertEquals(0, history.getAll().size());
    }

    @Nested
    @DisplayName("Adding")
    class Adding {

        @Test
        @DisplayName("keeps records in the order they were added")
        void keepsOrder() {
            history.add("2 + 3 = 5");
            history.add("10 / 2 = 5");

            assertEquals(List.of("2 + 3 = 5", "10 / 2 = 5"), history.getAll());
        }

        @ParameterizedTest(name = "after adding {0} records size is {1}")
        @CsvSource({
                "1, 1",
                "9, 9",
                "10, 10",
                "11, 10",
                "25, 10"
        })
        void keepsAtMostTenRecords(int added, int expectedSize) {
            addRecords(added);

            assertEquals(expectedSize, history.getAll().size());
        }

        @Test
        @DisplayName("11th record removes the oldest one")
        void removesOldestRecord() {
            addRecords(11);

            List<String> records = history.getAll();
            assertEquals("op 2", records.get(0));
            assertEquals("op 11", records.get(9));
        }

        @ParameterizedTest(name = "record \"{0}\" throws exception")
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        void throwsOnEmptyRecord(String record) {
            assertThrows(IllegalArgumentException.class, () -> history.add(record));
        }
    }

    @Nested
    @DisplayName("Clearing")
    class Clearing {

        @Test
        @DisplayName("clear removes all records")
        void clearRemovesAll() {
            addRecords(5);

            history.clear();

            assertTrue(history.isEmpty());
        }

        @Test
        @DisplayName("clear on empty history does not fail")
        void clearEmptyHistory() {
            assertDoesNotThrow(() -> history.clear());
        }

        @Test
        @DisplayName("records can be added after clear")
        void addAfterClear() {
            addRecords(10);
            history.clear();

            history.add("1 + 1 = 2");

            assertEquals(List.of("1 + 1 = 2"), history.getAll());
        }
    }

    @Test
    @DisplayName("changing returned list does not change history")
    void getAllReturnsCopy() {
        history.add("1 + 1 = 2");

        List<String> records = history.getAll();
        records.add("hacked");

        assertEquals(1, history.getAll().size());
    }
}