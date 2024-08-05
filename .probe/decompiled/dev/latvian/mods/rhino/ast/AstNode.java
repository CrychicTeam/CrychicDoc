package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Kit;
import dev.latvian.mods.rhino.Node;
import java.util.Comparator;

public abstract class AstNode extends Node implements Comparable<AstNode> {

    protected int position = -1;

    protected int length = 1;

    protected AstNode parent;

    protected AstNode inlineComment;

    public static RuntimeException codeBug() throws RuntimeException {
        throw Kit.codeBug();
    }

    public AstNode() {
        super(-1);
    }

    public AstNode(int pos) {
        this();
        this.position = pos;
    }

    public AstNode(int pos, int len) {
        this();
        this.position = pos;
        this.length = len;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getAbsolutePosition() {
        int pos = this.position;
        for (AstNode parent = this.parent; parent != null; parent = parent.getParent()) {
            pos += parent.getPosition();
        }
        return pos;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setBounds(int position, int end) {
        this.setPosition(position);
        this.setLength(end - position);
    }

    public void setRelative(int parentPosition) {
        this.position -= parentPosition;
    }

    public AstNode getParent() {
        return this.parent;
    }

    public void setParent(AstNode parent) {
        if (parent != this.parent) {
            if (this.parent != null) {
                this.setRelative(-this.parent.getAbsolutePosition());
            }
            this.parent = parent;
            if (parent != null) {
                this.setRelative(parent.getAbsolutePosition());
            }
        }
    }

    public void addChild(AstNode kid) {
        this.assertNotNull(kid);
        int end = kid.getPosition() + kid.getLength();
        this.setLength(end - this.getPosition());
        this.addChildToBack(kid);
        kid.setParent(this);
    }

    public AstRoot getAstRoot() {
        AstNode parent = this;
        while (parent != null && !(parent instanceof AstRoot)) {
            parent = parent.getParent();
        }
        return (AstRoot) parent;
    }

    public String shortName() {
        String classname = this.getClass().getName();
        int last = classname.lastIndexOf(".");
        return classname.substring(last + 1);
    }

    @Override
    public boolean hasSideEffects() {
        return switch(this.getType()) {
            case -1, 2, 3, 4, 5, 6, 7, 8, 30, 31, 35, 37, 38, 50, 51, 56, 57, 65, 69, 70, 71, 73, 82, 83, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 107, 108, 110, 111, 112, 113, 114, 115, 118, 119, 120, 121, 122, 123, 124, 125, 126, 130, 131, 132, 133, 135, 136, 140, 141, 142, 143, 154, 155, 159, 160, 166 ->
                true;
            default ->
                false;
        };
    }

    protected void assertNotNull(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException("arg cannot be null");
        }
    }

    public FunctionNode getEnclosingFunction() {
        AstNode parent = this.getParent();
        while (parent != null && !(parent instanceof FunctionNode)) {
            parent = parent.getParent();
        }
        return (FunctionNode) parent;
    }

    public Scope getEnclosingScope() {
        AstNode parent = this.getParent();
        while (parent != null && !(parent instanceof Scope)) {
            parent = parent.getParent();
        }
        return (Scope) parent;
    }

    public int compareTo(AstNode other) {
        if (this.equals(other)) {
            return 0;
        } else {
            int abs1 = this.getAbsolutePosition();
            int abs2 = other.getAbsolutePosition();
            if (abs1 < abs2) {
                return -1;
            } else if (abs2 < abs1) {
                return 1;
            } else {
                int len1 = this.getLength();
                int len2 = other.getLength();
                if (len1 < len2) {
                    return -1;
                } else {
                    return len2 < len1 ? 1 : this.hashCode() - other.hashCode();
                }
            }
        }
    }

    public int depth() {
        return this.parent == null ? 0 : 1 + this.parent.depth();
    }

    @Override
    public int getLineno() {
        if (this.lineno != -1) {
            return this.lineno;
        } else {
            return this.parent != null ? this.parent.getLineno() : -1;
        }
    }

    public AstNode getInlineComment() {
        return this.inlineComment;
    }

    public void setInlineComment(AstNode inlineComment) {
        this.inlineComment = inlineComment;
    }

    public static class PositionComparator implements Comparator<AstNode> {

        public int compare(AstNode n1, AstNode n2) {
            return n1.position - n2.position;
        }
    }
}