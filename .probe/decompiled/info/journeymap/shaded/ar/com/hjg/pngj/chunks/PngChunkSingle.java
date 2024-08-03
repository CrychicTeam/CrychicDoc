package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;

public abstract class PngChunkSingle extends PngChunk {

    protected PngChunkSingle(String id, ImageInfo imgInfo) {
        super(id, imgInfo);
    }

    @Override
    public final boolean allowsMultiple() {
        return false;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        return 31 * result + (this.id == null ? 0 : this.id.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            PngChunkSingle other = (PngChunkSingle) obj;
            if (this.id == null) {
                if (other.id != null) {
                    return false;
                }
            } else if (!this.id.equals(other.id)) {
                return false;
            }
            return true;
        }
    }
}