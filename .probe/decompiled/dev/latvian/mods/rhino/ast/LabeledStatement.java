package dev.latvian.mods.rhino.ast;

import java.util.ArrayList;
import java.util.List;

public class LabeledStatement extends AstNode {

    private final List<Label> labels = new ArrayList();

    private AstNode statement;

    public LabeledStatement() {
        this.type = 134;
    }

    public LabeledStatement(int pos) {
        super(pos);
        this.type = 134;
    }

    public LabeledStatement(int pos, int len) {
        super(pos, len);
        this.type = 134;
    }

    public List<Label> getLabels() {
        return this.labels;
    }

    public void setLabels(List<Label> labels) {
        this.assertNotNull(labels);
        if (this.labels != null) {
            this.labels.clear();
        }
        for (Label l : labels) {
            this.addLabel(l);
        }
    }

    public void addLabel(Label label) {
        this.assertNotNull(label);
        this.labels.add(label);
        label.setParent(this);
    }

    public AstNode getStatement() {
        return this.statement;
    }

    public void setStatement(AstNode statement) {
        this.assertNotNull(statement);
        this.statement = statement;
        statement.setParent(this);
    }

    public Label getLabelByName(String name) {
        for (Label label : this.labels) {
            if (name.equals(label.getName())) {
                return label;
            }
        }
        return null;
    }

    public Label getFirstLabel() {
        return (Label) this.labels.get(0);
    }

    @Override
    public boolean hasSideEffects() {
        return true;
    }
}