package dev.latvian.mods.rhino;

public class SymbolKey implements Symbol {

    public static final SymbolKey ITERATOR = new SymbolKey("Symbol.iterator");

    public static final SymbolKey TO_STRING_TAG = new SymbolKey("Symbol.toStringTag");

    public static final SymbolKey SPECIES = new SymbolKey("Symbol.species");

    public static final SymbolKey HAS_INSTANCE = new SymbolKey("Symbol.hasInstance");

    public static final SymbolKey IS_CONCAT_SPREADABLE = new SymbolKey("Symbol.isConcatSpreadable");

    public static final SymbolKey IS_REGEXP = new SymbolKey("Symbol.isRegExp");

    public static final SymbolKey TO_PRIMITIVE = new SymbolKey("Symbol.toPrimitive");

    public static final SymbolKey MATCH = new SymbolKey("Symbol.match");

    public static final SymbolKey REPLACE = new SymbolKey("Symbol.replace");

    public static final SymbolKey SEARCH = new SymbolKey("Symbol.search");

    public static final SymbolKey SPLIT = new SymbolKey("Symbol.split");

    public static final SymbolKey UNSCOPABLES = new SymbolKey("Symbol.unscopables");

    private final String name;

    public SymbolKey(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int hashCode() {
        return System.identityHashCode(this);
    }

    public boolean equals(Object o) {
        if (o instanceof SymbolKey) {
            return o == this;
        } else {
            return o instanceof NativeSymbol ? ((NativeSymbol) o).getKey() == this : false;
        }
    }

    public String toString() {
        return this.name == null ? "Symbol()" : "Symbol(" + this.name + ")";
    }
}