package net.mehvahdjukaar.supplementaries.common.misc.map_markers;

import java.util.Optional;
import java.util.Set;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.map.type.CustomDecorationType;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.misc.DataObjectReference;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers.BeaconMarker;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers.BedMarker;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers.CeilingBannerMarker;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers.ChestMarker;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers.FlagMarker;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers.NetherPortalMarker;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers.SignPostMarker;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers.WaystoneMarker;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class ModMapMarkers {

    public static final CustomDecorationType<CustomMapDecoration, SignPostMarker> SIGN_POST_DECORATION_TYPE = CustomDecorationType.withWorldMarker(Supplementaries.res("sign_post"), SignPostMarker::new, SignPostMarker::getFromWorld, CustomMapDecoration::new);

    public static final CustomDecorationType<ColoredDecoration, BedMarker> BED_DECORATION_TYPE = CustomDecorationType.withWorldMarker(Supplementaries.res("bed"), BedMarker::new, BedMarker::getFromWorld, ColoredDecoration::new);

    public static final CustomDecorationType<ColoredDecoration, FlagMarker> FLAG_DECORATION_TYPE = CustomDecorationType.withWorldMarker(Supplementaries.res("flag"), FlagMarker::new, FlagMarker::getFromWorld, ColoredDecoration::new);

    public static final CustomDecorationType<CustomMapDecoration, NetherPortalMarker> NETHER_PORTAL_DECORATION_TYPE = CustomDecorationType.withWorldMarker(Supplementaries.res("nether_portal"), NetherPortalMarker::new, NetherPortalMarker::getFromWorld, CustomMapDecoration::new);

    public static final CustomDecorationType<CustomMapDecoration, BeaconMarker> BEACON_DECORATION_TYPE = CustomDecorationType.withWorldMarker(Supplementaries.res("beacon"), BeaconMarker::new, BeaconMarker::getFromWorld, CustomMapDecoration::new);

    public static final CustomDecorationType<ColoredDecoration, CeilingBannerMarker> BANNER_DECORATION_TYPE = CustomDecorationType.withWorldMarker(Supplementaries.res("banner"), CeilingBannerMarker::new, CeilingBannerMarker::getFromWorld, ColoredDecoration::new);

    public static final CustomDecorationType<CustomMapDecoration, ChestMarker> CHEST_DECORATION_TYPE = CustomDecorationType.withWorldMarker(Supplementaries.res("chest"), ChestMarker::new, ChestMarker::getFromWorld, CustomMapDecoration::new);

    public static final CustomDecorationType<CustomMapDecoration, WaystoneMarker> WAYSTONE_DECORATION_TYPE = CustomDecorationType.withWorldMarker(Supplementaries.res("waystone"), WaystoneMarker::new, WaystoneMarker::getFromWorld, CustomMapDecoration::new);

    public static final DataObjectReference<MapDecorationType<?, ?>> DEATH_MARKER = new DataObjectReference<>(Supplementaries.res("death_marker"), MapDataRegistry.REGISTRY_KEY);

    public static void init() {
        MapDataRegistry.registerCustomType(SIGN_POST_DECORATION_TYPE);
        MapDataRegistry.registerCustomType(BED_DECORATION_TYPE);
        MapDataRegistry.registerCustomType(FLAG_DECORATION_TYPE);
        MapDataRegistry.registerCustomType(NETHER_PORTAL_DECORATION_TYPE);
        MapDataRegistry.registerCustomType(BEACON_DECORATION_TYPE);
        MapDataRegistry.registerCustomType(BANNER_DECORATION_TYPE);
        MapDataRegistry.registerCustomType(CHEST_DECORATION_TYPE);
        MapDataRegistry.registerCustomType(WAYSTONE_DECORATION_TYPE);
        MapDataRegistry.addDynamicServerMarkersEvent(ModMapMarkers::getForPlayer);
    }

    public static Set<MapBlockMarker<?>> getForPlayer(Player player, int mapId, MapItemSavedData data) {
        Optional<GlobalPos> v = player.getLastDeathLocation();
        if (v.isPresent() && data.dimension.equals(((GlobalPos) v.get()).dimension()) && ((CommonConfigs.DeathMarkerMode) CommonConfigs.Tweaks.DEATH_MARKER.get()).isOn(player)) {
            MapBlockMarker<?> marker = DEATH_MARKER.get().createEmptyMarker();
            marker.setPos(((GlobalPos) v.get()).pos());
            marker.setName(Component.translatable("message.supplementaries.death_marker"));
            return Set.of(marker);
        } else {
            return Set.of();
        }
    }
}