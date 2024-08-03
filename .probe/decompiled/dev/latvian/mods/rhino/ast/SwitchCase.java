package dev.latvian.mods.rhino.ast;

import java.util.ArrayList;
import java.util.List;

public class SwitchCase extends AstNode {

    private AstNode expression;

    private List<AstNode> statements;

    public SwitchCase() {
        this.type = 116;
    }

    public SwitchCase(int pos) {
        super(pos);
        this.type = 116;
    }

    public SwitchCase(int pos, int len) {
        super(pos, len);
        this.type = 116;
    }

    public AstNode getExpression() {
        return this.expression;
    }

    public void setExpression(AstNode expression) {
        this.expression = expression;
        if (expression != null) {
            expression.setParent(this);
        }
    }

    public boolean isDefault() {
        return this.expression == null;
    }

    public List<AstNode> getStatements() {
        return this.statements;
    }

    public void setStatements(List<AstNode> statements) {
        if (this.statements != null) {
            this.statements.clear();
        }
        for (AstNode s : statements) {
            this.addStatement(s);
        }
    }

    public void addStatement(AstNode statement) {
        this.assertNotNull(statement);
        if (this.statements == null) {
            this.statements = new ArrayList();
        }
        int end = statement.getPosition() + statement.getLength();
        this.setLength(end - this.getPosition());
        this.statements.add(statement);
        statement.setParent(this);
    }
}