package com.simibubi.create.foundation.ponder;

import com.simibubi.create.foundation.ponder.element.PonderElement;
import java.util.UUID;

public class ElementLink<T extends PonderElement> {

    private Class<T> elementClass;

    private UUID id;

    public ElementLink(Class<T> elementClass) {
        this(elementClass, UUID.randomUUID());
    }

    public ElementLink(Class<T> elementClass, UUID id) {
        this.elementClass = elementClass;
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }

    public T cast(PonderElement e) {
        return (T) this.elementClass.cast(e);
    }
}