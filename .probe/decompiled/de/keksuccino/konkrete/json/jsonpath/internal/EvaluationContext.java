package de.keksuccino.konkrete.json.jsonpath.internal;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import java.util.Collection;
import java.util.List;

public interface EvaluationContext {

    Configuration configuration();

    Object rootDocument();

    <T> T getValue();

    <T> T getValue(boolean var1);

    <T> T getPath();

    List<String> getPathList();

    Collection<PathRef> updateOperations();
}