package team.lodestar.lodestone.systems.datagen.statesmith;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneBlockStateProvider;

public abstract class AbstractBlockStateSmith<T extends Block> {

    public final Class<T> blockClass;

    public AbstractBlockStateSmith(Class<T> blockClass) {
        this.blockClass = blockClass;
    }

    public interface StateFunction<T extends Block> {

        void act(T var1, ModelFile var2);
    }

    public static class StateSmithData {

        public final LodestoneBlockStateProvider provider;

        public final Consumer<Supplier<? extends Block>> consumer;

        public StateSmithData(LodestoneBlockStateProvider provider, Consumer<Supplier<? extends Block>> consumer) {
            this.provider = provider;
            this.consumer = consumer;
        }
    }
}