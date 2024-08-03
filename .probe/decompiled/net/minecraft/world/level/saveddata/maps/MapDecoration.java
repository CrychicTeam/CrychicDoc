package net.minecraft.world.level.saveddata.maps;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class MapDecoration {

    private final MapDecoration.Type type;

    private final byte x;

    private final byte y;

    private final byte rot;

    @Nullable
    private final Component name;

    public MapDecoration(MapDecoration.Type mapDecorationType0, byte byte1, byte byte2, byte byte3, @Nullable Component component4) {
        this.type = mapDecorationType0;
        this.x = byte1;
        this.y = byte2;
        this.rot = byte3;
        this.name = component4;
    }

    public byte getImage() {
        return this.type.getIcon();
    }

    public MapDecoration.Type getType() {
        return this.type;
    }

    public byte getX() {
        return this.x;
    }

    public byte getY() {
        return this.y;
    }

    public byte getRot() {
        return this.rot;
    }

    public boolean renderOnFrame() {
        return this.type.isRenderedOnFrame();
    }

    @Nullable
    public Component getName() {
        return this.name;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof MapDecoration $$1) ? false : this.type == $$1.type && this.rot == $$1.rot && this.x == $$1.x && this.y == $$1.y && Objects.equals(this.name, $$1.name);
        }
    }

    public int hashCode() {
        int $$0 = this.type.getIcon();
        $$0 = 31 * $$0 + this.x;
        $$0 = 31 * $$0 + this.y;
        $$0 = 31 * $$0 + this.rot;
        return 31 * $$0 + Objects.hashCode(this.name);
    }

    public static enum Type {

        PLAYER(false, true),
        FRAME(true, true),
        RED_MARKER(false, true),
        BLUE_MARKER(false, true),
        TARGET_X(true, false),
        TARGET_POINT(true, false),
        PLAYER_OFF_MAP(false, true),
        PLAYER_OFF_LIMITS(false, true),
        MANSION(true, 5393476, false),
        MONUMENT(true, 3830373, false),
        BANNER_WHITE(true, true),
        BANNER_ORANGE(true, true),
        BANNER_MAGENTA(true, true),
        BANNER_LIGHT_BLUE(true, true),
        BANNER_YELLOW(true, true),
        BANNER_LIME(true, true),
        BANNER_PINK(true, true),
        BANNER_GRAY(true, true),
        BANNER_LIGHT_GRAY(true, true),
        BANNER_CYAN(true, true),
        BANNER_PURPLE(true, true),
        BANNER_BLUE(true, true),
        BANNER_BROWN(true, true),
        BANNER_GREEN(true, true),
        BANNER_RED(true, true),
        BANNER_BLACK(true, true),
        RED_X(true, false);

        private final byte icon;

        private final boolean renderedOnFrame;

        private final int mapColor;

        private final boolean trackCount;

        private Type(boolean p_181304_, boolean p_181305_) {
            this(p_181304_, -1, p_181305_);
        }

        private Type(boolean p_181298_, int p_181299_, boolean p_181300_) {
            this.trackCount = p_181300_;
            this.icon = (byte) this.ordinal();
            this.renderedOnFrame = p_181298_;
            this.mapColor = p_181299_;
        }

        public byte getIcon() {
            return this.icon;
        }

        public boolean isRenderedOnFrame() {
            return this.renderedOnFrame;
        }

        public boolean hasMapColor() {
            return this.mapColor >= 0;
        }

        public int getMapColor() {
            return this.mapColor;
        }

        public static MapDecoration.Type byIcon(byte p_77855_) {
            return values()[Mth.clamp(p_77855_, 0, values().length - 1)];
        }

        public boolean shouldTrackCount() {
            return this.trackCount;
        }
    }
}