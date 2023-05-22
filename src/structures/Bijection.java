package structures;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a one-to-one correspondance, a two-way
 * map with unique keys and unique values.
 *
 * DO NOT ALTER THIS FILE!
 *
 * @author Josh Archer
 * @version 1.0
 * @param <K> the type of the keys
 * @param <V> the type of the values
 */
public class Bijection<K, V>
{
    private Map<K, V> leftToRight;
    private Map<V, K> rightToLeft;

    /**
     * Creates a new Bijection.
     */
    public Bijection()
    {
        leftToRight = new HashMap<>();
        rightToLeft = new HashMap<>();
    }

    /**
     * Adds a key - value pair to the map. All keys must be unique, relative
     * to one another. Also, values must be unique as well.
     *
     * @param key the new key
     * @param value the new value
     * @return true if the key - value pair was added to the map, or false otherwise
     */
    public boolean add(K key, V value)
    {
        //make sure key and value are both unique
        if (leftToRight.containsKey(key) ||
                rightToLeft.containsKey(value))
        {
            return false;
        }

        leftToRight.put(key, value);
        rightToLeft.put(value, key);
        return true;
    }

    /**
     * Retrieves a value, given a key.
     *
     * @param key the key to search for
     * @return a value, or null if none is found
     */
    public V getValue(K key)
    {
        return leftToRight.get(key);
    }

    /**
     * Retrieves a key, given a value.
     *
     * @param value the value to search for
     * @return a key, or null if none is found
     */
    public K getKey(V value)
    {
        return rightToLeft.get(value);
    }

    /**
     * Reports whether a key is in the map.
     *
     * @param key the key to search for
     * @return true if the key is found, or otherwise false
     */
    public boolean containsKey(K key)
    {
        return leftToRight.containsKey(key);
    }

    /**
     * Reports whether a value is in the map.
     *
     * @param value the value to search for
     * @return true if the value is found, or otherwise false
     */
    public boolean containsValue(V value)
    {
        return rightToLeft.containsKey(value);
    }

    /**
     * Returns all keys in the map.
     * @return a set of keys
     */
    public Set<K> keySet()
    {
        return Collections.unmodifiableSet(leftToRight.keySet());
    }

    /**
     * Returns all values in the map.
     * @return a set of values
     */
    public Set<V> valueSet()
    {
        return Collections.unmodifiableSet(rightToLeft.keySet());
    }

    /**
     * Removes a key-value pair from the map, given a key.
     * @param key the key to search for
     * @return true if the key-value pair was found and removed, otherwise false
     */
    public boolean removeKey(K key)
    {
        if (!leftToRight.containsKey(key))
        {
            return false;
        }

        V value = leftToRight.get(key);
        remove(key, value);
        return true;
    }

    /**
     * Removes a key-value pair from the map, given a value.
     * @param value the value to search for
     * @return true if the key-value pair was found and removed, otherwise false
     */
    public boolean removeValue(V value)
    {
        if (!rightToLeft.containsKey(value))
        {
            return false;
        }

        K key = rightToLeft.get(value);
        remove(key, value);
        return true;
    }

    private void remove(K key, V value)
    {
        leftToRight.remove(key);
        rightToLeft.remove(value);
    }

    /**
     * Removes all key-value pairs from the map.
     */
    public void clear()
    {
        leftToRight.clear();
        rightToLeft.clear();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        boolean first = true;
        for (K key : leftToRight.keySet())
        {
            if (!first)
            {
                builder.append(", ");
            }
            else
            {
                first = false;
            }

            builder.append(key);
            builder.append(" - ");
            builder.append(leftToRight.get(key));
        }

        return builder.toString();
    }
}
