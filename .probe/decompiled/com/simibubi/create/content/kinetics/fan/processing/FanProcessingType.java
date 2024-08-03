package com.simibubi.create.content.kinetics.fan.processing;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface FanProcessingType {

    boolean isValidAt(Level var1, BlockPos var2);

    int getPriority();

    boolean canProcess(ItemStack var1, Level var2);

    @Nullable
    List<ItemStack> process(ItemStack var1, Level var2);

    void spawnProcessingParticles(Level var1, Vec3 var2);

    void morphAirFlow(FanProcessingType.AirFlowParticleAccess var1, RandomSource var2);

    void affectEntity(Entity var1, Level var2);

    static FanProcessingType parse(String str) {
        ResourceLocation id = ResourceLocation.tryParse(str);
        if (id == null) {
            return AllFanProcessingTypes.NONE;
        } else {
            FanProcessingType type = FanProcessingTypeRegistry.getType(id);
            return (FanProcessingType) (type == null ? AllFanProcessingTypes.NONE : type);
        }
    }

    static FanProcessingType getAt(Level level, BlockPos pos) {
        for (FanProcessingType type : FanProcessingTypeRegistry.getSortedTypesView()) {
            if (type.isValidAt(level, pos)) {
                return type;
            }
        }
        return AllFanProcessingTypes.NONE;
    }

    public interface AirFlowParticleAccess {

        void setColor(int var1);

        void setAlpha(float var1);

        void spawnExtraParticle(ParticleOptions var1, float var2);
    }
}