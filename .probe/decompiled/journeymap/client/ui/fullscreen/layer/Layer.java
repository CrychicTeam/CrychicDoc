package journeymap.client.ui.fullscreen.layer;

import java.awt.geom.Point2D.Double;
import java.util.List;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.ui.fullscreen.Fullscreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public abstract class Layer {

    protected final Fullscreen fullscreen;

    public Layer(Fullscreen fullscreen) {
        this.fullscreen = fullscreen;
    }

    public abstract List<DrawStep> onMouseMove(Minecraft var1, GridRenderer var2, Double var3, BlockPos var4, float var5, boolean var6);

    public abstract List<DrawStep> onMouseClick(Minecraft var1, GridRenderer var2, Double var3, BlockPos var4, int var5, boolean var6, float var7);

    public abstract boolean propagateClick();
}