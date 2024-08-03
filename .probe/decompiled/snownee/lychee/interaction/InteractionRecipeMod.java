package snownee.lychee.interaction;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.RecipeTypes;
import snownee.lychee.core.LycheeContext;

public class InteractionRecipeMod {

    public static InteractionResult useItemOn(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isSpectator()) {
            return InteractionResult.PASS;
        } else if (hand == InteractionHand.OFF_HAND && player.m_21205_().isEmpty() && player.m_21206_().isEmpty()) {
            return InteractionResult.PASS;
        } else {
            ItemStack stack = player.m_21120_(hand);
            if (player.getCooldowns().isOnCooldown(stack.getItem())) {
                return InteractionResult.PASS;
            } else {
                LycheeContext.Builder<LycheeContext> builder = new LycheeContext.Builder<>(world);
                builder.withParameter(LycheeLootContextParams.DIRECTION, hitResult.getDirection());
                Optional<BlockInteractingRecipe> result = RecipeTypes.BLOCK_INTERACTING.process(player, hand, hitResult.getBlockPos(), hitResult.m_82450_(), builder);
                return result.isPresent() ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
        }
    }

    public static InteractionResult clickItemOn(Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction) {
        if (player.isSpectator()) {
            return InteractionResult.PASS;
        } else {
            ItemStack stack = player.m_21120_(hand);
            if (player.getCooldowns().isOnCooldown(stack.getItem())) {
                return InteractionResult.PASS;
            } else {
                Vec3 vec = Vec3.atCenterOf(pos);
                LycheeContext.Builder<LycheeContext> builder = new LycheeContext.Builder<>(world);
                builder.withParameter(LycheeLootContextParams.DIRECTION, direction);
                Optional<BlockClickingRecipe> result = RecipeTypes.BLOCK_CLICKING.process(player, hand, pos, vec, builder);
                return result.isPresent() ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
        }
    }
}