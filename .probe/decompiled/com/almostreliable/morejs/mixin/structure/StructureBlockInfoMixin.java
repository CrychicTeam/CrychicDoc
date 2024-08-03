package com.almostreliable.morejs.mixin.structure;

import com.almostreliable.morejs.features.structure.StructureBlockInfoModification;
import com.almostreliable.morejs.util.Utils;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ StructureTemplate.StructureBlockInfo.class })
public class StructureBlockInfoMixin implements StructureBlockInfoModification {

    @Mutable
    @Shadow
    @Final
    public CompoundTag nbt;

    @Mutable
    @Shadow
    @Final
    public BlockState state;

    @Shadow
    @Final
    public BlockPos pos;

    @Override
    public String getId() {
        return BuiltInRegistries.BLOCK.getKey(this.state.m_60734_()).toString();
    }

    @Override
    public Block getBlock() {
        return this.state.m_60734_();
    }

    @Override
    public void setBlock(ResourceLocation id) {
        Block block = (Block) BuiltInRegistries.BLOCK.m_6612_(id).orElseThrow(() -> new IllegalArgumentException("Block not found: " + id));
        this.state = block.defaultBlockState();
    }

    @Override
    public void setBlock(ResourceLocation id, Map<String, Object> properties) {
        this.setBlock(id);
        if (!properties.isEmpty()) {
            this.state = this.getBlock().defaultBlockState();
            for (Property<?> property : this.state.m_61147_()) {
                Object newValue = properties.get(property.getName());
                if (newValue != null) {
                    try {
                        this.state = (BlockState) this.state.m_61124_(property, Utils.cast(newValue));
                    } catch (Exception var7) {
                        property.getValue(newValue.toString()).ifPresent(v -> this.state = (BlockState) this.state.m_61124_(property, Utils.cast(v)));
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap();
        for (Property<?> property : this.state.m_61147_()) {
            Object value = this.state.m_61143_(property);
            properties.put(property.getName(), value);
        }
        return properties;
    }

    @Override
    public boolean hasNbt() {
        return this.nbt != null;
    }

    @Nullable
    @Override
    public CompoundTag getNbt() {
        return this.nbt;
    }

    @Override
    public void setNbt(@Nullable CompoundTag nbt) {
        this.nbt = nbt;
    }

    @Override
    public BlockPos getPosition() {
        return this.pos;
    }

    @HideFromJS
    @Override
    public void setVanillaBlockState(BlockState state) {
        this.state = state;
    }
}