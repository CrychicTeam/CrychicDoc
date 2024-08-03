package me.jellysquid.mods.sodium.client.gl.shader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class ShaderConstants {

    private final List<String> defines;

    private ShaderConstants(List<String> defines) {
        this.defines = defines;
    }

    public List<String> getDefineStrings() {
        return this.defines;
    }

    public static ShaderConstants.Builder builder() {
        return new ShaderConstants.Builder();
    }

    public static class Builder {

        private static final String EMPTY_VALUE = "";

        private final HashMap<String, String> constants = new HashMap();

        private Builder() {
        }

        public void add(String name) {
            this.add(name, "");
        }

        public void add(String name, String value) {
            String prev = (String) this.constants.get(name);
            if (prev != null) {
                throw new IllegalArgumentException("Constant " + name + " is already defined with value " + prev);
            } else {
                this.constants.put(name, value);
            }
        }

        public ShaderConstants build() {
            List<String> defines = new ArrayList(this.constants.size());
            for (Entry<String, String> entry : this.constants.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (value.isEmpty()) {
                    defines.add("#define " + key);
                } else {
                    defines.add("#define " + key + " " + value);
                }
            }
            return new ShaderConstants(Collections.unmodifiableList(defines));
        }

        public void addAll(List<String> defines) {
            for (String value : defines) {
                this.add(value);
            }
        }
    }
}