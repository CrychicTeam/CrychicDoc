package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.ExoflameHeatable;

public class CapabilityExoflame {

    public CapabilityExoflame.BuilderBlockEntity tileEntity() {
        return new CapabilityExoflame.BuilderBlockEntity();
    }

    public static class BuilderBlockEntity extends CapabilityBuilderForge<BlockEntity, ExoflameHeatable> {

        private Predicate<BlockEntity> canSmelt;

        private ToIntFunction<BlockEntity> getBurnTime;

        private Consumer<BlockEntity> boostBurnTime;

        private Consumer<BlockEntity> boostSpeed;

        public CapabilityExoflame.BuilderBlockEntity canSmelt(Predicate<BlockEntity> canSmelt) {
            this.canSmelt = canSmelt;
            return this;
        }

        public CapabilityExoflame.BuilderBlockEntity getBurnTime(ToIntFunction<BlockEntity> getBurnTime) {
            this.getBurnTime = getBurnTime;
            return this;
        }

        public CapabilityExoflame.BuilderBlockEntity boostBurnTime(Consumer<BlockEntity> boostBurnTime) {
            this.boostBurnTime = boostBurnTime;
            return this;
        }

        public CapabilityExoflame.BuilderBlockEntity boostSpeed(Consumer<BlockEntity> boostSpeed) {
            this.boostSpeed = boostSpeed;
            return this;
        }

        public ExoflameHeatable getCapability(BlockEntity instance) {
            return new ExoflameHeatable() {

                public boolean canSmelt() {
                    return BuilderBlockEntity.this.canSmelt.test(instance);
                }

                public int getBurnTime() {
                    return BuilderBlockEntity.this.getBurnTime.applyAsInt(instance);
                }

                public void boostBurnTime() {
                    if (BuilderBlockEntity.this.boostBurnTime != null) {
                        BuilderBlockEntity.this.boostBurnTime.accept(instance);
                    }
                }

                public void boostCookTime() {
                    if (BuilderBlockEntity.this.boostSpeed != null) {
                        BuilderBlockEntity.this.boostSpeed.accept(instance);
                    }
                }
            };
        }

        public Capability<ExoflameHeatable> getCapabilityKey() {
            return BotaniaForgeCapabilities.EXOFLAME_HEATABLE;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:exoflame_be");
        }
    }
}