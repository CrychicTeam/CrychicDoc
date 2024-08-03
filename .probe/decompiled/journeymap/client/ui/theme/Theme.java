package journeymap.client.ui.theme;

import com.google.common.base.Strings;
import com.google.gson.annotations.Since;
import java.awt.Color;
import journeymap.common.Journeymap;

public class Theme {

    public static final double VERSION = 2.0;

    @Since(2.0)
    public int schema;

    @Since(1.0)
    public String author;

    @Since(1.0)
    public String name;

    @Since(1.0)
    public String directory;

    @Since(1.0)
    public Theme.Container container = new Theme.Container();

    @Since(1.0)
    public Theme.Control control = new Theme.Control();

    @Since(1.0)
    public Theme.Fullscreen fullscreen = new Theme.Fullscreen();

    @Since(1.0)
    public Theme.ImageSpec icon = new Theme.ImageSpec();

    @Since(1.0)
    public Theme.Minimap minimap = new Theme.Minimap();

    public static String toHexColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String toHexColor(int rgb) {
        return toHexColor(new Color(rgb));
    }

    private static int getColor(String hexColor) {
        if (!Strings.isNullOrEmpty(hexColor)) {
            try {
                return Integer.parseInt(hexColor.replaceFirst("#", ""), 16);
            } catch (Exception var2) {
                Journeymap.getLogger().warn("Journeymap theme has an invalid color string: " + hexColor);
            }
        }
        return 16777215;
    }

    public String toString() {
        return Strings.isNullOrEmpty(this.name) ? "???" : this.name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Theme theme = (Theme) o;
            if (this.directory != null ? this.directory.equals(theme.directory) : theme.directory == null) {
                return this.name != null ? this.name.equals(theme.name) : theme.name == null;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        return 31 * result + (this.directory != null ? this.directory.hashCode() : 0);
    }

    public static class ColorSpec implements Cloneable {

        @Since(2.0)
        public String color = "#ffffff";

        private transient Integer _color;

        @Since(2.0)
        public float alpha = 1.0F;

        public ColorSpec() {
        }

        public ColorSpec(String color, float alpha) {
            this.color = color;
            this.alpha = alpha;
        }

        public int getColor() {
            if (this._color == null) {
                this._color = Theme.getColor(this.color);
            }
            return this._color;
        }

        public Theme.ColorSpec clone() {
            Theme.ColorSpec clone = new Theme.ColorSpec();
            clone.color = this.color;
            clone.alpha = this.alpha;
            return clone;
        }
    }

    public static class Container {

        @Since(1.0)
        public Theme.Container.Toolbar toolbar = new Theme.Container.Toolbar();

        public static class Toolbar {

            @Since(1.0)
            public Theme.Container.Toolbar.ToolbarSpec horizontal = new Theme.Container.Toolbar.ToolbarSpec();

            @Since(1.0)
            public Theme.Container.Toolbar.ToolbarSpec vertical = new Theme.Container.Toolbar.ToolbarSpec();

            public static class ToolbarSpec {

                @Since(1.0)
                public boolean useThemeImages;

                @Since(1.0)
                public String prefix = "";

                @Since(1.0)
                public int margin;

                @Since(1.0)
                public int padding;

                @Since(1.0)
                public Theme.ImageSpec begin;

                @Since(1.0)
                public Theme.ImageSpec inner;

                @Since(1.0)
                public Theme.ImageSpec end;
            }
        }
    }

    public static class Control {

        @Since(1.0)
        public Theme.Control.ButtonSpec button = new Theme.Control.ButtonSpec();

        @Since(1.0)
        public Theme.Control.ButtonSpec toggle = new Theme.Control.ButtonSpec();

        public static class ButtonSpec {

            @Since(1.0)
            public boolean useThemeImages;

            @Since(1.0)
            public int width;

            @Since(1.0)
            public int height;

            @Since(1.0)
            public String prefix = "";

            @Since(1.0)
            public String tooltipOnStyle = "";

            @Since(1.0)
            public String tooltipOffStyle = "";

            @Since(1.0)
            public String tooltipDisabledStyle = "";

            @Since(2.0)
            public Theme.ColorSpec iconOn = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec iconOff = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec iconHoverOn = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec iconHoverOff = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec iconDisabled = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec buttonOn = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec buttonOff = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec buttonHoverOn = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec buttonHoverOff = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec buttonDisabled = new Theme.ColorSpec();
        }
    }

    public static class DefaultPointer {

        @Since(1.0)
        public String directory;

        @Since(1.0)
        public String filename;

        @Since(1.0)
        public String name;

        protected DefaultPointer() {
        }

        public DefaultPointer(Theme theme) {
            this.name = theme.name;
            this.filename = theme.name;
            this.directory = theme.directory;
        }
    }

    public static class Fullscreen {

        @Since(2.0)
        public Theme.ColorSpec background = new Theme.ColorSpec();

        @Since(1.0)
        public Theme.LabelSpec statusLabel = new Theme.LabelSpec();
    }

    public static class ImageSpec extends Theme.ColorSpec {

        @Since(1.0)
        public int width;

        @Since(1.0)
        public int height;

        public ImageSpec() {
        }

        public ImageSpec(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public static class LabelSpec implements Cloneable {

        @Since(2.0)
        public int margin = 2;

        @Since(2.0)
        public Theme.ColorSpec background = new Theme.ColorSpec("#000000", 0.0F);

        @Since(2.0)
        public Theme.ColorSpec foreground = new Theme.ColorSpec();

        @Since(2.0)
        public Theme.ColorSpec highlight = new Theme.ColorSpec();

        @Since(1.0)
        public boolean shadow = false;

        public Theme.LabelSpec clone() {
            Theme.LabelSpec clone = new Theme.LabelSpec();
            clone.margin = this.margin;
            clone.background = this.background.clone();
            clone.foreground = this.foreground.clone();
            clone.highlight = this.highlight.clone();
            return clone;
        }
    }

    public static class Minimap {

        @Since(1.0)
        public Theme.Minimap.MinimapCircle circle = new Theme.Minimap.MinimapCircle();

        @Since(1.0)
        public Theme.Minimap.MinimapSquare square = new Theme.Minimap.MinimapSquare();

        public static class MinimapCircle extends Theme.Minimap.MinimapSpec {

            @Since(1.0)
            public Theme.ImageSpec rim256 = new Theme.ImageSpec(256, 256);

            @Since(1.0)
            public Theme.ImageSpec mask256 = new Theme.ImageSpec(256, 256);

            @Since(1.0)
            public Theme.ImageSpec rim512 = new Theme.ImageSpec(512, 512);

            @Since(1.0)
            public Theme.ImageSpec mask512 = new Theme.ImageSpec(512, 512);

            @Since(2.0)
            public boolean rotates = false;
        }

        public abstract static class MinimapSpec {

            @Since(1.0)
            public int margin;

            @Since(2.0)
            public Theme.LabelSpec labelTop = new Theme.LabelSpec();

            @Since(2.0)
            public boolean labelTopInside = false;

            @Since(2.0)
            public Theme.LabelSpec labelBottom = new Theme.LabelSpec();

            @Since(2.0)
            public boolean labelBottomInside = false;

            @Since(1.0)
            public Theme.LabelSpec compassLabel = new Theme.LabelSpec();

            @Since(1.0)
            public Theme.ImageSpec compassPoint = new Theme.ImageSpec();

            @Since(1.0)
            public int compassPointLabelPad;

            @Since(1.0)
            public double compassPointOffset;

            @Since(1.0)
            public boolean compassShowNorth = true;

            @Since(1.0)
            public boolean compassShowSouth = true;

            @Since(1.0)
            public boolean compassShowEast = true;

            @Since(1.0)
            public boolean compassShowWest = true;

            @Since(1.0)
            public double waypointOffset;

            @Since(2.0)
            public Theme.ColorSpec reticle = new Theme.ColorSpec();

            @Since(2.0)
            public Theme.ColorSpec reticleHeading = new Theme.ColorSpec();

            @Since(1.0)
            public double reticleThickness = 2.25;

            @Since(1.0)
            public double reticleHeadingThickness = 2.5;

            @Since(2.0)
            public int reticleOffsetOuter = 16;

            @Since(2.0)
            public int reticleOffsetInner = 16;

            @Since(2.0)
            public Theme.ColorSpec frame = new Theme.ColorSpec();

            @Since(1.0)
            public String prefix = "";
        }

        public static class MinimapSquare extends Theme.Minimap.MinimapSpec {

            @Since(1.0)
            public Theme.ImageSpec topLeft = new Theme.ImageSpec();

            @Since(1.0)
            public Theme.ImageSpec top = new Theme.ImageSpec();

            @Since(1.0)
            public Theme.ImageSpec topRight = new Theme.ImageSpec();

            @Since(1.0)
            public Theme.ImageSpec right = new Theme.ImageSpec();

            @Since(1.0)
            public Theme.ImageSpec bottomRight = new Theme.ImageSpec();

            @Since(1.0)
            public Theme.ImageSpec bottom = new Theme.ImageSpec();

            @Since(1.0)
            public Theme.ImageSpec bottomLeft = new Theme.ImageSpec();

            @Since(1.0)
            public Theme.ImageSpec left = new Theme.ImageSpec();
        }
    }
}