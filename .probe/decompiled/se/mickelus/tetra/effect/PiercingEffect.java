package se.mickelus.tetra.effect;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.util.ToolActionHelper;

@ParametersAreNonnullByDefault
public class PiercingEffect {

    public static void pierceBlocks(ItemModularHandheld item, ItemStack itemStack, int pierceAmount, ServerLevel world, BlockState state, BlockPos pos, LivingEntity entity) {
        Player player = (Player) CastOptional.cast(entity, Player.class).orElse(null);
        if (pierceAmount > 0) {
            float referenceHardness = state.m_60800_(world, pos);
            ToolAction referenceTool = (ToolAction) ToolActionHelper.getAppropriateTools(state).stream().filter(tool -> item.canPerformAction(itemStack, tool)).findFirst().orElse(null);
            if (referenceTool != null && item.getToolLevel(itemStack, referenceTool) > 0) {
                double critMultiplier = CritEffect.rollMultiplier(entity.getRandom(), item, itemStack);
                if (critMultiplier != 1.0) {
                    pierceAmount = (int) ((double) pierceAmount * critMultiplier);
                    world.sendParticles(ParticleTypes.ENCHANTED_HIT, (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), 15, 0.2, 0.2, 0.2, 0.0);
                }
                Vec3 entityPosition = entity.m_20299_(0.0F);
                double lookDistance = (Double) Optional.ofNullable(entity.getAttribute(ForgeMod.BLOCK_REACH.get())).map(AttributeInstance::m_22135_).orElse(5.0);
                Vec3 lookingPosition = entity.m_20154_().scale(lookDistance).add(entityPosition);
                BlockHitResult rayTrace = world.m_45547_(new ClipContext(entityPosition, lookingPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
                Direction direction = rayTrace.getType() == HitResult.Type.BLOCK ? rayTrace.getDirection().getOpposite() : Direction.orderedByNearest(entity)[0];
                enqueueBlockBreak(world, player, item, itemStack, direction, pos.relative(direction), referenceHardness, referenceTool, pierceAmount);
            }
        }
    }

    private static void enqueueBlockBreak(Level world, Player player, ItemModularHandheld item, ItemStack itemStack, Direction direction, BlockPos pos, float refHardness, ToolAction refTool, int remaining) {
        ServerScheduler.schedule(1, () -> {
            BlockState offsetState = world.getBlockState(pos);
            float blockHardness = offsetState.m_60800_(world, pos);
            if (ToolActionHelper.playerCanDestroyBlock(player, offsetState, pos, itemStack) && blockHardness != -1.0F && blockHardness <= refHardness && ToolActionHelper.isEffectiveOn(refTool, offsetState) && EffectHelper.breakBlock(world, player, itemStack, pos, offsetState, true, false)) {
                EffectHelper.sendEventToPlayer((ServerPlayer) player, 2001, pos, Block.getId(offsetState));
                item.applyBlockBreakEffects(itemStack, world, offsetState, pos, player);
                if (remaining > 0) {
                    enqueueBlockBreak(world, player, item, itemStack, direction, pos.relative(direction), refHardness, refTool, remaining - 1);
                }
            }
        });
    }
}