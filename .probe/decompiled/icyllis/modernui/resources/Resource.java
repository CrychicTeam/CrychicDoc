package icyllis.modernui.resources;

public class Resource {

    public static final int TYPE_ANIM = 0;

    public static final int TYPE_ANIMATOR = 1;

    public static final int TYPE_ARRAY = 2;

    public static final int TYPE_ATTR = 3;

    public static final int TYPE_ATTR_PRIVATE = 4;

    public static final int TYPE_BOOL = 5;

    public static final int TYPE_COLOR = 6;

    public static final int TYPE_CONFIG_VARYING = 7;

    public static final int TYPE_DIMEN = 8;

    public static final int TYPE_DRAWABLE = 9;

    public static final int TYPE_FONT = 10;

    public static final int TYPE_FRACTION = 11;

    public static final int TYPE_ID = 12;

    public static final int TYPE_INTEGER = 13;

    public static final int TYPE_INTERPOLATOR = 14;

    public static final int TYPE_LAYOUT = 15;

    public static final int TYPE_MACRO = 16;

    public static final int TYPE_MENU = 17;

    public static final int TYPE_MIPMAP = 18;

    public static final int TYPE_NAVIGATION = 19;

    public static final int TYPE_PLURALS = 20;

    public static final int TYPE_RAW = 21;

    public static final int TYPE_STRING = 22;

    public static final int TYPE_STYLE = 23;

    public static final int TYPE_STYLEABLE = 24;

    public static final int TYPE_TRANSITION = 25;

    public static final int TYPE_XML = 26;

    public static String getTypeName(int type) {
        return switch(type) {
            case 0 ->
                "anim";
            case 1 ->
                "animator";
            case 2 ->
                "array";
            case 3 ->
                "attr";
            case 4 ->
                "^attr-private";
            case 5 ->
                "bool";
            case 6 ->
                "color";
            default ->
                "";
            case 8 ->
                "dimen";
            case 9 ->
                "drawable";
            case 10 ->
                "font";
            case 11 ->
                "fraction";
            case 12 ->
                "id";
            case 13 ->
                "integer";
            case 14 ->
                "interpolator";
            case 15 ->
                "layout";
            case 21 ->
                "raw";
            case 22 ->
                "string";
            case 23 ->
                "style";
            case 24 ->
                "styleable";
            case 25 ->
                "transition";
            case 26 ->
                "xml";
        };
    }

    public static class ResourceName {

        public String namespace;

        public String typename;

        public int type;

        public String entry;

        public ResourceName() {
            this.namespace = "";
            this.typename = "";
            this.type = 21;
            this.entry = "";
        }

        public ResourceName(String namespace, int type, String entry) {
            this.namespace = namespace;
            this.typename = Resource.getTypeName(type);
            this.type = type;
            this.entry = entry;
        }

        public void setType(int type) {
            this.typename = Resource.getTypeName(type);
            this.type = type;
        }

        public String toString() {
            return (this.namespace.isEmpty() ? "" : this.namespace + ":") + this.typename + "/" + this.entry;
        }
    }
}