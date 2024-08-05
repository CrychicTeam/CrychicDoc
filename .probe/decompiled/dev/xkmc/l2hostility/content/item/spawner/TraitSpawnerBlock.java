package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class TraitSpawnerBlock {

    public static final EnumProperty<TraitSpawnerBlock.State> STATE = EnumProperty.create("state", TraitSpawnerBlock.State.class);

    public static final BlockEntityBlockMethodImpl<BurstSpawnerBlockEntity> BE_BURST = new BlockEntityBlockMethodImpl(LHBlocks.BE_BURST, BurstSpawnerBlockEntity.class);

    public static final BaseTraitMethod BASE = new BaseTraitMethod();

    public static final BurstTraitMethod BURST = new BurstTraitMethod();

    public static final ClickTraitMethod CLICK = new ClickTraitMethod();

    public static enum State implements StringRepresentable {

        IDLE(7), ACTIVATED(11), CLEAR(15), FAILED(11);

        private final int light;

        private State(int light) {
            this.light = light;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public int light() {
            return this.light;
        }
    }
}