package journeymap.client.waypoint;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.texture.TextureCache;
import journeymap.common.Journeymap;
import net.minecraft.resources.ResourceLocation;

public class JmReader {

    public Collection<Waypoint> loadWaypoints(File waypointDir) {
        ArrayList<Waypoint> waypoints = new ArrayList();
        File[] files = waypointDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".json") && !name.equals("waypoint_groups.json");
            }
        });
        if (files != null && files.length != 0) {
            ArrayList<File> obsoleteFiles = new ArrayList();
            for (File waypointFile : files) {
                Waypoint wp = this.load(waypointFile);
                if (wp != null) {
                    if (!wp.getFileName().endsWith(waypointFile.getName())) {
                        wp.setDirty(true);
                        obsoleteFiles.add(waypointFile);
                    }
                    waypoints.add(wp);
                }
            }
            while (!obsoleteFiles.isEmpty()) {
                this.remove((File) obsoleteFiles.remove(0));
            }
            return waypoints;
        } else {
            return waypoints;
        }
    }

    private void remove(File waypointFile) {
        try {
            waypointFile.delete();
        } catch (Exception var3) {
            Journeymap.getLogger().warn(String.format("Can't delete waypoint file %s: %s", waypointFile, var3.getMessage()));
            waypointFile.deleteOnExit();
        }
    }

    private Waypoint load(File waypointFile) {
        String waypointString = null;
        Waypoint waypoint = null;
        try {
            waypointString = Files.toString(waypointFile, Charset.forName("UTF-8"));
            waypoint = Waypoint.fromString(waypointString);
            this.migrateWaypoint(waypoint);
            return waypoint;
        } catch (Throwable var5) {
            Journeymap.getLogger().error(String.format("Can't load waypoint file %s with contents: %s because %s", waypointFile, waypointString, var5.getMessage()));
            return waypoint;
        }
    }

    private Waypoint migrateWaypoint(Waypoint waypoint) {
        List<String> dims = Lists.newArrayList();
        boolean hasOldDims = false;
        for (String dim : waypoint.getDimensions()) {
            if ("0".equals(dim) || "overworld".equalsIgnoreCase(dim)) {
                hasOldDims = true;
                dims.add("minecraft:overworld");
            } else if ("1".equals(dim) || "the_end".equalsIgnoreCase(dim)) {
                hasOldDims = true;
                dims.add("minecraft:the_end");
            } else if ("-1".equals(dim) || "the_nether".equalsIgnoreCase(dim)) {
                hasOldDims = true;
                dims.add("minecraft:the_nether");
            }
        }
        if ("waypoint-death-icon-map.png".equals(waypoint.icon) || "waypoint-death.png".equals(waypoint.icon)) {
            waypoint.setIcon(TextureCache.Deathpoint);
        }
        if ("waypoint-normal.png".equals(waypoint.icon)) {
            waypoint.setIcon(TextureCache.Waypoint);
        }
        if (waypoint.colorizedIcon == null) {
            waypoint.setIcon(new ResourceLocation(waypoint.icon));
        }
        if (waypoint.iconColor == null) {
            waypoint.iconColor = -1;
            waypoint.setDirty();
        }
        if (hasOldDims) {
            waypoint.setDimensions(dims);
        }
        if (Waypoint.Origin.TEMP.value.equals(waypoint.origin) && !waypoint.getId().contains(Constants.getString("jm.waypoint.temp"))) {
            return waypoint;
        } else {
            waypoint.updateId();
            waypoint.setDirty();
            return waypoint;
        }
    }
}