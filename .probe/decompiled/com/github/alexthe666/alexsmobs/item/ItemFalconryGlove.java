package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.IFalconry;
import com.github.alexthe666.alexsmobs.message.MessageSyncEntityPos;
import com.google.common.base.Predicate;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemFalconryGlove extends Item implements ILeftClick {

    public ItemFalconryGlove(Item.Properties properties) {
        super(properties);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsMobs.PROXY.getISTERProperties());
    }

    @Override
    public boolean onLeftClick(ItemStack stack, LivingEntity playerIn) {
        if (stack.getItem() == AMItemRegistry.FALCONRY_GLOVE.get()) {
            float dist = 128.0F;
            Vec3 Vector3d = playerIn.m_20299_(1.0F);
            Vec3 Vector3d1 = playerIn.m_20252_(1.0F);
            double vector3d1xDist = Vector3d1.x * 128.0;
            double vector3d1yDist = Vector3d1.y * 128.0;
            double vector3d1zDist = Vector3d1.z * 128.0;
            Vec3 Vector3d2 = Vector3d.add(vector3d1xDist, vector3d1yDist, vector3d1zDist);
            double d1 = 128.0;
            Entity pointedEntity = null;
            for (Entity entity1 : playerIn.m_9236_().getEntities(playerIn, playerIn.m_20191_().expandTowards(vector3d1xDist, vector3d1yDist, vector3d1zDist).inflate(1.0, 1.0, 1.0), new Predicate<Entity>() {

                public boolean apply(@Nullable Entity entity) {
                    return entity != null && entity.isPickable() && (entity instanceof Player || entity instanceof LivingEntity);
                }
            })) {
                AABB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
                Optional<Vec3> optional = axisalignedbb.clip(Vector3d, Vector3d2);
                if (axisalignedbb.contains(Vector3d)) {
                    if (d1 >= 0.0) {
                        d1 = 0.0;
                    }
                } else if (optional.isPresent()) {
                    double d3 = Vector3d.distanceTo((Vec3) optional.get());
                    if (d3 < d1 || d1 == 0.0) {
                        if (entity1.getRootVehicle() != playerIn.m_20201_() || playerIn.canRiderInteract()) {
                            pointedEntity = entity1;
                            d1 = d3;
                        } else if (d1 == 0.0) {
                            pointedEntity = entity1;
                        }
                    }
                }
            }
            if (!playerIn.m_20197_().isEmpty()) {
                for (Entity entity : playerIn.m_20197_()) {
                    if (entity instanceof IFalconry && entity instanceof Animal animal) {
                        IFalconry falcon = (IFalconry) entity;
                        animal.m_6038_();
                        animal.m_7678_(playerIn.m_20185_(), playerIn.m_20188_(), playerIn.m_20189_(), animal.m_146908_(), animal.m_146909_());
                        if (animal.m_9236_().isClientSide) {
                            AlexsMobs.sendMSGToServer(new MessageSyncEntityPos(animal.m_19879_(), playerIn.m_20185_(), playerIn.m_20188_(), playerIn.m_20189_()));
                        } else {
                            AlexsMobs.sendMSGToAll(new MessageSyncEntityPos(animal.m_19879_(), playerIn.m_20185_(), playerIn.m_20188_(), playerIn.m_20189_()));
                        }
                        if (playerIn instanceof Player) {
                            falcon.onLaunch((Player) playerIn, pointedEntity);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}