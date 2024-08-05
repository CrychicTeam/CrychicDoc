package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class FossilFeatureConfiguration implements FeatureConfiguration {

    public static final Codec<FossilFeatureConfiguration> CODEC = RecordCodecBuilder.create(p_159816_ -> p_159816_.group(ResourceLocation.CODEC.listOf().fieldOf("fossil_structures").forGetter(p_159830_ -> p_159830_.fossilStructures), ResourceLocation.CODEC.listOf().fieldOf("overlay_structures").forGetter(p_159828_ -> p_159828_.overlayStructures), StructureProcessorType.LIST_CODEC.fieldOf("fossil_processors").forGetter(p_204759_ -> p_204759_.fossilProcessors), StructureProcessorType.LIST_CODEC.fieldOf("overlay_processors").forGetter(p_204757_ -> p_204757_.overlayProcessors), Codec.intRange(0, 7).fieldOf("max_empty_corners_allowed").forGetter(p_159818_ -> p_159818_.maxEmptyCornersAllowed)).apply(p_159816_, FossilFeatureConfiguration::new));

    public final List<ResourceLocation> fossilStructures;

    public final List<ResourceLocation> overlayStructures;

    public final Holder<StructureProcessorList> fossilProcessors;

    public final Holder<StructureProcessorList> overlayProcessors;

    public final int maxEmptyCornersAllowed;

    public FossilFeatureConfiguration(List<ResourceLocation> listResourceLocation0, List<ResourceLocation> listResourceLocation1, Holder<StructureProcessorList> holderStructureProcessorList2, Holder<StructureProcessorList> holderStructureProcessorList3, int int4) {
        if (listResourceLocation0.isEmpty()) {
            throw new IllegalArgumentException("Fossil structure lists need at least one entry");
        } else if (listResourceLocation0.size() != listResourceLocation1.size()) {
            throw new IllegalArgumentException("Fossil structure lists must be equal lengths");
        } else {
            this.fossilStructures = listResourceLocation0;
            this.overlayStructures = listResourceLocation1;
            this.fossilProcessors = holderStructureProcessorList2;
            this.overlayProcessors = holderStructureProcessorList3;
            this.maxEmptyCornersAllowed = int4;
        }
    }
}