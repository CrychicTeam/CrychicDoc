package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import java.util.HashMap;
import java.util.Map;

public abstract class Boxes {

    protected final Map<String, Class<? extends Box>> mappings = new HashMap();

    public Class<? extends Box> toClass(String fourcc) {
        return (Class<? extends Box>) this.mappings.get(fourcc);
    }

    public void override(String fourcc, Class<? extends Box> cls) {
        this.mappings.put(fourcc, cls);
    }

    public void clear() {
        this.mappings.clear();
    }
}