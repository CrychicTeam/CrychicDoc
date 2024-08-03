package dev.xkmc.l2backpack.content.quickswap.merged;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.common.BackpackModelItem;
import dev.xkmc.l2backpack.content.quickswap.common.SimpleMenuPvd;
import dev.xkmc.l2backpack.init.data.LangData;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class EnderSwitch extends MultiSwitch implements BackpackModelItem {

    public EnderSwitch(Item.Properties props) {
        super(props);
    }

    @Override
    public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
        new SimpleMenuPvd(player, slot, this, stack, (a, b, c, d, e) -> new EnderSwitchMenu(a, b, player.m_36327_(), c, d, e)).open();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        PickupConfig.addText(stack, list);
        LangData.addInfo(list, LangData.Info.ENDER_SWITCH_INFO);
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack stack) {
        return new ResourceLocation("l2backpack", "textures/block/ender_swap.png");
    }

    @Override
    public boolean shouldRender() {
        return false;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EnderSwitchCaps(stack);
    }
}