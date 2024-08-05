package dev.ftb.mods.ftbquests.quest;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface Movable {

    long getMovableID();

    Chapter getChapter();

    double getX();

    double getY();

    double getWidth();

    double getHeight();

    String getShape();

    @OnlyIn(Dist.CLIENT)
    void move(Chapter var1, double var2, double var4);

    void onMoved(double var1, double var3, long var5);

    void copyToClipboard();

    Component getTitle();

    @OnlyIn(Dist.CLIENT)
    default void drawMoved(GuiGraphics graphics) {
        QuestShape.get(this.getShape()).getShape().withColor(Color4I.WHITE.withAlpha(30)).draw(graphics, 0, 0, 1, 1);
    }
}