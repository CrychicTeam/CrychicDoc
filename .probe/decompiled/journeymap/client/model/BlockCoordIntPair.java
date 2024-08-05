package journeymap.client.model;

public class BlockCoordIntPair {

    public int x;

    public int z;

    public BlockCoordIntPair() {
        this.setLocation(0, 0);
    }

    public BlockCoordIntPair(int x, int z) {
        this.setLocation(x, z);
    }

    public void setLocation(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            BlockCoordIntPair that = (BlockCoordIntPair) o;
            return this.x != that.x ? false : this.z == that.z;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.x;
        return 31 * result + this.z;
    }
}