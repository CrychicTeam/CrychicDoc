package net.minecraft.client.gui.screens.inventory;

import com.google.common.collect.ImmutableList;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.properties.StructureMode;

public class StructureBlockEditScreen extends Screen {

    private static final Component NAME_LABEL = Component.translatable("structure_block.structure_name");

    private static final Component POSITION_LABEL = Component.translatable("structure_block.position");

    private static final Component SIZE_LABEL = Component.translatable("structure_block.size");

    private static final Component INTEGRITY_LABEL = Component.translatable("structure_block.integrity");

    private static final Component CUSTOM_DATA_LABEL = Component.translatable("structure_block.custom_data");

    private static final Component INCLUDE_ENTITIES_LABEL = Component.translatable("structure_block.include_entities");

    private static final Component DETECT_SIZE_LABEL = Component.translatable("structure_block.detect_size");

    private static final Component SHOW_AIR_LABEL = Component.translatable("structure_block.show_air");

    private static final Component SHOW_BOUNDING_BOX_LABEL = Component.translatable("structure_block.show_boundingbox");

    private static final ImmutableList<StructureMode> ALL_MODES = ImmutableList.copyOf(StructureMode.values());

    private static final ImmutableList<StructureMode> DEFAULT_MODES = (ImmutableList<StructureMode>) ALL_MODES.stream().filter(p_169859_ -> p_169859_ != StructureMode.DATA).collect(ImmutableList.toImmutableList());

    private final StructureBlockEntity structure;

    private Mirror initialMirror = Mirror.NONE;

    private Rotation initialRotation = Rotation.NONE;

    private StructureMode initialMode = StructureMode.DATA;

    private boolean initialEntityIgnoring;

    private boolean initialShowAir;

    private boolean initialShowBoundingBox;

    private EditBox nameEdit;

    private EditBox posXEdit;

    private EditBox posYEdit;

    private EditBox posZEdit;

    private EditBox sizeXEdit;

    private EditBox sizeYEdit;

    private EditBox sizeZEdit;

    private EditBox integrityEdit;

    private EditBox seedEdit;

    private EditBox dataEdit;

    private Button saveButton;

    private Button loadButton;

    private Button rot0Button;

    private Button rot90Button;

    private Button rot180Button;

    private Button rot270Button;

    private Button detectButton;

    private CycleButton<Boolean> includeEntitiesButton;

    private CycleButton<Mirror> mirrorButton;

    private CycleButton<Boolean> toggleAirButton;

    private CycleButton<Boolean> toggleBoundingBox;

    private final DecimalFormat decimalFormat = new DecimalFormat("0.0###");

    public StructureBlockEditScreen(StructureBlockEntity structureBlockEntity0) {
        super(Component.translatable(Blocks.STRUCTURE_BLOCK.getDescriptionId()));
        this.structure = structureBlockEntity0;
        this.decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    }

    @Override
    public void tick() {
        this.nameEdit.tick();
        this.posXEdit.tick();
        this.posYEdit.tick();
        this.posZEdit.tick();
        this.sizeXEdit.tick();
        this.sizeYEdit.tick();
        this.sizeZEdit.tick();
        this.integrityEdit.tick();
        this.seedEdit.tick();
        this.dataEdit.tick();
    }

    private void onDone() {
        if (this.sendToServer(StructureBlockEntity.UpdateType.UPDATE_DATA)) {
            this.f_96541_.setScreen(null);
        }
    }

    private void onCancel() {
        this.structure.setMirror(this.initialMirror);
        this.structure.setRotation(this.initialRotation);
        this.structure.setMode(this.initialMode);
        this.structure.setIgnoreEntities(this.initialEntityIgnoring);
        this.structure.setShowAir(this.initialShowAir);
        this.structure.setShowBoundingBox(this.initialShowBoundingBox);
        this.f_96541_.setScreen(null);
    }

    @Override
    protected void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_99460_ -> this.onDone()).bounds(this.f_96543_ / 2 - 4 - 150, 210, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_99457_ -> this.onCancel()).bounds(this.f_96543_ / 2 + 4, 210, 150, 20).build());
        this.initialMirror = this.structure.getMirror();
        this.initialRotation = this.structure.getRotation();
        this.initialMode = this.structure.getMode();
        this.initialEntityIgnoring = this.structure.isIgnoreEntities();
        this.initialShowAir = this.structure.getShowAir();
        this.initialShowBoundingBox = this.structure.getShowBoundingBox();
        this.saveButton = (Button) this.m_142416_(Button.builder(Component.translatable("structure_block.button.save"), p_280866_ -> {
            if (this.structure.getMode() == StructureMode.SAVE) {
                this.sendToServer(StructureBlockEntity.UpdateType.SAVE_AREA);
                this.f_96541_.setScreen(null);
            }
        }).bounds(this.f_96543_ / 2 + 4 + 100, 185, 50, 20).build());
        this.loadButton = (Button) this.m_142416_(Button.builder(Component.translatable("structure_block.button.load"), p_280864_ -> {
            if (this.structure.getMode() == StructureMode.LOAD) {
                this.sendToServer(StructureBlockEntity.UpdateType.LOAD_AREA);
                this.f_96541_.setScreen(null);
            }
        }).bounds(this.f_96543_ / 2 + 4 + 100, 185, 50, 20).build());
        this.m_142416_(CycleButton.<StructureMode>builder(p_169852_ -> Component.translatable("structure_block.mode." + p_169852_.getSerializedName())).withValues(DEFAULT_MODES, ALL_MODES).displayOnlyValue().withInitialValue(this.initialMode).create(this.f_96543_ / 2 - 4 - 150, 185, 50, 20, Component.literal("MODE"), (p_169846_, p_169847_) -> {
            this.structure.setMode(p_169847_);
            this.updateMode(p_169847_);
        }));
        this.detectButton = (Button) this.m_142416_(Button.builder(Component.translatable("structure_block.button.detect_size"), p_280865_ -> {
            if (this.structure.getMode() == StructureMode.SAVE) {
                this.sendToServer(StructureBlockEntity.UpdateType.SCAN_AREA);
                this.f_96541_.setScreen(null);
            }
        }).bounds(this.f_96543_ / 2 + 4 + 100, 120, 50, 20).build());
        this.includeEntitiesButton = (CycleButton<Boolean>) this.m_142416_(CycleButton.onOffBuilder(!this.structure.isIgnoreEntities()).displayOnlyValue().create(this.f_96543_ / 2 + 4 + 100, 160, 50, 20, INCLUDE_ENTITIES_LABEL, (p_169861_, p_169862_) -> this.structure.setIgnoreEntities(!p_169862_)));
        this.mirrorButton = (CycleButton<Mirror>) this.m_142416_(CycleButton.<Mirror>builder(Mirror::m_153787_).withValues(Mirror.values()).displayOnlyValue().withInitialValue(this.initialMirror).create(this.f_96543_ / 2 - 20, 185, 40, 20, Component.literal("MIRROR"), (p_169843_, p_169844_) -> this.structure.setMirror(p_169844_)));
        this.toggleAirButton = (CycleButton<Boolean>) this.m_142416_(CycleButton.onOffBuilder(this.structure.getShowAir()).displayOnlyValue().create(this.f_96543_ / 2 + 4 + 100, 80, 50, 20, SHOW_AIR_LABEL, (p_169856_, p_169857_) -> this.structure.setShowAir(p_169857_)));
        this.toggleBoundingBox = (CycleButton<Boolean>) this.m_142416_(CycleButton.onOffBuilder(this.structure.getShowBoundingBox()).displayOnlyValue().create(this.f_96543_ / 2 + 4 + 100, 80, 50, 20, SHOW_BOUNDING_BOX_LABEL, (p_169849_, p_169850_) -> this.structure.setShowBoundingBox(p_169850_)));
        this.rot0Button = (Button) this.m_142416_(Button.builder(Component.literal("0"), p_99425_ -> {
            this.structure.setRotation(Rotation.NONE);
            this.updateDirectionButtons();
        }).bounds(this.f_96543_ / 2 - 1 - 40 - 1 - 40 - 20, 185, 40, 20).build());
        this.rot90Button = (Button) this.m_142416_(Button.builder(Component.literal("90"), p_99415_ -> {
            this.structure.setRotation(Rotation.CLOCKWISE_90);
            this.updateDirectionButtons();
        }).bounds(this.f_96543_ / 2 - 1 - 40 - 20, 185, 40, 20).build());
        this.rot180Button = (Button) this.m_142416_(Button.builder(Component.literal("180"), p_169854_ -> {
            this.structure.setRotation(Rotation.CLOCKWISE_180);
            this.updateDirectionButtons();
        }).bounds(this.f_96543_ / 2 + 1 + 20, 185, 40, 20).build());
        this.rot270Button = (Button) this.m_142416_(Button.builder(Component.literal("270"), p_169841_ -> {
            this.structure.setRotation(Rotation.COUNTERCLOCKWISE_90);
            this.updateDirectionButtons();
        }).bounds(this.f_96543_ / 2 + 1 + 40 + 1 + 20, 185, 40, 20).build());
        this.nameEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 152, 40, 300, 20, Component.translatable("structure_block.structure_name")) {

            @Override
            public boolean charTyped(char p_99476_, int p_99477_) {
                return !StructureBlockEditScreen.this.m_96583_(this.m_94155_(), p_99476_, this.m_94207_()) ? false : super.charTyped(p_99476_, p_99477_);
            }
        };
        this.nameEdit.setMaxLength(128);
        this.nameEdit.setValue(this.structure.getStructureName());
        this.m_7787_(this.nameEdit);
        BlockPos $$0 = this.structure.getStructurePos();
        this.posXEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 152, 80, 80, 20, Component.translatable("structure_block.position.x"));
        this.posXEdit.setMaxLength(15);
        this.posXEdit.setValue(Integer.toString($$0.m_123341_()));
        this.m_7787_(this.posXEdit);
        this.posYEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 72, 80, 80, 20, Component.translatable("structure_block.position.y"));
        this.posYEdit.setMaxLength(15);
        this.posYEdit.setValue(Integer.toString($$0.m_123342_()));
        this.m_7787_(this.posYEdit);
        this.posZEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 + 8, 80, 80, 20, Component.translatable("structure_block.position.z"));
        this.posZEdit.setMaxLength(15);
        this.posZEdit.setValue(Integer.toString($$0.m_123343_()));
        this.m_7787_(this.posZEdit);
        Vec3i $$1 = this.structure.getStructureSize();
        this.sizeXEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 152, 120, 80, 20, Component.translatable("structure_block.size.x"));
        this.sizeXEdit.setMaxLength(15);
        this.sizeXEdit.setValue(Integer.toString($$1.getX()));
        this.m_7787_(this.sizeXEdit);
        this.sizeYEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 72, 120, 80, 20, Component.translatable("structure_block.size.y"));
        this.sizeYEdit.setMaxLength(15);
        this.sizeYEdit.setValue(Integer.toString($$1.getY()));
        this.m_7787_(this.sizeYEdit);
        this.sizeZEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 + 8, 120, 80, 20, Component.translatable("structure_block.size.z"));
        this.sizeZEdit.setMaxLength(15);
        this.sizeZEdit.setValue(Integer.toString($$1.getZ()));
        this.m_7787_(this.sizeZEdit);
        this.integrityEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 152, 120, 80, 20, Component.translatable("structure_block.integrity.integrity"));
        this.integrityEdit.setMaxLength(15);
        this.integrityEdit.setValue(this.decimalFormat.format((double) this.structure.getIntegrity()));
        this.m_7787_(this.integrityEdit);
        this.seedEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 72, 120, 80, 20, Component.translatable("structure_block.integrity.seed"));
        this.seedEdit.setMaxLength(31);
        this.seedEdit.setValue(Long.toString(this.structure.getSeed()));
        this.m_7787_(this.seedEdit);
        this.dataEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 152, 120, 240, 20, Component.translatable("structure_block.custom_data"));
        this.dataEdit.setMaxLength(128);
        this.dataEdit.setValue(this.structure.getMetaData());
        this.m_7787_(this.dataEdit);
        this.updateDirectionButtons();
        this.updateMode(this.initialMode);
        this.m_264313_(this.nameEdit);
    }

    @Override
    public void resize(Minecraft minecraft0, int int1, int int2) {
        String $$3 = this.nameEdit.getValue();
        String $$4 = this.posXEdit.getValue();
        String $$5 = this.posYEdit.getValue();
        String $$6 = this.posZEdit.getValue();
        String $$7 = this.sizeXEdit.getValue();
        String $$8 = this.sizeYEdit.getValue();
        String $$9 = this.sizeZEdit.getValue();
        String $$10 = this.integrityEdit.getValue();
        String $$11 = this.seedEdit.getValue();
        String $$12 = this.dataEdit.getValue();
        this.m_6575_(minecraft0, int1, int2);
        this.nameEdit.setValue($$3);
        this.posXEdit.setValue($$4);
        this.posYEdit.setValue($$5);
        this.posZEdit.setValue($$6);
        this.sizeXEdit.setValue($$7);
        this.sizeYEdit.setValue($$8);
        this.sizeZEdit.setValue($$9);
        this.integrityEdit.setValue($$10);
        this.seedEdit.setValue($$11);
        this.dataEdit.setValue($$12);
    }

    private void updateDirectionButtons() {
        this.rot0Button.f_93623_ = true;
        this.rot90Button.f_93623_ = true;
        this.rot180Button.f_93623_ = true;
        this.rot270Button.f_93623_ = true;
        switch(this.structure.getRotation()) {
            case NONE:
                this.rot0Button.f_93623_ = false;
                break;
            case CLOCKWISE_180:
                this.rot180Button.f_93623_ = false;
                break;
            case COUNTERCLOCKWISE_90:
                this.rot270Button.f_93623_ = false;
                break;
            case CLOCKWISE_90:
                this.rot90Button.f_93623_ = false;
        }
    }

    private void updateMode(StructureMode structureMode0) {
        this.nameEdit.setVisible(false);
        this.posXEdit.setVisible(false);
        this.posYEdit.setVisible(false);
        this.posZEdit.setVisible(false);
        this.sizeXEdit.setVisible(false);
        this.sizeYEdit.setVisible(false);
        this.sizeZEdit.setVisible(false);
        this.integrityEdit.setVisible(false);
        this.seedEdit.setVisible(false);
        this.dataEdit.setVisible(false);
        this.saveButton.f_93624_ = false;
        this.loadButton.f_93624_ = false;
        this.detectButton.f_93624_ = false;
        this.includeEntitiesButton.f_93624_ = false;
        this.mirrorButton.f_93624_ = false;
        this.rot0Button.f_93624_ = false;
        this.rot90Button.f_93624_ = false;
        this.rot180Button.f_93624_ = false;
        this.rot270Button.f_93624_ = false;
        this.toggleAirButton.f_93624_ = false;
        this.toggleBoundingBox.f_93624_ = false;
        switch(structureMode0) {
            case SAVE:
                this.nameEdit.setVisible(true);
                this.posXEdit.setVisible(true);
                this.posYEdit.setVisible(true);
                this.posZEdit.setVisible(true);
                this.sizeXEdit.setVisible(true);
                this.sizeYEdit.setVisible(true);
                this.sizeZEdit.setVisible(true);
                this.saveButton.f_93624_ = true;
                this.detectButton.f_93624_ = true;
                this.includeEntitiesButton.f_93624_ = true;
                this.toggleAirButton.f_93624_ = true;
                break;
            case LOAD:
                this.nameEdit.setVisible(true);
                this.posXEdit.setVisible(true);
                this.posYEdit.setVisible(true);
                this.posZEdit.setVisible(true);
                this.integrityEdit.setVisible(true);
                this.seedEdit.setVisible(true);
                this.loadButton.f_93624_ = true;
                this.includeEntitiesButton.f_93624_ = true;
                this.mirrorButton.f_93624_ = true;
                this.rot0Button.f_93624_ = true;
                this.rot90Button.f_93624_ = true;
                this.rot180Button.f_93624_ = true;
                this.rot270Button.f_93624_ = true;
                this.toggleBoundingBox.f_93624_ = true;
                this.updateDirectionButtons();
                break;
            case CORNER:
                this.nameEdit.setVisible(true);
                break;
            case DATA:
                this.dataEdit.setVisible(true);
        }
    }

    private boolean sendToServer(StructureBlockEntity.UpdateType structureBlockEntityUpdateType0) {
        BlockPos $$1 = new BlockPos(this.parseCoordinate(this.posXEdit.getValue()), this.parseCoordinate(this.posYEdit.getValue()), this.parseCoordinate(this.posZEdit.getValue()));
        Vec3i $$2 = new Vec3i(this.parseCoordinate(this.sizeXEdit.getValue()), this.parseCoordinate(this.sizeYEdit.getValue()), this.parseCoordinate(this.sizeZEdit.getValue()));
        float $$3 = this.parseIntegrity(this.integrityEdit.getValue());
        long $$4 = this.parseSeed(this.seedEdit.getValue());
        this.f_96541_.getConnection().send(new ServerboundSetStructureBlockPacket(this.structure.m_58899_(), structureBlockEntityUpdateType0, this.structure.getMode(), this.nameEdit.getValue(), $$1, $$2, this.structure.getMirror(), this.structure.getRotation(), this.dataEdit.getValue(), this.structure.isIgnoreEntities(), this.structure.getShowAir(), this.structure.getShowBoundingBox(), $$3, $$4));
        return true;
    }

    private long parseSeed(String string0) {
        try {
            return Long.valueOf(string0);
        } catch (NumberFormatException var3) {
            return 0L;
        }
    }

    private float parseIntegrity(String string0) {
        try {
            return Float.valueOf(string0);
        } catch (NumberFormatException var3) {
            return 1.0F;
        }
    }

    private int parseCoordinate(String string0) {
        try {
            return Integer.parseInt(string0);
        } catch (NumberFormatException var3) {
            return 0;
        }
    }

    @Override
    public void onClose() {
        this.onCancel();
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (super.keyPressed(int0, int1, int2)) {
            return true;
        } else if (int0 != 257 && int0 != 335) {
            return false;
        } else {
            this.onDone();
            return true;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        StructureMode $$4 = this.structure.getMode();
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 10, 16777215);
        if ($$4 != StructureMode.DATA) {
            guiGraphics0.drawString(this.f_96547_, NAME_LABEL, this.f_96543_ / 2 - 153, 30, 10526880);
            this.nameEdit.m_88315_(guiGraphics0, int1, int2, float3);
        }
        if ($$4 == StructureMode.LOAD || $$4 == StructureMode.SAVE) {
            guiGraphics0.drawString(this.f_96547_, POSITION_LABEL, this.f_96543_ / 2 - 153, 70, 10526880);
            this.posXEdit.m_88315_(guiGraphics0, int1, int2, float3);
            this.posYEdit.m_88315_(guiGraphics0, int1, int2, float3);
            this.posZEdit.m_88315_(guiGraphics0, int1, int2, float3);
            guiGraphics0.drawString(this.f_96547_, INCLUDE_ENTITIES_LABEL, this.f_96543_ / 2 + 154 - this.f_96547_.width(INCLUDE_ENTITIES_LABEL), 150, 10526880);
        }
        if ($$4 == StructureMode.SAVE) {
            guiGraphics0.drawString(this.f_96547_, SIZE_LABEL, this.f_96543_ / 2 - 153, 110, 10526880);
            this.sizeXEdit.m_88315_(guiGraphics0, int1, int2, float3);
            this.sizeYEdit.m_88315_(guiGraphics0, int1, int2, float3);
            this.sizeZEdit.m_88315_(guiGraphics0, int1, int2, float3);
            guiGraphics0.drawString(this.f_96547_, DETECT_SIZE_LABEL, this.f_96543_ / 2 + 154 - this.f_96547_.width(DETECT_SIZE_LABEL), 110, 10526880);
            guiGraphics0.drawString(this.f_96547_, SHOW_AIR_LABEL, this.f_96543_ / 2 + 154 - this.f_96547_.width(SHOW_AIR_LABEL), 70, 10526880);
        }
        if ($$4 == StructureMode.LOAD) {
            guiGraphics0.drawString(this.f_96547_, INTEGRITY_LABEL, this.f_96543_ / 2 - 153, 110, 10526880);
            this.integrityEdit.m_88315_(guiGraphics0, int1, int2, float3);
            this.seedEdit.m_88315_(guiGraphics0, int1, int2, float3);
            guiGraphics0.drawString(this.f_96547_, SHOW_BOUNDING_BOX_LABEL, this.f_96543_ / 2 + 154 - this.f_96547_.width(SHOW_BOUNDING_BOX_LABEL), 70, 10526880);
        }
        if ($$4 == StructureMode.DATA) {
            guiGraphics0.drawString(this.f_96547_, CUSTOM_DATA_LABEL, this.f_96543_ / 2 - 153, 110, 10526880);
            this.dataEdit.m_88315_(guiGraphics0, int1, int2, float3);
        }
        guiGraphics0.drawString(this.f_96547_, $$4.getDisplayName(), this.f_96543_ / 2 - 153, 174, 10526880);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}