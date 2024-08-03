package dev.ftb.mods.ftbquests.quest.task;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.Tristate;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.IconAnimation;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.math.Bits;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.api.FTBQuestsTags;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.client.gui.CustomToast;
import dev.ftb.mods.ftbquests.client.gui.quests.ValidItemsScreen;
import dev.ftb.mods.ftbquests.integration.item_filtering.ItemMatchingSystem;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbquests.item.MissingItem;
import dev.ftb.mods.ftbquests.net.FTBQuestsNetHandler;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.util.NBTUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemTask extends Task implements Predicate<ItemStack> {

    @Deprecated(forRemoval = true)
    public static final TagKey<Item> CHECK_NBT_ITEM_FILTERS = TagKey.create(Registries.ITEM, new ResourceLocation("itemfilters", "check_nbt"));

    private ItemStack itemStack = ItemStack.EMPTY;

    private long count = 1L;

    private Tristate consumeItems = Tristate.DEFAULT;

    private Tristate onlyFromCrafting = Tristate.DEFAULT;

    private Tristate matchNBT = Tristate.DEFAULT;

    private boolean weakNBTmatch = false;

    private boolean taskScreenOnly = false;

    public ItemTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return TaskTypes.ITEM;
    }

    @Override
    public long getMaxProgress() {
        return this.count;
    }

    public ItemTask setStackAndCount(ItemStack stack, int count) {
        this.itemStack = stack.copy();
        this.count = (long) count;
        return this;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setConsumeItems(Tristate consumeItems) {
        this.consumeItems = consumeItems;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        NBTUtils.write(nbt, "item", this.itemStack);
        if (this.count > 1L) {
            nbt.putLong("count", this.count);
        }
        this.consumeItems.write(nbt, "consume_items");
        this.onlyFromCrafting.write(nbt, "only_from_crafting");
        this.matchNBT.write(nbt, "match_nbt");
        if (this.weakNBTmatch) {
            nbt.putBoolean("weak_nbt_match", true);
        }
        if (this.taskScreenOnly) {
            nbt.putBoolean("task_screen_only", true);
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.itemStack = NBTUtils.read(nbt, "item");
        this.count = Math.max(nbt.getLong("count"), 1L);
        this.consumeItems = Tristate.read(nbt, "consume_items");
        this.onlyFromCrafting = Tristate.read(nbt, "only_from_crafting");
        this.matchNBT = Tristate.read(nbt, "match_nbt");
        this.weakNBTmatch = nbt.getBoolean("weak_nbt_match");
        this.taskScreenOnly = nbt.getBoolean("task_screen_only");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        int flags = 0;
        flags = Bits.setFlag(flags, 1, this.count > 1L);
        flags = Bits.setFlag(flags, 2, this.consumeItems != Tristate.DEFAULT);
        flags = Bits.setFlag(flags, 4, this.consumeItems == Tristate.TRUE);
        flags = Bits.setFlag(flags, 8, this.onlyFromCrafting != Tristate.DEFAULT);
        flags = Bits.setFlag(flags, 16, this.onlyFromCrafting == Tristate.TRUE);
        flags = Bits.setFlag(flags, 32, this.matchNBT != Tristate.DEFAULT);
        flags = Bits.setFlag(flags, 64, this.matchNBT == Tristate.TRUE);
        flags = Bits.setFlag(flags, 128, this.weakNBTmatch);
        flags = Bits.setFlag(flags, 256, this.taskScreenOnly);
        buffer.writeVarInt(flags);
        FTBQuestsNetHandler.writeItemType(buffer, this.itemStack);
        if (this.count > 1L) {
            buffer.writeVarLong(this.count);
        }
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        int flags = buffer.readVarInt();
        this.itemStack = FTBQuestsNetHandler.readItemType(buffer);
        this.count = Bits.getFlag(flags, 1) ? buffer.readVarLong() : 1L;
        this.consumeItems = Bits.getFlag(flags, 2) ? (Bits.getFlag(flags, 4) ? Tristate.TRUE : Tristate.FALSE) : Tristate.DEFAULT;
        this.onlyFromCrafting = Bits.getFlag(flags, 8) ? (Bits.getFlag(flags, 16) ? Tristate.TRUE : Tristate.FALSE) : Tristate.DEFAULT;
        this.matchNBT = Bits.getFlag(flags, 32) ? (Bits.getFlag(flags, 64) ? Tristate.TRUE : Tristate.FALSE) : Tristate.DEFAULT;
        this.weakNBTmatch = Bits.getFlag(flags, 128);
        this.taskScreenOnly = Bits.getFlag(flags, 256);
    }

    public List<ItemStack> getValidDisplayItems() {
        return ItemMatchingSystem.INSTANCE.getAllMatchingStacks(this.itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return this.count > 1L ? Component.literal(this.count + "x ").append(this.itemStack.getHoverName()) : Component.literal("").append(this.itemStack.getHoverName());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        List<Icon> icons = new ArrayList();
        for (ItemStack stack : this.getValidDisplayItems()) {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            Icon icon = ItemIcon.getItemIcon(copy);
            if (!icon.isEmpty()) {
                icons.add(icon);
            }
        }
        return icons.isEmpty() ? ItemIcon.getItemIcon((Item) FTBQuestsItems.MISSING_ITEM.get()) : IconAnimation.fromList(icons, false);
    }

    public boolean test(ItemStack stack) {
        return this.itemStack.isEmpty() ? true : ItemMatchingSystem.INSTANCE.doesItemMatch(this.itemStack, stack, this.shouldMatchNBT(), this.weakNBTmatch);
    }

    private boolean shouldMatchNBT() {
        return switch(this.matchNBT) {
            case TRUE ->
                true;
            case FALSE ->
                false;
            case DEFAULT ->
                this.hasNBTCheckTag();
        };
    }

    private boolean hasNBTCheckTag() {
        Holder.Reference<Item> itemReference = this.itemStack.getItem().builtInRegistryHolder();
        return itemReference.is(FTBQuestsTags.Items.CHECK_NBT) || itemReference.is(CHECK_NBT_ITEM_FILTERS);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addItemStack("item", this.itemStack, v -> this.itemStack = v, ItemStack.EMPTY, true, false).setNameKey("ftbquests.task.ftbquests.item");
        config.addLong("count", this.count, v -> this.count = v, 1L, 1L, Long.MAX_VALUE);
        config.addEnum("consume_items", this.consumeItems, v -> this.consumeItems = v, Tristate.NAME_MAP);
        config.addEnum("only_from_crafting", this.onlyFromCrafting, v -> this.onlyFromCrafting = v, Tristate.NAME_MAP);
        config.addEnum("match_nbt", this.matchNBT, v -> this.matchNBT = v, Tristate.NAME_MAP);
        config.addBool("weak_nbt_match", this.weakNBTmatch, v -> this.weakNBTmatch = v, false);
        config.addBool("task_screen_only", this.taskScreenOnly, v -> this.taskScreenOnly = v, false);
    }

    @Override
    public boolean consumesResources() {
        return this.consumeItems.get(this.getQuest().getChapter().consumeItems());
    }

    @Override
    public boolean canInsertItem() {
        return this.consumesResources();
    }

    @Override
    public boolean submitItemsOnInventoryChange() {
        return !this.consumesResources();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onButtonClicked(Button button, boolean canClick) {
        button.playClickSound();
        List<ItemStack> validItems = this.getValidDisplayItems();
        if (!this.consumesResources() && validItems.size() == 1 && FTBQuests.getRecipeModHelper().isRecipeModAvailable()) {
            FTBQuests.getRecipeModHelper().showRecipes((ItemStack) validItems.get(0));
        } else if (validItems.isEmpty()) {
            Minecraft.getInstance().getToasts().addToast(new CustomToast(Component.literal("No valid items!"), ItemIcon.getItemIcon((Item) FTBQuestsItems.MISSING_ITEM.get()), Component.literal("Report this bug to modpack author!")));
        } else {
            new ValidItemsScreen(this, validItems, canClick).openGui();
        }
    }

    @Override
    public void addMouseOverHeader(TooltipList list, TeamData teamData, boolean advanced) {
        if (!this.rawTitle.isEmpty()) {
            list.add(this.getTitle());
        } else {
            ItemStack stack = this.getIcon() instanceof ItemIcon i ? i.getStack() : this.itemStack;
            List<Component> lines = stack.getTooltipLines(FTBQuestsClient.getClientPlayer(), advanced ? TooltipFlag.Default.f_256730_ : TooltipFlag.Default.f_256752_);
            if (!lines.isEmpty()) {
                lines.set(0, this.getTitle());
            } else {
                lines.add(this.getTitle());
            }
            lines.forEach(list::add);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addMouseOverText(TooltipList list, TeamData teamData) {
        if (this.taskScreenOnly) {
            list.blankLine();
            list.add(Component.translatable("ftbquests.task.task_screen_only").withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE));
        } else if (this.consumesResources() && !teamData.isCompleted(this)) {
            list.blankLine();
            list.add(Component.translatable("ftbquests.task.click_to_submit").withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE));
        } else if (this.getValidDisplayItems().size() > 1) {
            list.blankLine();
            list.add(Component.translatable("ftbquests.task.ftbquests.item.view_items").withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE));
        } else if (FTBQuests.getRecipeModHelper().isRecipeModAvailable()) {
            list.blankLine();
            list.add(Component.translatable("ftbquests.task.ftbquests.item.click_recipe").withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE));
        }
    }

    public ItemStack insert(TeamData teamData, ItemStack stack, boolean simulate) {
        if (!teamData.isCompleted(this) && this.consumesResources() && this.test(stack)) {
            long add = Math.min((long) stack.getCount(), this.count - teamData.getProgress(this));
            if (add > 0L) {
                if (!simulate && teamData.getFile().isServerSide()) {
                    teamData.addProgress(this, add);
                }
                ItemStack copy = stack.copy();
                copy.setCount((int) ((long) stack.getCount() - add));
                return copy;
            }
        }
        return stack;
    }

    @Override
    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
        if (!this.taskScreenOnly && this.checkTaskSequence(teamData) && !teamData.isCompleted(this) && !(this.itemStack.getItem() instanceof MissingItem) && !(craftedItem.getItem() instanceof MissingItem)) {
            if (!this.consumesResources()) {
                if (this.onlyFromCrafting.get(false)) {
                    if (!craftedItem.isEmpty() && this.test(craftedItem)) {
                        teamData.addProgress(this, (long) craftedItem.getCount());
                    }
                } else {
                    long c = Math.min(this.count, player.m_150109_().items.stream().filter(this).mapToLong(ItemStack::m_41613_).sum());
                    if (c > teamData.getProgress(this)) {
                        teamData.setProgress(this, c);
                    }
                }
            } else if (craftedItem.isEmpty()) {
                boolean changed = false;
                for (int i = 0; i < player.m_150109_().items.size(); i++) {
                    ItemStack stack = player.m_150109_().items.get(i);
                    ItemStack stack1 = this.insert(teamData, stack, false);
                    if (stack != stack1) {
                        changed = true;
                        player.m_150109_().items.set(i, stack1.isEmpty() ? ItemStack.EMPTY : stack1);
                    }
                }
                if (changed) {
                    player.m_150109_().setChanged();
                    player.f_36096_.broadcastChanges();
                }
            }
        }
    }

    public boolean isTaskScreenOnly() {
        return this.taskScreenOnly;
    }

    public boolean isOnlyFromCrafting() {
        return this.onlyFromCrafting.get(false);
    }
}