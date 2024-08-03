package journeymap.client.render.map;

public class TilePos implements Comparable<TilePos> {

    public final int deltaX;

    public final int deltaZ;

    final double startX;

    final double startZ;

    final double endX;

    final double endZ;

    TilePos(int deltaX, int deltaZ) {
        this.deltaX = deltaX;
        this.deltaZ = deltaZ;
        this.startX = (double) (deltaX * 512);
        this.startZ = (double) (deltaZ * 512);
        this.endX = this.startX + 512.0;
        this.endZ = this.startZ + 512.0;
    }

    public int hashCode() {
        int prime = 37;
        int result = 1;
        result = 37 * result + this.deltaX;
        return 37 * result + this.deltaZ;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            TilePos other = (TilePos) obj;
            return this.deltaX != other.deltaX ? false : this.deltaZ == other.deltaZ;
        }
    }

    public String toString() {
        return "TilePos [" + this.deltaX + "," + this.deltaZ + "]";
    }

    public int compareTo(TilePos o) {
        int result = Integer.compare(this.deltaZ, o.deltaZ);
        if (result == 0) {
            result = Integer.compare(this.deltaX, o.deltaX);
        }
        return result;
    }
}