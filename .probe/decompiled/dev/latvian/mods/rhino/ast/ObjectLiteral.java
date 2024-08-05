package dev.latvian.mods.rhino.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectLiteral extends AstNode implements DestructuringForm {

    private static final List<ObjectProperty> NO_ELEMS = Collections.unmodifiableList(new ArrayList());

    boolean isDestructuring;

    private List<ObjectProperty> elements;

    public ObjectLiteral() {
        this.type = 67;
    }

    public ObjectLiteral(int pos) {
        super(pos);
        this.type = 67;
    }

    public ObjectLiteral(int pos, int len) {
        super(pos, len);
        this.type = 67;
    }

    public List<ObjectProperty> getElements() {
        return this.elements != null ? this.elements : NO_ELEMS;
    }

    public void setElements(List<ObjectProperty> elements) {
        if (elements == null) {
            this.elements = null;
        } else {
            if (this.elements != null) {
                this.elements.clear();
            }
            for (ObjectProperty o : elements) {
                this.addElement(o);
            }
        }
    }

    public void addElement(ObjectProperty element) {
        this.assertNotNull(element);
        if (this.elements == null) {
            this.elements = new ArrayList();
        }
        this.elements.add(element);
        element.setParent(this);
    }

    @Override
    public void setIsDestructuring(boolean destructuring) {
        this.isDestructuring = destructuring;
    }

    @Override
    public boolean isDestructuring() {
        return this.isDestructuring;
    }
}