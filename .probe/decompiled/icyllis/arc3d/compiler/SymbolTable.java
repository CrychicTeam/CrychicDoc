package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.tree.FunctionDecl;
import icyllis.arc3d.compiler.tree.Symbol;
import icyllis.arc3d.compiler.tree.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SymbolTable {

    private final Map<String, Symbol> mTable = new HashMap();

    private final SymbolTable mParent;

    private final boolean mIsBuiltin;

    private final boolean mIsModuleLevel;

    SymbolTable() {
        this(null, true, false);
    }

    private SymbolTable(SymbolTable parent, boolean isBuiltin, boolean isModuleLevel) {
        this.mParent = parent;
        this.mIsBuiltin = isBuiltin;
        this.mIsModuleLevel = isModuleLevel;
    }

    @Nonnull
    SymbolTable enterScope() {
        return new SymbolTable(this, this.mIsBuiltin, false);
    }

    @Nonnull
    SymbolTable enterModule(boolean isBuiltin) {
        if ((!isBuiltin || this.mIsBuiltin) && (this.mIsModuleLevel || this.mParent == null)) {
            return new SymbolTable(this, isBuiltin, true);
        } else {
            throw new AssertionError();
        }
    }

    SymbolTable leaveScope() {
        if (!this.mIsModuleLevel && this.mParent != null) {
            return this.mParent;
        } else {
            throw new AssertionError();
        }
    }

    public SymbolTable getParent() {
        return this.mParent;
    }

    public boolean isBuiltin() {
        return this.mIsBuiltin;
    }

    @Nullable
    public Symbol find(String name) {
        Symbol symbol;
        if ((symbol = (Symbol) this.mTable.get(name)) != null) {
            return symbol;
        } else {
            return this.mParent != null ? this.mParent.find(name) : null;
        }
    }

    @Nullable
    public Symbol findBuiltinSymbol(String name) {
        if (this.mIsBuiltin) {
            return this.find(name);
        } else {
            return this.mParent != null ? this.mParent.findBuiltinSymbol(name) : null;
        }
    }

    public boolean isType(String name) {
        return this.find(name) instanceof Type;
    }

    public boolean isBuiltinType(String name) {
        return this.mIsBuiltin ? this.isType(name) : this.mParent != null && this.mParent.isBuiltinType(name);
    }

    <T extends Symbol> T insert(@Nonnull T symbol) {
        return (T) Objects.requireNonNull(this.insert(null, symbol));
    }

    @Nullable
    public <T extends Symbol> T insert(Context context, @Nonnull T symbol) {
        String key = symbol.getName();
        if (key.isEmpty()) {
            return symbol;
        } else if (symbol instanceof FunctionDecl && this.find(key) instanceof FunctionDecl next) {
            ((FunctionDecl) symbol).setNextOverload(next);
            this.mTable.put(key, symbol);
            return symbol;
        } else if ((!this.mIsModuleLevel || this.mParent == null || this.mParent.find(key) == null) && this.mTable.putIfAbsent(key, symbol) == null) {
            return symbol;
        } else {
            context.error(symbol.mPosition, "symbol '" + key + "' is already defined");
            return null;
        }
    }

    public Type getArrayType(Type type, int size) {
        if (size == 0) {
            return type;
        } else if (!this.mIsModuleLevel && this.mParent != null && type.isInBuiltinTypes()) {
            return this.mParent.getArrayType(type, size);
        } else {
            String name = type.getArrayName(size);
            Symbol symbol;
            if ((symbol = this.find(name)) != null) {
                return (Type) symbol;
            } else {
                Type result = Type.makeArrayType(name, type, size);
                return this.insert(result);
            }
        }
    }
}