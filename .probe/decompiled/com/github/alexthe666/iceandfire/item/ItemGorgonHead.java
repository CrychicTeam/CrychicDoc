package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.client.render.tile.RenderGorgonHead;
import com.github.alexthe666.iceandfire.datagen.tags.IafEntityTags;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import org.jetbrains.annotations.NotNull;

public class ItemGorgonHead extends Item {

    public ItemGorgonHead() {
        super(new Item.Properties().durability(1));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            static final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> new RenderGorgonHead(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @NotNull
    @Override
    public UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, Level worldIn, LivingEntity entity, int timeLeft) {
        double dist = 32.0;
        Vec3 Vector3d = entity.m_20299_(1.0F);
        Vec3 Vector3d1 = entity.m_20252_(1.0F);
        Vec3 Vector3d2 = Vector3d.add(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist);
        Entity pointedEntity = null;
        List<Entity> list = worldIn.getEntities(entity, entity.m_20191_().expandTowards(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist).inflate(1.0, 1.0, 1.0), new Predicate<Entity>() {

            public boolean apply(@Nullable Entity entity) {
                if (!(entity instanceof LivingEntity livingEntity)) {
                    return false;
                } else {
                    boolean isImmune = livingEntity instanceof IBlacklistedFromStatues blacklisted && !blacklisted.canBeTurnedToStone() || entity.getType().is(IafEntityTags.IMMUNE_TO_GORGON_STONE) || livingEntity.hasEffect(MobEffects.BLINDNESS);
                    return !isImmune && entity.isPickable() && !livingEntity.isDeadOrDying() && (entity instanceof Player || DragonUtils.isAlive(livingEntity));
                }
            }
        });
        double d2 = dist;
        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);
            AABB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
            Optional<Vec3> optional = axisalignedbb.clip(Vector3d, Vector3d2);
            if (axisalignedbb.contains(Vector3d)) {
                if (d2 >= 0.0) {
                    d2 = 0.0;
                }
            } else if (optional.isPresent()) {
                double d3 = Vector3d.distanceTo((Vec3) optional.get());
                if (d3 < d2 || d2 == 0.0) {
                    if (entity1.getRootVehicle() != entity.m_20201_() || entity.canRiderInteract()) {
                        pointedEntity = entity1;
                        d2 = d3;
                    } else if (d2 == 0.0) {
                        pointedEntity = entity1;
                    }
                }
            }
        }
        if (pointedEntity != null && pointedEntity instanceof LivingEntity livingEntity) {
            boolean wasSuccesful = true;
            if (pointedEntity instanceof Player) {
                wasSuccesful = pointedEntity.hurt(IafDamageRegistry.causeGorgonDamage(pointedEntity), 2.1474836E9F);
            } else if (!worldIn.isClientSide) {
                pointedEntity.remove(Entity.RemovalReason.KILLED);
            }
            if (wasSuccesful) {
                pointedEntity.playSound(IafSoundRegistry.TURN_STONE, 1.0F, 1.0F);
                EntityStoneStatue statue = EntityStoneStatue.buildStatueEntity(livingEntity);
                statue.m_19890_(pointedEntity.getX(), pointedEntity.getY(), pointedEntity.getZ(), pointedEntity.getYRot(), pointedEntity.getXRot());
                statue.f_20883_ = pointedEntity.getYRot();
                if (!worldIn.isClientSide) {
                    worldIn.m_7967_(statue);
                }
            }
            if (entity instanceof Player player && !player.isCreative()) {
                stack.shrink(1);
            }
        }
        stack.getTag().putBoolean("Active", false);
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = playerIn.m_21120_(hand);
        playerIn.m_6672_(hand);
        itemStackIn.getTag().putBoolean("Active", true);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStackIn);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity player, @NotNull ItemStack stack, int count) {
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
    }
}