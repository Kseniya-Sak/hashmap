package edu.sakovich.hashmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {
    private static MyHashMap<Integer, String> testMyHashMap;
    private static final String TEST = "test";
    private static final String WRONG_TYPE = "wrongType";

    @BeforeEach
    private void createMyHashMap() {
        testMyHashMap = new MyHashMap<>();
    }

    @Test
    void testPutTwoIdenticalItemsAreNull() {
        testMyHashMap.put(null, null);
        assertAll(
                () -> assertNull(testMyHashMap.put(null, null)),
                () -> assertEquals(1, testMyHashMap.size()),
                () -> assertNull(testMyHashMap.put(null, null)),
                () -> assertEquals(1, testMyHashMap.size()),
                () -> assertEquals("{null=null}", testMyHashMap.toString())
        );
    }

    @Test
    void testPutThreeItemsKeyIsNullValueIsNotNull() {
        assertAll(
                () -> assertNull(testMyHashMap.put(null, "firstValue")),
                () -> assertEquals(1, testMyHashMap.size()),
                () -> assertEquals("firstValue", testMyHashMap.put(null, "secondValue")),
                () -> assertEquals(1, testMyHashMap.size()),
                () -> assertEquals("secondValue", testMyHashMap.put(null, "thirdValue")),
                () -> assertEquals(1, testMyHashMap.size()),
                () -> assertEquals("{null=thirdValue}", testMyHashMap.toString())
        );
    }

    @Test
    void testPutThreeItemsKeyIsNotNullValueIsNull() {
        assertAll(
                () -> assertNull(testMyHashMap.put(1, null)),
                () -> assertEquals(1, testMyHashMap.size()),
                () -> assertNull(testMyHashMap.put(2, null)),
                () -> assertEquals(2, testMyHashMap.size()),
                () -> assertNull(testMyHashMap.put(3, null)),
                () -> assertEquals(3, testMyHashMap.size()),
                () -> assertEquals("{1=null, 2=null, 3=null}", testMyHashMap.toString())
        );
    }

    @Test
    void testPutTwoIdenticalItemsNotNull() {
        assertAll(
                () -> assertNull(testMyHashMap.put(1, "firstValue")),
                () -> assertEquals(1, testMyHashMap.size()),
                () -> assertEquals("firstValue", testMyHashMap.put(1, "secondValue")),
                () -> assertEquals(1, testMyHashMap.size()),
                () -> assertEquals("{1=secondValue}", testMyHashMap.toString())
        );
    }

    @Test
    void testPutTenMillionItems() {
        fullTestMyHashMap(0, 10_000_000);

        assertAll(
                () -> assertEquals(10_000_000, testMyHashMap.size())
        );
    }

    @Test
    void testClear() {
        fullTestMyHashMap(0, 20);

        assertAll(
                () -> assertEquals(20, testMyHashMap.size()),
                () -> {
                    testMyHashMap.clear();
                    assertEquals(0, testMyHashMap.size());
                },
                () -> assertEquals("{}", testMyHashMap.toString())
        );
    }


    @Test
    void testGet() {
        assertAll(
                () -> assertNull(testMyHashMap.get(100)),
                () -> {
                    fullTestMyHashMap(0, 20);
                    assertEquals("test0", testMyHashMap.get(0));
                },
                () -> assertEquals("test10", testMyHashMap.get(10)),
                () -> assertEquals("test19", testMyHashMap.get(19)),
                () -> {
                    testMyHashMap.put(20, null);
                    assertNull(testMyHashMap.get(20));
                },
                () -> {
                    testMyHashMap.put(null, TEST);
                    assertEquals(TEST, testMyHashMap.get(null));
                },
                () -> assertNull(testMyHashMap.get(WRONG_TYPE))
        );
    }

    @Test
    void testGetTenMillionItems() {
        fullTestMyHashMap(0, 10_000_000);

        for (int i = 0; i < 10_000_000; i++) {
            assertEquals(TEST + i, testMyHashMap.get(i));
        }
    }

    @Test
    void testRemove() {
        assertAll(
                () -> assertNull(testMyHashMap.remove(0)),
                () -> {
                    fullTestMyHashMap(0, 100);
                    assertEquals("test0", testMyHashMap.remove(0));
                },
                () -> assertEquals("test99", testMyHashMap.remove(99)),
                () -> assertNull(testMyHashMap.remove(99)),
                () -> assertNull(testMyHashMap.remove(WRONG_TYPE))
        );
    }

    @Test
    void testRemoveTenThousandItems() {
        fullTestMyHashMap(0, 10_000);

        for (int i = 0; i < 10_000; i++) {
            assertEquals(TEST + i, testMyHashMap.remove(i));

        }
    }

    @Test
    void testContainsKey() {
        assertAll(
                () -> assertFalse(testMyHashMap.containsKey(0)),
                () -> {
                    fullTestMyHashMap(0, 20);
                    assertTrue(testMyHashMap.containsKey(0));
                },
                () -> assertTrue(testMyHashMap.containsKey(13)),
                () -> assertTrue(testMyHashMap.containsKey(19)),
                () -> assertFalse(testMyHashMap.containsKey(-1)),
                () -> assertFalse(testMyHashMap.containsKey(1000)),
                () -> assertFalse(testMyHashMap.containsKey(WRONG_TYPE))
        );
    }

    @Test
    void testContainsKeyTenMillionItems() {
        fullTestMyHashMap(0, 10_000_000);
        for (int i = 0; i < 10_000_000; i++) {
            assertTrue(testMyHashMap.containsKey(i));
        }
    }

    @Test
    void testContainsValueTenThousandItems() {
        fullTestMyHashMap(0, 10_000);

        for (int i = 0; i < 10_000; i++) {
            assertTrue(testMyHashMap.containsValue(TEST + i));

        }
    }

    @Test
    void testContainsValue() {
        assertAll(
                () -> assertFalse(testMyHashMap.containsValue(TEST)),
                () -> {
                    fullTestMyHashMap(0, 20);
                    assertTrue(testMyHashMap.containsValue("test0"));
                },
                () -> assertTrue(testMyHashMap.containsValue("test13")),
                () -> assertTrue(testMyHashMap.containsValue("test19")),
                () -> assertFalse(testMyHashMap.containsValue(TEST)),
                () -> assertFalse(testMyHashMap.containsValue("test1000")),
                () -> assertFalse(testMyHashMap.containsValue(0))
        );
    }

    @Test
    void testSize() {
        assertAll(
                () -> assertEquals(0, testMyHashMap.size()),
                () -> {
                    fullTestMyHashMap(0, 20);
                    assertEquals(20, testMyHashMap.size());
                }
        );
    }

    private void fullTestMyHashMap(int start, int end) {
        for (int i = start; i < end; i++) {
            testMyHashMap.put(i, TEST + i);
        }
    }

    @Test
    void testIsEmpty() {
        assertAll(
                () -> assertTrue(testMyHashMap.isEmpty()),
                () -> {
                    testMyHashMap.put(1, TEST);
                    assertFalse(testMyHashMap.isEmpty());
                }
        );
    }

    @Test
    void testToString() {
        assertAll(
                () -> assertEquals("{}", testMyHashMap.toString()),
                () -> {
                    testMyHashMap.put(0, TEST);
                    assertEquals("{0=test}", testMyHashMap.toString());
                }
        );
    }
}