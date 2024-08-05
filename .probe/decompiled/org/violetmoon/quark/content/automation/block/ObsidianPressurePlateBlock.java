package org.violetmoon.quark.content.automation.block;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.ZetaPressurePlateBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class ObsidianPressurePlateBlock extends ZetaPressurePlateBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ObsidianPressurePlateBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(PressurePlateBlock.Sensitivity.EVERYTHING, regname, module, properties, BlockSetType.STONE);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(POWERED, false));
    }

    @Override
    protected int getSignalStrength(@NotNull Level worldIn, @NotNull BlockPos pos) {
        AABB bounds = f_49287_.move(pos);
        List<? extends Entity> entities = worldIn.m_45976_(Player.class, bounds);
        if (!entities.isEmpty()) {
            for (Entity entity : entities) {
                if (!entity.isIgnoringBlockTriggers()) {
                    return 15;
                }
            }
        }
        return 0;
    }
}