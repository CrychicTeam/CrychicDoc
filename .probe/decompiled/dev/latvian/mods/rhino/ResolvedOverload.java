package dev.latvian.mods.rhino;

import java.util.Arrays;

public class ResolvedOverload {

    final Class<?>[] types;

    final int index;

    ResolvedOverload(Object[] args, int index) {
        this.index = index;
        this.types = new Class[args.length];
        int i = 0;
        for (int l = args.length; i < l; i++) {
            Object arg = args[i];
            if (arg instanceof Wrapper) {
                arg = ((Wrapper) arg).unwrap();
            }
            this.types[i] = arg == null ? null : arg.getClass();
        }
    }

    boolean matches(Object[] args) {
        if (args.length != this.types.length) {
            return false;
        } else {
            int i = 0;
            for (int l = args.length; i < l; i++) {
                Object arg = args[i];
                if (arg instanceof Wrapper) {
                    arg = ((Wrapper) arg).unwrap();
                }
                if (arg == null) {
                    if (this.types[i] != null) {
                        return false;
                    }
                } else if (arg.getClass() != this.types[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean equals(Object other) {
        return !(other instanceof ResolvedOverload ovl) ? false : Arrays.equals(this.types, ovl.types) && this.index == ovl.index;
    }

    public int hashCode() {
        return Arrays.hashCode(this.types);
    }
}