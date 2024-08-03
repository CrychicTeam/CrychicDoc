package noppes.npcs.api.handler.data;

import net.minecraft.network.chat.Component;

public interface IQuestObjective {

    int getProgress();

    void setProgress(int var1);

    int getMaxProgress();

    boolean isCompleted();

    String getText();

    Component getMCText();
}