import java.util.Collection;

/**
 * A generic interface used by two databases in SIS, CourseDB and UserDB
 * @param <K> key value
 * @param <V> value type
 * @author Michael Jansen
 */
public interface DB<K, V> {
    /**
     * Add a value entry to the database in linear time
     * The database will determine the key based on the value type
     * @param value the value to add
     * @return the previous value associated with the key, otherwise null
     */
    V addValue(V value);

    /**
     * Get all the values in the database in linear time
     * @return all of the values
     */
    Collection<V> getAllValues();

    /**
     * Get the value for an associated key in constant time
     * @param key the key
     * @return the value that is associated with the key, or null if not present
     */
    V getValue(K key);

    /**
     * Indicates whether a key is in the database or not, in constant time
     * @param key the key to search for
     * @return whether the key is present or not
     */
    boolean hasKey(K key);
}
