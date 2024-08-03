package journeymap.client.api.display;

public interface IThemeToolBar {

    void setLayoutHorizontal(int var1, int var2, int var3, boolean var4);

    void setLayoutCenteredHorizontal(int var1, int var2, int var3, boolean var4);

    void setLayoutDistributedHorizontal(int var1, int var2, int var3, boolean var4);

    void setLayoutVertical(int var1, int var2, int var3, boolean var4);

    void setLayoutCenteredVertical(int var1, int var2, int var3, boolean var4);

    void setReverse();

    int getHeight();

    int getWidth();

    int getX();

    int getY();

    int getCenterX();

    int getMiddleY();

    int getBottomY();

    int getRightX();

    void setPosition(int var1, int var2);

    boolean isMouseOver();
}