package icyllis.modernui.resources;

import icyllis.modernui.annotation.Nullable;
import java.util.List;

public class ResourceValues {

    public static class Attribute extends ResourceValues.Value {

        public int type_mask;

        public int min_int;

        public int max_int;

        public List<ResourceValues.Attribute.Symbol> symbols;

        public Attribute(int t) {
            this.type_mask = t;
            this.min_int = Integer.MIN_VALUE;
            this.max_int = Integer.MAX_VALUE;
        }

        public String toString() {
            return "Attribute{type_mask=" + this.type_mask + ", min_int=" + this.min_int + ", max_int=" + this.max_int + ", symbols=" + this.symbols + "}";
        }

        public static class Symbol {

            ResourceValues.Reference symbol;

            int value;

            byte type;
        }
    }

    public static class Reference extends ResourceValues.Value {

        public static final byte RESOURCE = 0;

        public static final byte ATTRIBUTE = 1;

        @Nullable
        public Resource.ResourceName name;

        public boolean has_id;

        public int id;

        public boolean has_type_flags;

        public int type_flags;

        public byte reference_type;

        public boolean private_reference = false;

        public boolean is_dynamic = false;

        public boolean allow_raw = false;

        public String toString() {
            return "Reference{name=" + this.name + ", has_id=" + this.has_id + ", id=" + this.id + ", has_type_flags=" + this.has_type_flags + ", type_flags=" + this.type_flags + ", reference_type=" + this.reference_type + ", private_reference=" + this.private_reference + ", is_dynamic=" + this.is_dynamic + ", allow_raw=" + this.allow_raw + "}";
        }
    }

    public static class Styleable extends ResourceValues.Value {

        public List<ResourceValues.Reference> entries;

        public String toString() {
            return "Styleable{entries=" + this.entries + "}";
        }
    }

    public static class Value {

        protected boolean weak = false;

        protected boolean translatable = true;
    }
}