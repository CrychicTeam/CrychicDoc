package snownee.lychee.mixin;

import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ LocationCheck.class })
public interface LocationCheckAccess {

    @Accessor
    LocationPredicate getPredicate();

    @Accessor
    BlockPos getOffset();
}