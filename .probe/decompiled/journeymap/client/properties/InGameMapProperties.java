package journeymap.client.properties;

import journeymap.client.ui.minimap.EntityDisplay;
import journeymap.client.ui.option.LocationFormat;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.FloatField;
import journeymap.common.properties.config.StringField;

public abstract class InGameMapProperties extends MapProperties {

    public final EnumField<EntityDisplay> playerDisplay = new EnumField<>(Category.Inherit, "jm.minimap.player_display", EntityDisplay.LargeDots);

    public final FloatField selfDisplayScale = new FloatField(Category.Inherit, "jm.minimap.self_display_scale", 0.01F, 5.0F, 1.0F);

    public final FloatField playerDisplayScale = new FloatField(Category.Inherit, "jm.minimap.player_display_scale", 0.01F, 5.0F, 1.0F);

    public final BooleanField showPlayerHeading = new BooleanField(Category.Inherit, "jm.minimap.player_heading", true);

    public final EnumField<EntityDisplay> mobDisplay = new EnumField<>(Category.Inherit, "jm.minimap.mob_display", EntityDisplay.LargeDots);

    public final FloatField mobDisplayScale = new FloatField(Category.Inherit, "jm.minimap.mob_display_scale", 0.01F, 5.0F, 1.0F);

    public final BooleanField showMobHeading = new BooleanField(Category.Inherit, "jm.minimap.mob_heading", true);

    public final BooleanField showMobs = new BooleanField(Category.Inherit, "jm.common.show_mobs", true);

    public final BooleanField showAnimals = new BooleanField(Category.Inherit, "jm.common.show_animals", true);

    public final BooleanField showVillagers = new BooleanField(Category.Inherit, "jm.common.show_villagers", true);

    public final BooleanField showPets = new BooleanField(Category.Inherit, "jm.common.show_pets", true);

    public final BooleanField showPlayers = new BooleanField(Category.Inherit, "jm.common.show_players", true);

    public final FloatField fontScale = new FloatField(Category.Inherit, "jm.common.font_scale", 0.5F, 5.0F, 1.0F);

    public final BooleanField showWaypointLabels = new BooleanField(Category.Inherit, "jm.minimap.show_waypointlabels", true);

    public final FloatField waypointLabelScale = new FloatField(Category.Inherit, "jm.minimap.waypointlabel_scale", 0.5F, 5.0F, 1.0F);

    public final FloatField waypointIconScale = new FloatField(Category.Inherit, "jm.minimap.waypointicon_scale", 1.0F, 5.0F, 1.0F);

    public final BooleanField locationFormatVerbose = new BooleanField(Category.Inherit, "jm.common.location_format_verbose", true);

    public final StringField locationFormat = new StringField(Category.Inherit, "jm.common.location_format", LocationFormat.IdProvider.class);

    protected InGameMapProperties() {
    }

    @Override
    protected void postLoad(boolean isNew) {
        if (!isNew) {
            this.optionFix(this.playerDisplay);
            this.optionFix(this.mobDisplay);
        }
        super.postLoad(isNew);
    }

    protected void optionFix(EnumField<EntityDisplay> option) {
        if (option.get("value").toString().contains("Dots")) {
            option.set(EntityDisplay.LargeDots);
            this.save();
        } else if (option.get("value").toString().contains("Icons")) {
            option.set(EntityDisplay.LargeIcons);
        }
    }
}