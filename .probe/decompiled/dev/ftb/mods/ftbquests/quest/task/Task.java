package dev.ftb.mods.ftbquests.quest.task;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.util.StringUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.events.CustomTaskEvent;
import dev.ftb.mods.ftbquests.events.ObjectCompletedEvent;
import dev.ftb.mods.ftbquests.events.ObjectStartedEvent;
import dev.ftb.mods.ftbquests.events.QuestProgressEventData;
import dev.ftb.mods.ftbquests.integration.RecipeModHelper;
import dev.ftb.mods.ftbquests.net.SubmitTaskMessage;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.util.ProgressChange;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class Task extends QuestObject {

    private final Quest quest;

    private boolean optionalTask;

    public Task(long id, Quest quest) {
        super(id);
        this.quest = quest;
        this.optionalTask = false;
    }

    public Quest getQuest() {
        return this.quest;
    }

    @Override
    public final QuestObjectType getObjectType() {
        return QuestObjectType.TASK;
    }

    @Override
    public final BaseQuestFile getQuestFile() {
        return this.quest.getChapter().file;
    }

    @Override
    public final Chapter getQuestChapter() {
        return this.quest.getChapter();
    }

    @Override
    public final long getParentID() {
        return this.quest.id;
    }

    public abstract TaskType getType();

    @Override
    public final int getRelativeProgressFromChildren(TeamData data) {
        long max = this.getMaxProgress();
        if (max <= 0L) {
            return 0;
        } else {
            long progress = data.getProgress(this);
            if (progress <= 0L) {
                return 0;
            } else {
                return progress >= max ? 100 : (int) Math.max(1.0, (double) progress * 100.0 / (double) max);
            }
        }
    }

    @Override
    public void onStarted(QuestProgressEventData<?> data) {
        data.setStarted(this.id);
        ObjectStartedEvent.TASK.invoker().act(new ObjectStartedEvent.TaskEvent(data.withObject(this)));
        this.quest.onStarted(data.withObject(this.quest));
    }

    @Override
    public final void onCompleted(QuestProgressEventData<?> data) {
        data.setCompleted(this.id);
        ObjectCompletedEvent.TASK.invoker().act(new ObjectCompletedEvent.TaskEvent(data.withObject(this)));
        boolean questCompleted = this.quest.isCompletedRaw(data.getTeamData());
        if (this.quest.getTasks().size() > 1 && !questCompleted && !this.disableToast) {
            data.notifyPlayers(this.id);
        }
        if (questCompleted) {
            this.quest.onCompleted(data.withObject(this.quest));
        }
    }

    @Override
    public boolean isOptionalForProgression() {
        return this.optionalTask;
    }

    public long getMaxProgress() {
        return 1L;
    }

    public String formatMaxProgress() {
        return StringUtils.formatDouble((double) this.getMaxProgress(), true);
    }

    public String formatProgress(TeamData teamData, long progress) {
        return StringUtils.formatDouble((double) progress, true);
    }

    @Override
    public final void forceProgress(TeamData teamData, ProgressChange progressChange) {
        teamData.setProgress(this, progressChange.shouldReset() ? 0L : this.getMaxProgress());
    }

    @Override
    public final void deleteSelf() {
        this.quest.removeTask(this);
        for (TeamData data : this.quest.getChapter().file.getAllTeamData()) {
            data.resetProgress(this);
        }
        super.deleteSelf();
    }

    @Override
    public final void deleteChildren() {
        for (TeamData data : this.quest.getChapter().file.getAllTeamData()) {
            data.resetProgress(this);
        }
        super.deleteChildren();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void editedFromGUI() {
        QuestScreen gui = ClientUtils.getCurrentGuiAs(QuestScreen.class);
        if (gui != null) {
            gui.refreshQuestPanel();
            gui.refreshViewQuestPanel();
        }
    }

    @Override
    public final void onCreated() {
        this.quest.addTask(this);
        if (this instanceof CustomTask && this.getQuestFile().isServerSide()) {
            CustomTaskEvent.EVENT.invoker().act(new CustomTaskEvent((CustomTask) this));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getAltTitle() {
        return this.getType().getDisplayName();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        return this.getType().getIconSupplier();
    }

    @Override
    public final ConfigGroup createSubGroup(ConfigGroup group) {
        TaskType type = this.getType();
        return group.getOrCreateSubgroup(this.getObjectType().getId()).getOrCreateSubgroup(type.getTypeId().getNamespace()).getOrCreateSubgroup(type.getTypeId().getPath());
    }

    @OnlyIn(Dist.CLIENT)
    public void drawGUI(TeamData teamData, GuiGraphics graphics, int x, int y, int w, int h) {
        this.getIcon().draw(graphics, x, y, w, h);
    }

    public boolean canInsertItem() {
        return false;
    }

    public boolean consumesResources() {
        return this.canInsertItem();
    }

    public boolean hideProgressNumbers() {
        return this.getMaxProgress() <= 1L;
    }

    @OnlyIn(Dist.CLIENT)
    public void addMouseOverHeader(TooltipList list, TeamData teamData, boolean advanced) {
        list.add(this.getTitle());
    }

    @OnlyIn(Dist.CLIENT)
    public void addMouseOverText(TooltipList list, TeamData teamData) {
        if (this.consumesResources()) {
            list.blankLine();
            list.add(Component.translatable("ftbquests.task.click_to_submit").withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean addTitleInMouseOverText() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void onButtonClicked(Button button, boolean canClick) {
        if (canClick && this.autoSubmitOnPlayerTick() <= 0) {
            button.playClickSound();
            new SubmitTaskMessage(this.id).sendToServer();
        }
    }

    public boolean submitItemsOnInventoryChange() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public Optional<PositionedIngredient> getIngredient(Widget widget) {
        return this.addTitleInMouseOverText() ? PositionedIngredient.of(this.getIcon().getIngredient(), widget) : Optional.empty();
    }

    @Override
    public Set<RecipeModHelper.Components> componentsToRefresh() {
        return EnumSet.of(RecipeModHelper.Components.QUESTS);
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getButtonText() {
        return this.getMaxProgress() <= 1L && !this.consumesResources() ? Component.empty() : Component.literal(this.formatMaxProgress());
    }

    public int autoSubmitOnPlayerTick() {
        return 0;
    }

    @Override
    public final boolean cacheProgress() {
        return false;
    }

    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
    }

    public final void submitTask(TeamData teamData, ServerPlayer player) {
        this.submitTask(teamData, player, ItemStack.EMPTY);
    }

    protected final boolean checkTaskSequence(TeamData teamData) {
        if (!this.quest.getRequireSequentialTasks()) {
            return true;
        } else {
            List<Task> tasks = this.quest.getTasksAsList();
            int idx = tasks.indexOf(this);
            return idx >= 0 && (idx == 0 || teamData.isCompleted((QuestObject) tasks.get(idx - 1)));
        }
    }

    public boolean checkOnLogin() {
        return !this.consumesResources();
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        if (this.optionalTask) {
            nbt.putBoolean("optional_task", true);
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.optionalTask = nbt.getBoolean("optional_task");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeBoolean(this.optionalTask);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.optionalTask = buffer.readBoolean();
    }

    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addBool("optional_task", this.optionalTask, v -> this.optionalTask = v, false).setNameKey("ftbquests.quest.misc.optional");
    }

    protected ResourceLocation safeResourceLocation(String str, ResourceLocation fallback) {
        try {
            return new ResourceLocation(str);
        } catch (ResourceLocationException var4) {
            if (this.getQuestFile().isServerSide()) {
                FTBQuests.LOGGER.warn("Ignoring bad resource location '{}' for task {}", str, this.id);
            } else {
                FTBQuestsClient.getClientPlayer().displayClientMessage(Component.literal("Bad resource location: " + str).withStyle(ChatFormatting.RED), false);
            }
            return fallback;
        }
    }
}