package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.ChapterGroup;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.util.NetUtils;
import net.minecraft.network.FriendlyByteBuf;

public class ChangeChapterGroupMessage extends BaseC2SMessage {

    private final long chapterId;

    private final long groupId;

    public ChangeChapterGroupMessage(FriendlyByteBuf buffer) {
        this.chapterId = buffer.readLong();
        this.groupId = buffer.readLong();
    }

    public ChangeChapterGroupMessage(long chapterId, long groupId) {
        this.chapterId = chapterId;
        this.groupId = groupId;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.CHANGE_CHAPTER_GROUP;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.chapterId);
        buffer.writeLong(this.groupId);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (NetUtils.canEdit(context)) {
            Chapter chapter = ServerQuestFile.INSTANCE.getChapter(this.chapterId);
            if (chapter != null) {
                ChapterGroup group = ServerQuestFile.INSTANCE.getChapterGroup(this.groupId);
                if (chapter.getGroup() != group) {
                    chapter.getGroup().removeChapter(chapter);
                    group.addChapter(chapter);
                    chapter.file.clearCachedData();
                    chapter.file.markDirty();
                    new ChangeChapterGroupResponseMessage(this.chapterId, this.groupId).sendToAll(context.getPlayer().m_20194_());
                }
            }
        }
    }
}