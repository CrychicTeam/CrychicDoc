package net.mehvahdjukaar.supplementaries.common.block;

import java.util.List;
import net.mehvahdjukaar.moonlight.api.block.IWashable;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public interface ITextHolderProvider extends IOnePlayerGui, IWashable, IWaxable {

    TextHolder getTextHolder(int var1);

    default TextHolder getTextHolder() {
        return this.getTextHolder(0);
    }

    default int textHoldersCount() {
        return 1;
    }

    @Override
    default boolean tryWash(Level level, BlockPos pos, BlockState state) {
        if (this.isWaxed()) {
            this.setWaxed(false);
            return true;
        } else {
            boolean success = false;
            for (int i = 0; i < this.textHoldersCount(); i++) {
                TextHolder text = this.getTextHolder(i);
                if (!text.isEmpty(null)) {
                    text.clear();
                    success = true;
                }
            }
            if (success) {
                if (this instanceof BlockEntity be) {
                    be.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    default boolean tryAcceptingClientText(BlockPos pos, ServerPlayer player, List<List<FilteredText>> filteredText) {
        this.validatePlayerWhoMayEdit(player.m_9236_(), pos);
        if (!this.isWaxed() && player.m_20148_().equals(this.getPlayerWhoMayEdit())) {
            for (int i = 0; i < filteredText.size(); i++) {
                TextHolder holder = this.getTextHolder(i);
                holder.acceptClientMessages(player, (List<FilteredText>) filteredText.get(i));
            }
            this.setPlayerWhoMayEdit(null);
            return true;
        } else {
            Supplementaries.LOGGER.warn("Player {} just tried to change non-editable sign", player.m_7755_().getString());
            return false;
        }
    }

    @Override
    default boolean tryOpeningEditGui(ServerPlayer player, BlockPos pos) {
        boolean filtering = player.isTextFilteringEnabled();
        for (int i = 0; i < this.textHoldersCount(); i++) {
            if (!this.getTextHolder(i).hasEditableText(filtering)) {
                return false;
            }
        }
        return IOnePlayerGui.super.tryOpeningEditGui(player, pos);
    }

    default InteractionResult interactWithTextHolder(int index, Level level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
        InteractionResult result = this.getTextHolder(index).playerInteract(level, pos, player, hand);
        if (result == InteractionResult.PASS) {
            result = this.tryWaxing(level, pos, player, hand);
        }
        if (result != InteractionResult.PASS) {
            if (!level.isClientSide && this instanceof BlockEntity te) {
                te.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
            level.m_220407_(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, level.getBlockState(pos)));
            return result;
        } else {
            if (player instanceof ServerPlayer serverPlayer && this.tryOpeningEditGui(serverPlayer, pos)) {
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
    }
}