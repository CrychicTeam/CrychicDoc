package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Node;
import dev.latvian.mods.rhino.Token;
import java.util.ArrayList;
import java.util.List;

public class VariableDeclaration extends AstNode {

    private final List<VariableInitializer> variables = new ArrayList();

    private boolean isStatement;

    public VariableDeclaration() {
        this.type = 123;
    }

    public VariableDeclaration(int pos) {
        super(pos);
        this.type = 123;
    }

    public VariableDeclaration(int pos, int len) {
        super(pos, len);
        this.type = 123;
    }

    public List<VariableInitializer> getVariables() {
        return this.variables;
    }

    public void setVariables(List<VariableInitializer> variables) {
        this.assertNotNull(variables);
        this.variables.clear();
        for (VariableInitializer vi : variables) {
            this.addVariable(vi);
        }
    }

    public void addVariable(VariableInitializer v) {
        this.assertNotNull(v);
        this.variables.add(v);
        v.setParent(this);
    }

    @Override
    public Node setType(int type) {
        if (type != 123 && type != 155 && type != 154) {
            throw new IllegalArgumentException("invalid decl type: " + type);
        } else {
            return super.setType(type);
        }
    }

    public boolean isVar() {
        return this.type == 123;
    }

    public boolean isConst() {
        return this.type == 155;
    }

    public boolean isLet() {
        return this.type == 154;
    }

    public boolean isStatement() {
        return this.isStatement;
    }

    public void setIsStatement(boolean isStatement) {
        this.isStatement = isStatement;
    }

    private String declTypeName() {
        return Token.typeToName(this.type).toLowerCase();
    }
}