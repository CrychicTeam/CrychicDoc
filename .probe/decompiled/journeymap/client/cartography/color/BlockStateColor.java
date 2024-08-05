package journeymap.client.cartography.color;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.gson.annotations.Since;
import journeymap.client.JourneymapClient;
import journeymap.client.model.BlockMD;

class BlockStateColor implements Comparable<BlockStateColor> {

    @Since(5.45)
    String block;

    @Since(5.45)
    String state;

    @Since(5.2)
    String name;

    @Since(5.2)
    String color;

    @Since(5.2)
    Float alpha;

    BlockStateColor(BlockMD blockMD) {
        this(blockMD, blockMD.getTextureColor());
    }

    BlockStateColor(BlockMD blockMD, Integer color) {
        if (JourneymapClient.getInstance().getCoreProperties().verboseColorPalette.get()) {
            this.block = blockMD.getBlockId();
            this.state = blockMD.getBlockStateId();
            this.name = blockMD.getName();
        }
        this.color = RGB.toHexString(color);
        if (blockMD.getAlpha() != 1.0F) {
            this.alpha = blockMD.getAlpha();
        }
    }

    BlockStateColor(String color, Float alpha) {
        this.color = color;
        this.alpha = alpha == null ? 1.0F : alpha;
    }

    public int compareTo(BlockStateColor that) {
        Ordering ordering = Ordering.natural().nullsLast();
        return ComparisonChain.start().compare(this.name, that.name, ordering).compare(this.block, that.block, ordering).compare(this.state, that.state, ordering).compare(this.color, that.color, ordering).compare(this.alpha, that.alpha, ordering).result();
    }
}