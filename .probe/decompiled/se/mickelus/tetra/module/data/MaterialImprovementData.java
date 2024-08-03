package se.mickelus.tetra.module.data;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.properties.AttributeHelper;

@ParametersAreNonnullByDefault
public class MaterialImprovementData extends ImprovementData {

    public ResourceLocation[] materials = new ResourceLocation[0];

    public MaterialMultiplier extract = new MaterialMultiplier();

    public ImprovementData combine(MaterialData material) {
        UniqueImprovementData result = new UniqueImprovementData();
        result.key = this.key + material.key;
        result.level = this.level;
        result.group = this.group;
        result.enchantment = this.enchantment;
        result.aspects = AspectData.merge(this.aspects, material.aspects);
        if (material.category != null) {
            result.category = material.category;
        }
        result.attributes = AttributeHelper.collapseRound(AttributeHelper.merge(Arrays.asList(this.attributes, material.attributes, AttributeHelper.multiplyModifiers(this.extract.primaryAttributes, (double) material.primary.floatValue()), AttributeHelper.multiplyModifiers(this.extract.secondaryAttributes, (double) material.secondary.floatValue()), AttributeHelper.multiplyModifiers(this.extract.tertiaryAttributes, (double) material.tertiary.floatValue()))));
        result.durability = Math.round((float) this.durability + (Float) Optional.ofNullable(this.extract.durability).map(extracted -> extracted * material.durability).orElse(0.0F));
        result.durabilityMultiplier = this.durabilityMultiplier + (Float) Optional.ofNullable(this.extract.durabilityMultiplier).map(extracted -> extracted * material.durability).orElse(0.0F);
        result.integrity = this.integrity + (Integer) Optional.ofNullable(this.extract.integrity).map(extracted -> extracted * (extracted > 0.0F ? material.integrityGain : material.integrityCost)).map(Math::round).orElse(0);
        result.magicCapacity = Math.round((float) this.magicCapacity + (Float) Optional.ofNullable(this.extract.magicCapacity).map(extracted -> extracted * (float) material.magicCapacity).orElse(0.0F));
        result.effects = EffectData.merge(Arrays.asList(this.effects, material.effects, EffectData.multiply(this.extract.primaryEffects, material.primary, material.primary), EffectData.multiply(this.extract.secondaryEffects, material.secondary, material.secondary), EffectData.multiply(this.extract.tertiaryEffects, material.tertiary, material.tertiary)));
        result.tools = ToolData.merge(Arrays.asList(this.tools, ToolData.multiply(this.extract.tools, (float) material.toolLevel, material.toolEfficiency)));
        result.glyph = (GlyphData) Optional.ofNullable(this.extract.glyph).map(glyph -> new GlyphData(glyph.textureLocation, glyph.textureX, glyph.textureY, material.tints.glyph)).orElse(this.glyph);
        List<String> availableTextures = Arrays.asList(this.extract.availableTextures);
        result.models = (ModuleModel[]) Stream.concat(Arrays.stream(this.models), Arrays.stream(this.extract.models).map(model -> MaterialData.kneadModel(model, material, availableTextures))).toArray(ModuleModel[]::new);
        return result;
    }
}