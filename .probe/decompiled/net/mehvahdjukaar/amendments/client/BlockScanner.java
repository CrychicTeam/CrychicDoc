package net.mehvahdjukaar.amendments.client;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Set;
import net.mehvahdjukaar.amendments.common.block.WallLanternBlock;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import org.jetbrains.annotations.NotNull;

public class BlockScanner {

    private static final Set<Block> lanterns;

    private static final Set<Block> torches;

    private static final Set<Block> candleHolders;

    @NotNull
    public static Set<Block> getLanterns() {
        return lanterns;
    }

    @NotNull
    public static Set<Block> getTorches() {
        return torches;
    }

    @NotNull
    public static Set<Block> getCandleHolders() {
        return candleHolders;
    }

    static {
        Builder<Block> lanternBuilder = ImmutableSet.builder();
        Builder<Block> torchesBuilder = ImmutableSet.builder();
        Builder<Block> candleBuilder = ImmutableSet.builder();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (WallLanternBlock.isValidBlock(block)) {
                lanternBuilder.add(block);
            } else if ((!(block instanceof TorchBlock) || block instanceof WallTorchBlock) && (!CompatHandler.SUPPLEMENTARIES || !SuppCompat.isSconce(block))) {
                if (CompatHandler.SUPPLEMENTARIES && SuppCompat.isCandleHolder(block)) {
                    candleBuilder.add(block);
                }
            } else {
                torchesBuilder.add(block);
            }
        }
        lanterns = lanternBuilder.build();
        torches = torchesBuilder.build();
        candleHolders = candleBuilder.build();
    }
}