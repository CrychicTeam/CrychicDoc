package net.minecraft.world.entity.ai.village.poi;

import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;

public record PoiType(Set<BlockState> f_27325_, int f_27326_, int f_27328_) {

    private final Set<BlockState> matchingStates;

    private final int maxTickets;

    private final int validRange;

    public static final Predicate<Holder<PoiType>> NONE = p_218041_ -> false;

    public PoiType(Set<BlockState> f_27325_, int f_27326_, int f_27328_) {
        f_27325_ = Set.copyOf(f_27325_);
        this.matchingStates = f_27325_;
        this.maxTickets = f_27326_;
        this.validRange = f_27328_;
    }

    public boolean is(BlockState p_148693_) {
        return this.matchingStates.contains(p_148693_);
    }
}