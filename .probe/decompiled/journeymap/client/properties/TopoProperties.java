package journeymap.client.properties;

import com.google.common.base.Objects;
import journeymap.client.Constants;
import journeymap.client.cartography.color.RGB;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.StringField;

public class TopoProperties extends ClientPropertiesBase implements Comparable<TopoProperties> {

    private static final String[] HEADERS = new String[] { "// " + Constants.getString("jm.config.file_header_1"), "// " + Constants.getString("jm.config.file_header_5", "http://journeymap.info/Topographic") };

    private static final int MAX_COLORS = 128;

    private static final String DEFAULT_LAND_CONTOUR_COLOR = "#3F250B";

    private static final String DEFAULT_WATER_CONTOUR_COLOR = "#0000dd";

    private static final String DEFAULT_LAND_COLORS = "#010c02,#041105,#071609,#0a1b0c,#0d200f,#102513,#132a16,#162f19,#19341c,#1b3a20,#1e3f23,#214426,#24492a,#274e2d,#2a5330,#2d5834,#375f41,#41674d,#4b6e5a,#557567,#5f7c73,#698480,#738b8d,#7c929a,#8699a6,#90a1b3,#9aa8c0,#a4afcc,#aeb6d9,#b8bee6,#c2c5f2,#ccccff,#cccfff,#ccd2ff,#ccd6ff,#ccd9ff,#ccdcff,#ccdfff,#cce2ff,#cce5ff,#cce9ff,#ccecff,#ccefff,#ccf2ff,#ccf5ff,#ccf9ff,#ccfcff,#ccffff,#cfffff,#d2ffff,#d6ffff,#d9ffff,#dcffff,#dfffff,#e2ffff,#e5ffff,#e9ffff,#ecffff,#efffff,#f2ffff,#f5ffff,#f9ffff,#fcffff,#ffffff";

    private static final String DEFAULT_WATER_COLORS = "#000040,#02024e,#03035d,#05056b,#070779,#080887,#0a0a96,#0b0ba4,#1a1aaa,#2a2aaf,#3939b5,#4848bb,#5757c0,#6767c6,#7676cc,#8585d2,#9494d7,#a4a4dd,#b3b3e3,#c2c2e8,#d1d1ee,#d7d7f0,#ddddf2,#e2e2f4,#e8e8f6,#eeeef9,#f4f4fb,#f9f9ff,#f9f9ff,#f9f9ff,#f9f9ff,#f9f9ff";

    public final BooleanField showContour = new BooleanField(Category.Hidden, true);

    public final StringField landContour = new StringField(Category.Hidden, "").set("#3F250B");

    public final StringField waterContour = new StringField(Category.Hidden, "").set("#0000dd");

    public final StringField land = new StringField(Category.Hidden, "").multiline(true).set("#010c02,#041105,#071609,#0a1b0c,#0d200f,#102513,#132a16,#162f19,#19341c,#1b3a20,#1e3f23,#214426,#24492a,#274e2d,#2a5330,#2d5834,#375f41,#41674d,#4b6e5a,#557567,#5f7c73,#698480,#738b8d,#7c929a,#8699a6,#90a1b3,#9aa8c0,#a4afcc,#aeb6d9,#b8bee6,#c2c5f2,#ccccff,#cccfff,#ccd2ff,#ccd6ff,#ccd9ff,#ccdcff,#ccdfff,#cce2ff,#cce5ff,#cce9ff,#ccecff,#ccefff,#ccf2ff,#ccf5ff,#ccf9ff,#ccfcff,#ccffff,#cfffff,#d2ffff,#d6ffff,#d9ffff,#dcffff,#dfffff,#e2ffff,#e5ffff,#e9ffff,#ecffff,#efffff,#f2ffff,#f5ffff,#f9ffff,#fcffff,#ffffff");

    public final StringField water = new StringField(Category.Hidden, "").multiline(true).set("#000040,#02024e,#03035d,#05056b,#070779,#080887,#0a0a96,#0b0ba4,#1a1aaa,#2a2aaf,#3939b5,#4848bb,#5757c0,#6767c6,#7676cc,#8585d2,#9494d7,#a4a4dd,#b3b3e3,#c2c2e8,#d1d1ee,#d7d7f0,#ddddf2,#e2e2f4,#e8e8f6,#eeeef9,#f4f4fb,#f9f9ff,#f9f9ff,#f9f9ff,#f9f9ff,#f9f9ff");

    private transient Integer[] landColors;

    private transient Integer[] waterColors;

    private transient Integer landContourColor;

    private transient Integer waterContourColor;

    @Override
    public String getName() {
        return "topo";
    }

    public Integer[] getLandColors() {
        if (this.landColors == null) {
            this.landColors = this.getColors("land", this.land.get());
            if (this.landColors == null || this.landColors.length == 0) {
                this.error("TopoProperties reverting to default land colors");
                this.landColors = this.getColors("land", "#010c02,#041105,#071609,#0a1b0c,#0d200f,#102513,#132a16,#162f19,#19341c,#1b3a20,#1e3f23,#214426,#24492a,#274e2d,#2a5330,#2d5834,#375f41,#41674d,#4b6e5a,#557567,#5f7c73,#698480,#738b8d,#7c929a,#8699a6,#90a1b3,#9aa8c0,#a4afcc,#aeb6d9,#b8bee6,#c2c5f2,#ccccff,#cccfff,#ccd2ff,#ccd6ff,#ccd9ff,#ccdcff,#ccdfff,#cce2ff,#cce5ff,#cce9ff,#ccecff,#ccefff,#ccf2ff,#ccf5ff,#ccf9ff,#ccfcff,#ccffff,#cfffff,#d2ffff,#d6ffff,#d9ffff,#dcffff,#dfffff,#e2ffff,#e5ffff,#e9ffff,#ecffff,#efffff,#f2ffff,#f5ffff,#f9ffff,#fcffff,#ffffff");
            }
        }
        return this.landColors;
    }

    public Integer[] getWaterColors() {
        if (this.waterColors == null) {
            this.waterColors = this.getColors("water", this.water.get());
            if (this.waterColors == null || this.waterColors.length == 0) {
                this.error("TopoProperties reverting to default water colors");
                this.waterColors = this.getColors("water", "#000040,#02024e,#03035d,#05056b,#070779,#080887,#0a0a96,#0b0ba4,#1a1aaa,#2a2aaf,#3939b5,#4848bb,#5757c0,#6767c6,#7676cc,#8585d2,#9494d7,#a4a4dd,#b3b3e3,#c2c2e8,#d1d1ee,#d7d7f0,#ddddf2,#e2e2f4,#e8e8f6,#eeeef9,#f4f4fb,#f9f9ff,#f9f9ff,#f9f9ff,#f9f9ff,#f9f9ff");
            }
        }
        return this.waterColors;
    }

    public Integer getLandContourColor() {
        if (!this.showContour.get()) {
            return null;
        } else {
            if (this.landContourColor == null) {
                this.landContourColor = RGB.hexToInt(this.landContour.get());
            }
            return this.landContourColor;
        }
    }

    public Integer getWaterContourColor() {
        if (!this.showContour.get()) {
            return null;
        } else {
            if (this.waterContourColor == null) {
                this.waterContourColor = RGB.hexToInt(this.waterContour.get());
            }
            return this.waterContourColor;
        }
    }

    private Integer[] getColors(String name, String colorString) {
        String[] colors = colorString.split(",");
        int size = Math.min(128, colors.length);
        if (size == 0) {
            this.error(String.format("TopoProperties bad value for %s: %s", name, colorString));
            return null;
        } else {
            if (size > 128) {
                this.warn(String.format("TopoProperties will ignore more than %s colors for %s. Found: %s", 128, name, size));
            }
            Integer[] colorInts = new Integer[size];
            for (int i = 0; i < colors.length; i++) {
                colorInts[i] = RGB.hexToInt(colors[i].trim());
            }
            return colorInts;
        }
    }

    public int compareTo(TopoProperties other) {
        return Integer.valueOf(this.hashCode()).compareTo(other.hashCode());
    }

    @Override
    public <T extends PropertiesBase> void updateFrom(T otherInstance) {
        super.updateFrom(otherInstance);
    }

    @Override
    protected void postLoad(boolean isNew) {
        super.postLoad(isNew);
        this.landColors = null;
        this.waterColors = null;
        this.landContourColor = null;
        this.waterContourColor = null;
    }

    @Override
    public boolean isValid(boolean fix) {
        boolean valid = super.isValid(fix);
        valid = this.isValid(this.water.get(), fix) && valid;
        return this.isValid(this.land.get(), fix) && valid;
    }

    @Override
    protected void preSave() {
        super.preSave();
    }

    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    private boolean isValid(String colorString, boolean fix) {
        boolean valid = true;
        String[] colors = colorString.split(",");
        for (int i = 0; i < colors.length; i++) {
            String colorStr = colors[i];
            try {
                colorStr = RGB.toHexString(Integer.parseInt(colorStr));
            } catch (Exception var8) {
                colorStr = "0x000000";
            }
            if (!Objects.equal(colorStr, colors[i])) {
                if (fix) {
                    colors[i] = colorStr;
                } else {
                    valid = false;
                }
            }
        }
        return valid;
    }
}