package dev.latvian.mods.kubejs.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PoiTypeBuilder extends BuilderBase<PoiType> {

    public transient Set<BlockState> blockStates = Set.of();

    public transient int maxTickets = 1;

    public transient int validRange = 1;

    public PoiTypeBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public final RegistryInfo getRegistryType() {
        return RegistryInfo.POINT_OF_INTEREST_TYPE;
    }

    public PoiType createObject() {
        return new PoiType(this.blockStates, this.maxTickets, this.validRange);
    }

    public PoiTypeBuilder blocks(BlockState[] r) {
        this.blockStates = Set.of(r);
        return this;
    }

    public PoiTypeBuilder block(Block r) {
        this.blockStates = Set.copyOf(r.getStateDefinition().getPossibleStates());
        return this;
    }

    public PoiTypeBuilder maxTickets(int i) {
        this.maxTickets = i;
        return this;
    }

    public PoiTypeBuilder validRange(int i) {
        this.validRange = i;
        return this;
    }
}