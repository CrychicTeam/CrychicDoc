package dev.latvian.mods.rhino.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TemplateLiteral extends AstNode {

    private List<AstNode> elements;

    public TemplateLiteral() {
        this.type = 167;
    }

    public TemplateLiteral(int pos) {
        super(pos);
        this.type = 167;
    }

    public TemplateLiteral(int pos, int len) {
        super(pos, len);
        this.type = 167;
    }

    public List<TemplateCharacters> getTemplateStrings() {
        if (this.elements == null) {
            return Collections.emptyList();
        } else {
            List<TemplateCharacters> strings = new ArrayList();
            for (AstNode e : this.elements) {
                if (e.getType() == 168) {
                    strings.add((TemplateCharacters) e);
                }
            }
            return Collections.unmodifiableList(strings);
        }
    }

    public List<AstNode> getSubstitutions() {
        if (this.elements == null) {
            return Collections.emptyList();
        } else {
            List<AstNode> subs = new ArrayList();
            for (AstNode e : this.elements) {
                if (e.getType() != 168) {
                    subs.add(e);
                }
            }
            return Collections.unmodifiableList(subs);
        }
    }

    public List<AstNode> getElements() {
        return this.elements == null ? Collections.emptyList() : this.elements;
    }

    public void setElements(List<AstNode> elements) {
        if (elements == null) {
            this.elements = null;
        } else {
            if (this.elements != null) {
                this.elements.clear();
            }
            for (AstNode e : elements) {
                this.addElement(e);
            }
        }
    }

    public void addElement(AstNode element) {
        this.assertNotNull(element);
        if (this.elements == null) {
            this.elements = new ArrayList();
        }
        this.elements.add(element);
        element.setParent(this);
    }

    public int getSize() {
        return this.elements == null ? 0 : this.elements.size();
    }

    public AstNode getElement(int index) {
        if (this.elements == null) {
            throw new IndexOutOfBoundsException("no elements");
        } else {
            return (AstNode) this.elements.get(index);
        }
    }
}