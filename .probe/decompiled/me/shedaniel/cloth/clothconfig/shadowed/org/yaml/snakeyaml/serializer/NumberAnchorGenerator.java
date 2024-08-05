package me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.serializer;

import java.text.NumberFormat;
import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.nodes.Node;

public class NumberAnchorGenerator implements AnchorGenerator {

    private int lastAnchorId = 0;

    public NumberAnchorGenerator(int lastAnchorId) {
        this.lastAnchorId = lastAnchorId;
    }

    @Override
    public String nextAnchor(Node node) {
        this.lastAnchorId++;
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(3);
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(false);
        String anchorId = format.format((long) this.lastAnchorId);
        return "id" + anchorId;
    }
}