package noppes.npcs.api.handler.data;

import java.util.List;

public interface IDialog {

    int getId();

    String getName();

    void setName(String var1);

    String getText();

    void setText(String var1);

    IQuest getQuest();

    void setQuest(IQuest var1);

    String getCommand();

    void setCommand(String var1);

    List<IDialogOption> getOptions();

    IDialogOption getOption(int var1);

    IAvailability getAvailability();

    IDialogCategory getCategory();

    void save();
}