package moe.wolfgirl.probejs.lang.typescript;

import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;

public class Declaration {

    private static final String SYMBOL_TEMPLATE = "%s$%d";

    public static final String INPUT_TEMPLATE = "%s$Type";

    public final Map<ClassPath, Reference> references = new HashMap();

    private final Map<ClassPath, Pair<String, String>> symbols = new HashMap();

    private final Set<String> excludedName = new HashSet();

    public void addClass(ClassPath path) {
        Pair<String, String> names = this.getSymbolName(path);
        this.references.put(path, new Reference(path, (String) names.getFirst(), (String) names.getSecond()));
    }

    public void exclude(String name) {
        this.excludedName.add(name);
    }

    private void putSymbolName(ClassPath path, String name) {
        this.symbols.put(path, new Pair(name, "%s$Type".formatted(name)));
    }

    private boolean containsSymbol(String name) {
        return this.excludedName.contains(name) || this.symbols.containsValue(new Pair(name, "%s$Type".formatted(name)));
    }

    private Pair<String, String> getSymbolName(ClassPath path) {
        if (!this.symbols.containsKey(path)) {
            String name = path.getName();
            if (!this.containsSymbol(name)) {
                this.putSymbolName(path, name);
            } else {
                int counter = 0;
                while (this.containsSymbol("%s$%d".formatted(name, counter))) {
                    counter++;
                }
                this.putSymbolName(path, "%s$%d".formatted(name, counter));
            }
        }
        return (Pair<String, String>) this.symbols.get(path);
    }

    public String getSymbol(ClassPath path) {
        return this.getSymbol(path, false);
    }

    public String getSymbol(ClassPath path, boolean input) {
        if (!this.references.containsKey(path)) {
            throw new RuntimeException("Trying to get a symbol of a classpath that is not resolved yet!");
        } else {
            Reference reference = (Reference) this.references.get(path);
            return input ? reference.input() : reference.original();
        }
    }
}