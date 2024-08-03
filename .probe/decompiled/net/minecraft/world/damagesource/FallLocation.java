package net.minecraft.world.damagesource;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record FallLocation(String f_289026_) {

    private final String id;

    public static final FallLocation GENERIC = new FallLocation("generic");

    public static final FallLocation LADDER = new FallLocation("ladder");

    public static final FallLocation VINES = new FallLocation("vines");

    public static final FallLocation WEEPING_VINES = new FallLocation("weeping_vines");

    public static final FallLocation TWISTING_VINES = new FallLocation("twisting_vines");

    public static final FallLocation SCAFFOLDING = new FallLocation("scaffolding");

    public static final FallLocation OTHER_CLIMBABLE = new FallLocation("other_climbable");

    public static final FallLocation WATER = new FallLocation("water");

    public FallLocation(String f_289026_) {
        this.id = f_289026_;
    }

    public static FallLocation blockToFallLocation(BlockState p_289530_) {
        if (p_289530_.m_60713_(Blocks.LADDER) || p_289530_.m_204336_(BlockTags.TRAPDOORS)) {
            return LADDER;
        } else if (p_289530_.m_60713_(Blocks.VINE)) {
            return VINES;
        } else if (p_289530_.m_60713_(Blocks.WEEPING_VINES) || p_289530_.m_60713_(Blocks.WEEPING_VINES_PLANT)) {
            return WEEPING_VINES;
        } else if (p_289530_.m_60713_(Blocks.TWISTING_VINES) || p_289530_.m_60713_(Blocks.TWISTING_VINES_PLANT)) {
            return TWISTING_VINES;
        } else {
            return p_289530_.m_60713_(Blocks.SCAFFOLDING) ? SCAFFOLDING : OTHER_CLIMBABLE;
        }
    }

    @Nullable
    public static FallLocation getCurrentFallLocation(LivingEntity p_289566_) {
        Optional<BlockPos> $$1 = p_289566_.getLastClimbablePos();
        if ($$1.isPresent()) {
            BlockState $$2 = p_289566_.m_9236_().getBlockState((BlockPos) $$1.get());
            return blockToFallLocation($$2);
        } else {
            return p_289566_.m_20069_() ? WATER : null;
        }
    }

    public String languageKey() {
        return "death.fell.accident." + this.id;
    }
}