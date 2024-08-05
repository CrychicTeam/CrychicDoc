package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2weaponry.content.item.types.SpearItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EnderSpear extends SpearItem implements LegendaryWeapon, IGlowingTarget {

    public EnderSpear(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        LivingEntity target = RayTraceUtil.serverGetTarget(player);
        if (target != null && (level.isClientSide() || TeleportUtil.teleport(player, target, false))) {
            if (!level.isClientSide()) {
                player.getCooldowns().addCooldown(this, 60);
            }
            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.getCooldowns().getCooldownPercent(this, 0.0F) > 0.0F) {
            return InteractionResult.PASS;
        } else if (player.m_9236_().isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (TeleportUtil.teleport(player, target, false)) {
            player.getCooldowns().addCooldown(this, 60);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.ENDER_SPEAR.get(this.getDistance(pStack)));
    }

    @Override
    public int getDistance(ItemStack itemStack) {
        return LWConfig.COMMON.hauntingDemonDistance.get();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (selected && level.isClientSide() && entity instanceof Player player) {
            RayTraceUtil.clientUpdateTarget(player, (double) this.getDistance(stack));
        }
    }
}