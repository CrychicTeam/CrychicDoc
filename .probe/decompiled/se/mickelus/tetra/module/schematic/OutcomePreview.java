package se.mickelus.tetra.module.schematic;

import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.ToolData;

@ParametersAreNonnullByDefault
public class OutcomePreview {

    public String moduleKey;

    public String variantName;

    public String variantKey;

    public String category;

    public int level;

    public GlyphData glyph;

    public ItemStack itemStack;

    public SchematicType type;

    public ToolData tools;

    public ItemStack[] materials;

    public OutcomePreview(String moduleKey, String variantKey, String variantName, String category, int level, GlyphData glyph, ItemStack itemStack, SchematicType type, ToolData tools, ItemStack[] materials) {
        this.moduleKey = moduleKey;
        this.variantKey = variantKey;
        this.variantName = variantName;
        this.category = category;
        this.level = level;
        this.glyph = glyph;
        this.itemStack = itemStack;
        this.type = type;
        this.tools = tools;
        this.materials = materials;
    }

    public boolean isApplied(ItemStack itemStack, String slot) {
        return this.moduleKey != null ? false : (Boolean) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).flatMap(module -> CastOptional.cast(module, ItemModuleMajor.class)).map(module -> module.getImprovementLevel(itemStack, this.variantKey) == this.level).orElse(false);
    }

    public OutcomePreview clone() {
        return new OutcomePreview(this.moduleKey, this.variantKey, this.variantName, this.category, this.level, this.glyph, this.itemStack.copy(), this.type, this.tools, this.materials);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OutcomePreview preview = (OutcomePreview) o;
            return Objects.equals(this.moduleKey, preview.moduleKey) && Objects.equals(this.variantKey, preview.variantKey) && this.level == preview.level;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.moduleKey, this.variantKey });
    }
}