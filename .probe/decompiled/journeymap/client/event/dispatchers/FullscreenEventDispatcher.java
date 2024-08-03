package journeymap.client.event.dispatchers;

import java.awt.geom.Point2D.Double;
import journeymap.client.api.event.FullscreenMapEvent;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.model.IBlockInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class FullscreenEventDispatcher {

    public static boolean clickEventPre(BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
        FullscreenMapEvent preClickEvent = new FullscreenMapEvent.ClickEvent.Pre(location, level, mousePosition, button);
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(preClickEvent);
        return preClickEvent.isCancelled();
    }

    public static void clickEventPost(BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
        FullscreenMapEvent postClickEvent = new FullscreenMapEvent.ClickEvent.Post(location, level, mousePosition, button);
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(postClickEvent);
    }

    public static boolean dragEventPre(BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
        FullscreenMapEvent draggedEvent = new FullscreenMapEvent.MouseDraggedEvent.Pre(location, level, mousePosition, button);
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(draggedEvent);
        return draggedEvent.isCancelled();
    }

    public static void dragEventPost(BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
        FullscreenMapEvent draggedEvent = new FullscreenMapEvent.MouseDraggedEvent.Post(location, level, mousePosition, button);
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(draggedEvent);
    }

    public static void moveEvent(ResourceKey<Level> level, IBlockInfo info, Double mousePosition) {
        FullscreenMapEvent mouseMoveEvent = new FullscreenMapEvent.MouseMoveEvent(level, info, mousePosition);
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(mouseMoveEvent);
    }
}