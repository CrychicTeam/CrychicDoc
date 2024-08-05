package net.mehvahdjukaar.supplementaries.common.misc.mob_container;

import net.mehvahdjukaar.supplementaries.api.CapturedMobInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class DataCapturedMobInstance<T extends Entity> extends CapturedMobInstance<T> {

    private final DataDefinedCatchableMob properties;

    @Nullable
    private final BuiltinAnimation<T> builtinAnimationInstance;

    protected DataCapturedMobInstance(T entity, float width, float height, DataDefinedCatchableMob type) {
        super(entity, width, height);
        this.properties = type;
        this.builtinAnimationInstance = BuiltinAnimation.get(entity, this.properties.builtinAnimation);
    }

    @Override
    public void containerTick(Level world, BlockPos pos, float entityScale, CompoundTag entityData) {
        this.entity.tickCount++;
        if (this.properties.tickMode.isValid(world) && this.entity instanceof LivingEntity livingEntity) {
            livingEntity.aiStep();
        }
        if (world instanceof ServerLevel serverLevel) {
            this.properties.loot.ifPresent(lootParam -> lootParam.tryDropping(serverLevel, pos, this.entity));
        }
        if (this.builtinAnimationInstance != null) {
            this.builtinAnimationInstance.tick(this.entity, world, pos);
        }
    }

    @Override
    public void onContainerWaterlogged(boolean waterlogged) {
        ResourceLocation f = (ResourceLocation) this.properties.forceFluidID.orElse(null);
        if (!waterlogged && f != null && f.getPath().equals("water")) {
            super.onContainerWaterlogged(true);
        } else {
            super.onContainerWaterlogged(waterlogged);
        }
    }
}