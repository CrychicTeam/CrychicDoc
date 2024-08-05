package snownee.kiwi.shadowed.org.yaml.snakeyaml.composer;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.error.Mark;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.error.MarkedYAMLException;

public class ComposerException extends MarkedYAMLException {

    private static final long serialVersionUID = 2146314636913113935L;

    protected ComposerException(String context, Mark contextMark, String problem, Mark problemMark) {
        super(context, contextMark, problem, problemMark);
    }
}