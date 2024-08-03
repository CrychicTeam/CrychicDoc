package se.mickelus.tetra.blocks.geode;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.geode.particle.SparkleParticleType;

@ParametersAreNonnullByDefault
public class GeodeBlock extends TetraBlock {

    public static final String identifier = "block_geode";

    @ObjectHolder(registryName = "block", value = "tetra:block_geode")
    public static GeodeBlock instance;

    public GeodeBlock() {
        super(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE));
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return Blocks.DEEPSLATE.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
        if (random.nextInt(2) == 0) {
            Direction direction = Direction.getRandom(random);
            BlockPos offsetPos = blockPos.relative(direction);
            if (level.m_7232_(SectionPos.blockToSectionCoord(offsetPos.m_123341_()), SectionPos.blockToSectionCoord(offsetPos.m_123343_())) && level.m_45524_(offsetPos, 0) > 2) {
                Vec3 particlePos = Vec3.atCenterOf(blockPos).add(Vec3.atLowerCornerOf(direction.getNormal()).scale(0.55));
                double dx = direction.getStepX() == 0 ? Mth.nextDouble(random, -0.5, 0.5) : 0.0;
                double dy = direction.getStepY() == 0 ? Mth.nextDouble(random, -0.5, 0.5) : 0.0;
                double dz = direction.getStepZ() == 0 ? Mth.nextDouble(random, -0.5, 0.5) : 0.0;
                level.addParticle(SparkleParticleType.instance, particlePos.x + dx, particlePos.y + dy, particlePos.z + dz, 0.0, 0.0, 0.0);
            }
        }
    }
}