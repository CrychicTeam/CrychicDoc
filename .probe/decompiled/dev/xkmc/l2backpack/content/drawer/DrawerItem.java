package dev.xkmc.l2backpack.content.drawer;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.click.DoubleClickItem;
import dev.xkmc.l2backpack.content.common.ContentTransfer;
import dev.xkmc.l2backpack.content.render.BaseItemRenderer;
import dev.xkmc.l2backpack.init.data.LangData;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public class DrawerItem extends BlockItem implements BaseDrawerItem, ContentTransfer.Quad, DoubleClickItem {

    private static final String COUNT = "drawerCount";

    private static final ResourceLocation BG = new ResourceLocation("l2backpack", "textures/block/drawer/drawer_side.png");

    public static int getCount(ItemStack drawer) {
        return (Integer) Optional.ofNullable(drawer.getTag()).map(e -> e.getInt("drawerCount")).orElse(0);
    }

    public static void setCount(ItemStack drawer, int count) {
        drawer.getOrCreateTag().putInt("drawerCount", count);
    }

    public DrawerItem(Block block, Item.Properties properties) {
        super(block, properties.stacksTo(1).fireResistant());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(BaseItemRenderer.EXTENSIONS);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (world.isClientSide()) {
            ContentTransfer.playDrawerSound(player);
            return InteractionResultHolder.success(stack);
        } else {
            if (!player.m_6144_()) {
                Item item = BaseDrawerItem.getItem(stack);
                int count = getCount(stack);
                int max = Math.min(item.getMaxStackSize(), count);
                player.getInventory().placeItemBackInInventory(new ItemStack(item, max));
                setCount(stack, count - max);
                ContentTransfer.onExtract(player, max, stack);
            } else {
                Item item = BaseDrawerItem.getItem(stack);
                int count = getCount(stack);
                int max = item.getMaxStackSize() * BaseDrawerItem.getStacking(stack);
                boolean perform = !this.canSetNewItem(stack);
                if (!perform) {
                    item = ContentTransfer.filterMaxItem(new InvWrapper(player.getInventory()));
                    if (item != Items.AIR) {
                        perform = true;
                        this.setItem(stack, item, player);
                    }
                }
                if (perform) {
                    int ext = BaseDrawerItem.loadFromInventory(max, count, item, player);
                    count += ext;
                    setCount(stack, count);
                    ContentTransfer.onCollect(player, ext, stack);
                }
            }
            return InteractionResultHolder.success(stack);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = ContentTransfer.blockInteract(context, this);
        if (result == InteractionResult.PASS && context.getPlayer() != null && context.getPlayer().m_6144_()) {
            result = super.useOn(context);
            if (result == InteractionResult.FAIL) {
                result = InteractionResult.PASS;
            }
        }
        return result;
    }

    @Override
    public void click(Player player, ItemStack stack, boolean client, boolean shift, boolean right, @Nullable IItemHandler target) {
        if (!client && shift && right && target != null) {
            Item item = BaseDrawerItem.getItem(stack);
            int count = getCount(stack);
            int remain = ContentTransfer.transfer(item, count, target);
            ContentTransfer.onDump(player, count - remain, stack);
            setCount(stack, remain);
        } else if (client && shift && right && target != null) {
            ContentTransfer.playDrawerSound(player);
        }
        if (!client && shift && !right && target != null) {
            Item item = BaseDrawerItem.getItem(stack);
            boolean perform = !this.canSetNewItem(stack);
            if (!perform) {
                item = ContentTransfer.filterMaxItem(target);
                if (item != Items.AIR) {
                    this.setItem(stack, item, player);
                    perform = true;
                }
            }
            if (perform) {
                int count = getCount(stack);
                int max = BaseDrawerItem.getStacking(stack) * item.getMaxStackSize();
                int remain = ContentTransfer.loadFrom(item, max - count, target);
                ContentTransfer.onLoad(player, remain, stack);
                setCount(stack, count + remain);
            }
        } else if (client && shift && !right && target != null) {
            ContentTransfer.playDrawerSound(player);
        }
    }

    @Override
    public void insert(ItemStack drawer, ItemStack stack, @Nullable Player player) {
        int count = getCount(drawer);
        int allow = Math.min(BaseDrawerItem.getStacking(drawer) * stack.getMaxStackSize() - count, stack.getCount());
        setCount(drawer, count + allow);
        stack.shrink(allow);
    }

    @Override
    public ItemStack takeItem(ItemStack drawer, int max, @Nullable Player player, boolean simulate) {
        if (this.canSetNewItem(drawer)) {
            return ItemStack.EMPTY;
        } else {
            Item item = BaseDrawerItem.getItem(drawer);
            int count = getCount(drawer);
            int take = Math.min(count, Math.min(max, item.getMaxStackSize()));
            if (!simulate) {
                setCount(drawer, count - take);
            }
            return new ItemStack(item, take);
        }
    }

    @Override
    public boolean canSetNewItem(ItemStack drawer) {
        return BaseDrawerItem.getItem(drawer) == Items.AIR || getCount(drawer) == 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        Item item = BaseDrawerItem.getItem(stack);
        int count = getCount(stack);
        if (!this.canSetNewItem(stack)) {
            list.add(LangData.IDS.DRAWER_CONTENT.get(item.getDescription(), count));
        }
        list.add(LangData.IDS.BACKPACK_SLOT.get(BaseDrawerItem.getStackingFactor(stack), 8).withStyle(ChatFormatting.GRAY));
        PickupConfig.addText(stack, list);
        LangData.addInfo(list, LangData.Info.DRAWER_USE, LangData.Info.LOAD, LangData.Info.PLACE, LangData.Info.EXTRACT_DRAWER, LangData.Info.COLLECT_DRAWER, LangData.Info.PICKUP);
    }

    @Override
    public String getDescriptionId() {
        return this.m_41467_();
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        DrawerInvAccess access = new DrawerInvAccess(stack, this);
        return new DrawerInvWrapper(stack, trace -> access);
    }

    @Override
    public ResourceLocation backgroundLoc() {
        return BG;
    }

    @Override
    public int remainingSpace(ItemStack drawer) {
        if (this.canSetNewItem(drawer)) {
            return 0;
        } else {
            int count = getCount(drawer);
            int maxStack = BaseDrawerItem.getItem(drawer).getMaxStackSize();
            return BaseDrawerItem.getStacking(drawer) * maxStack - count;
        }
    }

    @Override
    public boolean canAbsorb(Slot src, ItemStack stack) {
        return this.canSetNewItem(stack) ? false : BaseDrawerItem.canAccept(stack, src.getItem());
    }

    @Override
    public void mergeStack(ItemStack drawer, ItemStack stack) {
        int count = getCount(drawer);
        int allow = Math.min(BaseDrawerItem.getStacking(drawer) * stack.getMaxStackSize() - count, stack.getCount());
        setCount(drawer, count + allow);
        stack.shrink(allow);
    }
}