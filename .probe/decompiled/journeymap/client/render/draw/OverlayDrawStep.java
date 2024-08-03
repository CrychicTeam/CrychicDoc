package journeymap.client.render.draw;

import com.mojang.blaze3d.vertex.PoseStack;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.awt.geom.Rectangle2D.Double;
import journeymap.client.api.display.Overlay;
import journeymap.client.render.map.GridRenderer;

public interface OverlayDrawStep extends DrawStep {

    Overlay getOverlay();

    Double getBounds();

    boolean isOnScreen(PoseStack var1, double var2, double var4, GridRenderer var6, double var7);

    void setTitlePosition(@Nullable java.awt.geom.Point2D.Double var1);

    void setEnabled(boolean var1);
}