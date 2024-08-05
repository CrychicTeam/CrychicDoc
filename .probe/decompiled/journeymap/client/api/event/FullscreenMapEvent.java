package journeymap.client.api.event;

import java.awt.geom.Point2D.Double;
import journeymap.client.api.model.IBlockInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class FullscreenMapEvent extends ClientEvent {

    private final BlockPos location;

    private FullscreenMapEvent(ClientEvent.Type type, BlockPos location, ResourceKey<Level> level) {
        super(type, level);
        this.location = location;
    }

    public BlockPos getLocation() {
        return this.location;
    }

    public ResourceKey<Level> getLevel() {
        return this.dimension;
    }

    public static class ClickEvent extends FullscreenMapEvent {

        private final int button;

        private final Double mousePosition;

        private final FullscreenMapEvent.Stage stage;

        public ClickEvent(FullscreenMapEvent.Stage stage, BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
            super(ClientEvent.Type.MAP_CLICKED, location, level);
            this.stage = stage;
            this.mousePosition = mousePosition;
            this.button = button;
        }

        public FullscreenMapEvent.Stage getStage() {
            return this.stage;
        }

        public double getMouseX() {
            return this.mousePosition.x;
        }

        public double getMouseY() {
            return this.mousePosition.y;
        }

        public Double getMousePosition() {
            return this.mousePosition;
        }

        public int getButton() {
            return this.button;
        }

        public static class Post extends FullscreenMapEvent.ClickEvent {

            public Post(BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
                super(FullscreenMapEvent.Stage.POST, location, level, mousePosition, button);
            }

            @Override
            public boolean isCancellable() {
                return false;
            }
        }

        public static class Pre extends FullscreenMapEvent.ClickEvent {

            public Pre(BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
                super(FullscreenMapEvent.Stage.PRE, location, level, mousePosition, button);
            }
        }
    }

    public static class MouseDraggedEvent extends FullscreenMapEvent {

        private final int button;

        private final Double mousePosition;

        private final FullscreenMapEvent.Stage stage;

        public MouseDraggedEvent(FullscreenMapEvent.Stage stage, BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
            super(ClientEvent.Type.MAP_DRAGGED, location, level);
            this.stage = stage;
            this.mousePosition = mousePosition;
            this.button = button;
        }

        public FullscreenMapEvent.Stage getStage() {
            return this.stage;
        }

        public double getMouseX() {
            return this.mousePosition.x;
        }

        public double getMouseY() {
            return this.mousePosition.y;
        }

        public Double getMousePosition() {
            return this.mousePosition;
        }

        public int getButton() {
            return this.button;
        }

        public static class Post extends FullscreenMapEvent.MouseDraggedEvent {

            public Post(BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
                super(FullscreenMapEvent.Stage.POST, location, level, mousePosition, button);
            }

            @Override
            public boolean isCancellable() {
                return false;
            }
        }

        public static class Pre extends FullscreenMapEvent.MouseDraggedEvent {

            public Pre(BlockPos location, ResourceKey<Level> level, Double mousePosition, int button) {
                super(FullscreenMapEvent.Stage.PRE, location, level, mousePosition, button);
            }
        }
    }

    public static class MouseMoveEvent extends FullscreenMapEvent {

        private final Double mousePosition;

        private final IBlockInfo info;

        public MouseMoveEvent(ResourceKey<Level> level, IBlockInfo info, Double mousePosition) {
            super(ClientEvent.Type.MAP_MOUSE_MOVED, info.getBlockPos(), level);
            this.mousePosition = mousePosition;
            this.info = info;
        }

        public Double getMousePosition() {
            return this.mousePosition;
        }

        public double getMouseX() {
            return this.mousePosition.x;
        }

        public double getMouseY() {
            return this.mousePosition.y;
        }

        public IBlockInfo getInfo() {
            return this.info;
        }
    }

    public static enum Stage {

        PRE, POST
    }
}