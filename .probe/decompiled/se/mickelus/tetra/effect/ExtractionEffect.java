package se.mickelus.tetra.effect;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.mutil.util.RotationHelper;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.util.ToolActionHelper;

@ParametersAreNonnullByDefault
public class ExtractionEffect {

    public static void breakBlocks(ItemModularHandheld item, ItemStack itemStack, int effectLevel, ServerLevel world, BlockState state, BlockPos pos, LivingEntity entity) {
        Player player = (Player) CastOptional.cast(entity, Player.class).orElse(null);
        if (effectLevel > 0) {
            Vec3 entityPosition = entity.m_20299_(0.0F);
            double lookDistance = (Double) Optional.ofNullable(entity.getAttribute(ForgeMod.BLOCK_REACH.get())).map(AttributeInstance::m_22135_).orElse(5.0);
            Vec3 lookingPosition = entity.m_20154_().scale(lookDistance).add(entityPosition);
            BlockHitResult rayTrace = world.m_45547_(new ClipContext(entityPosition, lookingPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
            Direction direction = rayTrace.getType() == HitResult.Type.BLOCK ? rayTrace.getDirection().getOpposite() : Direction.orderedByNearest(entity)[0];
            float referenceHardness = state.m_60800_(world, pos);
            ToolAction referenceTool = (ToolAction) ToolActionHelper.getAppropriateTools(state).stream().filter(tool -> item.canPerformAction(itemStack, tool)).findFirst().orElse(null);
            if (referenceTool != null && item.getToolLevel(itemStack, referenceTool) > 0) {
                double critMultiplier = CritEffect.rollMultiplier(entity.getRandom(), item, itemStack);
                if (critMultiplier != 1.0) {
                    effectLevel = (int) ((double) effectLevel * critMultiplier);
                    world.sendParticles(ParticleTypes.ENCHANTED_HIT, (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), 15, 0.2, 0.2, 0.2, 0.0);
                }
                breakRecursive(world, player, item, itemStack, direction, pos, referenceHardness, referenceTool, effectLevel);
                item.applyDamage(effectLevel, itemStack, entity);
                item.tickProgression(entity, itemStack, Mth.ceil((double) effectLevel / 2.0));
            }
        }
    }

    private static void breakRecursive(Level world, Player player, ItemModularHandheld item, ItemStack itemStack, Direction direction, BlockPos pos, float refHardness, ToolAction refTool, int remaining) {
        if (remaining > 0) {
            ServerScheduler.schedule(2, () -> breakInner(world, player, item, itemStack, direction, pos, refHardness, refTool));
        }
        if (remaining > 1) {
            ServerScheduler.schedule(4, () -> breakOuter(world, player, item, itemStack, direction, pos, refHardness, refTool));
        }
        if (remaining > 2) {
            ServerScheduler.schedule(6, () -> {
                BlockPos offsetPos = pos.relative(direction);
                if (breakBlock(world, player, item, itemStack, offsetPos, refHardness, refTool)) {
                    breakRecursive(world, player, item, itemStack, direction, offsetPos, refHardness, refTool, remaining - 2);
                }
            });
        }
    }

    private static void breakInner(Level world, Player player, ItemModularHandheld item, ItemStack itemStack, Direction direction, BlockPos pos, float refHardness, ToolAction refTool) {
        Vec3i axis1 = RotationHelper.shiftAxis(direction.getNormal());
        Vec3i axis2 = RotationHelper.shiftAxis(axis1);
        breakBlock(world, player, item, itemStack, pos.offset(axis1), refHardness, refTool);
        breakBlock(world, player, item, itemStack, pos.subtract(axis1), refHardness, refTool);
        breakBlock(world, player, item, itemStack, pos.offset(axis2), refHardness, refTool);
        breakBlock(world, player, item, itemStack, pos.subtract(axis2), refHardness, refTool);
    }

    private static void breakOuter(Level world, Player player, ItemModularHandheld item, ItemStack itemStack, Direction direction, BlockPos pos, float refHardness, ToolAction refTool) {
        Vec3i axis1 = RotationHelper.shiftAxis(direction.getNormal());
        Vec3i axis2 = RotationHelper.shiftAxis(axis1);
        breakBlock(world, player, item, itemStack, pos.offset(axis1).offset(axis2), refHardness, refTool);
        breakBlock(world, player, item, itemStack, pos.subtract(axis1).subtract(axis2), refHardness, refTool);
        breakBlock(world, player, item, itemStack, pos.offset(axis1).subtract(axis2), refHardness, refTool);
        breakBlock(world, player, item, itemStack, pos.subtract(axis1).offset(axis2), refHardness, refTool);
    }

    private static boolean breakBlock(Level world, Player player, ItemModularHandheld item, ItemStack itemStack, BlockPos pos, float refHardness, ToolAction refTool) {
        BlockState offsetState = world.getBlockState(pos);
        float blockHardness = offsetState.m_60800_(world, pos);
        if (ToolActionHelper.playerCanDestroyBlock(player, offsetState, pos, itemStack) && blockHardness != -1.0F && blockHardness <= refHardness && ToolActionHelper.isEffectiveOn(refTool, offsetState) && EffectHelper.breakBlock(world, player, itemStack, pos, offsetState, true, false)) {
            EffectHelper.sendEventToPlayer((ServerPlayer) player, 2001, pos, Block.getId(offsetState));
            item.applyBlockBreakEffects(itemStack, world, offsetState, pos, player);
            return true;
        } else {
            return false;
        }
    }
}