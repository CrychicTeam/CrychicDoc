package snownee.lychee.mixin;

import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.lychee.core.def.LocationPredicateHelper;

@Mixin({ LocationPredicate.class })
public class LocationPredicateMixin implements LocationPredicateHelper {

    @Unique
    private TagKey<Biome> lychee$biomeTag;

    @Override
    public void lychee$setBiomeTag(TagKey<Biome> biomeTag) {
        this.lychee$biomeTag = biomeTag;
    }

    @Override
    public TagKey<Biome> lychee$getBiomeTag() {
        return this.lychee$biomeTag;
    }

    @Inject(method = { "matches" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isLoaded(Lnet/minecraft/core/BlockPos;)Z", shift = Shift.BY, by = 2) }, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void lychee_matches(ServerLevel level, double d, double e, double f, CallbackInfoReturnable<Boolean> cir, BlockPos blockpos, boolean loaded) {
        if (this.lychee$biomeTag != null && !level.m_204166_(blockpos).is(this.lychee$biomeTag)) {
            cir.setReturnValue(false);
        }
    }
}