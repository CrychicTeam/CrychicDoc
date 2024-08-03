package team.lodestar.lodestone.systems.datagen.statesmith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.datagen.ItemModelSmithTypes;
import team.lodestar.lodestone.systems.datagen.itemsmith.ItemModelSmith;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneBlockStateProvider;

public class BlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final BlockStateSmith.SmithStateSupplier<T> stateSupplier;

    public final ItemModelSmith itemModelSmith;

    public BlockStateSmith(Class<T> blockClass, BlockStateSmith.SmithStateSupplier<T> stateSupplier) {
        this(blockClass, ItemModelSmithTypes.BLOCK_MODEL_ITEM, stateSupplier);
    }

    public BlockStateSmith(Class<T> blockClass, ItemModelSmith itemModelSmith, BlockStateSmith.SmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
        this.itemModelSmith = itemModelSmith;
    }

    @SafeVarargs
    public final void act(AbstractBlockStateSmith.StateSmithData data, Supplier<? extends Block>... blocks) {
        for (Supplier<? extends Block> block : blocks) {
            this.act(data, block);
        }
        List.of(blocks).forEach(data.consumer);
    }

    public void act(AbstractBlockStateSmith.StateSmithData data, Collection<Supplier<? extends Block>> blocks) {
        blocks.forEach(r -> this.act(data, r));
        new ArrayList(blocks).forEach(data.consumer);
    }

    private void act(AbstractBlockStateSmith.StateSmithData data, Supplier<? extends Block> registryObject) {
        Block block = (Block) registryObject.get();
        if (this.blockClass.isInstance(block)) {
            this.stateSupplier.act((T) this.blockClass.cast(block), data.provider);
            this.itemModelSmith.act(block::m_5456_, data.provider.itemModelProvider);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + ForgeRegistries.BLOCKS.getKey(block));
        }
    }

    public interface SmithStateSupplier<T extends Block> {

        void act(T var1, LodestoneBlockStateProvider var2);
    }
}