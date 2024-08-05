package snownee.jade.api.ui;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import snownee.jade.impl.Tooltip;

@NonExtendable
public interface ITooltipRenderer {

    int TOP = 0;

    int RIGHT = 1;

    int BOTTOM = 2;

    int LEFT = 3;

    int getPadding(int var1);

    @Deprecated
    default void setPadding(int i, float value) {
        this.setPadding(i, (int) value);
    }

    void setPadding(int var1, int var2);

    Tooltip getTooltip();

    boolean hasIcon();

    IElement getIcon();

    void setIcon(IElement var1);

    Rect2i getPosition();

    Vec2 getSize();

    void setSize(Vec2 var1);

    void recalculateSize();

    float getRealScale();

    @Nullable
    Rect2i getRealRect();

    void recalculateRealRect();
}