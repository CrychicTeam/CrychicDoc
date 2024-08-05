package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Token;

public class InfixExpression extends AstNode {

    protected AstNode left;

    protected AstNode right;

    protected int operatorPosition = -1;

    public InfixExpression() {
    }

    public InfixExpression(int pos) {
        super(pos);
    }

    public InfixExpression(int pos, int len) {
        super(pos, len);
    }

    public InfixExpression(int pos, int len, AstNode left, AstNode right) {
        super(pos, len);
        this.setLeft(left);
        this.setRight(right);
    }

    public InfixExpression(AstNode left, AstNode right) {
        this.setLeftAndRight(left, right);
    }

    public InfixExpression(int operator, AstNode left, AstNode right, int operatorPos) {
        this.setType(operator);
        this.setOperatorPosition(operatorPos - left.getPosition());
        this.setLeftAndRight(left, right);
    }

    public void setLeftAndRight(AstNode left, AstNode right) {
        this.assertNotNull(left);
        this.assertNotNull(right);
        int beg = left.getPosition();
        int end = right.getPosition() + right.getLength();
        this.setBounds(beg, end);
        this.setLeft(left);
        this.setRight(right);
    }

    public int getOperator() {
        return this.getType();
    }

    public void setOperator(int operator) {
        if (!Token.isValidToken(operator)) {
            throw new IllegalArgumentException("Invalid token: " + operator);
        } else {
            this.setType(operator);
        }
    }

    public AstNode getLeft() {
        return this.left;
    }

    public void setLeft(AstNode left) {
        this.assertNotNull(left);
        this.left = left;
        this.setLineno(left.getLineno());
        left.setParent(this);
    }

    public AstNode getRight() {
        return this.right;
    }

    public void setRight(AstNode right) {
        this.assertNotNull(right);
        this.right = right;
        right.setParent(this);
    }

    public int getOperatorPosition() {
        return this.operatorPosition;
    }

    public void setOperatorPosition(int operatorPosition) {
        this.operatorPosition = operatorPosition;
    }

    @Override
    public boolean hasSideEffects() {
        return switch(this.getType()) {
            case 75, 105, 106 ->
                this.left != null && this.left.hasSideEffects() || this.right != null && this.right.hasSideEffects();
            case 90 ->
                this.right != null && this.right.hasSideEffects();
            default ->
                super.hasSideEffects();
        };
    }
}