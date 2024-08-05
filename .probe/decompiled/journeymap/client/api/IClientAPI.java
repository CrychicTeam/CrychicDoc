package journeymap.client.api;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.DisplayType;
import journeymap.client.api.display.Displayable;
import journeymap.client.api.display.Waypoint;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.util.UIState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

@ParametersAreNonnullByDefault
public interface IClientAPI {

    String API_OWNER = "journeymap";

    String API_VERSION = "1.9-SNAPSHOT";

    @Nullable
    UIState getUIState(Context.UI var1);

    void subscribe(String var1, EnumSet<ClientEvent.Type> var2);

    void show(Displayable var1) throws Exception;

    void remove(Displayable var1);

    void removeAll(String var1, DisplayType var2);

    void removeAll(String var1);

    boolean exists(Displayable var1);

    boolean playerAccepts(String var1, DisplayType var2);

    void requestMapTile(String var1, ResourceKey<Level> var2, Context.MapType var3, ChunkPos var4, ChunkPos var5, @Nullable Integer var6, int var7, boolean var8, Consumer<NativeImage> var9);

    void toggleDisplay(@Nullable ResourceKey<Level> var1, Context.MapType var2, Context.UI var3, boolean var4);

    void toggleWaypoints(@Nullable ResourceKey<Level> var1, Context.MapType var2, Context.UI var3, boolean var4);

    boolean isDisplayEnabled(@Nullable ResourceKey<Level> var1, Context.MapType var2, Context.UI var3);

    boolean isWaypointsEnabled(@Nullable ResourceKey<Level> var1, Context.MapType var2, Context.UI var3);

    @Nullable
    File getDataPath(String var1);

    List<Waypoint> getAllWaypoints();

    List<Waypoint> getAllWaypoints(ResourceKey<Level> var1);

    @Nullable
    Waypoint getWaypoint(String var1, String var2);

    List<Waypoint> getWaypoints(String var1);

    @Deprecated
    void setWorldId(String var1);

    String getWorldId();
}