package noppes.npcs.api.entity.data;

import noppes.npcs.api.entity.IPlayer;

public interface INPCDisplay {

    String getName();

    void setName(String var1);

    String getTitle();

    void setTitle(String var1);

    String getSkinUrl();

    void setSkinUrl(String var1);

    String getSkinPlayer();

    void setSkinPlayer(String var1);

    String getSkinTexture();

    void setSkinTexture(String var1);

    boolean getHasLivingAnimation();

    void setHasLivingAnimation(boolean var1);

    int getVisible();

    void setVisible(int var1);

    boolean isVisibleTo(IPlayer var1);

    int getBossbar();

    void setBossbar(int var1);

    int getSize();

    void setSize(int var1);

    int getTint();

    void setTint(int var1);

    int getShowName();

    void setShowName(int var1);

    void setCapeTexture(String var1);

    String getCapeTexture();

    void setOverlayTexture(String var1);

    String getOverlayTexture();

    void setModelScale(int var1, float var2, float var3, float var4);

    float[] getModelScale(int var1);

    int getBossColor();

    void setBossColor(int var1);

    void setModel(String var1);

    String getModel();

    void setHitboxState(byte var1);

    byte getHitboxState();
}