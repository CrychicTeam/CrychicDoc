package dev.xkmc.l2backpack.content.remote.player;

import dev.xkmc.l2backpack.content.capability.PickupBagItem;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.common.BackpackModelItem;
import dev.xkmc.l2backpack.content.common.ContentTransfer;
import dev.xkmc.l2backpack.content.common.InvTooltip;
import dev.xkmc.l2backpack.content.common.TooltipInvItem;
import dev.xkmc.l2backpack.content.insert.InsertOnlyItem;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import dev.xkmc.l2backpack.content.render.BaseItemRenderer;
import dev.xkmc.l2backpack.init.data.LangData;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public class EnderBackpackItem extends Item implements BackpackModelItem, PickupBagItem, InsertOnlyItem, TooltipInvItem, IQuickSwapItem {

    public EnderBackpackItem(Item.Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide()) {
            player.openMenu(new SimpleMenuProvider((id, inv, pl) -> ChestMenu.threeRows(id, inv, player.getEnderChestInventory()), stack.getHoverName()));
        } else {
            ContentTransfer.playSound(player);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        PickupConfig.addText(stack, list);
        LangData.addInfo(list, LangData.Info.QUICK_ANY_ACCESS, LangData.Info.KEYBIND, LangData.Info.PICKUP);
        LangData.altInsert(list);
    }

    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == EquipmentSlot.CHEST;
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack stack) {
        return new ResourceLocation("l2backpack", "textures/block/ender_backpack.png");
    }

    @Nullable
    @Override
    public IItemHandler getInvCap(ItemStack storage, ServerPlayer player) {
        return new InvWrapper(player.m_36327_());
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EnderBackpackCaps(stack);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(BaseItemRenderer.EXTENSIONS);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return InvTooltip.get(this, stack);
    }

    @Override
    public int getInvSize(ItemStack stack) {
        return 27;
    }

    @Override
    public List<ItemStack> getInvItems(ItemStack stack, Player player) {
        return EnderSyncCap.HOLDER.get(player).getItems();
    }

    @Nullable
    @Override
    public IQuickSwapToken<?> getTokenOfType(ItemStack stack, LivingEntity entity, QuickSwapType type) {
        return entity instanceof Player player ? EnderSyncCap.HOLDER.get(player).getToken(type) : null;
    }
}