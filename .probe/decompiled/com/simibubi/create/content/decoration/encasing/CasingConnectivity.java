package com.simibubi.create.content.decoration.encasing;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CasingConnectivity {

    private Map<Block, CasingConnectivity.Entry> entries = new IdentityHashMap();

    public CasingConnectivity.Entry get(BlockState blockState) {
        return (CasingConnectivity.Entry) this.entries.get(blockState.m_60734_());
    }

    public void makeCasing(Block block, CTSpriteShiftEntry casing) {
        new CasingConnectivity.Entry(block, casing, (s, f) -> true).register();
    }

    public void make(Block block, CTSpriteShiftEntry casing) {
        new CasingConnectivity.Entry(block, casing, (s, f) -> true).register();
    }

    public void make(Block block, CTSpriteShiftEntry casing, BiPredicate<BlockState, Direction> predicate) {
        new CasingConnectivity.Entry(block, casing, predicate).register();
    }

    public class Entry {

        private Block block;

        private CTSpriteShiftEntry casing;

        private BiPredicate<BlockState, Direction> predicate;

        private Entry(Block block, CTSpriteShiftEntry casing, BiPredicate<BlockState, Direction> predicate) {
            this.block = block;
            this.casing = casing;
            this.predicate = predicate;
        }

        public CTSpriteShiftEntry getCasing() {
            return this.casing;
        }

        public boolean isSideValid(BlockState state, Direction face) {
            return this.predicate.test(state, face);
        }

        public void register() {
            CasingConnectivity.this.entries.put(this.block, this);
        }
    }
}