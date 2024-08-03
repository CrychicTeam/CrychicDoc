package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.Tristate;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.integration.RecipeModHelper;
import dev.ftb.mods.ftbquests.net.ClaimRewardMessage;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.util.ProgressChange;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

public abstract class Reward extends QuestObjectBase {

    protected final Quest quest;

    private Tristate team;

    protected RewardAutoClaim autoclaim;

    private boolean excludeFromClaimAll;

    private boolean ignoreRewardBlocking;

    public Reward(long id, Quest q) {
        super(id);
        this.quest = q;
        this.team = Tristate.DEFAULT;
        this.autoclaim = RewardAutoClaim.DEFAULT;
        this.excludeFromClaimAll = this.getType().getExcludeFromListRewards();
        this.ignoreRewardBlocking = false;
    }

    public Quest getQuest() {
        return this.quest;
    }

    @Override
    public final QuestObjectType getObjectType() {
        return QuestObjectType.REWARD;
    }

    @Override
    public final BaseQuestFile getQuestFile() {
        return this.quest.getChapter().file;
    }

    @Nullable
    @Override
    public final Chapter getQuestChapter() {
        return this.quest.getChapter();
    }

    @Override
    public final long getParentID() {
        return this.quest.id;
    }

    public abstract RewardType getType();

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        if (this.team != Tristate.DEFAULT) {
            this.team.write(nbt, "team_reward");
        }
        if (this.autoclaim != RewardAutoClaim.DEFAULT) {
            nbt.putString("auto", this.autoclaim.id);
        }
        if (this.excludeFromClaimAll) {
            nbt.putBoolean("exclude_from_claim_all", true);
        }
        if (this.ignoreRewardBlocking) {
            nbt.putBoolean("ignore_reward_blocking", true);
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.team = Tristate.read(nbt, "team_reward");
        this.autoclaim = RewardAutoClaim.NAME_MAP.get(nbt.getString("auto"));
        this.excludeFromClaimAll = nbt.getBoolean("exclude_from_claim_all");
        this.ignoreRewardBlocking = nbt.getBoolean("ignore_reward_blocking");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        Tristate.NAME_MAP.write(buffer, this.team);
        RewardAutoClaim.NAME_MAP.write(buffer, this.autoclaim);
        buffer.writeBoolean(this.excludeFromClaimAll);
        buffer.writeBoolean(this.ignoreRewardBlocking);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.team = Tristate.NAME_MAP.read(buffer);
        this.autoclaim = RewardAutoClaim.NAME_MAP.read(buffer);
        this.excludeFromClaimAll = buffer.readBoolean();
        this.ignoreRewardBlocking = buffer.readBoolean();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addEnum("team", this.team, v -> this.team = v, Tristate.NAME_MAP).setNameKey("ftbquests.reward.team_reward");
        config.addEnum("autoclaim", this.autoclaim, v -> this.autoclaim = v, RewardAutoClaim.NAME_MAP).setNameKey("ftbquests.reward.autoclaim");
        config.addBool("exclude_from_claim_all", this.getExcludeFromClaimAll(), v -> this.excludeFromClaimAll = v, this.excludeFromClaimAll).setNameKey("ftbquests.reward.exclude_from_claim_all").setCanEdit(!this.isClaimAllHardcoded());
        config.addBool("ignore_reward_blocking", this.ignoreRewardBlocking(), v -> this.ignoreRewardBlocking = v, this.ignoreRewardBlocking).setNameKey("ftbquests.quest.misc.ignore_reward_blocking").setCanEdit(!this.isIgnoreRewardBlockingHardcoded());
    }

    public abstract void claim(ServerPlayer var1, boolean var2);

    public boolean automatedClaimPre(BlockEntity blockEntity, List<ItemStack> items, RandomSource random, UUID playerId, @Nullable ServerPlayer player) {
        return player != null;
    }

    public void automatedClaimPost(BlockEntity blockEntity, UUID playerId, @Nullable ServerPlayer player) {
        if (player != null) {
            this.claim(player, false);
        }
    }

    @Override
    public final void deleteSelf() {
        this.quest.removeReward(this);
        for (TeamData data : this.getQuestFile().getAllTeamData()) {
            data.deleteReward(this);
        }
        super.deleteSelf();
    }

    @Override
    public final void deleteChildren() {
        for (TeamData data : this.getQuestFile().getAllTeamData()) {
            data.deleteReward(this);
        }
        super.deleteChildren();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void editedFromGUI() {
        QuestScreen gui = ClientUtils.getCurrentGuiAs(QuestScreen.class);
        if (gui != null) {
            gui.refreshQuestPanel();
            if (gui.isViewingQuest()) {
                gui.refreshViewQuestPanel();
            }
        }
    }

    @Override
    public void onCreated() {
        this.quest.addReward(this);
    }

    public final boolean isTeamReward() {
        return this.team.get(this.quest.getQuestFile().isDefaultPerTeamReward());
    }

    public final RewardAutoClaim getAutoClaimType() {
        if (!this.quest.getChapter().isAlwaysInvisible() || this.autoclaim != RewardAutoClaim.DEFAULT && this.autoclaim != RewardAutoClaim.DISABLED) {
            return this.autoclaim == RewardAutoClaim.DEFAULT ? this.quest.getQuestFile().getDefaultRewardAutoClaim() : this.autoclaim;
        } else {
            return RewardAutoClaim.ENABLED;
        }
    }

    @Override
    public final void forceProgress(TeamData teamData, ProgressChange progressChange) {
        if (progressChange.shouldReset()) {
            teamData.resetReward(progressChange.getPlayerId(), this);
        } else {
            teamData.claimReward(progressChange.getPlayerId(), this, progressChange.getDate().getTime());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        return this.getType().getIconSupplier();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getAltTitle() {
        return this.getType().getDisplayName();
    }

    @Override
    public final ConfigGroup createSubGroup(ConfigGroup group) {
        RewardType type = this.getType();
        return group.getOrCreateSubgroup(this.getObjectType().getId()).getOrCreateSubgroup(type.getTypeId().getNamespace()).getOrCreateSubgroup(type.getTypeId().getPath());
    }

    @OnlyIn(Dist.CLIENT)
    public void addMouseOverText(TooltipList list) {
    }

    @OnlyIn(Dist.CLIENT)
    public boolean addTitleInMouseOverText() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void onButtonClicked(Button button, boolean canClick) {
        if (canClick) {
            button.playClickSound();
            new ClaimRewardMessage(this.id, true).sendToServer();
        }
    }

    public boolean getExcludeFromClaimAll() {
        return this.excludeFromClaimAll;
    }

    public boolean isClaimAllHardcoded() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public Optional<PositionedIngredient> getIngredient(Widget widget) {
        return PositionedIngredient.of(this.getIcon().getIngredient(), widget);
    }

    @Override
    public Set<RecipeModHelper.Components> componentsToRefresh() {
        return EnumSet.of(RecipeModHelper.Components.QUESTS);
    }

    @OnlyIn(Dist.CLIENT)
    public String getButtonText() {
        return "";
    }

    public boolean ignoreRewardBlocking() {
        return this.ignoreRewardBlocking;
    }

    protected boolean isIgnoreRewardBlockingHardcoded() {
        return false;
    }
}