package journeymap.client.render.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Stack;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.DisplayType;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.ShapeProperties;
import journeymap.client.api.model.TextProperties;
import journeymap.client.api.util.PolygonHelper;
import journeymap.client.io.nbt.RegionLoader;
import journeymap.client.model.RegionCoord;
import journeymap.client.ui.fullscreen.Fullscreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class RegionRenderer {

    private static RegionRenderer instance;

    public static boolean TOGGLED = false;

    private RegionRenderer() {
    }

    public static void render(boolean toggled) {
        if (instance == null) {
            instance = new RegionRenderer();
        }
        TOGGLED = toggled;
        if (toggled) {
            ClientAPI.INSTANCE.flagOverlaysForRerender();
            PolygonOverlay overlay = instance.createOverlay((RegionCoord) instance.getRegions().firstElement());
            ClientAPI.INSTANCE.show(overlay);
        } else {
            ClientAPI.INSTANCE.removeAll("journeymap", DisplayType.Polygon);
        }
    }

    private Stack<RegionCoord> getRegions() {
        try {
            Minecraft minecraft = Minecraft.getInstance();
            RegionLoader regionLoader = new RegionLoader(minecraft, Fullscreen.state().getMapType(), true);
            return regionLoader.getRegions();
        } catch (Exception var3) {
            throw new RuntimeException("Unable to load regions", var3);
        }
    }

    protected PolygonOverlay createOverlay(RegionCoord rCoord) {
        String displayId = "Region Display" + rCoord;
        String groupName = "Region";
        String label = "x:" + rCoord.regionX + ", z:" + rCoord.regionZ;
        ShapeProperties shapeProps = new ShapeProperties().setStrokeWidth(5.0F).setStrokeColor(16711680).setStrokeOpacity(0.7F).setFillOpacity(0.2F);
        TextProperties textProps = new TextProperties().setBackgroundColor(34).setBackgroundOpacity(0.5F).setColor(65280).setOpacity(1.0F).setFontShadow(true);
        int x = rCoord.getMinChunkX() << 4;
        int y = 70;
        int z = rCoord.getMinChunkZ() << 4;
        int maxX = (rCoord.getMaxChunkX() << 4) + 15;
        int maxZ = (rCoord.getMaxChunkZ() << 4) + 15;
        List<BlockPos> blockPosList = new ArrayList();
        blockPosList.add(new BlockPos(384, 256, 128));
        blockPosList.add(new BlockPos(144, 256, 304));
        blockPosList.add(new BlockPos(144, 256, 224));
        blockPosList.add(new BlockPos(160, 256, 224));
        blockPosList.add(new BlockPos(160, 256, 128));
        blockPosList.add(new BlockPos(272, 256, 128));
        blockPosList.add(new BlockPos(272, 256, 144));
        blockPosList.add(new BlockPos(288, 256, 144));
        blockPosList.add(new BlockPos(288, 256, 128));
        blockPosList.add(new BlockPos(368, 256, 128));
        MapPolygon hole = PolygonHelper.createChunkPolygonForWorldCoords(284, 70, 197);
        PolygonOverlay overlay = new PolygonOverlay("journeymap", displayId, rCoord.dimension, shapeProps, new MapPolygon(blockPosList), Collections.singletonList(hole));
        overlay.setOverlayGroupName(groupName).setTitle("Test Title").setLabel(label).setTextProperties(textProps).setActiveUIs(EnumSet.of(Context.UI.Fullscreen, Context.UI.Minimap, Context.UI.Webmap)).setActiveMapTypes(EnumSet.of(Context.MapType.Any));
        return overlay;
    }
}