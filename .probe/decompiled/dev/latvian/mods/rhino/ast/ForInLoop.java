package dev.latvian.mods.rhino.ast;

public class ForInLoop extends Loop {

    protected AstNode iterator;

    protected AstNode iteratedObject;

    protected int inPosition = -1;

    protected int eachPosition = -1;

    protected boolean isForEach;

    protected boolean isForOf;

    public ForInLoop() {
        this.type = 120;
    }

    public ForInLoop(int pos) {
        super(pos);
        this.type = 120;
    }

    public ForInLoop(int pos, int len) {
        super(pos, len);
        this.type = 120;
    }

    public AstNode getIterator() {
        return this.iterator;
    }

    public void setIterator(AstNode iterator) {
        this.assertNotNull(iterator);
        this.iterator = iterator;
        iterator.setParent(this);
    }

    public AstNode getIteratedObject() {
        return this.iteratedObject;
    }

    public void setIteratedObject(AstNode object) {
        this.assertNotNull(object);
        this.iteratedObject = object;
        object.setParent(this);
    }

    public boolean isForEach() {
        return this.isForEach;
    }

    public void setIsForEach(boolean isForEach) {
        this.isForEach = isForEach;
    }

    public boolean isForOf() {
        return this.isForOf;
    }

    public void setIsForOf(boolean isForOf) {
        this.isForOf = isForOf;
    }

    public int getInPosition() {
        return this.inPosition;
    }

    public void setInPosition(int inPosition) {
        this.inPosition = inPosition;
    }

    public int getEachPosition() {
        return this.eachPosition;
    }

    public void setEachPosition(int eachPosition) {
        this.eachPosition = eachPosition;
    }
}