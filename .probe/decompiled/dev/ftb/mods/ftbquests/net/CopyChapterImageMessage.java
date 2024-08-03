package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.ChapterImage;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import net.minecraft.network.FriendlyByteBuf;

public class CopyChapterImageMessage extends BaseC2SMessage {

    private final ChapterImage img;

    private final long chapterId;

    public CopyChapterImageMessage(ChapterImage toCopy, Chapter chapter, double newX, double newY) {
        this.img = toCopy.copy(chapter, newX, newY);
        this.chapterId = chapter.id;
    }

    public CopyChapterImageMessage(FriendlyByteBuf buf) {
        this.chapterId = buf.readLong();
        this.img = new ChapterImage(ServerQuestFile.INSTANCE.getChapter(this.chapterId));
        this.img.readNetData(buf);
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.COPY_CHAPTER_IMAGE;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeLong(this.chapterId);
        this.img.writeNetData(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Chapter chapter = this.img.getChapter();
        chapter.addImage(this.img);
        chapter.file.markDirty();
        new EditObjectResponseMessage(chapter).sendToAll(context.getPlayer().m_20194_());
    }
}