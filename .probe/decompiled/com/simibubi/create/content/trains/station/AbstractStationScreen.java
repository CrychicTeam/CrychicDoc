package com.simibubi.create.content.trains.station;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.CreateClient;
import com.simibubi.create.compat.computercraft.ComputerScreen;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainIconType;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Components;
import java.lang.ref.WeakReference;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class AbstractStationScreen extends AbstractSimiScreen {

    protected AllGuiTextures background;

    protected StationBlockEntity blockEntity;

    protected GlobalStation station;

    protected WeakReference<Train> displayedTrain;

    private IconButton confirmButton;

    public AbstractStationScreen(StationBlockEntity be, GlobalStation station) {
        super(be.m_58900_().m_60734_().getName());
        this.blockEntity = be;
        this.station = station;
        this.displayedTrain = new WeakReference(null);
    }

    @Override
    protected void init() {
        if (this.blockEntity.computerBehaviour.hasAttachedComputer()) {
            this.f_96541_.setScreen(new ComputerScreen(this.f_96539_, () -> Components.literal(this.station.name), this::renderAdditional, this, this.blockEntity.computerBehaviour::hasAttachedComputer));
        }
        this.setWindowSize(this.background.width, this.background.height);
        super.init();
        this.m_169413_();
        int x = this.guiLeft;
        int y = this.guiTop;
        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(this::m_7379_);
        this.m_142416_(this.confirmButton);
    }

    public int getTrainIconWidth(Train train) {
        TrainIconType icon = train.icon;
        List<Carriage> carriages = train.carriages;
        int w = icon.getIconWidth(-1);
        if (carriages.size() == 1) {
            return w;
        } else {
            for (int i = 1; i < carriages.size(); i++) {
                if (i == carriages.size() - 1 && train.doubleEnded) {
                    w += icon.getIconWidth(-2) + 1;
                    break;
                }
                Carriage carriage = (Carriage) carriages.get(i);
                w += icon.getIconWidth(carriage.bogeySpacing) + 1;
            }
            return w;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.blockEntity.computerBehaviour.hasAttachedComputer()) {
            this.f_96541_.setScreen(new ComputerScreen(this.f_96539_, () -> Components.literal(this.station.name), this::renderAdditional, this, this.blockEntity.computerBehaviour::hasAttachedComputer));
        }
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop;
        this.background.render(graphics, x, y);
        this.renderAdditional(graphics, mouseX, mouseY, partialTicks, x, y, this.background);
    }

    private void renderAdditional(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, int guiLeft, int guiTop, AllGuiTextures background) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) msr.pushPose()).translate((double) (guiLeft + background.width + 4), (double) (guiTop + background.height + 4), 100.0)).scale(40.0F)).rotateX(-22.0)).rotateY(63.0);
        GuiGameElement.of((BlockState) this.blockEntity.m_58900_().m_61124_(BlockStateProperties.WATERLOGGED, false)).render(graphics);
        if (this.blockEntity.resolveFlagAngle()) {
            msr.translate(0.0625, -1.1875, -0.75);
            StationRenderer.transformFlag(msr, this.blockEntity, partialTicks, 180, false);
            GuiGameElement.of(this.getFlag(partialTicks)).render(graphics);
        }
        ms.popPose();
    }

    protected abstract PartialModel getFlag(float var1);

    protected Train getImminent() {
        return this.blockEntity.imminentTrain == null ? null : (Train) CreateClient.RAILWAYS.trains.get(this.blockEntity.imminentTrain);
    }

    protected boolean trainPresent() {
        return this.blockEntity.trainPresent;
    }
}