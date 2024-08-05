package snownee.kiwi.shadowed.org.yaml.snakeyaml.inspector;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.nodes.Tag;

public final class TrustedTagInspector implements TagInspector {

    @Override
    public boolean isGlobalTagAllowed(Tag tag) {
        return true;
    }
}