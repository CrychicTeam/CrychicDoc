package com.simibubi.create.content.equipment.zapper.terrainzapper;

import com.simibubi.create.content.equipment.zapper.ConfigureZapperPacket;
import com.simibubi.create.content.equipment.zapper.ZapperScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.List;
import java.util.Vector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class WorldshaperScreen extends ZapperScreen {

    protected final Component placementSection = Lang.translateDirect("gui.terrainzapper.placement");

    protected final Component toolSection = Lang.translateDirect("gui.terrainzapper.tool");

    protected final List<Component> brushOptions = Lang.translatedOptions("gui.terrainzapper.brush", "cuboid", "sphere", "cylinder", "surface", "cluster");

    protected Vector<IconButton> toolButtons;

    protected Vector<IconButton> placementButtons;

    protected ScrollInput brushInput;

    protected Label brushLabel;

    protected Vector<ScrollInput> brushParams = new Vector(3);

    protected Vector<Label> brushParamLabels = new Vector(3);

    protected IconButton followDiagonals;

    protected IconButton acrossMaterials;

    protected Indicator followDiagonalsIndicator;

    protected Indicator acrossMaterialsIndicator;

    protected TerrainBrushes currentBrush;

    protected int[] currentBrushParams = new int[] { 1, 1, 1 };

    protected boolean currentFollowDiagonals;

    protected boolean currentAcrossMaterials;

    protected TerrainTools currentTool;

    protected PlacementOptions currentPlacement;

    public WorldshaperScreen(ItemStack zapper, InteractionHand hand) {
        super(AllGuiTextures.TERRAINZAPPER, zapper, hand);
        this.fontColor = 7763574;
        this.title = zapper.getHoverName();
        CompoundTag nbt = zapper.getOrCreateTag();
        this.currentBrush = NBTHelper.readEnum(nbt, "Brush", TerrainBrushes.class);
        if (nbt.contains("BrushParams", 10)) {
            BlockPos paramsData = NbtUtils.readBlockPos(nbt.getCompound("BrushParams"));
            this.currentBrushParams[0] = paramsData.m_123341_();
            this.currentBrushParams[1] = paramsData.m_123342_();
            this.currentBrushParams[2] = paramsData.m_123343_();
            if (this.currentBrushParams[1] == 0) {
                this.currentFollowDiagonals = true;
            }
            if (this.currentBrushParams[2] == 0) {
                this.currentAcrossMaterials = true;
            }
        }
        this.currentTool = NBTHelper.readEnum(nbt, "Tool", TerrainTools.class);
        this.currentPlacement = NBTHelper.readEnum(nbt, "Placement", PlacementOptions.class);
    }

    @Override
    protected void init() {
        super.init();
        int x = this.guiLeft;
        int y = this.guiTop;
        this.brushLabel = new Label(x + 61, y + 25, Components.immutableEmpty()).withShadow();
        this.brushInput = new SelectionScrollInput(x + 56, y + 20, 77, 18).forOptions(this.brushOptions).titled(Lang.translateDirect("gui.terrainzapper.brush")).writingTo(this.brushLabel).calling(brushIndex -> {
            this.currentBrush = TerrainBrushes.values()[brushIndex];
            this.initBrushParams(x, y);
        });
        this.brushInput.setState(this.currentBrush.ordinal());
        this.m_142416_(this.brushLabel);
        this.m_142416_(this.brushInput);
        this.initBrushParams(x, y);
    }

    protected void initBrushParams(int x, int y) {
        Brush currentBrush = this.currentBrush.get();
        this.removeWidgets(this.brushParamLabels);
        this.removeWidgets(this.brushParams);
        this.brushParamLabels.clear();
        this.brushParams.clear();
        for (int index = 0; index < 3; index++) {
            Label label = new Label(x + 65 + 20 * index, y + 45, Components.immutableEmpty()).withShadow();
            ScrollInput input = new ScrollInput(x + 56 + 20 * index, y + 40, 18, 18).withRange(currentBrush.getMin(index), currentBrush.getMax(index) + 1).writingTo(label).titled(currentBrush.getParamLabel(index).plainCopy()).calling(state -> {
                this.currentBrushParams[index] = state;
                label.m_252865_(x + 65 + 20 * index - this.f_96547_.width(label.text) / 2);
            });
            input.setState(this.currentBrushParams[index]);
            input.onChanged();
            if (index >= currentBrush.amtParams) {
                input.f_93624_ = false;
                label.f_93624_ = false;
                input.f_93623_ = false;
            }
            this.brushParamLabels.add(label);
            this.brushParams.add(input);
        }
        this.addRenderableWidgets(this.brushParamLabels);
        this.addRenderableWidgets(this.brushParams);
        if (this.followDiagonals != null) {
            this.m_169411_(this.followDiagonals);
            this.m_169411_(this.followDiagonalsIndicator);
            this.m_169411_(this.acrossMaterials);
            this.m_169411_(this.acrossMaterialsIndicator);
            this.followDiagonals = null;
            this.followDiagonalsIndicator = null;
            this.acrossMaterials = null;
            this.acrossMaterialsIndicator = null;
        }
        if (currentBrush.hasConnectivityOptions()) {
            int x1 = x + 7 + 72;
            int y1 = y + 79;
            this.followDiagonalsIndicator = new Indicator(x1, y1 - 6, Components.immutableEmpty());
            this.followDiagonals = new IconButton(x1, y1, AllIcons.I_FOLLOW_DIAGONAL);
            x1 += 18;
            this.acrossMaterialsIndicator = new Indicator(x1, y1 - 6, Components.immutableEmpty());
            this.acrossMaterials = new IconButton(x1, y1, AllIcons.I_FOLLOW_MATERIAL);
            this.followDiagonals.withCallback(() -> {
                this.followDiagonalsIndicator.state = this.followDiagonalsIndicator.state == Indicator.State.OFF ? Indicator.State.ON : Indicator.State.OFF;
                this.currentFollowDiagonals = !this.currentFollowDiagonals;
            });
            this.followDiagonals.setToolTip(Lang.translateDirect("gui.terrainzapper.searchDiagonal"));
            this.acrossMaterials.withCallback(() -> {
                this.acrossMaterialsIndicator.state = this.acrossMaterialsIndicator.state == Indicator.State.OFF ? Indicator.State.ON : Indicator.State.OFF;
                this.currentAcrossMaterials = !this.currentAcrossMaterials;
            });
            this.acrossMaterials.setToolTip(Lang.translateDirect("gui.terrainzapper.searchFuzzy"));
            this.m_142416_(this.followDiagonals);
            this.m_142416_(this.followDiagonalsIndicator);
            this.m_142416_(this.acrossMaterials);
            this.m_142416_(this.acrossMaterialsIndicator);
            if (this.currentFollowDiagonals) {
                this.followDiagonalsIndicator.state = Indicator.State.ON;
            }
            if (this.currentAcrossMaterials) {
                this.acrossMaterialsIndicator.state = Indicator.State.ON;
            }
        }
        if (this.toolButtons != null) {
            this.removeWidgets(this.toolButtons);
        }
        TerrainTools[] toolValues = currentBrush.getSupportedTools();
        this.toolButtons = new Vector(toolValues.length);
        for (int id = 0; id < toolValues.length; id++) {
            TerrainTools tool = toolValues[id];
            IconButton toolButton = new IconButton(x + 7 + id * 18, y + 79, tool.icon);
            toolButton.withCallback(() -> {
                this.toolButtons.forEach(b -> b.f_93623_ = true);
                toolButton.f_93623_ = false;
                this.currentTool = tool;
            });
            toolButton.setToolTip(Lang.translateDirect("gui.terrainzapper.tool." + tool.translationKey));
            this.toolButtons.add(toolButton);
        }
        int toolIndex = -1;
        for (int i = 0; i < toolValues.length; i++) {
            if (this.currentTool == toolValues[i]) {
                toolIndex = i;
            }
        }
        if (toolIndex == -1) {
            this.currentTool = toolValues[0];
            toolIndex = 0;
        }
        ((IconButton) this.toolButtons.get(toolIndex)).f_93623_ = false;
        this.addRenderableWidgets(this.toolButtons);
        if (this.placementButtons != null) {
            this.removeWidgets(this.placementButtons);
        }
        if (currentBrush.hasPlacementOptions()) {
            PlacementOptions[] placementValues = PlacementOptions.values();
            this.placementButtons = new Vector(placementValues.length);
            for (int id = 0; id < placementValues.length; id++) {
                PlacementOptions option = placementValues[id];
                IconButton placementButton = new IconButton(x + 136 + id * 18, y + 79, option.icon);
                placementButton.withCallback(() -> {
                    this.placementButtons.forEach(b -> b.f_93623_ = true);
                    placementButton.f_93623_ = false;
                    this.currentPlacement = option;
                });
                placementButton.setToolTip(Lang.translateDirect("gui.terrainzapper.placement." + option.translationKey));
                this.placementButtons.add(placementButton);
            }
            ((IconButton) this.placementButtons.get(this.currentPlacement.ordinal())).f_93623_ = false;
            this.addRenderableWidgets(this.placementButtons);
        }
    }

    @Override
    protected void drawOnBackground(GuiGraphics graphics, int x, int y) {
        super.drawOnBackground(graphics, x, y);
        Brush currentBrush = this.currentBrush.get();
        for (int index = 2; index >= currentBrush.amtParams; index--) {
            AllGuiTextures.TERRAINZAPPER_INACTIVE_PARAM.render(graphics, x + 56 + 20 * index, y + 40);
        }
        graphics.drawString(this.f_96547_, this.toolSection, x + 7, y + 69, this.fontColor, false);
        if (currentBrush.hasPlacementOptions()) {
            graphics.drawString(this.f_96547_, this.placementSection, x + 136, y + 69, this.fontColor, false);
        }
    }

    @Override
    protected ConfigureZapperPacket getConfigurationPacket() {
        int brushParamX = this.currentBrushParams[0];
        int brushParamY = this.followDiagonalsIndicator != null ? (this.followDiagonalsIndicator.state == Indicator.State.ON ? 0 : 1) : this.currentBrushParams[1];
        int brushParamZ = this.acrossMaterialsIndicator != null ? (this.acrossMaterialsIndicator.state == Indicator.State.ON ? 0 : 1) : this.currentBrushParams[2];
        return new ConfigureWorldshaperPacket(this.hand, this.currentPattern, this.currentBrush, brushParamX, brushParamY, brushParamZ, this.currentTool, this.currentPlacement);
    }
}