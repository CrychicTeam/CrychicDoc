package dev.latvian.mods.kubejs.fluid;

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.Nullable;

public class FluidBlockBuilder extends BlockBuilder {

    private final FluidBuilder fluidBuilder;

    public FluidBlockBuilder(FluidBuilder b) {
        super(b.id);
        this.fluidBuilder = b;
        this.defaultTranslucent();
        this.noItem();
        this.noDrops();
    }

    public Block createObject() {
        return new ArchitecturyLiquidBlock(() -> (FlowingFluid) Objects.requireNonNull(this.fluidBuilder.flowingFluid.get(), "Flowing Fluid is null!"), BlockBehaviour.Properties.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable());
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        generator.blockState(this.id, m -> m.simpleVariant("", this.id.getNamespace() + ":block/" + this.id.getPath()));
        generator.blockModel(this.id, m -> {
            m.parent("");
            m.texture("particle", this.fluidBuilder.stillTexture.toString());
        });
    }

    @Override
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i != null) {
            throw new IllegalStateException("Fluid blocks cannot have items!");
        } else {
            return super.item(null);
        }
    }
}