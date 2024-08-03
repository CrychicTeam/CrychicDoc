package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.block.entity.CommandBlockEntity;

public class CommandBlockEditScreen extends AbstractCommandBlockEditScreen {

    private final CommandBlockEntity autoCommandBlock;

    private CycleButton<CommandBlockEntity.Mode> modeButton;

    private CycleButton<Boolean> conditionalButton;

    private CycleButton<Boolean> autoexecButton;

    private CommandBlockEntity.Mode mode = CommandBlockEntity.Mode.REDSTONE;

    private boolean conditional;

    private boolean autoexec;

    public CommandBlockEditScreen(CommandBlockEntity commandBlockEntity0) {
        this.autoCommandBlock = commandBlockEntity0;
    }

    @Override
    BaseCommandBlock getCommandBlock() {
        return this.autoCommandBlock.getCommandBlock();
    }

    @Override
    int getPreviousY() {
        return 135;
    }

    @Override
    protected void init() {
        super.init();
        this.modeButton = (CycleButton<CommandBlockEntity.Mode>) this.m_142416_(CycleButton.<CommandBlockEntity.Mode>builder(p_287312_ -> {
            return switch(p_287312_) {
                case SEQUENCE ->
                    Component.translatable("advMode.mode.sequence");
                case AUTO ->
                    Component.translatable("advMode.mode.auto");
                case REDSTONE ->
                    Component.translatable("advMode.mode.redstone");
            };
        }).withValues(CommandBlockEntity.Mode.values()).displayOnlyValue().withInitialValue(this.mode).create(this.f_96543_ / 2 - 50 - 100 - 4, 165, 100, 20, Component.translatable("advMode.mode"), (p_169721_, p_169722_) -> this.mode = p_169722_));
        this.conditionalButton = (CycleButton<Boolean>) this.m_142416_(CycleButton.booleanBuilder(Component.translatable("advMode.mode.conditional"), Component.translatable("advMode.mode.unconditional")).displayOnlyValue().withInitialValue(this.conditional).create(this.f_96543_ / 2 - 50, 165, 100, 20, Component.translatable("advMode.type"), (p_169727_, p_169728_) -> this.conditional = p_169728_));
        this.autoexecButton = (CycleButton<Boolean>) this.m_142416_(CycleButton.booleanBuilder(Component.translatable("advMode.mode.autoexec.bat"), Component.translatable("advMode.mode.redstoneTriggered")).displayOnlyValue().withInitialValue(this.autoexec).create(this.f_96543_ / 2 + 50 + 4, 165, 100, 20, Component.translatable("advMode.triggering"), (p_169724_, p_169725_) -> this.autoexec = p_169725_));
        this.enableControls(false);
    }

    private void enableControls(boolean boolean0) {
        this.f_97648_.f_93623_ = boolean0;
        this.f_97650_.f_93623_ = boolean0;
        this.modeButton.f_93623_ = boolean0;
        this.conditionalButton.f_93623_ = boolean0;
        this.autoexecButton.f_93623_ = boolean0;
    }

    public void updateGui() {
        BaseCommandBlock $$0 = this.autoCommandBlock.getCommandBlock();
        this.f_97646_.setValue($$0.getCommand());
        boolean $$1 = $$0.isTrackOutput();
        this.mode = this.autoCommandBlock.getMode();
        this.conditional = this.autoCommandBlock.isConditional();
        this.autoexec = this.autoCommandBlock.isAutomatic();
        this.f_97650_.setValue($$1);
        this.modeButton.setValue(this.mode);
        this.conditionalButton.setValue(this.conditional);
        this.autoexecButton.setValue(this.autoexec);
        this.m_169598_($$1);
        this.enableControls(true);
    }

    @Override
    public void resize(Minecraft minecraft0, int int1, int int2) {
        super.resize(minecraft0, int1, int2);
        this.enableControls(true);
    }

    @Override
    protected void populateAndSendPacket(BaseCommandBlock baseCommandBlock0) {
        this.f_96541_.getConnection().send(new ServerboundSetCommandBlockPacket(BlockPos.containing(baseCommandBlock0.getPosition()), this.f_97646_.getValue(), this.mode, baseCommandBlock0.isTrackOutput(), this.conditional, this.autoexec));
    }
}