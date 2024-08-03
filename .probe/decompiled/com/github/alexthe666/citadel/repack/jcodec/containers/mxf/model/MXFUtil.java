package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MXFUtil {

    public static <T> T resolveRef(List<MXFMetadata> metadata, UL refs, Class<T> class1) {
        List<T> res = resolveRefs(metadata, new UL[] { refs }, class1);
        return (T) (res.size() > 0 ? res.get(0) : null);
    }

    public static <T> List<T> resolveRefs(List<MXFMetadata> metadata, UL[] refs, Class<T> class1) {
        List<MXFMetadata> copy = new ArrayList(metadata);
        Iterator<MXFMetadata> iterator = copy.iterator();
        while (iterator.hasNext()) {
            MXFMetadata next = (MXFMetadata) iterator.next();
            if (next.getUid() == null || !Platform.isAssignableFrom(class1, next.getClass())) {
                iterator.remove();
            }
        }
        List result = new ArrayList();
        for (int i = 0; i < refs.length; i++) {
            for (MXFMetadata meta : copy) {
                if (meta.getUid().equals(refs[i])) {
                    result.add(meta);
                }
            }
        }
        return result;
    }

    public static <T> T findMeta(Collection<MXFMetadata> metadata, Class<T> class1) {
        for (MXFMetadata mxfMetadata : metadata) {
            if (Platform.isAssignableFrom(mxfMetadata.getClass(), class1)) {
                return (T) mxfMetadata;
            }
        }
        return null;
    }

    public static <T> List<T> findAllMeta(Collection<MXFMetadata> metadata, Class<T> class1) {
        List result = new ArrayList();
        for (MXFMetadata mxfMetadata : metadata) {
            if (Platform.isAssignableFrom(class1, mxfMetadata.getClass())) {
                result.add(mxfMetadata);
            }
        }
        return result;
    }
}