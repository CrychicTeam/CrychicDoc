package journeymap.client.properties;

import journeymap.client.ui.minimap.Orientation;
import journeymap.client.ui.minimap.Position;
import journeymap.client.ui.minimap.ReticleOrientation;
import journeymap.client.ui.minimap.Shape;
import journeymap.client.ui.option.TimeFormat;
import journeymap.client.ui.theme.ThemeLabelSource;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.FloatField;
import journeymap.common.properties.config.IntegerField;
import journeymap.common.properties.config.StringField;
import net.minecraft.client.Minecraft;

public class MiniMapProperties extends InGameMapProperties {

    public final StringField gameTimeRealFormat = new StringField(Category.Inherit, "jm.common.time_format", TimeFormat.Provider.class);

    public final StringField systemTimeRealFormat = new StringField(Category.Inherit, "jm.common.system_time_format", TimeFormat.Provider.class);

    public final BooleanField enabled = new BooleanField(Category.Inherit, "jm.minimap.enable_minimap", true, true);

    public final EnumField<Shape> shape = new EnumField<>(Category.Inherit, "jm.minimap.shape", Shape.Circle);

    public final BooleanField showDayNight = new BooleanField(Category.Inherit, "jm.common.show_day_night", true);

    public final StringField info1Label = new StringField(Category.Inherit, "jm.minimap.info1_label.button", ThemeLabelSource.Blank.getKey(), ThemeLabelSource.class);

    public final StringField info2Label = new StringField(Category.Inherit, "jm.minimap.info2_label.button", ThemeLabelSource.GameTime.getKey(), ThemeLabelSource.class);

    public final StringField info3Label = new StringField(Category.Inherit, "jm.minimap.info3_label.button", ThemeLabelSource.Location.getKey(), ThemeLabelSource.class);

    public final StringField info4Label = new StringField(Category.Inherit, "jm.minimap.info4_label.button", ThemeLabelSource.Biome.getKey(), ThemeLabelSource.class);

    public final FloatField infoSlotAlpha = new FloatField(Category.Inherit, "jm.minimap.info_slot.background_alpha", 0.0F, 1.0F, 0.7F);

    public final FloatField positionX = new FloatField(Category.Hidden, "jm.minimap.position_x", 0.0F, 1.0F, 0.9F);

    public final FloatField positionY = new FloatField(Category.Hidden, "jm.minimap.position_y", 0.0F, 1.0F, 0.25F);

    public final IntegerField sizePercent = new IntegerField(Category.Inherit, "jm.minimap.size", 1, 100, 30);

    public final IntegerField frameAlpha = new IntegerField(Category.Inherit, "jm.minimap.frame_alpha", 0, 100, 100);

    public final IntegerField terrainAlpha = new IntegerField(Category.Inherit, "jm.minimap.terrain_alpha", 0, 100, 100);

    public final FloatField backgroundAlpha = new FloatField(Category.Inherit, "jm.minimap.terrain_background_alpha", 0.0F, 1.0F, 0.8F);

    public final EnumField<Orientation> orientation = new EnumField<>(Category.Inherit, "jm.minimap.orientation.button", Orientation.North);

    public final FloatField compassFontScale = new FloatField(Category.Inherit, "jm.minimap.compass_font_scale", 0.5F, 4.0F, 1.0F);

    public final BooleanField showCompass = new BooleanField(Category.Inherit, "jm.minimap.show_compass", true);

    public final BooleanField showReticle = new BooleanField(Category.Inherit, "jm.minimap.show_reticle", true);

    public final EnumField<ReticleOrientation> reticleOrientation = new EnumField<>(Category.Inherit, "jm.minimap.reticle_orientation", ReticleOrientation.Compass);

    public final BooleanField moveEffectIcons = new BooleanField(ClientCategory.MinimapPosition, "jm.hud.effects.enable", true);

    public final IntegerField effectTranslateX = new IntegerField(Category.Hidden, "jm.hud.effects.location.button", -10000, 10000, 0);

    public final IntegerField effectTranslateY = new IntegerField(Category.Hidden, "jm.hud.effects.location.button", -10000, 10000, 0);

    public final BooleanField effectVertical = new BooleanField(ClientCategory.MinimapPosition, "jm.hud.effects.style.vertical", false);

    public final BooleanField effectReversed = new BooleanField(ClientCategory.MinimapPosition, "jm.hud.effects.style.reverse", false);

    public final FloatField minimapKeyMovementSpeed = new FloatField(ClientCategory.MinimapPosition, "jm.hud.minimap.key_movement_speed", 0.001F, 0.025F, 0.001F, 0.001F, 3);

    public final EnumField<Position> position = new EnumField<>(ClientCategory.MinimapPosition, "jm.minimap.position", Position.TopRight);

    protected final transient int id;

    protected boolean active = false;

    public MiniMapProperties(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return String.format("minimap%s", this.id > 1 ? this.id : "");
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        if (this.active != active) {
            this.active = active;
            this.save();
        }
    }

    public int getId() {
        return this.id;
    }

    @Override
    public <T extends PropertiesBase> void updateFrom(T otherInstance) {
        super.updateFrom(otherInstance);
        if (otherInstance instanceof MiniMapProperties) {
            this.setActive(((MiniMapProperties) otherInstance).isActive());
        }
    }

    public int getSize() {
        return (int) Math.max(128.0, Math.floor((double) this.sizePercent.get().intValue() / 100.0 * (double) Minecraft.getInstance().getWindow().getScreenHeight()));
    }

    @Override
    protected void postLoad(boolean isNew) {
        super.postLoad(isNew);
        if (isNew) {
            if (this.getId() == 1) {
                this.setActive(true);
                if (Minecraft.getInstance() != null && Minecraft.getInstance().font.isBidirectional()) {
                    super.fontScale.set(Float.valueOf(2.0F));
                    this.compassFontScale.set(Float.valueOf(2.0F));
                }
            } else {
                this.setActive(false);
                this.positionX.set(Float.valueOf(90.0F));
                this.positionY.set(Float.valueOf(25.0F));
                this.position.set(Position.TopRight);
                this.shape.set(Shape.Rectangle);
                this.frameAlpha.set(Integer.valueOf(100));
                this.terrainAlpha.set(Integer.valueOf(100));
                this.orientation.set(Orientation.North);
                this.reticleOrientation.set(ReticleOrientation.Compass);
                this.sizePercent.set(Integer.valueOf(30));
                if (Minecraft.getInstance() != null && Minecraft.getInstance().font.isBidirectional()) {
                    super.fontScale.set(Float.valueOf(2.0F));
                    this.compassFontScale.set(Float.valueOf(2.0F));
                }
            }
        }
    }
}