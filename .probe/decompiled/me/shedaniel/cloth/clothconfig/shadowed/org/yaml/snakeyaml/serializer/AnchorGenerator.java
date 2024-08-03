package me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.serializer;

import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.Node;

public interface AnchorGenerator {

    String nextAnchor(Node var1);
}