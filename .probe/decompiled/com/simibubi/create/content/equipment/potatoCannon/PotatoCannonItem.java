package com.simibubi.create.content.equipment.potatoCannon;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class PotatoCannonItem extends ProjectileWeaponItem implements CustomArmPoseItem {

    public static ItemStack CLIENT_CURRENT_AMMO = ItemStack.EMPTY;

    public static final int MAX_DAMAGE = 100;

    public PotatoCannonItem(Item.Properties properties) {
        super(properties.defaultDurability(100));
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.POWER_ARROWS) {
            return true;
        } else if (enchantment == Enchantments.PUNCH_ARROWS) {
            return true;
        } else if (enchantment == Enchantments.FLAMING_ARROWS) {
            return true;
        } else if (enchantment == Enchantments.MOB_LOOTING) {
            return true;
        } else {
            return enchantment == AllEnchantments.POTATO_RECOVERY.get() ? true : super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return BacktankUtil.isBarVisible(stack, this.maxUses());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return BacktankUtil.getBarWidth(stack, this.maxUses());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BacktankUtil.getBarColor(stack, this.maxUses());
    }

    private int maxUses() {
        return AllConfigs.server().equipment.maxPotatoCannonShots.get();
    }

    public boolean isCannon(ItemStack stack) {
        return stack.getItem() instanceof PotatoCannonItem;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        return (InteractionResultHolder<ItemStack>) this.findAmmoInInventory(world, player, stack).map(itemStack -> {
            if (ShootableGadgetItemMethods.shouldSwap(player, stack, hand, this::isCannon)) {
                return InteractionResultHolder.fail(stack);
            } else if (world.isClientSide) {
                CreateClient.POTATO_CANNON_RENDER_HANDLER.dontAnimateItem(hand);
                return InteractionResultHolder.success(stack);
            } else {
                Vec3 barrelPos = ShootableGadgetItemMethods.getGunBarrelVec(player, hand == InteractionHand.MAIN_HAND, new Vec3(0.75, -0.15F, 1.5));
                Vec3 correction = ShootableGadgetItemMethods.getGunBarrelVec(player, hand == InteractionHand.MAIN_HAND, new Vec3(-0.05F, 0.0, 0.0)).subtract(player.m_20182_().add(0.0, (double) player.m_20192_(), 0.0));
                PotatoCannonProjectileType projectileType = (PotatoCannonProjectileType) PotatoProjectileTypeManager.getTypeForStack(itemStack).orElse(BuiltinPotatoProjectileTypes.FALLBACK);
                Vec3 lookVec = player.m_20154_();
                Vec3 motion = lookVec.add(correction).normalize().scale(2.0).scale((double) projectileType.getVelocityMultiplier());
                float soundPitch = projectileType.getSoundPitch() + (Create.RANDOM.nextFloat() - 0.5F) / 4.0F;
                boolean spray = projectileType.getSplit() > 1;
                Vec3 sprayBase = VecHelper.rotate(new Vec3(0.0, 0.1, 0.0), (double) (360.0F * Create.RANDOM.nextFloat()), Direction.Axis.Z);
                float sprayChange = 360.0F / (float) projectileType.getSplit();
                for (int i = 0; i < projectileType.getSplit(); i++) {
                    PotatoProjectileEntity projectile = (PotatoProjectileEntity) AllEntityTypes.POTATO_PROJECTILE.create(world);
                    projectile.setItem(itemStack);
                    projectile.setEnchantmentEffectsFromCannon(stack);
                    Vec3 splitMotion = motion;
                    if (spray) {
                        float imperfection = 40.0F * (Create.RANDOM.nextFloat() - 0.5F);
                        Vec3 sprayOffset = VecHelper.rotate(sprayBase, (double) ((float) i * sprayChange + imperfection), Direction.Axis.Z);
                        splitMotion = motion.add(VecHelper.lookAt(sprayOffset, motion));
                    }
                    if (i != 0) {
                        projectile.recoveryChance = 0.0F;
                    }
                    projectile.m_6034_(barrelPos.x, barrelPos.y, barrelPos.z);
                    projectile.m_20256_(splitMotion);
                    projectile.m_5602_(player);
                    world.m_7967_(projectile);
                }
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    if (itemStack.isEmpty()) {
                        player.getInventory().removeItem(itemStack);
                    }
                }
                if (!BacktankUtil.canAbsorbDamage(player, this.maxUses())) {
                    stack.hurtAndBreak(1, player, p -> p.m_21190_(hand));
                }
                Integer cooldown = (Integer) this.findAmmoInInventory(world, player, stack).flatMap(PotatoProjectileTypeManager::getTypeForStack).map(PotatoCannonProjectileType::getReloadTicks).orElse(10);
                ShootableGadgetItemMethods.applyCooldown(player, stack, hand, this::isCannon, cooldown);
                ShootableGadgetItemMethods.sendPackets(player, b -> new PotatoCannonPacket(barrelPos, lookVec.normalize(), itemStack, hand, soundPitch, b));
                return InteractionResultHolder.success(stack);
            }
        }).orElse(InteractionResultHolder.pass(stack));
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }

    private Optional<ItemStack> findAmmoInInventory(Level world, Player player, ItemStack held) {
        ItemStack findAmmo = player.getProjectile(held);
        return PotatoProjectileTypeManager.getTypeForStack(findAmmo).map($ -> findAmmo);
    }

    @OnlyIn(Dist.CLIENT)
    public static Optional<ItemStack> getAmmoforPreview(ItemStack cannon) {
        if (AnimationTickHolder.getTicks() % 3 != 0) {
            return Optional.of(CLIENT_CURRENT_AMMO).filter(stack -> !stack.isEmpty());
        } else {
            LocalPlayer player = Minecraft.getInstance().player;
            CLIENT_CURRENT_AMMO = ItemStack.EMPTY;
            if (player == null) {
                return Optional.empty();
            } else {
                ItemStack findAmmo = player.m_6298_(cannon);
                Optional<ItemStack> found = PotatoProjectileTypeManager.getTypeForStack(findAmmo).map($ -> findAmmo);
                found.ifPresent(stack -> CLIENT_CURRENT_AMMO = stack);
                return found;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        int power = stack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
        int punch = stack.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
        float additionalDamageMult = 1.0F + (float) power * 0.2F;
        float additionalKnockback = (float) punch * 0.5F;
        getAmmoforPreview(stack).ifPresent(ammo -> {
            String _attack = "potato_cannon.ammo.attack_damage";
            String _reload = "potato_cannon.ammo.reload_ticks";
            String _knockback = "potato_cannon.ammo.knockback";
            tooltip.add(Components.immutableEmpty());
            tooltip.add(Components.translatable(ammo.getDescriptionId()).append(Components.literal(":")).withStyle(ChatFormatting.GRAY));
            PotatoCannonProjectileType type = (PotatoCannonProjectileType) PotatoProjectileTypeManager.getTypeForStack(ammo).get();
            MutableComponent spacing = Components.literal(" ");
            ChatFormatting green = ChatFormatting.GREEN;
            ChatFormatting darkGreen = ChatFormatting.DARK_GREEN;
            float damageF = (float) type.getDamage() * additionalDamageMult;
            MutableComponent damage = Components.literal(damageF == (float) Mth.floor(damageF) ? Mth.floor(damageF) + "" : damageF + "");
            MutableComponent reloadTicks = Components.literal(type.getReloadTicks() + "");
            MutableComponent knockback = Components.literal(type.getKnockback() + additionalKnockback + "");
            damage = damage.withStyle(additionalDamageMult > 1.0F ? green : darkGreen);
            knockback = knockback.withStyle(additionalKnockback > 0.0F ? green : darkGreen);
            reloadTicks = reloadTicks.withStyle(darkGreen);
            tooltip.add(spacing.m_6879_().append(Lang.translateDirect(_attack, damage).withStyle(darkGreen)));
            tooltip.add(spacing.m_6879_().append(Lang.translateDirect(_reload, reloadTicks).withStyle(darkGreen)));
            tooltip.add(spacing.m_6879_().append(Lang.translateDirect(_knockback, knockback).withStyle(darkGreen)));
        });
        super.m_7373_(stack, world, tooltip, flag);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> PotatoProjectileTypeManager.getTypeForStack(stack).isPresent();
    }

    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Nullable
    @Override
    public HumanoidModel.ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        return !player.f_20911_ ? HumanoidModel.ArmPose.CROSSBOW_HOLD : null;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new PotatoCannonItemRenderer()));
    }
}