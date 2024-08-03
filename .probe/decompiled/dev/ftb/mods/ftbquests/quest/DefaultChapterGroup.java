package dev.ftb.mods.ftbquests.quest;

import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DefaultChapterGroup extends ChapterGroup {

    public DefaultChapterGroup(BaseQuestFile f) {
        super(0L, f);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getAltTitle() {
        return this.file.getTitle();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        return this.file.getIcon();
    }
}