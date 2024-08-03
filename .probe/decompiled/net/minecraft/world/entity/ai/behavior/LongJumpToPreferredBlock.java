package net.minecraft.world.entity.ai.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Block;

public class LongJumpToPreferredBlock<E extends Mob> extends LongJumpToRandomPos<E> {

    private final TagKey<Block> preferredBlockTag;

    private final float preferredBlocksChance;

    private final List<LongJumpToRandomPos.PossibleJump> notPrefferedJumpCandidates = new ArrayList();

    private boolean currentlyWantingPreferredOnes;

    public LongJumpToPreferredBlock(UniformInt uniformInt0, int int1, int int2, float float3, Function<E, SoundEvent> functionESoundEvent4, TagKey<Block> tagKeyBlock5, float float6, BiPredicate<E, BlockPos> biPredicateEBlockPos7) {
        super(uniformInt0, int1, int2, float3, functionESoundEvent4, biPredicateEBlockPos7);
        this.preferredBlockTag = tagKeyBlock5;
        this.preferredBlocksChance = float6;
    }

    @Override
    protected void start(ServerLevel serverLevel0, E e1, long long2) {
        super.start(serverLevel0, e1, long2);
        this.notPrefferedJumpCandidates.clear();
        this.currentlyWantingPreferredOnes = e1.m_217043_().nextFloat() < this.preferredBlocksChance;
    }

    @Override
    protected Optional<LongJumpToRandomPos.PossibleJump> getJumpCandidate(ServerLevel serverLevel0) {
        if (!this.currentlyWantingPreferredOnes) {
            return super.getJumpCandidate(serverLevel0);
        } else {
            BlockPos.MutableBlockPos $$1 = new BlockPos.MutableBlockPos();
            while (!this.f_147630_.isEmpty()) {
                Optional<LongJumpToRandomPos.PossibleJump> $$2 = super.getJumpCandidate(serverLevel0);
                if ($$2.isPresent()) {
                    LongJumpToRandomPos.PossibleJump $$3 = (LongJumpToRandomPos.PossibleJump) $$2.get();
                    if (serverLevel0.m_8055_($$1.setWithOffset($$3.getJumpTarget(), Direction.DOWN)).m_204336_(this.preferredBlockTag)) {
                        return $$2;
                    }
                    this.notPrefferedJumpCandidates.add($$3);
                }
            }
            return !this.notPrefferedJumpCandidates.isEmpty() ? Optional.of((LongJumpToRandomPos.PossibleJump) this.notPrefferedJumpCandidates.remove(0)) : Optional.empty();
        }
    }
}