package dev.ftb.mods.ftbquests.quest;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.net.MoveMovableMessage;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class QuestLink extends QuestObject implements Movable {

    private Chapter chapter;

    private long linkId;

    private double x;

    private double y;

    private String shape;

    private double size;

    public QuestLink(long id, Chapter chapter, long linkId) {
        super(id);
        this.chapter = chapter;
        this.linkId = linkId;
        this.shape = "";
        this.size = 1.0;
    }

    @Override
    public QuestObjectType getObjectType() {
        return QuestObjectType.QUEST_LINK;
    }

    @Override
    public BaseQuestFile getQuestFile() {
        return this.chapter.file;
    }

    @Override
    public Component getAltTitle() {
        return (Component) this.getQuest().map(Quest::getAltTitle).orElse(Component.empty());
    }

    @Override
    public Icon getAltIcon() {
        return (Icon) this.getQuest().map(Quest::getAltIcon).orElse(null);
    }

    @Override
    public boolean isVisible(TeamData data) {
        return (Boolean) this.getQuest().map(q -> q.isVisible(data)).orElse(false);
    }

    @Override
    public int getRelativeProgressFromChildren(TeamData data) {
        return 0;
    }

    public Optional<Quest> getQuest() {
        return this.chapter.file.get(this.linkId) instanceof Quest q ? Optional.of(q) : Optional.empty();
    }

    @Override
    public long getParentID() {
        return this.chapter.id;
    }

    @Override
    public void onCreated() {
        this.chapter.addQuestLink(this);
    }

    @Override
    public void deleteSelf() {
        super.deleteSelf();
        this.chapter.removeQuestLink(this);
    }

    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addEnum("shape", this.shape.isEmpty() ? "default" : this.shape, v -> this.shape = v.equals("default") ? "" : v, QuestShape.idMapWithDefault);
        config.addDouble("size", this.size, v -> this.size = v, 1.0, 0.0625, 8.0);
        config.addDouble("x", this.x, v -> this.x = v, 0.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        config.addDouble("y", this.y, v -> this.y = v, 0.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
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
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("linked_quest", getCodeString(this.linkId));
        nbt.putDouble("x", this.x);
        nbt.putDouble("y", this.y);
        if (!this.shape.isEmpty()) {
            nbt.putString("shape", this.shape);
        }
        if (this.size != 1.0) {
            nbt.putDouble("size", this.size);
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.linkId = Long.parseLong(nbt.getString("linked_quest"), 16);
        this.x = nbt.getDouble("x");
        this.y = nbt.getDouble("y");
        this.shape = nbt.getString("shape");
        if (this.shape.equals("default")) {
            this.shape = "";
        }
        this.size = nbt.contains("size") ? nbt.getDouble("size") : 1.0;
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeLong(this.linkId);
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.size);
        buffer.writeUtf(this.shape);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.linkId = buffer.readLong();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.size = buffer.readDouble();
        this.shape = buffer.readUtf(64);
    }

    public void setPosition(double qx, double qy) {
        this.x = qx;
        this.y = qy;
    }

    @Override
    public long getMovableID() {
        return this.id;
    }

    @Override
    public Chapter getChapter() {
        return this.chapter;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getWidth() {
        return this.size;
    }

    @Override
    public double getHeight() {
        return this.size;
    }

    @Override
    public String getShape() {
        return this.shape.isEmpty() ? this.chapter.getDefaultQuestShape() : this.shape;
    }

    @Override
    public void move(Chapter to, double x, double y) {
        new MoveMovableMessage(this, to.id, x, y).sendToServer();
    }

    @Override
    public void onMoved(double newX, double newY, long newChapterId) {
        this.x = newX;
        this.y = newY;
        if (newChapterId != this.chapter.id) {
            BaseQuestFile f = this.getQuestFile();
            Chapter newChapter = f.getChapter(newChapterId);
            if (newChapter != null) {
                this.chapter.removeQuestLink(this);
                newChapter.addQuestLink(this);
                this.chapter = newChapter;
            }
        }
    }

    @Override
    public void copyToClipboard() {
        FTBQuestsClient.copyToClipboard(this);
    }

    public boolean linksTo(Quest quest) {
        return this.linkId == quest.id;
    }
}