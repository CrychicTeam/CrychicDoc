package dev.latvian.mods.rhino.ast;

public class ConditionalExpression extends AstNode {

    private AstNode testExpression;

    private AstNode trueExpression;

    private AstNode falseExpression;

    private int questionMarkPosition = -1;

    private int colonPosition = -1;

    public ConditionalExpression() {
        this.type = 103;
    }

    public ConditionalExpression(int pos) {
        super(pos);
        this.type = 103;
    }

    public ConditionalExpression(int pos, int len) {
        super(pos, len);
        this.type = 103;
    }

    public AstNode getTestExpression() {
        return this.testExpression;
    }

    public void setTestExpression(AstNode testExpression) {
        this.assertNotNull(testExpression);
        this.testExpression = testExpression;
        testExpression.setParent(this);
    }

    public AstNode getTrueExpression() {
        return this.trueExpression;
    }

    public void setTrueExpression(AstNode trueExpression) {
        this.assertNotNull(trueExpression);
        this.trueExpression = trueExpression;
        trueExpression.setParent(this);
    }

    public AstNode getFalseExpression() {
        return this.falseExpression;
    }

    public void setFalseExpression(AstNode falseExpression) {
        this.assertNotNull(falseExpression);
        this.falseExpression = falseExpression;
        falseExpression.setParent(this);
    }

    public int getQuestionMarkPosition() {
        return this.questionMarkPosition;
    }

    public void setQuestionMarkPosition(int questionMarkPosition) {
        this.questionMarkPosition = questionMarkPosition;
    }

    public int getColonPosition() {
        return this.colonPosition;
    }

    public void setColonPosition(int colonPosition) {
        this.colonPosition = colonPosition;
    }

    @Override
    public boolean hasSideEffects() {
        if (this.testExpression == null || this.trueExpression == null || this.falseExpression == null) {
            codeBug();
        }
        return this.trueExpression.hasSideEffects() && this.falseExpression.hasSideEffects();
    }
}