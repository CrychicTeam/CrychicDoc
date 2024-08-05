package se.mickelus.tetra.module.data;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import se.mickelus.tetra.properties.AttributeHelper;

@ParametersAreNonnullByDefault
public class MaterialVariantData extends VariantData {

    public ResourceLocation[] materials = new ResourceLocation[0];

    public MaterialMultiplier extract = new MaterialMultiplier();

    public VariantData combine(MaterialData material) {
        UniqueVariantData result = new UniqueVariantData();
        result.key = this.key + material.key;
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
        result.aspects = AspectData.merge(this.aspects, material.aspects);
        result.glyph = (GlyphData) Optional.ofNullable(this.extract.glyph).map(glyph -> new GlyphData(glyph.textureLocation, glyph.textureX, glyph.textureY, material.tints.glyph)).orElse(this.glyph);
        List<String> availableTextures = Arrays.asList(this.extract.availableTextures);
        result.models = (ModuleModel[]) Stream.concat(Arrays.stream(this.models), Arrays.stream(this.extract.models).map(model -> MaterialData.kneadModel(model, material, availableTextures))).toArray(ModuleModel[]::new);
        if (this.tags == null) {
            result.tags = material.tags;
        } else if (material.tags == null) {
            result.tags = this.tags;
        } else {
            result.tags = (Set<TagKey<Item>>) Stream.concat(this.tags.stream(), material.tags.stream()).collect(Collectors.toSet());
        }
        return result;
    }
}