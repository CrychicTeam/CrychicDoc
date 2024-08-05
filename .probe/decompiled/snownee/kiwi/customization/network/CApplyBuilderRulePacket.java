package snownee.kiwi.customization.network;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.builder.BuilderRule;
import snownee.kiwi.customization.builder.BuilderRules;
import snownee.kiwi.network.KiwiPacket;
import snownee.kiwi.network.PacketHandler;
import snownee.kiwi.util.KHolder;

@KiwiPacket(value = "apply_builder_rule", dir = KiwiPacket.Direction.PLAY_TO_SERVER)
public class CApplyBuilderRulePacket extends PacketHandler {

    public static CApplyBuilderRulePacket I;

    public static void send(UseOnContext context, KHolder<BuilderRule> holder, List<BlockPos> positions) {
        I.sendToServer(buf -> {
            buf.writeEnum(context.getHand());
            buf.writeBlockPos(context.getClickedPos());
            buf.writeResourceLocation(holder.key());
            buf.writeCollection(positions, FriendlyByteBuf::m_130064_);
        });
    }

    @Override
    public CompletableFuture<FriendlyByteBuf> receive(Function<Runnable, CompletableFuture<FriendlyByteBuf>> function, FriendlyByteBuf buf, @Nullable ServerPlayer player) {
        Objects.requireNonNull(player);
        InteractionHand hand = buf.readEnum(InteractionHand.class);
        BlockPos pos = buf.readBlockPos();
        ResourceLocation ruleId = buf.readResourceLocation();
        List<BlockPos> positions = buf.readCollection(Lists::newArrayListWithExpectedSize, FriendlyByteBuf::m_130135_);
        return Stream.concat(Stream.of(pos), positions.stream()).anyMatch($ -> !player.m_9236_().isLoaded($)) ? null : (CompletableFuture) function.apply((Runnable) () -> {
            BuilderRule rule = BuilderRules.get(ruleId);
            if (rule != null) {
                if (player.m_19907_(10.0, 1.0F, false) instanceof BlockHitResult blockHitResult && blockHitResult.getBlockPos().equals(pos)) {
                    BlockState blockState = player.m_9236_().getBlockState(pos);
                    Block block = blockState.m_60734_();
                    if (rule.relatedBlocks().noneMatch(block::equals)) {
                        return;
                    }
                    UseOnContext context = new UseOnContext(player, hand, blockHitResult);
                    if (rule.matches(player, context.getItemInHand(), blockState)) {
                        rule.apply(context, positions);
                    }
                    return;
                }
            }
        });
    }
}