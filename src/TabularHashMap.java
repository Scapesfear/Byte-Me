import java.util.HashMap;
import java.util.Map;

public class TabularHashMap<K1, K2, V> {

    private final Map<K1, Map<K2, V>> mMap;

    public TabularHashMap() {
        mMap = new HashMap<K1, Map<K2, V>>();
    }


    public V put(K1 key1, K2 key2, V value) {
        Map<K2, V> map;
        if (mMap.containsKey(key1)) {
            map = mMap.get(key1);
        } else {
            map = new HashMap<K2, V>();
            mMap.put(key1, map);
        }

        return map.put(key2, value);
    }


    public V get(K1 key1, K2 key2) {
        if (mMap.containsKey(key1)) {
            return mMap.get(key1).get(key2);
        } else {
            return null;
        }
    }

    public Map<K2, V> get(K1 key1) {
        if (mMap.containsKey(key1)) {
            return mMap.get(key1);
        } else {
            return null;
        }
    }


    public boolean containsKeys(K1 key1, K2 key2) {
        return mMap.containsKey(key1) && mMap.get(key1).containsKey(key2);
    }

    public void clear() {
        mMap.clear();
    }

}