package me.jellysquid.mods.lithium.mixin.collections.mob_spawning;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import me.jellysquid.mods.lithium.common.util.collections.HashedReferenceList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ WeightedRandomList.class })
public class PoolMixin<E extends WeightedEntry> {

    @Mutable
    @Shadow
    @Final
    private ImmutableList<E> items;

    private List<E> entryHashList;

    @Inject(method = { "<init>(Ljava/util/List;)V" }, at = { @At("RETURN") })
    private void init(List<? extends E> entries, CallbackInfo ci) {
        this.entryHashList = (List<E>) (this.items.size() < 4 ? this.items : Collections.unmodifiableList(new HashedReferenceList(this.items)));
    }

    @Overwrite
    public List<E> unwrap() {
        return this.entryHashList;
    }
}