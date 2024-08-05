package journeymap.client.waypoint;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Since;
import java.awt.Color;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.api.model.MapImage;
import journeymap.client.cartography.color.RGB;
import journeymap.client.properties.WaypointProperties;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.common.CommonConstants;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.util.Strings;

public class Waypoint implements Serializable {

    public static final int VERSION = 4;

    public static final Gson GSON = new GsonBuilder().setVersion(4.0).create();

    protected static final ResourceLocation ICON_NORMAL = new ResourceLocation("journeymap", "ui/img/waypoint-icon.png");

    protected static final ResourceLocation ICON_DEATH = new ResourceLocation("journeymap", "ui/img/waypoint-death-icon.png");

    @Since(1.0)
    protected String id;

    @Since(1.0)
    protected String name;

    @Since(3.0)
    protected String groupName;

    @Since(2.0)
    protected String displayId;

    @Since(1.0)
    protected String icon;

    @Since(3.0)
    protected String colorizedIcon;

    @Since(1.0)
    protected int x;

    @Since(1.0)
    protected int y;

    @Since(1.0)
    protected int z;

    @Since(1.0)
    protected int r;

    @Since(1.0)
    protected int g;

    @Since(1.0)
    protected int b;

    @Since(1.0)
    protected boolean enable;

    @Since(1.0)
    protected Waypoint.Type type;

    @Since(1.0)
    protected String origin;

    @Since(1.0)
    protected TreeSet<String> dimensions;

    @Since(2.0)
    protected boolean persistent;

    @Since(3.0)
    protected boolean showDeviation;

    @Since(3.0)
    protected Integer iconColor;

    protected transient WaypointGroup group;

    protected transient boolean dirty;

    private boolean customIconColor = false;

    public Waypoint() {
    }

    public Waypoint(Waypoint original) {
        this(original.name, original.x, original.y, original.z, original.enable, original.r, original.g, original.b, original.type, original.origin, original.dimensions != null && !original.dimensions.isEmpty() ? (String) original.dimensions.first() : null, original.dimensions, original.showDeviation);
        this.x = original.x;
        this.y = original.y;
        this.z = original.z;
        this.iconColor = original.iconColor;
        this.displayId = original.displayId;
    }

    public Waypoint(journeymap.client.api.display.Waypoint modWaypoint) {
        this(modWaypoint.getName(), modWaypoint.getPosition(), modWaypoint.getColor() == null ? Color.WHITE : new Color(modWaypoint.getColor()), Waypoint.Type.Normal, modWaypoint.getDimension(), false);
        String[] prim = modWaypoint.getDisplayDimensions();
        ArrayList<String> dims = new ArrayList(prim.length);
        for (String aPrim : prim) {
            dims.add(aPrim);
        }
        this.enable = modWaypoint.isEnabled();
        this.dimensions = new TreeSet(dims);
        this.origin = modWaypoint.getModId();
        this.displayId = modWaypoint.getId();
        this.persistent = modWaypoint.isPersistent();
        if (modWaypoint.getGroup() != null) {
            WaypointGroup group = WaypointGroupStore.INSTANCE.get(this.origin, modWaypoint.getGroup().getName());
            this.origin = group.getOrigin();
            this.groupName = group.getName();
            this.group = group;
        }
    }

    public Waypoint(String name, BlockPos pos, Color color, Waypoint.Type type, String currentDimension, boolean showDeviation) {
        this(name, pos.m_123341_(), pos.m_123342_(), pos.m_123343_(), true, color.getRed(), color.getGreen(), color.getBlue(), type, "journeymap", currentDimension, Arrays.asList(currentDimension), showDeviation);
    }

    public Waypoint(String name, int x, int y, int z, boolean enable, int red, int green, int blue, Waypoint.Type type, String origin, String currentDimension, Collection<String> dimensions, boolean showDeviation) {
        if (name == null) {
            name = createName(x, z);
        }
        if (dimensions == null || dimensions.size() == 0) {
            dimensions = new TreeSet();
            dimensions.add(DimensionHelper.getDimKeyName(Minecraft.getInstance().player.m_9236_().dimension()));
        }
        this.dimensions = new TreeSet(dimensions);
        if (currentDimension != null) {
            this.dimensions.add(currentDimension);
        }
        this.name = name;
        this.setLocation(x, y, z, currentDimension);
        this.r = red;
        this.g = green;
        this.b = blue;
        this.enable = enable;
        this.type = type;
        this.origin = origin;
        this.persistent = true;
        this.showDeviation = showDeviation;
        this.iconColor = RGB.toInteger(red, green, blue);
        switch(type) {
            case Normal:
                this.setIcon(ICON_NORMAL);
                break;
            case Death:
                this.setIcon(ICON_DEATH);
        }
    }

    public static Waypoint of(Entity player) {
        BlockPos blockPos = new BlockPos(Mth.floor(player.getX()), Mth.floor(player.getY()), Mth.floor(player.getZ()));
        return at(blockPos, Waypoint.Type.Normal, Minecraft.getInstance().player.m_9236_().dimension().location().toString());
    }

    public static Waypoint at(BlockPos blockPos, Waypoint.Type type, String dimension) {
        String name;
        if (type == Waypoint.Type.Death) {
            Date now = new Date();
            WaypointProperties properties = JourneymapClient.getInstance().getWaypointProperties();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(properties.timeFormat.get() + " " + properties.dateFormat.get());
            String timeDate = simpleDateFormat.format(now);
            name = String.format("%s %s", Constants.getString("jm.waypoint.deathpoint"), timeDate);
        } else {
            name = createName(blockPos.m_123341_(), blockPos.m_123343_());
        }
        Waypoint waypoint = new Waypoint(name, blockPos, Color.white, type, dimension, false);
        waypoint.setRandomColor();
        return waypoint;
    }

    private static String createName(int x, int z) {
        return String.format("%s, %s", x, z);
    }

    public void validateName() {
        if (Strings.isEmpty(this.getName())) {
            this.setName(createName(this.getX(), this.getZ()));
        }
    }

    public static Waypoint fromString(String json) {
        return (Waypoint) GSON.fromJson(json, Waypoint.class);
    }

    public static journeymap.client.api.display.Waypoint toModWaypoint(Waypoint waypoint) {
        waypoint.validateName();
        journeymap.client.api.display.Waypoint wp = new journeymap.client.api.display.Waypoint(waypoint.getOrigin(), waypoint.displayId == null ? waypoint.id : waypoint.displayId, waypoint.getName(), waypoint.dimensions.isEmpty() ? null : (String) waypoint.dimensions.first(), waypoint.getBlockPos());
        Texture image = waypoint.getTexture();
        wp.setColor(waypoint.getColor()).setDisplayDimensions((String[]) waypoint.getDimensions().toArray(new String[0])).setIcon(new MapImage((ResourceLocation) TextureCache.waypointIconCache.get(waypoint.icon), 0, 0, image.getHeight(), image.getWidth(), waypoint.getIconColor(), image.getAlpha())).setPersistent(waypoint.isPersistent()).setEnabled(waypoint.enable);
        return wp;
    }

    public journeymap.client.api.display.Waypoint modWaypoint() {
        return toModWaypoint(this);
    }

    public Waypoint setLocation(int x, int y, int z, String currentDimension) {
        this.x = "minecraft:the_nether".equalsIgnoreCase(currentDimension) ? x * 8 : x;
        this.y = y;
        this.z = "minecraft:the_nether".equalsIgnoreCase(currentDimension) ? z * 8 : z;
        this.updateId();
        return this.setDirty();
    }

    public String updateId() {
        String oldId = this.id;
        this.id = CommonConstants.getCSSSafeString(String.format("%s_%s,%s,%s", this.name, this.x, this.y, this.z), "-");
        return oldId;
    }

    public boolean isDeathPoint() {
        return this.type == Waypoint.Type.Death;
    }

    public Texture getTexture() {
        return TextureCache.getTexture(this.getTextureResource());
    }

    public ResourceLocation getTextureResource() {
        if (TextureCache.waypointIconCache.get(this.colorizedIcon) == null) {
            this.setIcon(new ResourceLocation(this.icon));
        }
        return (ResourceLocation) TextureCache.waypointIconCache.get(this.icon);
    }

    public ChunkPos getChunkCoordIntPair() {
        return new ChunkPos(this.x >> 4, this.z >> 4);
    }

    public Waypoint setGroup(WaypointGroup group) {
        this.setOrigin(group.getOrigin());
        this.groupName = group.getName();
        this.group = group;
        return this.setDirty();
    }

    public Waypoint setGroupName(String groupName) {
        WaypointGroup group = WaypointGroupStore.INSTANCE.get(this.origin, groupName);
        this.setGroup(group);
        return this;
    }

    public WaypointGroup getGroup() {
        if (this.group == null) {
            if (!Strings.isEmpty(this.origin) && !Strings.isEmpty(this.groupName)) {
                this.setGroup(WaypointGroupStore.INSTANCE.get(this.origin, this.groupName));
            } else {
                this.setGroup(WaypointGroup.DEFAULT);
            }
        }
        return this.group;
    }

    public Waypoint setRandomColor() {
        return this.setColor(RGB.randomColor());
    }

    public Integer getColor() {
        return RGB.toInteger(this.r, this.g, this.b);
    }

    public String getPrettyName() {
        ChatFormatting textFormatting = ChatFormatting.WHITE;
        for (ChatFormatting tf : ChatFormatting.values()) {
            if (tf.getColor() != null && tf.getColor().equals((this.r & 0xFF) << 16 | (this.g & 0xFF) << 8 | this.b & 0xFF)) {
                textFormatting = tf;
            }
        }
        return textFormatting.toString() + this.name + ChatFormatting.WHITE;
    }

    public void setIconColor(Integer iconColor) {
        this.customIconColor = true;
        this.iconColor = iconColor;
    }

    public Integer getIconColor() {
        return this.customIconColor ? this.iconColor : this.getColor();
    }

    public Waypoint setColor(Integer color) {
        int[] c = RGB.ints(color);
        this.r = c[0];
        this.g = c[1];
        this.b = c[2];
        return this.setDirty();
    }

    public Integer getSafeColor() {
        return this.r + this.g + this.b >= 100 ? this.getColor() : 4210752;
    }

    public Collection<String> getDimensions() {
        return this.dimensions;
    }

    public Waypoint setDimensions(Collection<String> dims) {
        this.dimensions = new TreeSet(dims);
        return this.setDirty();
    }

    public boolean isTeleportReady() {
        return this.y >= 0 && this.isInPlayerDimension();
    }

    public boolean isInPlayerDimension() {
        return this.dimensions.contains(Minecraft.getInstance().player.m_9236_().dimension().location().toString());
    }

    public String getDisplayId() {
        return this.displayId;
    }

    public String getId() {
        return this.displayId != null ? this.getGuid() : this.id;
    }

    public String getGuid() {
        return this.origin + ":" + this.displayId;
    }

    public String getName() {
        return this.name;
    }

    public Waypoint setName(String name) {
        this.name = name;
        return this.setDirty();
    }

    public ResourceLocation getIcon() {
        return (ResourceLocation) TextureCache.waypointIconCache.get(this.icon);
    }

    public Waypoint setIcon(ResourceLocation iconResource) {
        this.icon = iconResource.toString();
        TextureCache.waypointIconCache.put(this.icon, iconResource);
        return this.setDirty();
    }

    public int getX() {
        Minecraft mc = Minecraft.getInstance();
        return mc != null && mc.player != null && DimensionHelper.isNetherWorld(mc.level) ? this.x >> 3 : this.x;
    }

    public double getRawCenterX() {
        return (double) this.x + 0.5;
    }

    public double getRawCenterZ() {
        return (double) this.z + 0.5;
    }

    public double getBlockCenteredX() {
        return (double) this.getX() + 0.5;
    }

    public int getY() {
        return this.y;
    }

    public double getBlockCenteredY() {
        return (double) this.getY() + 0.5;
    }

    public int getZ() {
        Minecraft mc = Minecraft.getInstance();
        return mc != null && mc.player != null && DimensionHelper.isNetherWorld(mc.level) ? this.z >> 3 : this.z;
    }

    public double getBlockCenteredZ() {
        return (double) this.getZ() + 0.5;
    }

    public Vec3 getPosition() {
        return new Vec3(this.getBlockCenteredX(), this.getBlockCenteredY(), this.getBlockCenteredZ());
    }

    public BlockPos getBlockPos() {
        return new BlockPos(this.getX(), this.getY(), this.getZ());
    }

    public int getR() {
        return this.r;
    }

    public Waypoint setR(int r) {
        this.r = r;
        return this.setDirty();
    }

    public int getG() {
        return this.g;
    }

    public Waypoint setG(int g) {
        this.g = g;
        return this.setDirty();
    }

    public int getB() {
        return this.b;
    }

    public Waypoint setB(int b) {
        this.b = b;
        return this.setDirty();
    }

    public boolean isEnable() {
        return this.enable;
    }

    public Waypoint setEnable(boolean enable) {
        if (enable != this.enable) {
            this.enable = enable;
            this.setDirty();
        }
        return this;
    }

    public Waypoint.Type getType() {
        return this.type;
    }

    public Waypoint setType(Waypoint.Type type) {
        this.type = type;
        return this.setDirty();
    }

    public String getOrigin() {
        return this.origin;
    }

    public Waypoint setOrigin(String origin) {
        this.origin = origin;
        return this.setDirty();
    }

    public String getFileName() {
        String fileName = this.id.replaceAll("[\\\\/:\"*?<>|]", "_").concat(".json");
        if (fileName.equals("waypoint_groups.json")) {
            fileName = "_" + fileName;
        }
        return fileName;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public Waypoint setDirty() {
        return this.setDirty(true);
    }

    public Waypoint setDirty(boolean dirty) {
        if (!this.customIconColor && this.getIcon() != null) {
            ResourceLocation coloredIconResource = TextureCache.coloredImageResource(this.getIcon(), this.getIconColor());
            this.colorizedIcon = coloredIconResource.toString();
            TextureCache.waypointIconCache.put(this.colorizedIcon, coloredIconResource);
        } else {
            this.colorizedIcon = this.icon;
        }
        if (this.isPersistent()) {
            this.dirty = dirty;
        }
        return this;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public Waypoint setPersistent(boolean persistent) {
        this.persistent = persistent;
        this.dirty = persistent;
        return this;
    }

    public String toChatString() {
        boolean useName = !this.getName().equals(String.format("%s, %s", this.getX(), this.getZ()));
        return this.toChatString(useName);
    }

    public String toChatString(boolean useName) {
        boolean useDim = !"overworld".equalsIgnoreCase((String) this.dimensions.first());
        List<String> parts = new ArrayList();
        List<Object> args = new ArrayList();
        if (useName) {
            parts.add("name:\"%s\"");
            args.add(this.getName().replaceAll("\"", " "));
        }
        parts.add("x:%s, y:%s, z:%s");
        args.add(this.getX());
        args.add(this.getY());
        args.add(this.getZ());
        if (useDim) {
            parts.add("dim:%s");
            args.add(this.dimensions.first());
        }
        String format = "[" + Joiner.on(", ").join(parts) + "]";
        String result = String.format(format, args.toArray());
        if (WaypointParser.parse(result) == null) {
            Journeymap.getLogger().warn("Couldn't produce parsable chat string from Waypoint: " + this);
            if (useName) {
                return this.toChatString(false);
            }
        }
        return result;
    }

    public String toString() {
        return GSON.toJson(this);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Waypoint waypoint = (Waypoint) o;
            if (this.b != waypoint.b) {
                return false;
            } else if (this.enable != waypoint.enable) {
                return false;
            } else if (this.g != waypoint.g) {
                return false;
            } else if (this.r != waypoint.r) {
                return false;
            } else if (this.x != waypoint.x) {
                return false;
            } else if (this.y != waypoint.y) {
                return false;
            } else if (this.z != waypoint.z) {
                return false;
            } else if (!this.dimensions.equals(waypoint.dimensions)) {
                return false;
            } else if (!this.icon.equals(waypoint.icon)) {
                return false;
            } else if (!this.id.equals(waypoint.id)) {
                return false;
            } else if (!this.name.equals(waypoint.name)) {
                return false;
            } else if (this.origin.equals(waypoint.origin)) {
                return false;
            } else if (this.type != waypoint.type) {
                return false;
            } else {
                return this.showDeviation != waypoint.showDeviation ? false : this.iconColor == waypoint.iconColor;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean showDeviation() {
        return this.showDeviation;
    }

    public Waypoint setShowDeviation(Boolean toggled) {
        if (toggled != this.showDeviation) {
            this.showDeviation = toggled;
            this.setDirty();
        }
        return this;
    }

    public void setY(int y) {
        this.y = y;
        this.setDirty();
    }

    public static enum Origin {

        SERVER("server"), COMMAND("command"), EXTERNAL("external"), EXTERNAL_FORCE("external-force"), TEMP("temp");

        final String value;

        private static final List<String> values = Lists.newArrayList();

        private Origin(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static List<String> getValues() {
            return values;
        }

        static {
            for (Waypoint.Origin o : values()) {
                values.add(o.value);
            }
        }
    }

    public static enum Type {

        Normal, Death
    }
}