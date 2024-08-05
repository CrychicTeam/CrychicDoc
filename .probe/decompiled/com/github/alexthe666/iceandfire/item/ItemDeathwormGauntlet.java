package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.client.render.tile.RenderDeathWormGauntlet;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import java.util.List;
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

public class ItemDeathwormGauntlet extends Item {

    private boolean deathwormReceded = true;

    private boolean deathwormLaunched = false;

    private int specialDamage = 0;

    public ItemDeathwormGauntlet() {
        super(new Item.Properties().durability(500));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            static final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> new RenderDeathWormGauntlet(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 1;
    }

    @NotNull
    @Override
    public UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = playerIn.m_21120_(hand);
        playerIn.m_6672_(hand);
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int count) {
        if (!this.deathwormReceded && !this.deathwormLaunched && entity instanceof Player player) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.getInt("HolderID") != player.m_19879_()) {
                tag.putInt("HolderID", player.m_19879_());
            }
            if (player.getCooldowns().getCooldownPercent(this, 0.0F) == 0.0F) {
                player.getCooldowns().addCooldown(this, 10);
                player.playSound(IafSoundRegistry.DEATHWORM_ATTACK, 1.0F, 1.0F);
                this.deathwormReceded = false;
                this.deathwormLaunched = true;
            }
        }
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity LivingEntity, int timeLeft) {
        if (this.specialDamage > 0) {
            stack.hurtAndBreak(this.specialDamage, LivingEntity, player -> player.broadcastBreakEvent(LivingEntity.getUsedItemHand()));
            this.specialDamage = 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.getInt("HolderID") != -1) {
            tag.putInt("HolderID", -1);
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof LivingEntity) {
            EntityDataProvider.getCapability(entity).ifPresent(data -> {
                int tempLungeTicks = data.miscData.lungeTicks;
                if (this.deathwormReceded) {
                    if (tempLungeTicks > 0) {
                        tempLungeTicks -= 4;
                    }
                    if (tempLungeTicks <= 0) {
                        tempLungeTicks = 0;
                        this.deathwormReceded = false;
                        this.deathwormLaunched = false;
                    }
                } else if (this.deathwormLaunched) {
                    tempLungeTicks += 4;
                    if (tempLungeTicks > 20) {
                        this.deathwormReceded = true;
                    }
                }
                if (data.miscData.lungeTicks == 20 && entity instanceof Player player) {
                    Vec3 Vector3d = player.m_20252_(1.0F).normalize();
                    double range = 5.0;
                    for (LivingEntity livingEntity : world.m_45976_(LivingEntity.class, new AABB(player.m_20185_() - range, player.m_20186_() - range, player.m_20189_() - range, player.m_20185_() + range, player.m_20186_() + range, player.m_20189_() + range))) {
                        if (livingEntity != entity) {
                            Vec3 Vector3d1 = new Vec3(livingEntity.m_20185_() - player.m_20185_(), livingEntity.m_20186_() - player.m_20186_(), livingEntity.m_20189_() - player.m_20189_());
                            double d0 = Vector3d1.length();
                            Vector3d1 = Vector3d1.normalize();
                            double d1 = Vector3d.dot(Vector3d1);
                            boolean canSee = d1 > 1.0 - 0.5 / d0 && player.m_142582_(livingEntity);
                            if (canSee) {
                                this.specialDamage++;
                                livingEntity.hurt(entity.level().damageSources().playerAttack((Player) entity), 3.0F);
                                livingEntity.knockback(0.5, livingEntity.m_20185_() - player.m_20185_(), livingEntity.m_20189_() - player.m_20189_());
                            }
                        }
                    }
                }
                data.miscData.setLungeTicks(tempLungeTicks);
            });
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.deathworm_gauntlet.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.deathworm_gauntlet.desc_1").withStyle(ChatFormatting.GRAY));
    }
}