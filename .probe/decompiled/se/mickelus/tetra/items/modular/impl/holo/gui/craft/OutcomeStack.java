package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class OutcomeStack {

    UpgradeSchematic schematic;

    OutcomePreview preview;

    public OutcomeStack(UpgradeSchematic schematic, OutcomePreview preview) {
        this.schematic = schematic;
        this.preview = preview;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OutcomeStack that = (OutcomeStack) o;
            return this.schematicEquals(that.schematic) && this.previewEquals(that.preview);
        } else {
            return false;
        }
    }

    public boolean schematicEquals(UpgradeSchematic schematic) {
        return Objects.equals(this.schematic.getKey(), schematic.getKey());
    }

    public boolean previewEquals(OutcomePreview preview) {
        return Objects.equals(this.preview, preview);
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.schematic, this.preview });
    }
}