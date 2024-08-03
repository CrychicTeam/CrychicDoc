package se.mickelus.tetra.blocks.forged.chthonic;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.forged.extractor.SeepingBedrockBlock;

@ParametersAreNonnullByDefault
public class FracturedBedrockBlock extends TetraBlock implements EntityBlock {

    public static final String identifier = "fractured_bedrock";

    @ObjectHolder(registryName = "block", value = "tetra:fractured_bedrock")
    public static FracturedBedrockBlock instance;

    public FracturedBedrockBlock() {
        super(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).noLootTable());
    }

    public static boolean canPierce(Level world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return Blocks.BEDROCK.equals(blockState.m_60734_()) || blockState.m_60713_(SeepingBedrockBlock.instance) && !SeepingBedrockBlock.isActive(blockState);
    }

    public static void pierce(Level world, BlockPos pos, int amount) {
        FracturedBedrockTile tile = (FracturedBedrockTile) TileEntityOptional.from(world, pos, FracturedBedrockTile.class).orElse(null);
        if (tile == null && canPierce(world, pos)) {
            BlockState blockState = world.getBlockState(pos);
            world.setBlock(pos, instance.m_49966_(), 2);
            tile = (FracturedBedrockTile) TileEntityOptional.from(world, pos, FracturedBedrockTile.class).orElse(null);
            if (!world.isClientSide) {
                tile.updateLuck();
            }
        }
        if (tile != null) {
            tile.activate(amount);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FracturedBedrockTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return getTicker(entityType, FracturedBedrockTile.type, (lvl, pos, blockState, tile) -> tile.tick(lvl, pos, blockState));
    }
}