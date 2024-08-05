package com.simibubi.create.content.kinetics.transmission.sequencer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.compat.computercraft.ComputerScreen;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Vector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SequencedGearshiftScreen extends AbstractSimiScreen {

    private final ItemStack renderedItem = AllBlocks.SEQUENCED_GEARSHIFT.asStack();

    private final AllGuiTextures background = AllGuiTextures.SEQUENCER;

    private IconButton confirmButton;

    private SequencedGearshiftBlockEntity be;

    private ListTag compareTag;

    private Vector<Instruction> instructions;

    private Vector<Vector<ScrollInput>> inputs;

    public SequencedGearshiftScreen(SequencedGearshiftBlockEntity be) {
        super(Lang.translateDirect("gui.sequenced_gearshift.title"));
        this.instructions = be.instructions;
        this.be = be;
        this.compareTag = Instruction.serializeAll(this.instructions);
    }

    @Override
    protected void init() {
        if (this.be.computerBehaviour.hasAttachedComputer()) {
            this.f_96541_.setScreen(new ComputerScreen(this.f_96539_, this::renderAdditional, this, this.be.computerBehaviour::hasAttachedComputer));
        }
        this.setWindowSize(this.background.width, this.background.height);
        this.setWindowOffset(-20, 0);
        super.init();
        int x = this.guiLeft;
        int y = this.guiTop;
        this.inputs = new Vector(5);
        for (int row = 0; row < this.inputs.capacity(); row++) {
            this.inputs.add(new Vector(3));
        }
        for (int row = 0; row < this.instructions.size(); row++) {
            this.initInputsOfRow(row, x, y);
        }
        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> this.m_7379_());
        this.m_142416_(this.confirmButton);
    }

    public void initInputsOfRow(int row, int backgroundX, int backgroundY) {
        int x = backgroundX + 30;
        int y = backgroundY + 20;
        int rowHeight = 22;
        Vector<ScrollInput> rowInputs = (Vector<ScrollInput>) this.inputs.get(row);
        this.removeWidgets(rowInputs);
        rowInputs.clear();
        Instruction instruction = (Instruction) this.instructions.get(row);
        ScrollInput type = new SelectionScrollInput(x, y + rowHeight * row, 50, 18).forOptions(SequencerInstructions.getOptions()).calling(state -> this.instructionUpdated(row, state)).setState(instruction.instruction.ordinal()).titled(Lang.translateDirect("gui.sequenced_gearshift.instruction"));
        ScrollInput value = new ScrollInput(x + 58, y + rowHeight * row, 28, 18).calling(state -> instruction.value = state);
        ScrollInput direction = new SelectionScrollInput(x + 88, y + rowHeight * row, 28, 18).forOptions(InstructionSpeedModifiers.getOptions()).calling(state -> instruction.speedModifier = InstructionSpeedModifiers.values()[state]).titled(Lang.translateDirect("gui.sequenced_gearshift.speed"));
        rowInputs.add(type);
        rowInputs.add(value);
        rowInputs.add(direction);
        this.addRenderableWidgets(rowInputs);
        this.updateParamsOfRow(row);
    }

    public void updateParamsOfRow(int row) {
        Instruction instruction = (Instruction) this.instructions.get(row);
        Vector<ScrollInput> rowInputs = (Vector<ScrollInput>) this.inputs.get(row);
        SequencerInstructions def = instruction.instruction;
        boolean hasValue = def.hasValueParameter;
        boolean hasModifier = def.hasSpeedParameter;
        ScrollInput value = (ScrollInput) rowInputs.get(1);
        value.f_93623_ = value.f_93624_ = hasValue;
        if (hasValue) {
            value.withRange(1, def.maxValue + 1).titled(Lang.translateDirect(def.parameterKey)).withShiftStep(def.shiftStep).setState(instruction.value).onChanged();
        }
        if (def == SequencerInstructions.DELAY) {
            value.withStepFunction(context -> {
                int v = context.currentValue;
                if (!context.forward) {
                    v--;
                }
                return v < 20 ? context.shift ? 20 : 1 : context.shift ? 100 : 20;
            });
        } else {
            value.withStepFunction(value.standardStep());
        }
        ScrollInput modifier = (ScrollInput) rowInputs.get(2);
        modifier.f_93623_ = modifier.f_93624_ = hasModifier;
        if (hasModifier) {
            modifier.setState(instruction.speedModifier.ordinal());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.be.computerBehaviour.hasAttachedComputer()) {
            this.f_96541_.setScreen(new ComputerScreen(this.f_96539_, this::renderAdditional, this, this.be.computerBehaviour::hasAttachedComputer));
        }
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop;
        this.background.render(graphics, x, y);
        for (int row = 0; row < this.instructions.capacity(); row++) {
            AllGuiTextures toDraw = AllGuiTextures.SEQUENCER_EMPTY;
            int yOffset = toDraw.height * row;
            toDraw.render(graphics, x, y + 16 + yOffset);
        }
        for (int row = 0; row < this.instructions.capacity(); row++) {
            AllGuiTextures toDraw = AllGuiTextures.SEQUENCER_EMPTY;
            int yOffset = toDraw.height * row;
            if (row >= this.instructions.size()) {
                toDraw.render(graphics, x, y + 16 + yOffset);
            } else {
                Instruction instruction = (Instruction) this.instructions.get(row);
                SequencerInstructions def = instruction.instruction;
                def.background.render(graphics, x, y + 16 + yOffset);
                this.label(graphics, 36, yOffset - 1, Lang.translateDirect(def.translationKey));
                if (def.hasValueParameter) {
                    String text = def.formatValue(instruction.value);
                    int stringWidth = this.f_96547_.width(text);
                    this.label(graphics, 90 + (12 - stringWidth / 2), yOffset - 1, Components.literal(text));
                }
                if (def.hasSpeedParameter) {
                    this.label(graphics, 127, yOffset - 1, instruction.speedModifier.label);
                }
            }
        }
        graphics.drawString(this.f_96547_, this.f_96539_, x + (this.background.width - 8) / 2 - this.f_96547_.width(this.f_96539_) / 2, y + 4, 5841956, false);
        this.renderAdditional(graphics, mouseX, mouseY, partialTicks, x, y, this.background);
    }

    private void renderAdditional(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, int guiLeft, int guiTop, AllGuiTextures background) {
        GuiGameElement.of(this.renderedItem).<GuiGameElement.GuiRenderBuilder>at((float) (guiLeft + background.width + 6), (float) (guiTop + background.height - 56), 100.0F).scale(5.0).render(graphics);
    }

    private void label(GuiGraphics graphics, int x, int y, Component text) {
        graphics.drawString(this.f_96547_, text, this.guiLeft + x, this.guiTop + 26 + y, 16777198);
    }

    public void sendPacket() {
        ListTag serialized = Instruction.serializeAll(this.instructions);
        if (!serialized.equals(this.compareTag)) {
            AllPackets.getChannel().sendToServer(new ConfigureSequencedGearshiftPacket(this.be.m_58899_(), serialized));
        }
    }

    @Override
    public void removed() {
        this.sendPacket();
    }

    private void instructionUpdated(int index, int state) {
        SequencerInstructions newValue = SequencerInstructions.values()[state];
        ((Instruction) this.instructions.get(index)).instruction = newValue;
        ((Instruction) this.instructions.get(index)).value = newValue.defaultValue;
        this.updateParamsOfRow(index);
        if (newValue == SequencerInstructions.END) {
            for (int i = this.instructions.size() - 1; i > index; i--) {
                this.instructions.remove(i);
                Vector<ScrollInput> rowInputs = (Vector<ScrollInput>) this.inputs.get(i);
                this.removeWidgets(rowInputs);
                rowInputs.clear();
            }
        } else if (index + 1 < this.instructions.capacity() && index + 1 == this.instructions.size()) {
            this.instructions.add(new Instruction(SequencerInstructions.END));
            this.initInputsOfRow(index + 1, this.guiLeft, this.guiTop);
        }
    }
}