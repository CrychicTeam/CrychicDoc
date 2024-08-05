package journeymap.client.api.model;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.gson.annotations.Since;
import java.util.Arrays;
import javax.annotation.Nullable;
import journeymap.client.api.display.Displayable;
import journeymap.client.api.display.IWaypointDisplay;
import org.apache.commons.lang3.ArrayUtils;

public abstract class WaypointBase<T extends WaypointBase> extends Displayable implements IWaypointDisplay {

    @Since(1.4)
    protected String name;

    @Since(1.4)
    protected Integer color;

    @Since(1.4)
    protected Integer bgColor;

    @Since(1.4)
    protected MapImage icon;

    @Since(1.4)
    protected String[] displayDims;

    @Since(1.4)
    protected transient boolean dirty;

    protected WaypointBase(String modId, String name) {
        super(modId);
        this.setName(name);
    }

    protected WaypointBase(String modId, String id, String name) {
        super(modId, id);
        this.setName(name);
    }

    protected abstract IWaypointDisplay getDelegate();

    protected abstract boolean hasDelegate();

    public final String getName() {
        return this.name;
    }

    public final T setName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("name may not be blank");
        } else {
            this.name = name;
            return this.setDirty();
        }
    }

    @Override
    public final Integer getColor() {
        return this.color == null && this.hasDelegate() ? this.getDelegate().getColor() : this.color;
    }

    public final T setColor(int color) {
        this.color = clampRGB(color);
        return this.setDirty();
    }

    public final T clearColor() {
        this.color = null;
        return this.setDirty();
    }

    @Override
    public final Integer getBackgroundColor() {
        return this.bgColor == null && this.hasDelegate() ? this.getDelegate().getBackgroundColor() : this.bgColor;
    }

    public final T setBackgroundColor(int bgColor) {
        this.bgColor = clampRGB(bgColor);
        return this.setDirty();
    }

    public final T clearBackgroundColor() {
        this.bgColor = null;
        return this.setDirty();
    }

    @Override
    public String[] getDisplayDimensions() {
        return this.displayDims == null && this.hasDelegate() ? this.getDelegate().getDisplayDimensions() : this.displayDims;
    }

    public final T setDisplayDimensions(String... dimensions) {
        this.displayDims = dimensions;
        return this.setDirty();
    }

    public final T clearDisplayDimensions() {
        this.displayDims = null;
        return this.setDirty();
    }

    public void setDisplayed(String dimension, boolean displayed) {
        if (displayed && !this.isDisplayed(dimension)) {
            this.setDisplayDimensions((String[]) ArrayUtils.add(this.getDisplayDimensions(), dimension));
        } else if (!displayed && this.isDisplayed(dimension)) {
            this.setDisplayDimensions((String[]) ArrayUtils.removeElement(this.getDisplayDimensions(), dimension));
        }
    }

    public final boolean isDisplayed(String dimension) {
        return Arrays.binarySearch(this.getDisplayDimensions(), dimension) > -1;
    }

    @Override
    public MapImage getIcon() {
        return this.icon == null && this.hasDelegate() ? this.getDelegate().getIcon() : this.icon;
    }

    public final T setIcon(@Nullable MapImage icon) {
        this.icon = icon;
        return this.setDirty();
    }

    public final T clearIcon() {
        this.icon = null;
        return this.setDirty();
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public T setDirty(boolean dirty) {
        this.dirty = dirty;
        return (T) this;
    }

    public T setDirty() {
        return this.setDirty(true);
    }

    public boolean hasIcon() {
        return this.icon != null;
    }

    public boolean hasColor() {
        return this.color != null;
    }

    public boolean hasBackgroundColor() {
        return this.bgColor != null;
    }

    public boolean hasDisplayDimensions() {
        return this.displayDims != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof WaypointBase)) {
            return false;
        } else if (!super.equals(o)) {
            return false;
        } else {
            WaypointBase<?> that = (WaypointBase<?>) o;
            return Objects.equal(this.getName(), that.getName()) && Objects.equal(this.getIcon(), that.getIcon()) && Objects.equal(this.getColor(), that.getColor()) && Objects.equal(this.getBackgroundColor(), that.getBackgroundColor()) && Arrays.equals(this.getDisplayDimensions(), that.getDisplayDimensions());
        }
    }
}