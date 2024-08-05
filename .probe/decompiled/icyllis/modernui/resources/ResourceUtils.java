package icyllis.modernui.resources;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import javax.xml.stream.XMLStreamReader;

public class ResourceUtils {

    @Nullable
    public static String findAttribute(@NonNull XMLStreamReader reader, @NonNull String name) {
        String res = reader.getAttributeValue(null, name);
        return res != null ? res.trim() : null;
    }

    @Nullable
    public static String findNonEmptyAttribute(@NonNull XMLStreamReader reader, @NonNull String name) {
        String res = reader.getAttributeValue(null, name);
        if (res != null) {
            String trim = res.trim();
            if (!trim.isEmpty()) {
                return trim;
            }
        }
        return null;
    }

    @NonNull
    public static ResourceValues.Reference parseXmlAttributeName(@NonNull String s) {
        String name = s.trim();
        int start = 0;
        ResourceValues.Reference ref = new ResourceValues.Reference();
        if (!name.isEmpty() && name.charAt(0) == '*') {
            ref.private_reference = true;
            start++;
        }
        int i = name.indexOf(58, start);
        String namespace;
        String entry;
        if (i >= 0) {
            namespace = name.substring(0, i);
            entry = name.substring(i + 1);
        } else {
            namespace = "";
            entry = name.substring(start);
        }
        ref.name = new Resource.ResourceName(namespace, 3, entry);
        return ref;
    }
}