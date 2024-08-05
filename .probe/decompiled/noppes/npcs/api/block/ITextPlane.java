package noppes.npcs.api.block;

public interface ITextPlane {

    String getText();

    void setText(String var1);

    int getRotationX();

    int getRotationY();

    int getRotationZ();

    void setRotationX(int var1);

    void setRotationY(int var1);

    void setRotationZ(int var1);

    float getOffsetX();

    float getOffsetY();

    float getOffsetZ();

    void setOffsetX(float var1);

    void setOffsetY(float var1);

    void setOffsetZ(float var1);

    float getScale();

    void setScale(float var1);
}