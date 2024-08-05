package journeymap.client.api.display;

import java.awt.geom.Point2D.Double;
import journeymap.client.api.util.UIState;
import net.minecraft.core.BlockPos;

public interface IOverlayListener {

    void onActivate(UIState var1);

    void onDeactivate(UIState var1);

    void onMouseMove(UIState var1, Double var2, BlockPos var3);

    void onMouseOut(UIState var1, Double var2, BlockPos var3);

    boolean onMouseClick(UIState var1, Double var2, BlockPos var3, int var4, boolean var5);

    void onOverlayMenuPopup(UIState var1, Double var2, BlockPos var3, ModPopupMenu var4);
}