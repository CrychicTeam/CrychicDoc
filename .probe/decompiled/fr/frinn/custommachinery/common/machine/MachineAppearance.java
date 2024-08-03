package fr.frinn.custommachinery.common.machine;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.DataResult.PartialResult;
import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.machine.IMachineAppearance;
import fr.frinn.custommachinery.api.machine.MachineAppearanceProperty;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.MachineModelLocation;
import fr.frinn.custommachinery.common.util.MachineShape;
import fr.frinn.custommachinery.impl.codec.NamedMapCodec;
import fr.frinn.custommachinery.impl.util.IMachineModelLocation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MachineAppearance implements IMachineAppearance {

    public static final NamedMapCodec<Map<MachineAppearanceProperty<?>, Object>> CODEC = new NamedMapCodec<Map<MachineAppearanceProperty<?>, Object>>() {

        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Registration.APPEARANCE_PROPERTY_REGISTRY.getIds().stream().map(loc -> ops.createString(loc.toString()));
        }

        public <T> DataResult<Map<MachineAppearanceProperty<?>, Object>> decode(DynamicOps<T> ops, MapLike<T> input) {
            Builder<MachineAppearanceProperty<?>, Object> properties = ImmutableMap.builder();
            for (MachineAppearanceProperty<?> property : Registration.APPEARANCE_PROPERTY_REGISTRY) {
                if (property.getId() != null && input.get(property.getId().toString()) != null) {
                    DataResult<?> result = property.getCodec().read(ops, input.get(property.getId().toString()));
                    if (result.result().isPresent()) {
                        properties.put(property, result.result().get());
                    } else if (result.error().isPresent()) {
                        ICustomMachineryAPI.INSTANCE.logger().warn("Couldn't deserialize appearance property: {}, invalid value: {}, error: {}, using default value instead.", property.getId(), input.get(property.getId().toString()), ((PartialResult) result.error().get()).message());
                        properties.put(property, property.getDefaultValue());
                    }
                } else if (property.getId() != null && input.get(property.getId().getPath()) != null) {
                    DataResult<?> result = property.getCodec().read(ops, input.get(property.getId().getPath()));
                    if (result.result().isPresent()) {
                        properties.put(property, result.result().get());
                    } else if (result.error().isPresent()) {
                        ICustomMachineryAPI.INSTANCE.logger().warn("Couldn't deserialize appearance property: {}, invalid value: {}, error: {}, using default value instead.", property.getId(), input.get(property.getId().getPath()), ((PartialResult) result.error().get()).message());
                        properties.put(property, property.getDefaultValue());
                    }
                } else {
                    properties.put(property, property.getDefaultValue());
                }
            }
            return DataResult.success(properties.build());
        }

        public <T> RecordBuilder<T> encode(Map<MachineAppearanceProperty<?>, Object> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            for (Entry<MachineAppearanceProperty<?>, Object> entry : input.entrySet()) {
                if (entry.getValue() != ((MachineAppearanceProperty) entry.getKey()).getDefaultValue() && ((MachineAppearanceProperty) entry.getKey()).getId() != null) {
                    prefix.add(((MachineAppearanceProperty) entry.getKey()).getId().toString(), ((MachineAppearanceProperty) entry.getKey()).getCodec().encodeStart(ops, entry.getValue()));
                }
            }
            return prefix;
        }

        @Override
        public String name() {
            return "Machine Appearance";
        }
    };

    public static final MachineAppearance DEFAULT = new MachineAppearance(defaultProperties());

    private final Map<MachineAppearanceProperty<?>, Object> properties;

    public MachineAppearance(Map<MachineAppearanceProperty<?>, Object> properties) {
        this.properties = properties;
    }

    @Override
    public <T> T getProperty(MachineAppearanceProperty<T> property) {
        if (!this.properties.containsKey(property)) {
            throw new IllegalStateException("Can't get Machine Appearance property for: " + property.getId() + ", this property may not be registered");
        } else {
            return (T) this.properties.get(property);
        }
    }

    @Override
    public IMachineModelLocation getBlockModel() {
        return this.getProperty((MachineAppearanceProperty<IMachineModelLocation>) Registration.BLOCK_MODEL_PROPERTY.get());
    }

    @Override
    public IMachineModelLocation getItemModel() {
        MachineModelLocation itemModel = this.getProperty((MachineAppearanceProperty<MachineModelLocation>) Registration.ITEM_MODEL_PROPERTY.get());
        if (itemModel != null && itemModel.getLoc() != null && itemModel.getLoc().getPath() != null && itemModel.getLoc().getPath().equals("default/custom_machine_default")) {
            IMachineModelLocation blockItemModel = this.getBlockModel();
            if (blockItemModel != null && blockItemModel.getLoc() != null && blockItemModel.getLoc().getPath() != null && !blockItemModel.getLoc().getPath().equals("default/custom_machine_default")) {
                return blockItemModel;
            }
        }
        return itemModel;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return this.getProperty((MachineAppearanceProperty<SoundEvent>) Registration.AMBIENT_SOUND_PROPERTY.get());
    }

    @Override
    public SoundType getInteractionSound() {
        return this.getProperty((MachineAppearanceProperty<SoundType>) Registration.INTERACTION_SOUND_PROPERTY.get());
    }

    @Override
    public int getLightLevel() {
        return this.<Integer>getProperty((MachineAppearanceProperty<Integer>) Registration.LIGHT_PROPERTY.get());
    }

    @Override
    public int getColor() {
        return this.<Integer>getProperty((MachineAppearanceProperty<Integer>) Registration.COLOR_PROPERTY.get());
    }

    @Override
    public float getHardness() {
        return this.<Float>getProperty((MachineAppearanceProperty<Float>) Registration.HARDNESS_PROPERTY.get());
    }

    @Override
    public float getResistance() {
        return this.<Float>getProperty((MachineAppearanceProperty<Float>) Registration.RESISTANCE_PROPERTY.get());
    }

    @Override
    public List<TagKey<Block>> getTool() {
        return this.getProperty((MachineAppearanceProperty<List<TagKey<Block>>>) Registration.TOOL_TYPE_PROPERTY.get());
    }

    @Override
    public TagKey<Block> getMiningLevel() {
        return this.getProperty((MachineAppearanceProperty<TagKey<Block>>) Registration.MINING_LEVEL_PROPERTY.get());
    }

    @Override
    public boolean requiresCorrectToolForDrops() {
        return this.<Boolean>getProperty((MachineAppearanceProperty<Boolean>) Registration.REQUIRES_TOOL.get());
    }

    public MachineShape getShape() {
        return this.getProperty((MachineAppearanceProperty<MachineShape>) Registration.SHAPE_PROPERTY.get());
    }

    @Override
    public Function<Direction, VoxelShape> getCollisionShape() {
        MachineShape collisionShape = this.getProperty((MachineAppearanceProperty<MachineShape>) Registration.SHAPE_COLLISION_PROPERTY.get());
        return collisionShape == MachineShape.DEFAULT_COLLISION ? this.getShape() : collisionShape;
    }

    public MachineAppearance copy() {
        return new MachineAppearance(ImmutableMap.copyOf(this.properties));
    }

    public Map<MachineAppearanceProperty<?>, Object> getProperties() {
        return this.properties;
    }

    public static Map<MachineAppearanceProperty<?>, Object> defaultProperties() {
        Map<MachineAppearanceProperty<?>, Object> map = new HashMap();
        for (MachineAppearanceProperty<?> property : Registration.APPEARANCE_PROPERTY_REGISTRY) {
            map.put(property, property.getDefaultValue());
        }
        return map;
    }
}