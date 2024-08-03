package se.mickelus.tetra.blocks.forged.chthonic;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.blocks.TetraBlock;

@ParametersAreNonnullByDefault
public class DepletedBedrockBlock extends TetraBlock {

    public static final String identifier = "depleted_bedrock";

    @ObjectHolder(registryName = "block", value = "tetra:depleted_bedrock")
    public static DepletedBedrockBlock instance;

    public DepletedBedrockBlock() {
        super(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).noLootTable());
    }
}