package net.minecraft.world.level.block.entity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.FilteredText;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class SignBlockEntity extends BlockEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int MAX_TEXT_LINE_WIDTH = 90;

    private static final int TEXT_LINE_HEIGHT = 10;

    @Nullable
    private UUID playerWhoMayEdit;

    private SignText frontText = this.createDefaultSignText();

    private SignText backText = this.createDefaultSignText();

    private boolean isWaxed;

    public SignBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        this(BlockEntityType.SIGN, blockPos0, blockState1);
    }

    public SignBlockEntity(BlockEntityType blockEntityType0, BlockPos blockPos1, BlockState blockState2) {
        super(blockEntityType0, blockPos1, blockState2);
    }

    protected SignText createDefaultSignText() {
        return new SignText();
    }

    public boolean isFacingFrontText(Player player0) {
        if (this.m_58900_().m_60734_() instanceof SignBlock $$1) {
            Vec3 $$2 = $$1.getSignHitboxCenterPosition(this.m_58900_());
            double $$3 = player0.m_20185_() - ((double) this.m_58899_().m_123341_() + $$2.x);
            double $$4 = player0.m_20189_() - ((double) this.m_58899_().m_123343_() + $$2.z);
            float $$5 = $$1.getYRotationDegrees(this.m_58900_());
            float $$6 = (float) (Mth.atan2($$4, $$3) * 180.0F / (float) Math.PI) - 90.0F;
            return Mth.degreesDifferenceAbs($$5, $$6) <= 90.0F;
        } else {
            return false;
        }
    }

    public SignText getTextFacingPlayer(Player player0) {
        return this.getText(this.isFacingFrontText(player0));
    }

    public SignText getText(boolean boolean0) {
        return boolean0 ? this.frontText : this.backText;
    }

    public SignText getFrontText() {
        return this.frontText;
    }

    public SignText getBackText() {
        return this.backText;
    }

    public int getTextLineHeight() {
        return 10;
    }

    public int getMaxTextLineWidth() {
        return 90;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        SignText.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, this.frontText).resultOrPartial(LOGGER::error).ifPresent(p_277417_ -> compoundTag0.put("front_text", p_277417_));
        SignText.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, this.backText).resultOrPartial(LOGGER::error).ifPresent(p_277389_ -> compoundTag0.put("back_text", p_277389_));
        compoundTag0.putBoolean("is_waxed", this.isWaxed);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        if (compoundTag0.contains("front_text")) {
            SignText.DIRECT_CODEC.parse(NbtOps.INSTANCE, compoundTag0.getCompound("front_text")).resultOrPartial(LOGGER::error).ifPresent(p_278212_ -> this.frontText = this.loadLines(p_278212_));
        }
        if (compoundTag0.contains("back_text")) {
            SignText.DIRECT_CODEC.parse(NbtOps.INSTANCE, compoundTag0.getCompound("back_text")).resultOrPartial(LOGGER::error).ifPresent(p_278213_ -> this.backText = this.loadLines(p_278213_));
        }
        this.isWaxed = compoundTag0.getBoolean("is_waxed");
    }

    private SignText loadLines(SignText signText0) {
        for (int $$1 = 0; $$1 < 4; $$1++) {
            Component $$2 = this.loadLine(signText0.getMessage($$1, false));
            Component $$3 = this.loadLine(signText0.getMessage($$1, true));
            signText0 = signText0.setMessage($$1, $$2, $$3);
        }
        return signText0;
    }

    private Component loadLine(Component component0) {
        if (this.f_58857_ instanceof ServerLevel $$1) {
            try {
                return ComponentUtils.updateForEntity(createCommandSourceStack(null, $$1, this.f_58858_), component0, null, 0);
            } catch (CommandSyntaxException var4) {
            }
        }
        return component0;
    }

    public void updateSignText(Player player0, boolean boolean1, List<FilteredText> listFilteredText2) {
        if (!this.isWaxed() && player0.m_20148_().equals(this.getPlayerWhoMayEdit()) && this.f_58857_ != null) {
            this.updateText(p_277776_ -> this.setMessages(player0, listFilteredText2, p_277776_), boolean1);
            this.setAllowedPlayerEditor(null);
            this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        } else {
            LOGGER.warn("Player {} just tried to change non-editable sign", player0.getName().getString());
        }
    }

    public boolean updateText(UnaryOperator<SignText> unaryOperatorSignText0, boolean boolean1) {
        SignText $$2 = this.getText(boolean1);
        return this.setText((SignText) unaryOperatorSignText0.apply($$2), boolean1);
    }

    private SignText setMessages(Player player0, List<FilteredText> listFilteredText1, SignText signText2) {
        for (int $$3 = 0; $$3 < listFilteredText1.size(); $$3++) {
            FilteredText $$4 = (FilteredText) listFilteredText1.get($$3);
            Style $$5 = signText2.getMessage($$3, player0.isTextFilteringEnabled()).getStyle();
            if (player0.isTextFilteringEnabled()) {
                signText2 = signText2.setMessage($$3, Component.literal($$4.filteredOrEmpty()).setStyle($$5));
            } else {
                signText2 = signText2.setMessage($$3, Component.literal($$4.raw()).setStyle($$5), Component.literal($$4.filteredOrEmpty()).setStyle($$5));
            }
        }
        return signText2;
    }

    public boolean setText(SignText signText0, boolean boolean1) {
        return boolean1 ? this.setFrontText(signText0) : this.setBackText(signText0);
    }

    private boolean setBackText(SignText signText0) {
        if (signText0 != this.backText) {
            this.backText = signText0;
            this.markUpdated();
            return true;
        } else {
            return false;
        }
    }

    private boolean setFrontText(SignText signText0) {
        if (signText0 != this.frontText) {
            this.frontText = signText0;
            this.markUpdated();
            return true;
        } else {
            return false;
        }
    }

    public boolean canExecuteClickCommands(boolean boolean0, Player player1) {
        return this.isWaxed() && this.getText(boolean0).hasAnyClickCommands(player1);
    }

    public boolean executeClickCommandsIfPresent(Player player0, Level level1, BlockPos blockPos2, boolean boolean3) {
        boolean $$4 = false;
        for (Component $$5 : this.getText(boolean3).getMessages(player0.isTextFilteringEnabled())) {
            Style $$6 = $$5.getStyle();
            ClickEvent $$7 = $$6.getClickEvent();
            if ($$7 != null && $$7.getAction() == ClickEvent.Action.RUN_COMMAND) {
                player0.m_20194_().getCommands().performPrefixedCommand(createCommandSourceStack(player0, level1, blockPos2), $$7.getValue());
                $$4 = true;
            }
        }
        return $$4;
    }

    private static CommandSourceStack createCommandSourceStack(@Nullable Player player0, Level level1, BlockPos blockPos2) {
        String $$3 = player0 == null ? "Sign" : player0.getName().getString();
        Component $$4 = (Component) (player0 == null ? Component.literal("Sign") : player0.getDisplayName());
        return new CommandSourceStack(CommandSource.NULL, Vec3.atCenterOf(blockPos2), Vec2.ZERO, (ServerLevel) level1, 2, $$3, $$4, level1.getServer(), player0);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public void setAllowedPlayerEditor(@Nullable UUID uUID0) {
        this.playerWhoMayEdit = uUID0;
    }

    @Nullable
    public UUID getPlayerWhoMayEdit() {
        return this.playerWhoMayEdit;
    }

    private void markUpdated() {
        this.m_6596_();
        this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
    }

    public boolean isWaxed() {
        return this.isWaxed;
    }

    public boolean setWaxed(boolean boolean0) {
        if (this.isWaxed != boolean0) {
            this.isWaxed = boolean0;
            this.markUpdated();
            return true;
        } else {
            return false;
        }
    }

    public boolean playerIsTooFarAwayToEdit(UUID uUID0) {
        Player $$1 = this.f_58857_.m_46003_(uUID0);
        return $$1 == null || $$1.m_20275_((double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_()) > 64.0;
    }

    public static void tick(Level level0, BlockPos blockPos1, BlockState blockState2, SignBlockEntity signBlockEntity3) {
        UUID $$4 = signBlockEntity3.getPlayerWhoMayEdit();
        if ($$4 != null) {
            signBlockEntity3.clearInvalidPlayerWhoMayEdit(signBlockEntity3, level0, $$4);
        }
    }

    private void clearInvalidPlayerWhoMayEdit(SignBlockEntity signBlockEntity0, Level level1, UUID uUID2) {
        if (signBlockEntity0.playerIsTooFarAwayToEdit(uUID2)) {
            signBlockEntity0.setAllowedPlayerEditor(null);
        }
    }
}