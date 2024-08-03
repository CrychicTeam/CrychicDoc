package se.mickelus.tetra.module.data;

import com.google.common.collect.Multimap;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.properties.AttributeHelper;

@ParametersAreNonnullByDefault
public class TweakData {

    private final VariantData properties = new VariantData();

    public String variant;

    public String improvement;

    public String key;

    public int steps;

    public ItemProperties getProperties(int step) {
        return this.properties.multiply((float) step);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(int step) {
        return AttributeHelper.multiplyModifiers(this.properties.attributes, (double) step);
    }

    public ToolData getToolData(int step) {
        return ToolData.multiply(this.properties.tools, (float) step, (float) step);
    }

    public EffectData getEffectData(int step) {
        return EffectData.multiply(this.properties.effects, (float) step, (float) step);
    }

    public int getEffectLevel(ItemEffect effect, int step) {
        return step * this.properties.effects.getLevel(effect);
    }

    public int getToolLevel(ToolAction tool, int step) {
        return step * this.properties.tools.getLevel(tool);
    }
}