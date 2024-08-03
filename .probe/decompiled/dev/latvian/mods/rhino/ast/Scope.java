package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Scope extends Jump {

    protected Map<String, AstSymbol> symbolTable;

    protected Scope parentScope;

    protected ScriptNode top;

    private List<Scope> childScopes;

    public static Scope splitScope(Scope scope) {
        Scope result = new Scope(scope.getType());
        result.symbolTable = scope.symbolTable;
        scope.symbolTable = null;
        result.parent = scope.parent;
        result.setParentScope(scope.getParentScope());
        result.setParentScope(result);
        scope.parent = result;
        result.top = scope.top;
        return result;
    }

    public static void joinScopes(Scope source, Scope dest) {
        Map<String, AstSymbol> src = source.ensureSymbolTable();
        Map<String, AstSymbol> dst = dest.ensureSymbolTable();
        if (!Collections.disjoint(src.keySet(), dst.keySet())) {
            codeBug();
        }
        for (Entry<String, AstSymbol> entry : src.entrySet()) {
            AstSymbol sym = (AstSymbol) entry.getValue();
            sym.setContainingTable(dest);
            dst.put((String) entry.getKey(), sym);
        }
    }

    public Scope() {
        this.type = 130;
    }

    public Scope(int pos) {
        this.type = 130;
        this.position = pos;
    }

    public Scope(int pos, int len) {
        this(pos);
        this.length = len;
    }

    public Scope getParentScope() {
        return this.parentScope;
    }

    public void setParentScope(Scope parentScope) {
        this.parentScope = parentScope;
        this.top = parentScope == null ? (ScriptNode) this : parentScope.top;
    }

    public void clearParentScope() {
        this.parentScope = null;
    }

    public List<Scope> getChildScopes() {
        return this.childScopes;
    }

    public void addChildScope(Scope child) {
        if (this.childScopes == null) {
            this.childScopes = new ArrayList();
        }
        this.childScopes.add(child);
        child.setParentScope(this);
    }

    public void replaceWith(Scope newScope) {
        if (this.childScopes != null) {
            for (Scope kid : this.childScopes) {
                newScope.addChildScope(kid);
            }
            this.childScopes.clear();
            this.childScopes = null;
        }
        if (this.symbolTable != null && !this.symbolTable.isEmpty()) {
            joinScopes(this, newScope);
        }
    }

    public ScriptNode getTop() {
        return this.top;
    }

    public void setTop(ScriptNode top) {
        this.top = top;
    }

    public Scope getDefiningScope(String name) {
        for (Scope s = this; s != null; s = s.parentScope) {
            Map<String, AstSymbol> symbolTable = s.getSymbolTable();
            if (symbolTable != null && symbolTable.containsKey(name)) {
                return s;
            }
        }
        return null;
    }

    public AstSymbol getSymbol(String name) {
        return this.symbolTable == null ? null : (AstSymbol) this.symbolTable.get(name);
    }

    public void putSymbol(AstSymbol symbol) {
        if (symbol.getName() == null) {
            throw new IllegalArgumentException("null symbol name");
        } else {
            this.ensureSymbolTable();
            this.symbolTable.put(symbol.getName(), symbol);
            symbol.setContainingTable(this);
            this.top.addSymbol(symbol);
        }
    }

    public Map<String, AstSymbol> getSymbolTable() {
        return this.symbolTable;
    }

    public void setSymbolTable(Map<String, AstSymbol> table) {
        this.symbolTable = table;
    }

    private Map<String, AstSymbol> ensureSymbolTable() {
        if (this.symbolTable == null) {
            this.symbolTable = new LinkedHashMap(5);
        }
        return this.symbolTable;
    }

    public List<AstNode> getStatements() {
        List<AstNode> stmts = new ArrayList();
        for (Node n = this.getFirstChild(); n != null; n = n.getNext()) {
            stmts.add((AstNode) n);
        }
        return stmts;
    }
}