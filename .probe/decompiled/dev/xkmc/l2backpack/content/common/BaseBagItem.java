package dev.xkmc.l2backpack.content.common;

import dev.xkmc.l2backpack.content.capability.PickupBagItem;
import dev.xkmc.l2backpack.content.insert.InsertOnlyItem;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseBagItem extends Item implements ContentTransfer.Quad, PickupBagItem, InsertOnlyItem, TooltipInvItem {

    protected static final String LOOT = "loot";

    protected static final String SEED = "seed";

    public static ListTag getListTag(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains("Items") ? stack.getOrCreateTag().getList("Items", 10) : new ListTag();
    }

    public static void setListTag(ItemStack stack, ListTag list) {
        stack.getOrCreateTag().put("Items", list);
    }

    public static long getTimeStamp(ItemStack stack) {
        return stack.getOrCreateTag().getLong("TimeStamp");
    }

    @OnlyIn(Dist.CLIENT)
    public static float isOpened(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        if (entity != Proxy.getClientPlayer()) {
            return 0.0F;
        } else {
            if (Minecraft.getInstance().screen instanceof BaseOpenableScreen<?> gui && gui.m_6262_() instanceof BaseBagMenu<?> cont) {
                return cont.getStack() == stack ? 1.0F : 0.0F;
            }
            return 0.0F;
        }
    }

    public BaseBagItem(Item.Properties props) {
        super(props);
    }

    public static List<ItemStack> getItems(ItemStack stack) {
        List<ItemStack> ans = new ArrayList();
        for (Tag value : getListTag(stack)) {
            if (value instanceof CompoundTag) {
                CompoundTag ctag = (CompoundTag) value;
                ItemStack i = ItemStack.of(ctag);
                int count = ctag.getInt("Count");
                if (i.getCount() < count) {
                    i.setCount(count);
                }
                ans.add(i);
            }
        }
        if (!ans.isEmpty()) {
            int size = ((BaseBagItem) stack.getItem()).getRows(stack) * 9;
            while (ans.size() < size) {
                ans.add(ItemStack.EMPTY);
            }
        }
        return ans;
    }

    public static void setItems(ItemStack stack, List<ItemStack> list) {
        ListTag tag = new ListTag();
        for (int i = 0; i < list.size(); i++) {
            tag.add(i, (Tag) ((ItemStack) list.get(i)).save(new CompoundTag()));
        }
        setListTag(stack, tag);
    }

    public static void checkLootGen(ItemStack stack, Player player) {
        if (getListTag(stack).isEmpty()) {
            CompoundTag ctag = stack.getOrCreateTag();
            if (ctag.contains("loot")) {
                ResourceLocation rl = new ResourceLocation(ctag.getString("loot"));
                long seed = ctag.getLong("seed");
                ctag.remove("loot");
                ctag.remove("seed");
                if (player.m_9236_() instanceof ServerLevel sl) {
                    LootTable var13 = sl.getServer().getLootData().m_278676_(rl);
                    LootParams.Builder builder = new LootParams.Builder(sl);
                    builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
                    BaseBagItem bag = (BaseBagItem) stack.getItem();
                    SimpleContainer cont = new SimpleContainer(bag.getRows(stack));
                    var13.fill(cont, builder.create(LootContextParamSets.EMPTY), seed);
                    ArrayList list = new ArrayList();
                    for (int i = 0; i < cont.getContainerSize(); i++) {
                        list.add(cont.getItem(i));
                    }
                    setItems(stack, list);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide()) {
            int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;
            this.open((ServerPlayer) player, PlayerSlot.ofInventory(slot), stack);
        } else {
            ContentTransfer.playSound(player);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return ContentTransfer.blockInteract(context, this);
    }

    @Override
    public void click(Player player, ItemStack stack, boolean client, boolean shift, boolean right, @Nullable IItemHandler target) {
        List<ItemStack> list = null;
        if (!client && shift && target != null) {
            list = getItems(stack);
            int slot = this.getRows(stack) * 9;
            while (list.size() < slot) {
                list.add(ItemStack.EMPTY);
            }
        }
        if (!client && shift && right && target != null) {
            int moved = ContentTransfer.transfer(list, target);
            setItems(stack, list);
            ContentTransfer.onDump(player, moved, stack);
        } else if (client && shift && right && target != null) {
            ContentTransfer.playSound(player);
        }
        if (!client && shift && !right && target != null) {
            int moved = ContentTransfer.loadFrom(list, target, player, this::isValidContent);
            setItems(stack, list);
            ContentTransfer.onLoad(player, moved, stack);
        } else if (client && shift && !right && target != null) {
            ContentTransfer.playSound(player);
        }
    }

    @Override
    public boolean isValidContent(ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems();
    }

    public abstract void open(ServerPlayer var1, PlayerSlot<?> var2, ItemStack var3);

    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == EquipmentSlot.CHEST;
    }

    @Nullable
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.CHEST;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return InvTooltip.get(this, stack);
    }

    public int getRows(ItemStack stack) {
        return 1;
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.isValidContent(stack);
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new BaseBagInvWrapper(stack);
    }

    @Override
    public int getInvSize(ItemStack stack) {
        return getItems(stack).size();
    }

    @Override
    public List<ItemStack> getInvItems(ItemStack stack, Player player) {
        return getItems(stack);
    }

    public void checkInit(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.getBoolean("init")) {
            tag.putBoolean("init", true);
            tag.putUUID("container_id", UUID.randomUUID());
            if (!tag.contains("Items")) {
                List<ItemStack> list = getItems(stack);
                int size = this.getRows(stack) * 9;
                while (list.size() < size) {
                    list.add(ItemStack.EMPTY);
                }
                setItems(stack, list);
            }
        }
    }
}