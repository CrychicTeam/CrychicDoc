package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import dev.ftb.mods.ftbquests.util.ConfigQuestObject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class RandomReward extends Reward {

    private RewardTable table = null;

    public RandomReward(long id, Quest parent) {
        super(id, parent);
    }

    @Override
    public RewardType getType() {
        return RewardTypes.RANDOM;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        if (this.getTable() != null) {
            nbt.putLong("table_id", this.table.id);
            if (this.table.id == -1L) {
                SNBTCompoundTag tag = new SNBTCompoundTag();
                this.table.writeData(tag);
                nbt.put("table_data", tag);
            }
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.table = null;
        BaseQuestFile file = this.getQuestFile();
        long id = nbt.getLong("table_id");
        if (id != 0L) {
            this.table = file.getRewardTable(id);
        }
        if (this.table == null && nbt.contains("table_data")) {
            this.table = new RewardTable(-1L, file);
            this.table.readData(nbt.getCompound("table_data"));
            this.table.setRawTitle("Internal");
        }
    }

    @Nullable
    public RewardTable getTable() {
        if (this.table != null && !this.table.isValid()) {
            this.table = null;
        }
        return this.table;
    }

    public void setTable(RewardTable table) {
        this.table = table;
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        RewardTable table = this.getTable();
        buffer.writeLong(table == null ? 0L : table.id);
        if (table != null && table.id == -1L) {
            table.writeNetData(buffer);
        }
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        BaseQuestFile file = this.getQuestFile();
        long t = buffer.readLong();
        if (t == -1L) {
            this.table = new RewardTable(-1L, file);
            this.table.readNetData(buffer);
            this.table.setRawTitle("Internal");
        } else {
            this.table = file.getRewardTable(t);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.add("table", new ConfigQuestObject(QuestObjectType.REWARD_TABLE), this.table, v -> this.table = v, this.getTable()).setNameKey("ftbquests.reward_table");
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
        RewardTable table = this.getTable();
        if (table != null) {
            for (WeightedReward wr : table.generateWeightedRandomRewards(player.m_217043_(), 1, false)) {
                wr.getReward().claim(player, notify);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getAltTitle() {
        return this.getTable() == null ? super.getAltTitle() : this.getTable().getTitleOrElse(super.getAltTitle());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        return this.getTable() == null ? super.getAltIcon() : this.getTable().getIcon();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addMouseOverText(TooltipList list) {
        if (this.getTable() != null) {
            this.getTable().addMouseOverText(list, true, false);
        }
    }

    @Override
    public boolean getExcludeFromClaimAll() {
        return false;
    }

    @Override
    public boolean isClaimAllHardcoded() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Optional<PositionedIngredient> getIngredient(Widget widget) {
        return this.getTable() != null && this.getTable().getLootCrate() != null ? PositionedIngredient.of(this.getTable().getLootCrate().createStack(), widget) : Optional.empty();
    }

    @Override
    public boolean automatedClaimPre(BlockEntity blockEntity, List<ItemStack> items, RandomSource random, UUID playerId, @Nullable ServerPlayer player) {
        return false;
    }

    @Override
    public void automatedClaimPost(BlockEntity blockEntity, UUID playerId, @Nullable ServerPlayer player) {
    }
}