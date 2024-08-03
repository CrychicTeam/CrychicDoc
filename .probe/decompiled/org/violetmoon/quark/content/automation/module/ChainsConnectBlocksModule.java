package org.violetmoon.quark.content.automation.module;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.violetmoon.zeta.api.IIndirectConnector;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "automation")
public class ChainsConnectBlocksModule extends ZetaModule {

    @Hint
    Item chain = Items.CHAIN;

    public static boolean staticEnabled;

    @LoadEvent
    public final void register(ZRegister event) {
        IIndirectConnector.INDIRECT_STICKY_BLOCKS.add(Pair.of(ChainsConnectBlocksModule.ChainConnection.PREDICATE, ChainsConnectBlocksModule.ChainConnection.INSTANCE));
    }

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
    }

    public static class ChainConnection implements IIndirectConnector {

        public static ChainsConnectBlocksModule.ChainConnection INSTANCE = new ChainsConnectBlocksModule.ChainConnection();

        public static Predicate<BlockState> PREDICATE = s -> s.m_60734_() == Blocks.CHAIN;

        @Override
        public boolean isEnabled() {
            return ChainsConnectBlocksModule.staticEnabled;
        }

        @Override
        public boolean canConnectIndirectly(Level world, BlockPos ourPos, BlockPos sourcePos, BlockState ourState, BlockState sourceState) {
            Direction.Axis axis = (Direction.Axis) ourState.m_61143_(ChainBlock.f_55923_);
            switch(axis) {
                case X:
                    if (ourPos.m_123341_() == sourcePos.m_123341_()) {
                        return false;
                    }
                    break;
                case Y:
                    if (ourPos.m_123342_() == sourcePos.m_123342_()) {
                        return false;
                    }
                    break;
                case Z:
                    if (ourPos.m_123343_() == sourcePos.m_123343_()) {
                        return false;
                    }
            }
            if (sourceState.m_60734_() == ourState.m_60734_()) {
                Direction.Axis otherAxis = (Direction.Axis) sourceState.m_61143_(ChainBlock.f_55923_);
                return axis == otherAxis;
            } else {
                return true;
            }
        }
    }
}