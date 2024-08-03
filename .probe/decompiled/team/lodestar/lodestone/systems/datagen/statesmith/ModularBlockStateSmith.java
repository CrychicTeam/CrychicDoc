package team.lodestar.lodestone.systems.datagen.statesmith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.datagen.ItemModelSmithTypes;
import team.lodestar.lodestone.systems.datagen.itemsmith.ItemModelSmith;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneBlockStateProvider;

public class ModularBlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final ModularBlockStateSmith.ModularSmithStateSupplier<T> stateSupplier;

    public ModularBlockStateSmith(Class<T> blockClass, ModularBlockStateSmith.ModularSmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
    }

    @SafeVarargs
    public final void act(AbstractBlockStateSmith.StateSmithData data, AbstractBlockStateSmith.StateFunction<T> actor, ModularBlockStateSmith.ModelFileSupplier modelFileSupplier, Supplier<? extends Block>... blocks) {
        this.act(data, ItemModelSmithTypes.BLOCK_MODEL_ITEM, actor, modelFileSupplier, blocks);
    }

    @SafeVarargs
    public final void act(AbstractBlockStateSmith.StateSmithData data, ItemModelSmith itemModelSmith, AbstractBlockStateSmith.StateFunction<T> actor, ModularBlockStateSmith.ModelFileSupplier modelFileSupplier, Supplier<? extends Block>... blocks) {
        for (Supplier<? extends Block> block : blocks) {
            this.act(data, itemModelSmith, actor, modelFileSupplier, block);
        }
        List.of(blocks).forEach(data.consumer);
    }

    public void act(AbstractBlockStateSmith.StateSmithData data, AbstractBlockStateSmith.StateFunction<T> actor, ModularBlockStateSmith.ModelFileSupplier modelFileSupplier, Collection<Supplier<? extends Block>> blocks) {
        this.act(data, ItemModelSmithTypes.BLOCK_MODEL_ITEM, actor, modelFileSupplier, blocks);
    }

    public void act(AbstractBlockStateSmith.StateSmithData data, ItemModelSmith itemModelSmith, AbstractBlockStateSmith.StateFunction<T> actor, ModularBlockStateSmith.ModelFileSupplier modelFileSupplier, Collection<Supplier<? extends Block>> blocks) {
        blocks.forEach(r -> this.act(data, itemModelSmith, actor, modelFileSupplier, r));
        new ArrayList(blocks).forEach(data.consumer);
    }

    private void act(AbstractBlockStateSmith.StateSmithData data, ItemModelSmith itemModelSmith, AbstractBlockStateSmith.StateFunction<T> actor, ModularBlockStateSmith.ModelFileSupplier modelFileSupplier, Supplier<? extends Block> registryObject) {
        Block block = (Block) registryObject.get();
        if (this.blockClass.isInstance(block)) {
            this.stateSupplier.act((T) this.blockClass.cast(block), data.provider, actor, modelFileSupplier);
            itemModelSmith.act(block::m_5456_, data.provider.itemModelProvider);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + ForgeRegistries.BLOCKS.getKey(block));
        }
    }

    public interface ModelFileSupplier {

        ModelFile generateModel(Block var1);
    }

    public interface ModularSmithStateSupplier<T extends Block> {

        void act(T var1, LodestoneBlockStateProvider var2, AbstractBlockStateSmith.StateFunction<T> var3, ModularBlockStateSmith.ModelFileSupplier var4);
    }
}