package fr.lucreeper74.createmetallurgy.content.foundry_mixer;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.utility.VecHelper;
import fr.lucreeper74.createmetallurgy.content.foundry_basin.FoundryBasinBlockEntity;
import fr.lucreeper74.createmetallurgy.content.foundry_lid.FoundryLidBlock;
import fr.lucreeper74.createmetallurgy.content.glassed_foundry_lid.GlassedFoundryLidBlockEntity;
import fr.lucreeper74.createmetallurgy.registries.CMRecipeTypes;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FoundryMixerBlockEntity extends MechanicalMixerBlockEntity {

    private static final Object AlloyingRecipesKey = new Object();

    public FoundryMixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return recipe.getType() == CMRecipeTypes.ALLOYING.getType();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_ != null && !this.running && !this.f_58857_.isClientSide) {
            this.basinChecker.scheduleUpdate();
        }
    }

    @Override
    protected void spillParticle(ParticleOptions data) {
        float angle = this.f_58857_.random.nextFloat() * 360.0F;
        Vec3 offset = new Vec3(0.0, 0.0, 0.25);
        offset = VecHelper.rotate(offset, (double) angle, Direction.Axis.Y);
        Vec3 target = VecHelper.rotate(offset, this.getSpeed() > 0.0F ? 25.0 : -25.0, Direction.Axis.Y).add(0.0, 0.25, 0.0);
        Vec3 center = offset.add(VecHelper.getCenterOf(this.f_58858_));
        target = VecHelper.offsetRandomly(target.subtract(offset), this.f_58857_.random, 0.0078125F);
        this.f_58857_.addParticle(data, center.x, center.y - 1.65F, center.z, target.x, target.y, target.z);
    }

    @Override
    protected Optional<BasinBlockEntity> getBasin() {
        if (this.f_58857_ == null) {
            return Optional.empty();
        } else {
            BlockEntity basinBE = this.f_58857_.getBlockEntity(this.f_58858_.below(2));
            BlockEntity topBE = this.f_58857_.getBlockEntity(this.f_58858_.below());
            if (basinBE instanceof FoundryBasinBlockEntity && topBE instanceof GlassedFoundryLidBlockEntity) {
                return topBE.getBlockState().m_61143_(FoundryLidBlock.OPEN) ? Optional.empty() : Optional.of((FoundryBasinBlockEntity) basinBE);
            } else {
                return Optional.empty();
            }
        }
    }

    @Override
    protected Object getRecipeCacheKey() {
        return AlloyingRecipesKey;
    }
}