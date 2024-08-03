package dev.latvian.mods.kubejs.util;

import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassFilter {

    private static final byte V_DEF = -1;

    private static final byte V_DENY = 0;

    private static final byte V_ALLOW = 1;

    private final Set<String> denyStrong = new HashSet();

    private final List<String> denyWeak = new ArrayList();

    private final Set<String> allowStrong = new HashSet();

    private final List<String> allowWeak = new ArrayList();

    private final Object2ByteOpenHashMap<String> cache = new Object2ByteOpenHashMap();

    public ClassFilter() {
        this.cache.defaultReturnValue((byte) -1);
    }

    public void deny(String s) {
        if (!(s = s.trim()).isEmpty()) {
            this.denyStrong.add(s);
            if (!this.denyWeak.contains(s)) {
                this.denyWeak.add(s);
            }
        }
    }

    public void deny(Class<?> c) {
        this.deny(c.getName());
    }

    public void allow(String s) {
        if (!(s = s.trim()).isEmpty()) {
            this.allowStrong.add(s);
            if (!this.allowWeak.contains(s)) {
                this.allowWeak.add(s);
            }
        }
    }

    public void allow(Class<?> c) {
        this.allow(c.getName());
    }

    private byte isAllowed0(String s) {
        if (this.denyStrong.contains(s)) {
            return 0;
        } else if (this.allowStrong.contains(s)) {
            return 1;
        } else {
            for (String s1 : this.denyWeak) {
                if (s.startsWith(s1)) {
                    return 0;
                }
            }
            return 1;
        }
    }

    public boolean isAllowed(String s) {
        byte b = this.cache.getByte(s);
        if (b == -1) {
            b = this.isAllowed0(s);
            this.cache.put(s, b);
        }
        return b == 1;
    }
}