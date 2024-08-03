package noppes.npcs.api.entity.data;

import noppes.npcs.api.IContainer;
import noppes.npcs.api.handler.data.IQuest;

public interface IPlayerMail {

    String getSender();

    void setSender(String var1);

    String getSubject();

    void setSubject(String var1);

    String[] getText();

    void setText(String[] var1);

    IQuest getQuest();

    void setQuest(int var1);

    IContainer getContainer();
}