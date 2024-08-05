package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import net.minecraft.resources.ResourceLocation;

public class SetRegHelper {

    private final ArtifactRegistrate reg;

    private final String id;

    public SetRegHelper(ArtifactRegistrate reg, String id) {
        this.reg = reg;
        this.id = id;
    }

    public ResourceLocation getId() {
        return new ResourceLocation(this.reg.getModid(), this.id);
    }

    public <I extends BaseArtifact> LinearFuncEntry regLinear(String id, double v, double s) {
        return this.reg.regLinear(id, this, v, s);
    }

    public SetBuilder<ArtifactSet, BaseArtifact, ArtifactRegistrate> regSet(int min, int max, String lang) {
        return this.reg.regSet(this.id, ArtifactSet::new, min, max, lang);
    }

    public <T extends SetEffect> SetEffectBuilder<T, ArtifactRegistrate> setEffect(String id, NonNullSupplier<T> sup) {
        return this.reg.setEffect(id, sup);
    }
}