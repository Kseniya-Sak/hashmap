package edu.sakovich.hashmap;

import java.util.Objects;

/**
 * This class provides all the basic HashMap operations, and permits null values and the null key.
 * This class makes no guarantees as to the order of the map.
 *
 * An instance of MyHashMap has load factor's field. The load factor is a measure of how full
 * the MyHashMap is allowed to get before its capacity is automatically increased. When
 * the number of entries in the MyHashMap exceeds the product of the load factor and the current
 * capacity, the MyHashMap is rehashed so that the hash table has approximately twice the number
 * of buckets.
 *
 * Note that this implementation is not synchronized. If multiple threads access a hash map
 * concurrently, and at least one of the threads modifies the map structurally, it must be
 * synchronized externally.
 * @param <K> the type of keys maintained by this MyHashMap
 * @param <V> the type of values maintained by this MyHashMap
 */

public class MyHashMap<K, V> {

    private Node<K, V>[] arrayNodes;
    private int size;
    static float LOAD_FACTOR = 0.75F;
    static final int MAX_CAPACITY = 1 << 30;
    static final int DEFAULT_CAPACITY = 1 << 4;

    /**
     * Basic MyHashMap node, used for most entries.
     * @param <K> the type of keys maintained by this MyHashMap
     * @param <V> the type of values maintained by this MyHashMap
     */
    protected static class Node<K, V> {
        final int hashcode;
        final K key;
        V value;
        Node<K, V> next;

        /**
         * Constructs a Node with the specified hashcode, key, value and next Node.
         * @param hashcode - a string representation of the key
         * @param key with which the specified value is to be associated
         * @param value to be associated with the specified key
         * @param next Node in the basket
         */
        protected Node(int hashcode, K key, V value, Node<K, V> next) {
            this.hashcode = hashcode;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    /**
     * Constructs an empty MyHashMap with the initial capacity (16) and the load factor (0.75).
     */
    public MyHashMap() {
    }

    /**
     * Saves the specified value with the specified key in MyHashMap. If the MyHashMap previously
     * contained a mapping for the key, the old value is replaced.
     * @param key with which the specified value is to be associated
     * @param value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no mapping for key.
     * A null return can also indicate that the MyHashMap previously associated null with key.
     */
    public V put(K key, V value) {
        if (arrayNodes == null) {
            resizeArrayNodes();
        }

        int index = findIndexBy(key);

        if (isEmptyBasket(index)) {
            arrayNodes[index] = new Node<>(hashcode(key), key, value, null);
        } else {
            Node<K, V> previousNode = null;
            Node<K, V> currentNode = arrayNodes[index];

            do {
                if (isIdenticalKeyInNode(key, currentNode)) {
                    V previousValue = currentNode.value;
                    currentNode.value = value;
                    return previousValue;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            } while (currentNode != null);

            previousNode.next = new Node<>(hashcode(key), key, value, null);
        }

        if (++size > arrayNodes.length * LOAD_FACTOR && arrayNodes.length < MAX_CAPACITY) {
            resizeArrayNodes();
        }
        return null;
    }

    private int hashcode(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private boolean isIdenticalKeyInNode(Object key, Node<K, V> node) {
        return hashcode(key) == node.hashcode &&
                (Objects.equals(key, node.key));
    }

    private boolean isEmptyBasket(int index) {
        return arrayNodes[index] == null;
    }

    private boolean isNotEmptyBasket(int index) {
        return !isEmptyBasket(index);
    }

    private int findIndexBy(Object key) {
        return hashcode(key) & (arrayNodes.length - 1);
    }

    private void resizeArrayNodes() {
        int newCapacity = arrayNodes == null ? DEFAULT_CAPACITY :
                Math.min((arrayNodes.length << 1), MAX_CAPACITY);
        if (this.isEmpty()) {
            arrayNodes = (Node<K, V>[]) new Node[newCapacity];
        } else {
            addNodesFromOldArrayNodesToNewArrayNodes(newCapacity);
        }
    }

    private void addNodesFromOldArrayNodesToNewArrayNodes(int newCapacity) {
        Node<K, V>[] oldArrayNodes = arrayNodes;
        size = 0;
        arrayNodes = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : oldArrayNodes) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                this.put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    /**
     * Returns true if this MyHashMap contains no key-value mappings.
     * @return true if this MyHashMap contains no key-value mappings
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all the mappings from this MyHashMap. The MyHashMap will be empty after this call returns.
     */
    public void clear() {
        for (int i = 0; i < arrayNodes.length; i++) {
            arrayNodes[i] = null;
        }
        size = 0;
    }

    /**
     * Returns the number of key-value mappings in this MyHashMap.
     * @return the number of key-value mappings in this MyHashMap.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or null if this MyHashMap contains no mapping for the key.
     * A return value of null does not necessarily indicate that
     * the MyHashMap contains no mapping for the key; it's also possible
     * that the MyHashMap explicitly maps the key to null. The containsKey
     * operation may be used to distinguish these two cases.
     * @param key by which the value is searched
     * @return the value maintained by this MyHashMap or null
     */
    public V get(Object key) {
        if (Objects.isNull(arrayNodes)) {
            return null;
        }
        Node<K, V> node;
        return (node = getNodeBy(key)) == null ? null : node.value;

    }

    private Node<K, V> getNodeBy(Object key) {
        if (isNotEmptyBasket(findIndexBy(key))) {
            Node<K, V> currentNode = arrayNodes[findIndexBy(key)];
            while (currentNode != null) {
                if (isIdenticalKeyInNode(key, currentNode)) {
                    return currentNode;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    /**
     * Removes the mapping for the specified key from this MyHashMap if present.
     * @param key whose mapping is to be removed from the MyHashMap
     * @return the previous value associated with key, or null if there was no mapping for this key.
     * A null return can also indicate that the MyHashMap previously associated null with this key.
     */
    public V remove(Object key) {
        if (arrayNodes == null || isEmptyBasket(findIndexBy(key))) {
            return null;
        }
        return removeFromIsNotEmptyBasket(key);
    }

    private V removeFromIsNotEmptyBasket(Object key) {

        int index = findIndexBy(key);
        Node<K, V> currentNode = arrayNodes[index];
        Node<K, V> previousNode = null;
        do {
            if (isIdenticalKeyInNode(key, currentNode)) {
                if (isSingleNodeInBasket(previousNode, currentNode.next)) {
                    return removeSingleNodeInBasket(currentNode);
                }
                if (isFirstNodeInBasket(previousNode, currentNode.next)) {
                    return removeFirstNodeInBasket(currentNode);
                }
                if (isNodeBetweenNodesInBasket(previousNode, currentNode.next)) {
                    return removeNodeBetweenNodesInBasket(previousNode, currentNode, currentNode.next);
                }
                return removeLastNodeInBasket(previousNode, currentNode);
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        } while (currentNode != null);

        return null;
    }

    private boolean isSingleNodeInBasket(Node<K,V> previousNode, Node<K,V> nextNode) {
        return previousNode == null && nextNode == null;
    }

    private V removeSingleNodeInBasket(Node<K,V> currentNode) {
            arrayNodes[findIndexBy(currentNode.key)] = null;
            size--;
            return currentNode.value;
    }

    private boolean isFirstNodeInBasket(Node<K,V> previousNode, Node<K,V> nextNode) {
        return previousNode == null && nextNode != null;
    }

    private V removeFirstNodeInBasket(Node<K,V> currentNode) {
        arrayNodes[findIndexBy(currentNode.key)] = currentNode.next;
        size--;
        return currentNode.value;
    }

    private boolean isNodeBetweenNodesInBasket(Node<K,V> previousNode, Node<K,V> nextNode) {
        return previousNode != null && nextNode != null;
    }

    private V removeNodeBetweenNodesInBasket(Node<K,V> previousNode, Node<K,V> currentNode, Node<K,V> nextNode) {
        previousNode.next = nextNode;
        size--;
        return currentNode.value;
    }

    private V removeLastNodeInBasket(Node<K,V> previousNode, Node<K,V> currentNode) {
        previousNode.next = null;
        size--;
        return currentNode.value;
    }

    /**
     * Returns true if this MyHashMap contains a mapping for the specified key.
     * @param key whose presence in this MyHashMap is to be tested
     * @return true if this MyHashMap contains a mapping for the specified key
     */
    public boolean containsKey(Object key) {
        if (arrayNodes == null) {
            return false;
        }
        int index = findIndexBy(key);
        Node<K, V> currentNode = arrayNodes[index];
        while (currentNode != null) {
            if (isIdenticalKeyInNode(key, currentNode)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    /**
     * Returns true if this MyHashMap contains one or more keys to the specified value.
     * @param value whose presence in this MyHashMap is to be tested
     * @return true if this MyHashMap contains one or more keys to the specified value
     */
    public boolean containsValue(Object value) {
        if (arrayNodes == null) {
            return false;
        }
        for (Node<K, V> currentNode : arrayNodes) {
            while (currentNode != null) {
                if (Objects.equals(currentNode.value, value)) {
                    return true;
                }
                currentNode = currentNode.next;
            }
        }
        return false;
    }

    /**
     * Returns a string representation of MyHashMap's object.
     * @return a string representation of MyHashMap's object.
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        for (Node<K, V> node : arrayNodes) {
            while (node != null) {
                sb.append(node.key)
                        .append('=')
                        .append(node.value)
                        .append(',').append(' ');
                node = node.next;
            }
        }
        sb.replace(sb.length() - 2, sb.length(), "}");
        return sb.toString();
    }
}
