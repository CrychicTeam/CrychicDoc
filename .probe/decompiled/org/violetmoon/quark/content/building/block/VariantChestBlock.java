package org.violetmoon.quark.content.building.block;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.building.block.be.VariantChestBlockEntity;
import org.violetmoon.quark.content.building.module.VariantChestsModule;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class VariantChestBlock extends ChestBlock implements IZetaBlock, VariantChestsModule.IVariantChest {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    protected final String type;

    public VariantChestBlock(String prefix, String type, ZetaModule module, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, BlockBehaviour.Properties props) {
        super(props, supplier);
        this.module = module;
        this.type = type;
        if (module != null) {
            String resloc = (prefix != null ? prefix + "_" : "") + type + "_chest";
            module.zeta.registry.registerBlock(this, resloc, true);
        }
    }

    public VariantChestBlock(String type, ZetaModule module, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, BlockBehaviour.Properties props) {
        this(null, type, module, supplier, props);
    }

    @Override
    public int getFlammabilityZeta(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    public boolean isFlammableZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return false;
    }

    public VariantChestBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new VariantChestBlockEntity(pos, state);
    }

    @Override
    public String getTexturePath() {
        return this.type;
    }
}