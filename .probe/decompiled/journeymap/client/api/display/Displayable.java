package journeymap.client.api.display;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.gson.annotations.Since;
import java.util.UUID;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class Displayable implements Comparable<Displayable> {

    @Since(1.1)
    protected final String modId;

    @Since(1.1)
    protected final String id;

    @Since(1.1)
    protected final DisplayType displayType;

    private Displayable() {
        this.modId = null;
        this.id = null;
        this.displayType = null;
    }

    protected Displayable(String modId) {
        this(modId, UUID.randomUUID().toString());
    }

    protected Displayable(String modId, String displayId) {
        if (Strings.isNullOrEmpty(modId)) {
            throw new IllegalArgumentException("modId may not be blank");
        } else if (Strings.isNullOrEmpty(displayId)) {
            throw new IllegalArgumentException("displayId may not be blank");
        } else {
            this.modId = modId;
            this.id = displayId;
            this.displayType = DisplayType.of(this.getClass());
        }
    }

    public static int clampRGB(int rgb) {
        return 0xFF000000 | rgb;
    }

    public static float clampOpacity(float opacity) {
        return Math.max(0.0F, Math.min(opacity, 1.0F));
    }

    public abstract int getDisplayOrder();

    public final String getModId() {
        return this.modId;
    }

    public final String getId() {
        return this.id;
    }

    public final DisplayType getDisplayType() {
        return this.displayType;
    }

    public final String getGuid() {
        return Joiner.on("-").join(this.modId, this.displayType, new Object[] { this.id });
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof Displayable that) ? false : Objects.equal(this.modId, that.modId) && Objects.equal(this.displayType, that.displayType) && Objects.equal(this.id, that.id);
        }
    }

    public int hashCode() {
        return Objects.hashCode(new Object[] { this.modId, this.displayType, this.id });
    }

    public int compareTo(Displayable o) {
        return Integer.compare(this.getDisplayOrder(), o.getDisplayOrder());
    }
}