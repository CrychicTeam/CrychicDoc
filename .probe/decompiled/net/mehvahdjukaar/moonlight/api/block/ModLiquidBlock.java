package net.mehvahdjukaar.moonlight.api.block;

import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;

public class ModLiquidBlock extends LiquidBlock {

    private static Field FORGE_BLOCK_SUPPLIER;

    private static Field INIT;

    public ModLiquidBlock(Supplier<? extends FlowingFluid> supplier, BlockBehaviour.Properties arg) {
        super(PlatHelper.getPlatform().isFabric() ? (FlowingFluid) supplier.get() : Fluids.WATER, arg);
        if (PlatHelper.getPlatform().isForge()) {
            if (FORGE_BLOCK_SUPPLIER == null) {
                FORGE_BLOCK_SUPPLIER = PlatHelper.findField(LiquidBlock.class, "supplier");
            }
            if (INIT == null) {
                INIT = PlatHelper.findField(LiquidBlock.class, "fluidStateCacheInitialized");
            }
            try {
                for (Field f : LiquidBlock.class.getDeclaredFields()) {
                    if (f.getType() == FlowingFluid.class) {
                        f.setAccessible(true);
                        f.set(this, null);
                    } else if (f.getType() == ArrayList.class) {
                        f.setAccessible(true);
                        f.set(this, Lists.newArrayList());
                    }
                }
                INIT.setAccessible(true);
                INIT.set(this, false);
                this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54688_, 0));
                FORGE_BLOCK_SUPPLIER.setAccessible(true);
                FORGE_BLOCK_SUPPLIER.set(this, supplier);
                INIT.set(this, false);
            } catch (Exception var7) {
                Moonlight.LOGGER.error("Failed to setup ModLiquidBlock class : " + var7);
                throw new RuntimeException(var7);
            }
        }
    }
}