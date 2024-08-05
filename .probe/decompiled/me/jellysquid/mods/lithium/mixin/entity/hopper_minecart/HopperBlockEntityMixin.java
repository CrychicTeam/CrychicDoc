package me.jellysquid.mods.lithium.mixin.entity.hopper_minecart;

import java.util.Collections;
import java.util.List;
import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
import me.jellysquid.mods.lithium.common.util.collections.BucketedList;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ HopperBlockEntity.class })
public class HopperBlockEntityMixin {

    @Overwrite
    public static List<ItemEntity> getItemsAtAndAbove(Level world, Hopper hopper) {
        AABB encompassingBox = hopper.getSuckShape().bounds();
        double xOffset = hopper.getLevelX() - 0.5;
        double yOffset = hopper.getLevelY() - 0.5;
        double zOffset = hopper.getLevelZ() - 0.5;
        List<ItemEntity> nearbyEntities = world.m_6443_(ItemEntity.class, encompassingBox.move(xOffset, yOffset, zOffset), EntitySelector.ENTITY_STILL_ALIVE);
        if (nearbyEntities.isEmpty()) {
            return Collections.emptyList();
        } else {
            AABB[] boundingBoxes = HopperHelper.getHopperPickupVolumeBoxes(hopper);
            int numBoxes = boundingBoxes.length;
            AABB[] offsetBoundingBoxes = new AABB[numBoxes];
            for (int i = 0; i < numBoxes; i++) {
                offsetBoundingBoxes[i] = boundingBoxes[i].move(xOffset, yOffset, zOffset);
            }
            BucketedList<ItemEntity> entities = new BucketedList<>(numBoxes);
            for (ItemEntity itemEntity : nearbyEntities) {
                AABB entityBoundingBox = itemEntity.m_20191_();
                for (int j = 0; j < numBoxes; j++) {
                    if (entityBoundingBox.intersects(offsetBoundingBoxes[j])) {
                        entities.addToBucket(j, itemEntity);
                        break;
                    }
                }
            }
            return entities;
        }
    }
}