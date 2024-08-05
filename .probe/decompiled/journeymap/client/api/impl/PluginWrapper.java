package journeymap.client.api.impl;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table.Cell;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.DisplayType;
import journeymap.client.api.display.Displayable;
import journeymap.client.api.display.ImageOverlay;
import journeymap.client.api.display.MarkerOverlay;
import journeymap.client.api.display.Overlay;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.display.Waypoint;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.model.MapImage;
import journeymap.client.api.util.UIState;
import journeymap.client.data.DataCache;
import journeymap.client.log.StatTimer;
import journeymap.client.render.draw.DrawImageStep;
import journeymap.client.render.draw.DrawMarkerStep;
import journeymap.client.render.draw.DrawPolygonStep;
import journeymap.client.render.draw.OverlayDrawStep;
import journeymap.client.texture.DynamicTextureImpl;
import journeymap.client.texture.ImageUtil;
import journeymap.client.texture.SimpleTextureImpl;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.CommonConstants;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

@ParametersAreNonnullByDefault
class PluginWrapper {

    private final IClientPlugin plugin;

    private final String modId;

    private final StatTimer eventTimer;

    private final HashMap<String, HashBasedTable<String, Overlay, OverlayDrawStep>> dimensionOverlays = new HashMap();

    private final HashBasedTable<String, Waypoint, journeymap.client.waypoint.Waypoint> waypoints = HashBasedTable.create();

    private EnumSet<ClientEvent.Type> subscribedClientEventTypes = EnumSet.noneOf(ClientEvent.Type.class);

    public PluginWrapper(IClientPlugin plugin) {
        this.modId = plugin.getModId();
        this.plugin = plugin;
        this.eventTimer = StatTimer.get("pluginClientEvent_" + this.modId, 1, 200);
    }

    private HashBasedTable<String, Overlay, OverlayDrawStep> getOverlays(ResourceKey<Level> dimension) {
        String dimName = DimensionHelper.getDimKeyName(dimension);
        HashBasedTable<String, Overlay, OverlayDrawStep> table = (HashBasedTable<String, Overlay, OverlayDrawStep>) this.dimensionOverlays.get(dimName);
        if (table == null) {
            table = HashBasedTable.create();
            this.dimensionOverlays.put(dimName, table);
        }
        return table;
    }

    public void show(Displayable displayable) throws Exception {
        String displayId = displayable.getId();
        switch(displayable.getDisplayType()) {
            case Polygon:
                PolygonOverlay polygon = (PolygonOverlay) displayable;
                DrawPolygonStep polygonStep = DataCache.INSTANCE.getDrawPolygonStep(polygon);
                polygonStep.setEnabled(true);
                this.getOverlays(polygon.getDimension()).put(displayId, polygon, polygonStep);
                break;
            case Marker:
                MarkerOverlay marker = (MarkerOverlay) displayable;
                DrawMarkerStep markerStep = DataCache.INSTANCE.getDrawMakerStep(marker);
                markerStep.setEnabled(true);
                this.getOverlays(marker.getDimension()).put(displayId, marker, markerStep);
                break;
            case Image:
                ImageOverlay imageOverlay = (ImageOverlay) displayable;
                DrawImageStep imageStep = DataCache.INSTANCE.getDrawImageStep(imageOverlay);
                imageStep.setEnabled(true);
                this.getOverlays(imageOverlay.getDimension()).put(displayId, imageOverlay, imageStep);
                break;
            case Waypoint:
                Waypoint modWaypoint = (Waypoint) displayable;
                journeymap.client.waypoint.Waypoint waypoint = new journeymap.client.waypoint.Waypoint(modWaypoint);
                if (modWaypoint.getIcon() != null && (modWaypoint.getIcon().getImageLocation() != null || modWaypoint.getIcon().getImage() != null)) {
                    waypoint.setIconColor(modWaypoint.getIcon().getColor());
                    waypoint.setIcon(this.getWaypointImageResource(modWaypoint));
                }
                waypoint.setDirty();
                WaypointStore.INSTANCE.save(waypoint, true);
                this.waypoints.put(displayId, modWaypoint, waypoint);
        }
    }

    @Nullable
    public Waypoint getWaypoint(String displayId) {
        return (Waypoint) WaypointStore.INSTANCE.getAll().stream().filter(wp -> this.modId.equals(wp.getOrigin()) && displayId.equals(wp.getDisplayId())).map(journeymap.client.waypoint.Waypoint::toModWaypoint).toList().stream().findAny().orElse(null);
    }

    public List<Waypoint> getWaypoints() {
        return (List<Waypoint>) WaypointStore.INSTANCE.getAll().stream().filter(wp -> this.modId.equals(wp.getOrigin())).map(journeymap.client.waypoint.Waypoint::toModWaypoint).collect(Collectors.toList());
    }

    private ResourceLocation getWaypointImageResource(Waypoint modWaypoint) {
        MapImage image = modWaypoint.getIcon();
        ResourceLocation resourceLocation = image.getImageLocation();
        Texture texture = null;
        if (resourceLocation == null) {
            resourceLocation = new ResourceLocation(modWaypoint.getModId(), CommonConstants.getSafeString(modWaypoint.getGuid(), "-").toLowerCase(Locale.ROOT));
            NativeImage coloredImage = ImageUtil.recolorImage(image.getImage(), image.getColor());
            Texture var12 = new DynamicTextureImpl(ImageUtil.getSizedImage((int) image.getDisplayWidth(), (int) image.getDisplayHeight(), coloredImage, true));
            Minecraft.getInstance().getTextureManager().register(resourceLocation, var12);
        } else {
            TextureManager manager = Minecraft.getInstance().getTextureManager();
            ResourceLocation fakeLocation = new ResourceLocation("fake", resourceLocation.getPath());
            if (manager.getTexture(fakeLocation, null) == null) {
                try (SimpleTextureImpl simpleTexture = new SimpleTextureImpl(resourceLocation)) {
                    NativeImage img = ImageUtil.getScaledImage(4.0F, simpleTexture.getNativeImage(), false);
                    DynamicTextureImpl scaledTexture = new DynamicTextureImpl(img, fakeLocation);
                    manager.register(fakeLocation, scaledTexture);
                    scaledTexture.setDisplayHeight((int) image.getDisplayHeight());
                    scaledTexture.setDisplayWidth((int) image.getDisplayWidth());
                    TextureCache.modTextureMap.put(resourceLocation, fakeLocation);
                }
            }
        }
        return resourceLocation;
    }

    public void remove(Displayable displayable) {
        String displayId = displayable.getId();
        try {
            switch(displayable.getDisplayType()) {
                case Waypoint:
                    this.remove((Waypoint) displayable);
                    break;
                default:
                    Overlay overlay = (Overlay) displayable;
                    OverlayDrawStep drawStep = (OverlayDrawStep) this.getOverlays(overlay.getDimension()).remove(displayId, displayable);
                    if (drawStep != null) {
                        drawStep.setEnabled(false);
                    }
            }
        } catch (Throwable var5) {
            Journeymap.getLogger().error("Error removing DrawMarkerStep: " + var5, LogFormatter.toString(var5));
        }
    }

    public void remove(Waypoint modWaypoint) {
        String displayId = modWaypoint.getId();
        journeymap.client.waypoint.Waypoint waypoint = (journeymap.client.waypoint.Waypoint) this.waypoints.remove(displayId, modWaypoint);
        if (waypoint == null) {
            waypoint = new journeymap.client.waypoint.Waypoint(modWaypoint);
        }
        WaypointStore.INSTANCE.remove(waypoint, true);
    }

    public void removeAll(DisplayType displayType) {
        if (displayType == DisplayType.Waypoint) {
            for (Waypoint modWaypoint : new ArrayList(this.waypoints.columnKeySet())) {
                this.remove(modWaypoint);
            }
        } else {
            for (HashBasedTable<String, Overlay, OverlayDrawStep> overlays : this.dimensionOverlays.values()) {
                for (Displayable displayable : new ArrayList(overlays.columnKeySet())) {
                    if (displayable.getDisplayType() == displayType) {
                        this.remove(displayable);
                        if (displayable instanceof PolygonOverlay polygonOverlay) {
                            DataCache.INSTANCE.invalidatePolygon(polygonOverlay);
                        }
                    }
                }
            }
        }
    }

    public void removeAll() {
        if (!this.waypoints.isEmpty()) {
            for (Waypoint modWaypoint : new ArrayList(this.waypoints.columnKeySet())) {
                this.remove(modWaypoint);
            }
        }
        if (!this.dimensionOverlays.isEmpty()) {
            this.dimensionOverlays.clear();
        }
    }

    public boolean exists(Displayable displayable) {
        String displayId = displayable.getId();
        switch(displayable.getDisplayType()) {
            case Waypoint:
                return this.waypoints.containsRow(displayId);
            default:
                if (displayable instanceof Overlay) {
                    ResourceKey<Level> dimension = ((Overlay) displayable).getDimension();
                    return this.getOverlays(dimension).containsRow(displayId);
                } else {
                    return false;
                }
        }
    }

    public void getDrawSteps(List<OverlayDrawStep> list, UIState uiState) {
        HashBasedTable<String, Overlay, OverlayDrawStep> table = this.getOverlays(uiState.dimension);
        for (Cell<String, Overlay, OverlayDrawStep> cell : table.cellSet()) {
            if (((Overlay) cell.getColumnKey()).isActiveIn(uiState)) {
                list.add((OverlayDrawStep) cell.getValue());
            }
        }
    }

    public void subscribe(EnumSet<ClientEvent.Type> enumSet) {
        this.subscribedClientEventTypes = EnumSet.copyOf(enumSet);
    }

    public EnumSet<ClientEvent.Type> getSubscribedClientEventTypes() {
        return this.subscribedClientEventTypes;
    }

    public void notify(ClientEvent clientEvent) {
        if (this.subscribedClientEventTypes.contains(clientEvent.type)) {
            try {
                boolean cancelled = clientEvent.isCancelled();
                boolean cancellable = clientEvent.type.cancellable;
                this.eventTimer.start();
                try {
                    this.plugin.onEvent(clientEvent);
                    if (cancellable && !cancelled && clientEvent.isCancelled()) {
                        Journeymap.getLogger().debug(String.format("Plugin %s cancelled event: %s", this, clientEvent.type));
                    }
                } catch (Throwable var9) {
                    Journeymap.getLogger().error(String.format("Plugin %s errored during event: %s", this, clientEvent.type), var9);
                } finally {
                    this.eventTimer.stop();
                    if (this.eventTimer.hasReachedElapsedLimit()) {
                        Journeymap.getLogger().warn(String.format("Plugin %s too slow handling event: %s", this, clientEvent.type));
                    }
                }
            } catch (Throwable var11) {
                Journeymap.getLogger().error(String.format("Plugin %s error during event: %s", this, clientEvent.type), var11);
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof PluginWrapper that) ? false : Objects.equal(this.modId, that.modId);
        }
    }

    public int hashCode() {
        return Objects.hashCode(new Object[] { this.modId });
    }

    public String toString() {
        return MoreObjects.toStringHelper(this.plugin).add("modId", this.modId).toString();
    }
}