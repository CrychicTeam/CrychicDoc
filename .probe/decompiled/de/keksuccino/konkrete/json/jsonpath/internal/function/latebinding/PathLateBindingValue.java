package de.keksuccino.konkrete.json.jsonpath.internal.function.latebinding;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import java.util.Objects;

public class PathLateBindingValue implements ILateBindingValue {

    private final Path path;

    private final String rootDocument;

    private final Configuration configuration;

    private final Object result;

    public PathLateBindingValue(Path path, Object rootDocument, Configuration configuration) {
        this.path = path;
        this.rootDocument = rootDocument.toString();
        this.configuration = configuration;
        this.result = path.evaluate(rootDocument, rootDocument, configuration).getValue();
    }

    @Override
    public Object get() {
        return this.result;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            PathLateBindingValue that = (PathLateBindingValue) o;
            return Objects.equals(this.path, that.path) && this.rootDocument.equals(that.rootDocument) && Objects.equals(this.configuration, that.configuration);
        } else {
            return false;
        }
    }
}