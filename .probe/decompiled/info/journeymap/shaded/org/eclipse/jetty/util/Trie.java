package info.journeymap.shaded.org.eclipse.jetty.util;

import java.nio.ByteBuffer;
import java.util.Set;

public interface Trie<V> {

    boolean put(String var1, V var2);

    boolean put(V var1);

    V remove(String var1);

    V get(String var1);

    V get(String var1, int var2, int var3);

    V get(ByteBuffer var1);

    V get(ByteBuffer var1, int var2, int var3);

    V getBest(String var1);

    V getBest(String var1, int var2, int var3);

    V getBest(byte[] var1, int var2, int var3);

    V getBest(ByteBuffer var1, int var2, int var3);

    Set<String> keySet();

    boolean isFull();

    boolean isCaseInsensitive();

    void clear();
}