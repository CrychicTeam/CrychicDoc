package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Token;

public class UnaryExpression extends AstNode {

    private AstNode operand;

    private boolean isPostfix;

    public UnaryExpression() {
    }

    public UnaryExpression(int pos) {
        super(pos);
    }

    public UnaryExpression(int pos, int len) {
        super(pos, len);
    }

    public UnaryExpression(int operator, int operatorPosition, AstNode operand) {
        this(operator, operatorPosition, operand, false);
    }

    public UnaryExpression(int operator, int operatorPosition, AstNode operand, boolean postFix) {
        this.assertNotNull(operand);
        int beg = postFix ? operand.getPosition() : operatorPosition;
        int end = postFix ? operatorPosition + 2 : operand.getPosition() + operand.getLength();
        this.setBounds(beg, end);
        this.setOperator(operator);
        this.setOperand(operand);
        this.isPostfix = postFix;
    }

    public int getOperator() {
        return this.type;
    }

    public void setOperator(int operator) {
        if (!Token.isValidToken(operator)) {
            throw new IllegalArgumentException("Invalid token: " + operator);
        } else {
            this.setType(operator);
        }
    }

    public AstNode getOperand() {
        return this.operand;
    }

    public void setOperand(AstNode operand) {
        this.assertNotNull(operand);
        this.operand = operand;
        operand.setParent(this);
    }

    public boolean isPostfix() {
        return this.isPostfix;
    }

    public boolean isPrefix() {
        return !this.isPostfix;
    }

    public void setIsPostfix(boolean isPostfix) {
        this.isPostfix = isPostfix;
    }
}