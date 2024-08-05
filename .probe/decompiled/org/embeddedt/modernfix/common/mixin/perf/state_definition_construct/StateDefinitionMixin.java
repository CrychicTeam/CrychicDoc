package org.embeddedt.modernfix.common.mixin.perf.state_definition_construct;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.embeddedt.modernfix.annotation.RequiresMod;
import org.embeddedt.modernfix.blockstate.FakeStateMap;
import org.embeddedt.modernfix.blockstate.FerriteCorePostProcess;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ StateDefinition.class })
@RequiresMod("ferritecore")
public class StateDefinitionMixin<O, S extends StateHolder<O, S>> {

    @Shadow
    @Final
    private ImmutableSortedMap<String, Property<?>> propertiesByName;

    @ModifyVariable(method = { "<init>" }, at = @At(value = "STORE", ordinal = 0), ordinal = 1, index = 8)
    private Map<Map<Property<?>, Comparable<?>>, S> useArrayMap(Map<Map<Property<?>, Comparable<?>>, S> in) {
        int numStates = 1;
        UnmodifiableIterator var3 = this.propertiesByName.values().iterator();
        while (var3.hasNext()) {
            Property<?> prop = (Property<?>) var3.next();
            numStates *= prop.getPossibleValues().size();
        }
        return new FakeStateMap(numStates);
    }

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void postProcess(CallbackInfo ci) {
        if (ModernFixPlatformHooks.INSTANCE.isDevEnv()) {
            FerriteCorePostProcess.postProcess((StateDefinition<O, S>) this);
        }
    }
}