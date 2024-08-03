package se.mickelus.tetra.module.schematic;

import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.ToolData;

public class OutcomePreviewEnchantment extends OutcomePreview {

    public OutcomePreviewEnchantment(String variantKey, String variantName, String category, int level, GlyphData glyph, ItemStack itemStack, SchematicType type, ToolData tools, ItemStack[] materials) {
        super(null, variantKey, variantName, category, level, glyph, itemStack, type, tools, materials);
    }

    @Override
    public boolean isApplied(ItemStack itemStack, String slot) {
        return CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).flatMap(module -> CastOptional.cast(module, ItemModuleMajor.class)).map(module -> module.getEnchantmentsPrimitive(itemStack)).filter(enchantments -> enchantments.containsKey(this.variantKey)).filter(enchantments -> (Integer) enchantments.get(this.variantKey) == this.level).isPresent();
    }

    @Override
    public OutcomePreview clone() {
        return new OutcomePreviewEnchantment(this.variantKey, this.variantName, this.category, this.level, this.glyph, this.itemStack.copy(), this.type, this.tools, this.materials);
    }
}