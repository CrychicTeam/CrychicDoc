package snownee.kiwi.shadowed.org.yaml.snakeyaml.constructor;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.nodes.Node;

public interface Construct {

    Object construct(Node var1);

    void construct2ndStep(Node var1, Object var2);
}