package de.keksuccino.konkrete.json.minidev.json.reader;

import de.keksuccino.konkrete.json.minidev.asm.Accessor;
import de.keksuccino.konkrete.json.minidev.asm.BeansAccess;
import de.keksuccino.konkrete.json.minidev.json.JSONObject;
import de.keksuccino.konkrete.json.minidev.json.JSONStyle;
import de.keksuccino.konkrete.json.minidev.json.JSONUtil;
import java.io.IOException;

public class BeansWriterASM implements JsonWriterI<Object> {

    @Override
    public <E> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
        try {
            Class<?> cls = value.getClass();
            boolean needSep = false;
            BeansAccess fields = BeansAccess.get(cls, JSONUtil.JSON_SMART_FIELD_FILTER);
            out.append('{');
            for (Accessor field : fields.getAccessors()) {
                Object v = fields.get(value, field.getIndex());
                if (v != null || !compression.ignoreNull()) {
                    if (needSep) {
                        out.append(',');
                    } else {
                        needSep = true;
                    }
                    String key = field.getName();
                    JSONObject.writeJSONKV(key, v, out, compression);
                }
            }
            out.append('}');
        } catch (IOException var13) {
            throw var13;
        }
    }
}