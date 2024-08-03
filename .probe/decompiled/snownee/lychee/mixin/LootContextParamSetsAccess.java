package snownee.lychee.mixin;

import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ LootContextParamSets.class })
public interface LootContextParamSetsAccess {

    @Invoker
    static LootContextParamSet callRegister(String pRegistryName, Consumer<LootContextParamSet.Builder> pBuilderConsumer) {
        throw new IllegalStateException();
    }
}