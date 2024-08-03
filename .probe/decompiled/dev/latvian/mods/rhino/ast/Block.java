package dev.latvian.mods.rhino.ast;

public class Block extends AstNode {

    public Block() {
        this.type = 130;
    }

    public Block(int pos) {
        super(pos);
        this.type = 130;
    }

    public Block(int pos, int len) {
        super(pos, len);
        this.type = 130;
    }

    public void addStatement(AstNode statement) {
        this.addChild(statement);
    }
}