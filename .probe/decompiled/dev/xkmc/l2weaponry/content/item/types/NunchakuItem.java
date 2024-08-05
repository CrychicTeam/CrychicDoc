package dev.xkmc.l2weaponry.content.item.types;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.util.raytrace.FastItem;
import dev.xkmc.l2weaponry.content.client.WeaponBEWLR;
import dev.xkmc.l2weaponry.content.item.base.GenericWeaponItem;
import dev.xkmc.l2weaponry.events.ClientRenderEvents;
import dev.xkmc.l2weaponry.init.data.LangData;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

public class NunchakuItem extends GenericWeaponItem implements FastItem {

    public NunchakuItem(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config, BlockTags.MINEABLE_WITH_HOE);
        LWItems.NUNCHAKU_DECO.add(this);
    }

    public static boolean check(@Nullable LivingEntity entity, ItemStack stack) {
        if (entity == null) {
            return false;
        } else if (!entity.isUsingItem()) {
            return false;
        } else {
            return entity.getMainHandItem() != stack ? false : entity.getUseItem() == stack || entity.getUseItem().canPerformAction(ToolActions.SHIELD_BLOCK);
        }
    }

    public static boolean delegate(Player player) {
        return player.m_21206_().canPerformAction(ToolActions.SHIELD_BLOCK) && !player.getCooldowns().isOnCooldown(player.m_21206_().getItem());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (hand != InteractionHand.OFF_HAND && !delegate(player)) {
            player.m_6672_(hand);
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.pass(itemstack);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity le, ItemStack stack, int remain) {
        if (le instanceof Player player && delegate(player)) {
            le.stopUsingItem();
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (selected && level.isClientSide() && entity instanceof Player player && player.m_6117_()) {
            ClientRenderEvents.onNunchakuUse(player, stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.TOOL_NUNCHAKU.get());
        super.appendHoverText(pStack, pLevel, list, pIsAdvanced);
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 72000;
    }

    @Override
    public boolean isSharp() {
        return false;
    }

    @Override
    public boolean isFast(ItemStack itemStack) {
        return true;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(WeaponBEWLR.EXTENSIONS);
    }
}