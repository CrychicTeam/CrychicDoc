package icyllis.arc3d.engine;

import java.util.Arrays;

public sealed interface Key permits Key.StorageKey, KeyBuilder {

    public static final class StorageKey implements Key {

        final int[] data;

        final int hash;

        public StorageKey(KeyBuilder b) {
            this.data = b.toIntArray();
            this.hash = Arrays.hashCode(this.data);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object o) {
            if (o instanceof Key.StorageKey key && Arrays.equals(this.data, key.data)) {
                return true;
            }
            return false;
        }
    }
}