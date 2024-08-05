package fr.frinn.custommachinery.common.machine.builder;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;
import fr.frinn.custommachinery.api.machine.MachineAppearanceProperty;
import fr.frinn.custommachinery.api.machine.MachineStatus;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.machine.MachineAppearance;
import fr.frinn.custommachinery.common.util.MachineModelLocation;
import fr.frinn.custommachinery.common.util.MachineShape;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class MachineAppearanceBuilder {

    @Nullable
    private final MachineStatus status;

    private final Map<MachineAppearanceProperty<?>, Object> properties;

    public MachineAppearanceBuilder(@Nullable MachineStatus status) {
        this.status = status;
        Builder<MachineAppearanceProperty<?>, Object> builder = ImmutableMap.builder();
        for (MachineAppearanceProperty<?> property : Registration.APPEARANCE_PROPERTY_REGISTRY) {
            builder.put(property, property.getDefaultValue());
        }
        this.properties = builder.build();
    }

    public MachineAppearanceBuilder(MachineAppearance appearance, @Nullable MachineStatus status) {
        this.status = status;
        this.properties = Maps.newHashMap(appearance.getProperties());
    }

    public MachineAppearanceBuilder(Map<MachineAppearanceProperty<?>, Object> properties, @Nullable MachineStatus status) {
        this.status = status;
        this.properties = Maps.newHashMap(properties);
    }

    @Nullable
    public MachineStatus getStatus() {
        return this.status;
    }

    public <T> T getProperty(MachineAppearanceProperty<T> property) {
        if (!this.properties.containsKey(property)) {
            throw new IllegalStateException("Can't get Machine Appearance property for: " + property.getId() + ", this property may not be registered");
        } else {
            return (T) this.properties.get(property);
        }
    }

    public <T> void setProperty(MachineAppearanceProperty<T> property, T value) {
        this.properties.put(property, value);
    }

    public MachineModelLocation getBlockModel() {
        return this.getProperty((MachineAppearanceProperty<MachineModelLocation>) Registration.BLOCK_MODEL_PROPERTY.get());
    }

    public void setBlockModel(MachineModelLocation blockModel) {
        this.setProperty((MachineAppearanceProperty<MachineModelLocation>) Registration.BLOCK_MODEL_PROPERTY.get(), blockModel);
    }

    public MachineModelLocation getItemModel() {
        return this.getProperty((MachineAppearanceProperty<MachineModelLocation>) Registration.ITEM_MODEL_PROPERTY.get());
    }

    public void setItemModel(MachineModelLocation itemModel) {
        this.setProperty((MachineAppearanceProperty<MachineModelLocation>) Registration.ITEM_MODEL_PROPERTY.get(), itemModel);
    }

    public SoundEvent getSound() {
        return this.getProperty((MachineAppearanceProperty<SoundEvent>) Registration.AMBIENT_SOUND_PROPERTY.get());
    }

    public void setSound(SoundEvent sound) {
        this.setProperty((MachineAppearanceProperty<SoundEvent>) Registration.AMBIENT_SOUND_PROPERTY.get(), sound);
    }

    public int getLightLevel() {
        return this.<Integer>getProperty((MachineAppearanceProperty<Integer>) Registration.LIGHT_PROPERTY.get());
    }

    public void setLightLevel(int lightLevel) {
        this.setProperty((MachineAppearanceProperty<Integer>) Registration.LIGHT_PROPERTY.get(), Mth.clamp(lightLevel, 0, 15));
    }

    public int getColor() {
        return this.<Integer>getProperty((MachineAppearanceProperty<Integer>) Registration.COLOR_PROPERTY.get());
    }

    public void setColor(int color) {
        this.setProperty((MachineAppearanceProperty<Integer>) Registration.COLOR_PROPERTY.get(), color);
    }

    public float getHardness() {
        return this.<Float>getProperty((MachineAppearanceProperty<Float>) Registration.HARDNESS_PROPERTY.get());
    }

    public void setHardness(float hardness) {
        this.setProperty((MachineAppearanceProperty<Float>) Registration.HARDNESS_PROPERTY.get(), Mth.clamp(hardness, 0.0F, Float.MAX_VALUE));
    }

    public float getResistance() {
        return this.<Float>getProperty((MachineAppearanceProperty<Float>) Registration.RESISTANCE_PROPERTY.get());
    }

    public void setResistance(float resistance) {
        this.setProperty((MachineAppearanceProperty<Float>) Registration.RESISTANCE_PROPERTY.get(), Mth.clamp(resistance, 0.0F, Float.MAX_VALUE));
    }

    public List<TagKey<Block>> getToolType() {
        return this.getProperty((MachineAppearanceProperty<List<TagKey<Block>>>) Registration.TOOL_TYPE_PROPERTY.get());
    }

    public void setToolType(TagKey<Block> toolType) {
        this.setProperty((MachineAppearanceProperty<List>) Registration.TOOL_TYPE_PROPERTY.get(), Collections.singletonList(toolType));
    }

    public TagKey<Block> getMiningLevel() {
        return this.getProperty((MachineAppearanceProperty<TagKey<Block>>) Registration.MINING_LEVEL_PROPERTY.get());
    }

    public void setMiningLevel(TagKey<Block> miningLevel) {
        this.setProperty((MachineAppearanceProperty<TagKey<Block>>) Registration.MINING_LEVEL_PROPERTY.get(), miningLevel);
    }

    public MachineShape getShape() {
        return this.getProperty((MachineAppearanceProperty<MachineShape>) Registration.SHAPE_PROPERTY.get());
    }

    public void setShape(MachineShape shape) {
        this.setProperty((MachineAppearanceProperty<MachineShape>) Registration.SHAPE_PROPERTY.get(), shape);
    }

    public MachineAppearance build() {
        return new MachineAppearance(ImmutableMap.copyOf(this.properties));
    }
}