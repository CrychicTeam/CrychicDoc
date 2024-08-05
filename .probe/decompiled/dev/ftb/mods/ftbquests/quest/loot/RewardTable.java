package dev.ftb.mods.ftbquests.quest.loot;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.IconAnimation;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.math.Bits;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.client.gui.EditRewardTableScreen;
import dev.ftb.mods.ftbquests.client.gui.RewardTablesScreen;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.integration.RecipeModHelper;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import dev.ftb.mods.ftbquests.quest.reward.ItemReward;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import dev.ftb.mods.ftbquests.quest.reward.RewardTypes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public final class RewardTable extends QuestObjectBase {

    private final BaseQuestFile file;

    private final List<WeightedReward> weightedRewards;

    private final Quest fakeQuest;

    private float emptyWeight;

    private int lootSize;

    private boolean hideTooltip;

    private boolean useTitle;

    private LootCrate lootCrate;

    private ResourceLocation lootTableId;

    private String filename;

    public RewardTable(long id, BaseQuestFile file) {
        this(id, file, "");
    }

    public RewardTable(long id, BaseQuestFile file, String filename) {
        super(id);
        this.file = file;
        this.filename = filename;
        this.weightedRewards = new ArrayList();
        this.fakeQuest = new Quest(-1L, new Chapter(-1L, this.file, file.getDefaultChapterGroup()));
        this.emptyWeight = 0.0F;
        this.lootSize = 1;
        this.hideTooltip = false;
        this.useTitle = false;
        this.lootCrate = null;
        this.lootTableId = null;
    }

    public Component getTitleOrElse(Component def) {
        return this.useTitle ? this.getTitle() : def;
    }

    public BaseQuestFile getFile() {
        return this.file;
    }

    public List<WeightedReward> getWeightedRewards() {
        return Collections.unmodifiableList(this.weightedRewards);
    }

    @Nullable
    public LootCrate getLootCrate() {
        return this.lootCrate;
    }

    public Quest getFakeQuest() {
        return this.fakeQuest;
    }

    @Override
    public QuestObjectType getObjectType() {
        return QuestObjectType.REWARD_TABLE;
    }

    @Override
    public BaseQuestFile getQuestFile() {
        return this.file;
    }

    public float getTotalWeight(boolean includeEmpty) {
        float initial = includeEmpty ? this.emptyWeight : 0.0F;
        return (Float) this.weightedRewards.stream().map(WeightedReward::getWeight).reduce(initial, Float::sum);
    }

    public Collection<WeightedReward> generateWeightedRandomRewards(RandomSource random, int nAttempts, boolean includeEmpty) {
        float total = this.getTotalWeight(includeEmpty);
        if (total <= 0.0F) {
            return List.of();
        } else {
            List<WeightedReward> res = (List<WeightedReward>) this.weightedRewards.stream().filter(rewardx -> rewardx.getWeight() == 0.0F).collect(Collectors.toCollection(ArrayList::new));
            nAttempts *= this.lootSize;
            for (int i = 0; i < nAttempts; i++) {
                float threshold = random.nextFloat() * total;
                float currentWeight = includeEmpty ? this.emptyWeight : 0.0F;
                if (currentWeight < threshold) {
                    for (WeightedReward reward : this.weightedRewards) {
                        currentWeight += reward.getWeight();
                        if (currentWeight >= threshold) {
                            res.add(reward);
                            break;
                        }
                    }
                }
            }
            return res;
        }
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        if (this.emptyWeight > 0.0F) {
            nbt.putFloat("empty_weight", this.emptyWeight);
        }
        nbt.putInt("loot_size", this.lootSize);
        if (this.hideTooltip) {
            nbt.putBoolean("hide_tooltip", true);
        }
        if (this.useTitle) {
            nbt.putBoolean("use_title", true);
        }
        ListTag list = new ListTag();
        for (WeightedReward wr : this.weightedRewards) {
            SNBTCompoundTag nbt1 = new SNBTCompoundTag();
            wr.getReward().writeData(nbt1);
            if (wr.getReward().getType() != RewardTypes.ITEM) {
                nbt1.m_128359_("type", wr.getReward().getType().getTypeForNBT());
            } else if (nbt1.m_128435_("item") == 8) {
                nbt1.singleLine();
            }
            if (wr.getWeight() != 1.0F) {
                nbt1.m_128350_("weight", wr.getWeight());
            }
            list.add(nbt1);
        }
        nbt.put("rewards", list);
        if (this.lootCrate != null) {
            CompoundTag nbt1x = new CompoundTag();
            this.lootCrate.writeData(nbt1x);
            nbt.put("loot_crate", nbt1x);
        }
        if (this.lootTableId != null) {
            nbt.putString("loot_table_id", this.lootTableId.toString());
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.emptyWeight = nbt.getFloat("empty_weight");
        this.lootSize = nbt.getInt("loot_size");
        this.hideTooltip = nbt.getBoolean("hide_tooltip");
        this.useTitle = nbt.getBoolean("use_title");
        this.weightedRewards.clear();
        ListTag list = nbt.getList("rewards", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag nbt1 = list.getCompound(i);
            Reward reward = RewardType.createReward(0L, this.fakeQuest, nbt1.getString("type"));
            if (reward != null) {
                reward.readData(nbt1);
                this.weightedRewards.add(new WeightedReward(reward, nbt1.contains("weight") ? nbt1.getFloat("weight") : 1.0F));
            }
        }
        this.lootCrate = null;
        if (nbt.contains("loot_crate")) {
            this.lootCrate = new LootCrate(this, false);
            this.lootCrate.readData(nbt.getCompound("loot_crate"));
        }
        this.lootTableId = nbt.contains("loot_table_id") ? new ResourceLocation(nbt.getString("loot_table_id")) : null;
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(this.filename, 32767);
        buffer.writeFloat(this.emptyWeight);
        buffer.writeVarInt(this.lootSize);
        int flags = 0;
        flags = Bits.setFlag(flags, 1, this.hideTooltip);
        flags = Bits.setFlag(flags, 2, this.useTitle);
        flags = Bits.setFlag(flags, 4, this.lootCrate != null);
        flags = Bits.setFlag(flags, 8, this.lootTableId != null);
        buffer.writeVarInt(flags);
        buffer.writeVarInt(this.weightedRewards.size());
        for (WeightedReward wr : this.weightedRewards) {
            buffer.writeVarInt(wr.getReward().getType().intId);
            wr.getReward().writeNetData(buffer);
            buffer.writeFloat(wr.getWeight());
        }
        if (this.lootCrate != null) {
            this.lootCrate.writeNetData(buffer);
        }
        if (this.lootTableId != null) {
            buffer.writeResourceLocation(this.lootTableId);
        }
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.filename = buffer.readUtf(32767);
        this.emptyWeight = buffer.readFloat();
        this.lootSize = buffer.readVarInt();
        int flags = buffer.readVarInt();
        this.hideTooltip = Bits.getFlag(flags, 1);
        this.useTitle = Bits.getFlag(flags, 2);
        boolean hasCrate = Bits.getFlag(flags, 4);
        boolean hasLootTableId = Bits.getFlag(flags, 8);
        this.weightedRewards.clear();
        int s = buffer.readVarInt();
        for (int i = 0; i < s; i++) {
            RewardType type = this.file.getRewardType(buffer.readVarInt());
            Reward reward = type.createReward(0L, this.fakeQuest);
            reward.readNetData(buffer);
            float weight = buffer.readFloat();
            this.weightedRewards.add(new WeightedReward(reward, weight));
        }
        this.lootCrate = null;
        if (hasCrate) {
            this.lootCrate = new LootCrate(this, false);
            this.lootCrate.readNetData(buffer);
        }
        this.lootTableId = hasLootTableId ? buffer.readResourceLocation() : null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addDouble("empty_weight", (double) this.emptyWeight, v -> this.emptyWeight = v.floatValue(), 0.0, 0.0, 2.147483647E9);
        config.addInt("loot_size", this.lootSize, v -> this.lootSize = v, 1, 1, Integer.MAX_VALUE);
        config.addBool("hide_tooltip", this.hideTooltip, v -> this.hideTooltip = v, false);
        config.addBool("use_title", this.useTitle, v -> this.useTitle = v, false);
        if (this.lootCrate != null) {
            this.lootCrate.fillConfigGroup(config.getOrCreateSubgroup("loot_crate").setNameKey("item.ftbquests.lootcrate"));
        }
    }

    @Override
    public void clearCachedData() {
        super.clearCachedData();
        this.weightedRewards.forEach(reward -> reward.getReward().clearCachedData());
    }

    @Override
    public void deleteSelf() {
        this.file.removeRewardTable(this);
        super.deleteSelf();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void editedFromGUI() {
        QuestScreen gui = ClientUtils.getCurrentGuiAs(QuestScreen.class);
        if (gui != null && gui.isViewingQuest()) {
            gui.refreshViewQuestPanel();
        } else {
            RewardTablesScreen gui1 = ClientUtils.getCurrentGuiAs(RewardTablesScreen.class);
            if (gui1 != null) {
                gui1.refreshWidgets();
            }
        }
        this.file.updateLootCrates();
    }

    @Override
    public void editedFromGUIOnServer() {
        this.file.updateLootCrates();
    }

    @Override
    public void onCreated() {
        if (this.filename.isEmpty()) {
            this.filename = this.file.generateRewardTableName((String) titleToID(this.rawTitle).orElse(this.toString()));
        }
        this.file.addRewardTable(this);
    }

    public String getFilename() {
        if (this.filename.isEmpty()) {
            this.filename = getCodeString(this);
        }
        return this.filename;
    }

    @Override
    public Optional<String> getPath() {
        return Optional.of("reward_tables/" + this.getFilename() + ".snbt");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getAltTitle() {
        return (Component) (this.weightedRewards.size() == 1 ? ((WeightedReward) this.weightedRewards.get(0)).getReward().getTitle() : Component.translatable("ftbquests.reward_table"));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        if (this.lootCrate != null) {
            return ItemIcon.getItemIcon(this.lootCrate.createStack());
        } else if (this.weightedRewards.isEmpty()) {
            return Icons.DICE;
        } else {
            List<Icon> icons = (List<Icon>) this.weightedRewards.stream().map(reward -> reward.getReward().getIcon()).collect(Collectors.toList());
            return IconAnimation.fromList(icons, false);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onEditButtonClicked(Runnable gui) {
        new EditRewardTableScreen(gui, this, editedReward -> {
            new EditObjectMessage(editedReward).sendToServer();
            this.clearCachedData();
        }).openGui();
    }

    public void addMouseOverText(TooltipList list, boolean includeWeight, boolean includeEmpty) {
        if (ClientQuestFile.INSTANCE.canEdit() || !this.hideTooltip) {
            float totalWeight = this.getTotalWeight(includeEmpty);
            if (includeWeight && includeEmpty && this.emptyWeight > 0.0F) {
                addItem(list, Component.translatable("ftbquests.reward_table.nothing"), this.emptyWeight, totalWeight);
            }
            List<WeightedReward> sortedRewards = this.weightedRewards.stream().sorted().toList();
            BaseScreen gui = ClientUtils.getCurrentGuiAs(BaseScreen.class);
            int maxLines = gui == null ? 12 : (gui.height - 20) / (gui.getTheme().getFontHeight() + 2);
            int nRewards = sortedRewards.size();
            int start = nRewards > maxLines ? (int) (FTBQuestsClient.getClientLevel().getGameTime() / 10L % (long) nRewards) : 0;
            int nLines = Math.min(maxLines, nRewards);
            for (int idx = 0; idx < nLines; idx++) {
                WeightedReward wr = (WeightedReward) sortedRewards.get((start + idx) % nRewards);
                if (includeWeight) {
                    addItem(list, wr.getReward().getTitle(), wr.getWeight(), totalWeight);
                } else {
                    list.add(Component.literal("- ").withStyle(ChatFormatting.GRAY).append(wr.getReward().getTitle()));
                }
            }
        }
    }

    private static void addItem(TooltipList list, Component text, float weight, float totalWeight) {
        list.add(Component.literal("- ").withStyle(ChatFormatting.GRAY).append(text).append(Component.literal(" [" + WeightedReward.chanceString(weight, totalWeight) + "]").withStyle(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public Set<RecipeModHelper.Components> componentsToRefresh() {
        return EnumSet.of(RecipeModHelper.Components.LOOT_CRATES);
    }

    public void addReward(WeightedReward weightedReward) {
        this.weightedRewards.add(weightedReward);
    }

    public void removeReward(WeightedReward weightedReward) {
        this.weightedRewards.remove(weightedReward);
    }

    public WeightedReward makeWeightedItemReward(ItemStack stack, float weight) {
        return new WeightedReward(new ItemReward(0L, this.fakeQuest, stack), weight);
    }

    public LootCrate toggleLootCrate() {
        if (this.lootCrate == null) {
            this.lootCrate = new LootCrate(this, true);
        } else {
            this.lootCrate = null;
        }
        return this.lootCrate;
    }

    public boolean shouldShowTooltip() {
        return !this.hideTooltip;
    }
}