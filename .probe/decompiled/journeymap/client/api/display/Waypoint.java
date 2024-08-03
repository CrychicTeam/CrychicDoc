package journeymap.client.api.display;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.gson.annotations.Since;
import java.util.Arrays;
import javax.annotation.Nullable;
import journeymap.client.api.model.WaypointBase;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

public class Waypoint extends WaypointBase<Waypoint> {

    public static final double VERSION = 1.5;

    protected final transient Waypoint.CachedDimPosition cachedDimPosition = new Waypoint.CachedDimPosition();

    @Since(1.4)
    protected final double version = 1.5;

    @Since(1.4)
    protected String dim;

    @Since(1.4)
    protected BlockPos pos;

    @Since(1.4)
    protected WaypointGroup group;

    @Since(1.4)
    protected boolean persistent = true;

    @Since(1.4)
    protected boolean editable = true;

    @Since(1.5)
    protected boolean enabled = true;

    public Waypoint(String modId, String name, ResourceKey<Level> dimension, BlockPos position) {
        super(modId, name);
        this.setPosition(dimension.location().toString(), position);
    }

    public Waypoint(String modId, String id, String name, ResourceKey<Level> dimension, BlockPos position) {
        super(modId, id, name);
        this.setPosition(dimension.location().toString(), position);
    }

    public Waypoint(String modId, String id, String name, String dimension, BlockPos position) {
        super(modId, id, name);
        this.setPosition(dimension, position);
    }

    public Waypoint(String modId, String name, String dimension, BlockPos position) {
        super(modId, name);
        this.setPosition(dimension, position);
    }

    public final WaypointGroup getGroup() {
        return this.group;
    }

    public Waypoint setGroup(@Nullable WaypointGroup group) {
        this.group = group;
        return this.setDirty();
    }

    public final String getDimension() {
        return this.dim;
    }

    public final BlockPos getPosition() {
        return this.pos;
    }

    public BlockPos getPosition(String targetDimension) {
        return this.cachedDimPosition.getPosition(targetDimension);
    }

    private BlockPos getInternalPosition(String targetDimension) {
        if (!this.dim.equalsIgnoreCase(targetDimension)) {
            if (this.dim.equalsIgnoreCase(Level.NETHER.location().toString())) {
                this.pos = new BlockPos(this.pos.m_123341_() * 8, this.pos.m_123342_(), this.pos.m_123343_() * 8);
            } else if (targetDimension.equalsIgnoreCase(Level.NETHER.location().toString())) {
                this.pos = BlockPos.containing((double) this.pos.m_123341_() / 8.0, (double) this.pos.m_123342_(), (double) this.pos.m_123343_() / 8.0);
            }
        }
        return this.pos;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Waypoint setPosition(String dimension, BlockPos position) {
        if (position == null) {
            throw new IllegalArgumentException("position may not be null");
        } else {
            this.dim = dimension;
            this.pos = position;
            this.cachedDimPosition.reset();
            return this.setDirty();
        }
    }

    public Vector3d getVec(String dimension) {
        return this.cachedDimPosition.getVec(dimension);
    }

    public Vector3d getCenteredVec(String dimension) {
        return this.cachedDimPosition.getCenteredVec(dimension);
    }

    public final boolean isPersistent() {
        return this.persistent;
    }

    public final Waypoint setPersistent(boolean persistent) {
        this.persistent = persistent;
        if (!persistent) {
            this.dirty = false;
        }
        return this.setDirty();
    }

    public final boolean isEditable() {
        return this.editable;
    }

    public final Waypoint setEditable(boolean editable) {
        this.editable = editable;
        return this.setDirty();
    }

    public final boolean isTeleportReady(String targetDimension) {
        BlockPos pos = this.getPosition(targetDimension);
        return pos != null && pos.m_123342_() >= 0;
    }

    protected WaypointGroup getDelegate() {
        return this.getGroup();
    }

    @Override
    protected boolean hasDelegate() {
        return this.group != null;
    }

    @Override
    public String[] getDisplayDimensions() {
        String[] dims = super.getDisplayDimensions();
        if (dims == null) {
            this.setDisplayDimensions(new String[] { this.dim });
        }
        return this.displayDims;
    }

    @Override
    public int getDisplayOrder() {
        return this.group != null ? this.group.getDisplayOrder() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Waypoint)) {
            return false;
        } else if (!super.equals(o)) {
            return false;
        } else {
            Waypoint that = (Waypoint) o;
            return this.isPersistent() == that.isPersistent() && this.isEditable() == that.isEditable() && Objects.equal(this.getDimension(), that.getDimension()) && Objects.equal(this.getColor(), that.getColor()) && Objects.equal(this.getBackgroundColor(), that.getBackgroundColor()) && Objects.equal(this.getName(), that.getName()) && Objects.equal(this.getPosition(), that.getPosition()) && Objects.equal(this.getIcon(), that.getIcon()) && Arrays.equals(this.getDisplayDimensions(), that.getDisplayDimensions());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(new Object[] { super.hashCode(), this.getName() });
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", this.name).add("dim", this.dim).add("pos", this.pos).add("group", this.group).add("icon", this.icon).add("color", this.color).add("bgColor", this.bgColor).add("displayDims", this.displayDims == null ? null : Lists.newArrayList(this.displayDims)).add("editable", this.editable).add("persistent", this.persistent).add("dirty", this.dirty).toString();
    }

    class CachedDimPosition {

        String cachedDim;

        BlockPos cachedPos;

        Vector3d cachedVec;

        Vector3d cachedCenteredVec;

        Waypoint.CachedDimPosition reset() {
            this.cachedDim = null;
            this.cachedPos = null;
            this.cachedVec = null;
            this.cachedCenteredVec = null;
            return this;
        }

        private Waypoint.CachedDimPosition ensure(String dimension) {
            if (this.cachedDim != dimension) {
                this.cachedDim = dimension;
                this.cachedPos = Waypoint.this.getInternalPosition(dimension);
                this.cachedVec = new Vector3d((double) this.cachedPos.m_123341_(), (double) this.cachedPos.m_123342_(), (double) this.cachedPos.m_123343_());
                this.cachedCenteredVec = new Vector3d(0.5, 0.5, 0.5);
                this.cachedCenteredVec.add(this.cachedVec);
            }
            return this;
        }

        public BlockPos getPosition(String dimension) {
            return this.ensure(dimension).cachedPos;
        }

        public Vector3d getVec(String dimension) {
            return this.ensure(dimension).cachedVec;
        }

        public Vector3d getCenteredVec(String dimension) {
            return this.ensure(dimension).cachedCenteredVec;
        }
    }
}