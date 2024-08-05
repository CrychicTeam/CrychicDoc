package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;

public class JigsawBlockEditScreen extends Screen {

    private static final int MAX_LEVELS = 7;

    private static final Component JOINT_LABEL = Component.translatable("jigsaw_block.joint_label");

    private static final Component POOL_LABEL = Component.translatable("jigsaw_block.pool");

    private static final Component NAME_LABEL = Component.translatable("jigsaw_block.name");

    private static final Component TARGET_LABEL = Component.translatable("jigsaw_block.target");

    private static final Component FINAL_STATE_LABEL = Component.translatable("jigsaw_block.final_state");

    private final JigsawBlockEntity jigsawEntity;

    private EditBox nameEdit;

    private EditBox targetEdit;

    private EditBox poolEdit;

    private EditBox finalStateEdit;

    int levels;

    private boolean keepJigsaws = true;

    private CycleButton<JigsawBlockEntity.JointType> jointButton;

    private Button doneButton;

    private Button generateButton;

    private JigsawBlockEntity.JointType joint;

    public JigsawBlockEditScreen(JigsawBlockEntity jigsawBlockEntity0) {
        super(GameNarrator.NO_TITLE);
        this.jigsawEntity = jigsawBlockEntity0;
    }

    @Override
    public void tick() {
        this.nameEdit.tick();
        this.targetEdit.tick();
        this.poolEdit.tick();
        this.finalStateEdit.tick();
    }

    private void onDone() {
        this.sendToServer();
        this.f_96541_.setScreen(null);
    }

    private void onCancel() {
        this.f_96541_.setScreen(null);
    }

    private void sendToServer() {
        this.f_96541_.getConnection().send(new ServerboundSetJigsawBlockPacket(this.jigsawEntity.m_58899_(), new ResourceLocation(this.nameEdit.getValue()), new ResourceLocation(this.targetEdit.getValue()), new ResourceLocation(this.poolEdit.getValue()), this.finalStateEdit.getValue(), this.joint));
    }

    private void sendGenerate() {
        this.f_96541_.getConnection().send(new ServerboundJigsawGeneratePacket(this.jigsawEntity.m_58899_(), this.levels, this.keepJigsaws));
    }

    @Override
    public void onClose() {
        this.onCancel();
    }

    @Override
    protected void init() {
        this.poolEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 152, 20, 300, 20, Component.translatable("jigsaw_block.pool"));
        this.poolEdit.setMaxLength(128);
        this.poolEdit.setValue(this.jigsawEntity.getPool().location().toString());
        this.poolEdit.setResponder(p_98986_ -> this.updateValidity());
        this.m_7787_(this.poolEdit);
        this.nameEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 152, 55, 300, 20, Component.translatable("jigsaw_block.name"));
        this.nameEdit.setMaxLength(128);
        this.nameEdit.setValue(this.jigsawEntity.getName().toString());
        this.nameEdit.setResponder(p_98981_ -> this.updateValidity());
        this.m_7787_(this.nameEdit);
        this.targetEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 152, 90, 300, 20, Component.translatable("jigsaw_block.target"));
        this.targetEdit.setMaxLength(128);
        this.targetEdit.setValue(this.jigsawEntity.getTarget().toString());
        this.targetEdit.setResponder(p_98977_ -> this.updateValidity());
        this.m_7787_(this.targetEdit);
        this.finalStateEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 152, 125, 300, 20, Component.translatable("jigsaw_block.final_state"));
        this.finalStateEdit.setMaxLength(256);
        this.finalStateEdit.setValue(this.jigsawEntity.getFinalState());
        this.m_7787_(this.finalStateEdit);
        this.joint = this.jigsawEntity.getJoint();
        int $$0 = this.f_96547_.width(JOINT_LABEL) + 10;
        this.jointButton = (CycleButton<JigsawBlockEntity.JointType>) this.m_142416_(CycleButton.<JigsawBlockEntity.JointType>builder(JigsawBlockEntity.JointType::m_155610_).withValues(JigsawBlockEntity.JointType.values()).withInitialValue(this.joint).displayOnlyValue().create(this.f_96543_ / 2 - 152 + $$0, 150, 300 - $$0, 20, JOINT_LABEL, (p_169765_, p_169766_) -> this.joint = p_169766_));
        boolean $$1 = JigsawBlock.getFrontFacing(this.jigsawEntity.m_58900_()).getAxis().isVertical();
        this.jointButton.f_93623_ = $$1;
        this.jointButton.f_93624_ = $$1;
        this.m_142416_(new AbstractSliderButton(this.f_96543_ / 2 - 154, 180, 100, 20, CommonComponents.EMPTY, 0.0) {

            {
                this.updateMessage();
            }

            @Override
            protected void updateMessage() {
                this.m_93666_(Component.translatable("jigsaw_block.levels", JigsawBlockEditScreen.this.levels));
            }

            @Override
            protected void applyValue() {
                JigsawBlockEditScreen.this.levels = Mth.floor(Mth.clampedLerp(0.0, 7.0, this.f_93577_));
            }
        });
        this.m_142416_(CycleButton.onOffBuilder(this.keepJigsaws).create(this.f_96543_ / 2 - 50, 180, 100, 20, Component.translatable("jigsaw_block.keep_jigsaws"), (p_169768_, p_169769_) -> this.keepJigsaws = p_169769_));
        this.generateButton = (Button) this.m_142416_(Button.builder(Component.translatable("jigsaw_block.generate"), p_98979_ -> {
            this.onDone();
            this.sendGenerate();
        }).bounds(this.f_96543_ / 2 + 54, 180, 100, 20).build());
        this.doneButton = (Button) this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_98973_ -> this.onDone()).bounds(this.f_96543_ / 2 - 4 - 150, 210, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_98964_ -> this.onCancel()).bounds(this.f_96543_ / 2 + 4, 210, 150, 20).build());
        this.m_264313_(this.poolEdit);
        this.updateValidity();
    }

    private void updateValidity() {
        boolean $$0 = ResourceLocation.isValidResourceLocation(this.nameEdit.getValue()) && ResourceLocation.isValidResourceLocation(this.targetEdit.getValue()) && ResourceLocation.isValidResourceLocation(this.poolEdit.getValue());
        this.doneButton.f_93623_ = $$0;
        this.generateButton.f_93623_ = $$0;
    }

    @Override
    public void resize(Minecraft minecraft0, int int1, int int2) {
        String $$3 = this.nameEdit.getValue();
        String $$4 = this.targetEdit.getValue();
        String $$5 = this.poolEdit.getValue();
        String $$6 = this.finalStateEdit.getValue();
        int $$7 = this.levels;
        JigsawBlockEntity.JointType $$8 = this.joint;
        this.m_6575_(minecraft0, int1, int2);
        this.nameEdit.setValue($$3);
        this.targetEdit.setValue($$4);
        this.poolEdit.setValue($$5);
        this.finalStateEdit.setValue($$6);
        this.levels = $$7;
        this.joint = $$8;
        this.jointButton.setValue($$8);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (super.keyPressed(int0, int1, int2)) {
            return true;
        } else if (!this.doneButton.f_93623_ || int0 != 257 && int0 != 335) {
            return false;
        } else {
            this.onDone();
            return true;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawString(this.f_96547_, POOL_LABEL, this.f_96543_ / 2 - 153, 10, 10526880);
        this.poolEdit.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawString(this.f_96547_, NAME_LABEL, this.f_96543_ / 2 - 153, 45, 10526880);
        this.nameEdit.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawString(this.f_96547_, TARGET_LABEL, this.f_96543_ / 2 - 153, 80, 10526880);
        this.targetEdit.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawString(this.f_96547_, FINAL_STATE_LABEL, this.f_96543_ / 2 - 153, 115, 10526880);
        this.finalStateEdit.m_88315_(guiGraphics0, int1, int2, float3);
        if (JigsawBlock.getFrontFacing(this.jigsawEntity.m_58900_()).getAxis().isVertical()) {
            guiGraphics0.drawString(this.f_96547_, JOINT_LABEL, this.f_96543_ / 2 - 153, 156, 16777215);
        }
        super.render(guiGraphics0, int1, int2, float3);
    }
}