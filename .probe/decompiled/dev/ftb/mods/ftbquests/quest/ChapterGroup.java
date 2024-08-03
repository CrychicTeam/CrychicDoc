package dev.ftb.mods.ftbquests.quest;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.IconAnimation;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.events.QuestProgressEventData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class ChapterGroup extends QuestObject {

    protected final BaseQuestFile file;

    private final List<Chapter> chapters;

    private boolean guiCollapsed;

    public ChapterGroup(long id, BaseQuestFile file) {
        super(id);
        this.file = file;
        this.chapters = new ArrayList();
        this.guiCollapsed = false;
    }

    public BaseQuestFile getFile() {
        return this.file;
    }

    public List<Chapter> getChapters() {
        return Collections.unmodifiableList(this.chapters);
    }

    public void addChapter(Chapter chapter) {
        this.chapters.add(chapter);
        chapter.setGroup(this);
    }

    public void removeChapter(Chapter chapter) {
        this.chapters.remove(chapter);
    }

    public void clearChapters() {
        this.chapters.clear();
    }

    public void sortChapters(Comparator<? super Chapter> c) {
        this.chapters.sort(c);
    }

    @Override
    public QuestObjectType getObjectType() {
        return QuestObjectType.CHAPTER_GROUP;
    }

    @Override
    public BaseQuestFile getQuestFile() {
        return this.file;
    }

    public boolean isFirstGroup() {
        return !this.file.chapterGroups.isEmpty() && this == this.file.chapterGroups.get(0);
    }

    public boolean isLastGroup() {
        return !this.file.chapterGroups.isEmpty() && this == this.file.chapterGroups.get(this.file.chapterGroups.size() - 1);
    }

    public boolean isDefaultGroup() {
        return this == this.file.getDefaultChapterGroup();
    }

    @Override
    public void onCreated() {
        this.file.chapterGroups.add(this);
    }

    @Override
    public void clearCachedData() {
        super.clearCachedData();
        for (Chapter chapter : this.chapters) {
            chapter.clearCachedData();
        }
    }

    @Override
    public void deleteSelf() {
        this.file.chapterGroups.remove(this);
        for (Chapter chapter : this.chapters) {
            this.file.getDefaultChapterGroup().addChapter(chapter);
        }
        super.deleteSelf();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        config.addString("title", this.rawTitle, v -> this.rawTitle = v, "").setNameKey("ftbquests.title").setOrder(-127);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void editedFromGUI() {
        QuestScreen gui = ClientUtils.getCurrentGuiAs(QuestScreen.class);
        if (gui != null) {
            gui.refreshChapterPanel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getAltTitle() {
        return Component.literal("Unnamed Group");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        List<Icon> list = new ArrayList();
        for (Chapter chapter : this.chapters) {
            list.add(chapter.getIcon());
        }
        return IconAnimation.fromList(list, false);
    }

    @Override
    public boolean isVisible(TeamData data) {
        return this.chapters.stream().anyMatch(chapter -> chapter.isVisible(data));
    }

    @Override
    public int getRelativeProgressFromChildren(TeamData data) {
        if (this.chapters.isEmpty()) {
            return 100;
        } else {
            int progress = 0;
            for (Chapter chapter : this.chapters) {
                progress += data.getRelativeProgress(chapter);
            }
            return getRelativeProgressFromChildren(progress, this.chapters.size());
        }
    }

    @Override
    public void onCompleted(QuestProgressEventData<?> data) {
        data.setCompleted(this.id);
        if (this.file.isCompletedRaw(data.getTeamData())) {
            this.file.onCompleted(data.withObject(this.file));
        }
    }

    public List<Chapter> getVisibleChapters(TeamData data) {
        return this.file.canEdit() ? this.chapters : this.chapters.stream().filter(chapter -> chapter.hasAnyVisibleChildren() && chapter.isVisible(data)).toList();
    }

    @Nullable
    public Chapter getFirstVisibleChapter(TeamData data) {
        if (this.chapters.isEmpty()) {
            return null;
        } else {
            return this.file.canEdit() ? (Chapter) this.chapters.get(0) : (Chapter) this.chapters.stream().filter(chapter -> !chapter.getQuests().isEmpty() && chapter.isVisible(data)).findFirst().orElse(null);
        }
    }

    @Override
    public Collection<? extends QuestObject> getChildren() {
        return this.chapters;
    }

    @Override
    public boolean hasUnclaimedRewardsRaw(TeamData teamData, UUID player) {
        return this.chapters.stream().anyMatch(chapter -> teamData.hasUnclaimedRewards(player, chapter));
    }

    public boolean moveChapterWithinGroup(Chapter chapter, boolean movingUp) {
        int index = this.chapters.indexOf(chapter);
        if (index != -1 && movingUp ? index > 0 : index < this.chapters.size() - 1) {
            this.chapters.remove(index);
            this.chapters.add(movingUp ? index - 1 : index + 1, chapter);
            return true;
        } else {
            return false;
        }
    }

    public void toggleCollapsed() {
        this.guiCollapsed = !this.guiCollapsed;
    }

    public boolean isGuiCollapsed() {
        return this.guiCollapsed;
    }
}