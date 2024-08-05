package com.simibubi.create.content.trains.station;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainIconType;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.utility.Lang;
import java.lang.ref.WeakReference;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class AssemblyScreen extends AbstractStationScreen {

    private IconButton quitAssembly;

    private IconButton toggleAssemblyButton;

    private List<ResourceLocation> iconTypes;

    private ScrollInput iconTypeScroll;

    public AssemblyScreen(StationBlockEntity be, GlobalStation station) {
        super(be, station);
        this.background = AllGuiTextures.STATION_ASSEMBLING;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.guiLeft;
        int y = this.guiTop;
        int by = y + this.background.height - 24;
        Renderable widget = (Renderable) this.f_169369_.get(0);
        if (widget instanceof IconButton ib) {
            ib.setIcon(AllIcons.I_PRIORITY_VERY_LOW);
            ib.setToolTip(Lang.translateDirect("station.close"));
        }
        this.iconTypes = TrainIconType.REGISTRY.keySet().stream().toList();
        this.iconTypeScroll = new ScrollInput(x + 4, y + 17, 184, 14).titled(Lang.translateDirect("station.icon_type"));
        this.iconTypeScroll.withRange(0, this.iconTypes.size());
        this.iconTypeScroll.withStepFunction(ctx -> -(Integer) this.iconTypeScroll.standardStep().apply(ctx));
        this.iconTypeScroll.calling(s -> {
            Train train = (Train) this.displayedTrain.get();
            if (train != null) {
                train.icon = TrainIconType.byId((ResourceLocation) this.iconTypes.get(s));
            }
        });
        this.iconTypeScroll.f_93623_ = this.iconTypeScroll.f_93624_ = false;
        this.m_142416_(this.iconTypeScroll);
        this.toggleAssemblyButton = new WideIconButton(x + 94, by, AllGuiTextures.I_ASSEMBLE_TRAIN);
        this.toggleAssemblyButton.f_93623_ = false;
        this.toggleAssemblyButton.setToolTip(Lang.translateDirect("station.assemble_train"));
        this.toggleAssemblyButton.withCallback(() -> AllPackets.getChannel().sendToServer(StationEditPacket.tryAssemble(this.blockEntity.m_58899_())));
        this.quitAssembly = new IconButton(x + 73, by, AllIcons.I_DISABLE);
        this.quitAssembly.f_93623_ = true;
        this.quitAssembly.setToolTip(Lang.translateDirect("station.cancel"));
        this.quitAssembly.withCallback(() -> {
            AllPackets.getChannel().sendToServer(StationEditPacket.configure(this.blockEntity.m_58899_(), false, this.station.name, null));
            this.f_96541_.setScreen(new StationScreen(this.blockEntity, this.station));
        });
        this.m_142416_(this.toggleAssemblyButton);
        this.m_142416_(this.quitAssembly);
        this.tickTrainDisplay();
    }

    @Override
    public void tick() {
        super.tick();
        this.tickTrainDisplay();
        Train train = (Train) this.displayedTrain.get();
        this.toggleAssemblyButton.f_93623_ = this.blockEntity.bogeyCount > 0 || train != null;
        if (train != null) {
            AllPackets.getChannel().sendToServer(StationEditPacket.configure(this.blockEntity.m_58899_(), false, this.station.name, null));
            this.f_96541_.setScreen(new StationScreen(this.blockEntity, this.station));
            for (Carriage carriage : train.carriages) {
                carriage.updateConductors();
            }
        }
    }

    private void tickTrainDisplay() {
        if (this.getImminent() == null) {
            this.displayedTrain = new WeakReference(null);
            this.quitAssembly.f_93623_ = true;
            this.iconTypeScroll.f_93623_ = this.iconTypeScroll.f_93624_ = false;
            this.toggleAssemblyButton.setToolTip(Lang.translateDirect("station.assemble_train"));
            this.toggleAssemblyButton.setIcon(AllGuiTextures.I_ASSEMBLE_TRAIN);
            this.toggleAssemblyButton.withCallback(() -> AllPackets.getChannel().sendToServer(StationEditPacket.tryAssemble(this.blockEntity.m_58899_())));
        } else {
            AllPackets.getChannel().sendToServer(StationEditPacket.configure(this.blockEntity.m_58899_(), false, this.station.name, null));
            this.f_96541_.setScreen(new StationScreen(this.blockEntity, this.station));
        }
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWindow(graphics, mouseX, mouseY, partialTicks);
        int x = this.guiLeft;
        int y = this.guiTop;
        MutableComponent header = Lang.translateDirect("station.assembly_title");
        graphics.drawString(this.f_96547_, header, x + this.background.width / 2 - this.f_96547_.width(header) / 2, y + 4, 926259, false);
        AssemblyException lastAssemblyException = this.blockEntity.lastException;
        if (lastAssemblyException != null) {
            MutableComponent text = Lang.translateDirect("station.failed");
            graphics.drawString(this.f_96547_, text, x + 97 - this.f_96547_.width(text) / 2, y + 47, 7822171, false);
            int offset = 0;
            if (this.blockEntity.failedCarriageIndex != -1) {
                graphics.drawString(this.f_96547_, Lang.translateDirect("station.carriage_number", this.blockEntity.failedCarriageIndex), x + 30, y + 67, 8026746, false);
                offset += 10;
            }
            graphics.drawWordWrap(this.f_96547_, lastAssemblyException.component, x + 30, y + 67 + offset, 134, 7822171);
            offset += this.f_96547_.split(lastAssemblyException.component, 134).size() * 9 + 5;
            graphics.drawWordWrap(this.f_96547_, Lang.translateDirect("station.retry"), x + 30, y + 67 + offset, 134, 8026746);
        } else {
            int bogeyCount = this.blockEntity.bogeyCount;
            MutableComponent text = Lang.translateDirect(bogeyCount == 0 ? "station.no_bogeys" : (bogeyCount == 1 ? "station.one_bogey" : "station.more_bogeys"), bogeyCount);
            graphics.drawString(this.f_96547_, text, x + 97 - this.f_96547_.width(text) / 2, y + 47, 8026746, false);
            graphics.drawWordWrap(this.f_96547_, Lang.translateDirect("station.how_to"), x + 28, y + 62, 134, 8026746);
            graphics.drawWordWrap(this.f_96547_, Lang.translateDirect("station.how_to_1"), x + 28, y + 94, 134, 8026746);
            graphics.drawWordWrap(this.f_96547_, Lang.translateDirect("station.how_to_2"), x + 28, y + 117, 138, 8026746);
        }
    }

    @Override
    public void removed() {
        super.m_7861_();
        Train train = (Train) this.displayedTrain.get();
        if (train != null) {
            ResourceLocation iconId = (ResourceLocation) this.iconTypes.get(this.iconTypeScroll.getState());
            train.icon = TrainIconType.byId(iconId);
            AllPackets.getChannel().sendToServer(new TrainEditPacket(train.id, "", iconId));
        }
    }

    @Override
    protected PartialModel getFlag(float partialTicks) {
        return AllPartialModels.STATION_ASSEMBLE;
    }
}