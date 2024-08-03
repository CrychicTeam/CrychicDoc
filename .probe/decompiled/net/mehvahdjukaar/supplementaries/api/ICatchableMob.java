package net.mehvahdjukaar.supplementaries.api;

import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.supplementaries.common.items.AbstractMobContainerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ICatchableMob {

    ICatchableMob DEFAULT = new ICatchableMob() {
    };

    default boolean canBeCaughtWithItem(Entity self, Item item, Player player) {
        if (item instanceof AbstractMobContainerItem containerItem) {
            if (self instanceof Slime slime && slime.getSize() != 1) {
                return false;
            }
            return containerItem.canItemCatch(self);
        } else {
            return false;
        }
    }

    default int getLightLevel(Level world, BlockPos pos) {
        return 0;
    }

    default void onCaptured(Entity entity, Player player, ItemStack stack) {
    }

    default float getHitBoxWidthIncrement(Entity self) {
        return 0.0F;
    }

    default float getHitBoxHeightIncrement(Entity self) {
        return 0.0F;
    }

    default boolean shouldHover(Entity self, boolean waterlogged) {
        return self.isNoGravity() || self instanceof FlyingAnimal || self.isIgnoringBlockTriggers() || self instanceof WaterAnimal;
    }

    default Optional<Holder<SoftFluid>> shouldRenderWithFluid() {
        return Optional.empty();
    }

    default int getFishTextureIndex() {
        return 0;
    }

    default boolean renderAs2DFish() {
        return this.getFishTextureIndex() != 0;
    }

    default <T extends Entity> CapturedMobInstance<T> createCapturedMobInstance(T self, float containerWidth, float containerHeight) {
        return new CapturedMobInstance.Default<>(self, containerWidth, containerHeight);
    }
}