package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.module.data.MaterialData;

@ParametersAreNonnullByDefault
public class HoloMaterialIntegrityStatGui extends HoloMaterialStatGui {

    public HoloMaterialIntegrityStatGui(int x, int y) {
        super(x, y, "integrity", LabelGetterBasic.integerLabel, data -> data.integrityGain);
    }

    @Override
    public void update(MaterialData current, MaterialData preview) {
        String gain = this.valueFormatter.getLabelMerged((double) current.integrityGain, (double) preview.integrityGain);
        String cost = this.valueFormatter.getLabelMerged((double) current.integrityCost, (double) preview.integrityCost);
        this.value.setString(gain + " " + cost);
    }
}