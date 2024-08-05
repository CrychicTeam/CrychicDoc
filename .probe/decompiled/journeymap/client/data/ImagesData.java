package journeymap.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageCache;

public class ImagesData {

    public static final String PARAM_SINCE = "images.since";

    final long since;

    final List<Object[]> regions;

    final long queryTime;

    public ImagesData(Long since) {
        long now = new Date().getTime();
        this.queryTime = now;
        this.since = since == null ? now : since;
        List<RegionCoord> dirtyRegions = RegionImageCache.INSTANCE.getChangedSince(null, this.since);
        if (dirtyRegions.isEmpty()) {
            this.regions = Collections.EMPTY_LIST;
        } else {
            this.regions = new ArrayList(dirtyRegions.size());
            for (RegionCoord rc : dirtyRegions) {
                this.regions.add(new Integer[] { rc.regionX, rc.regionZ });
            }
        }
    }
}