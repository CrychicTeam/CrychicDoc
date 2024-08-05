package snownee.kiwi.shadowed.org.yaml.snakeyaml.serializer;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.nodes.Node;

public interface AnchorGenerator {

    String nextAnchor(Node var1);
}