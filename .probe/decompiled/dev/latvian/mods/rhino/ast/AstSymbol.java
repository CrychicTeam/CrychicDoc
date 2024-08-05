package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Token;

public class AstSymbol {

    private int declType;

    private int index = -1;

    private String name;

    private Scope containingTable;

    public AstSymbol(int declType, String name) {
        this.setName(name);
        this.setDeclType(declType);
    }

    public int getDeclType() {
        return this.declType;
    }

    public void setDeclType(int declType) {
        if (declType != 110 && declType != 88 && declType != 123 && declType != 154 && declType != 155) {
            throw new IllegalArgumentException("Invalid declType: " + declType);
        } else {
            this.declType = declType;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Scope getContainingTable() {
        return this.containingTable;
    }

    public void setContainingTable(Scope containingTable) {
        this.containingTable = containingTable;
    }

    public String getDeclTypeName() {
        return Token.typeToName(this.declType);
    }

    public String toString() {
        return "Symbol (" + this.getDeclTypeName() + ") name=" + this.name;
    }
}