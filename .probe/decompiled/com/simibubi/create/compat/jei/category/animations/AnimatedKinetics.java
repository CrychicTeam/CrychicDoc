package com.simibubi.create.compat.jei.category.animations;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.gui.CustomLightingSettings;
import com.simibubi.create.foundation.gui.ILightingSettings;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class AnimatedKinetics implements IDrawable {

    public int offset = 0;

    public static final ILightingSettings DEFAULT_LIGHTING = CustomLightingSettings.builder().firstLightRotation(12.5F, 45.0F).secondLightRotation(-20.0F, 50.0F).build();

    public static GuiGameElement.GuiRenderBuilder defaultBlockElement(BlockState state) {
        return GuiGameElement.of(state).lighting(DEFAULT_LIGHTING);
    }

    public static GuiGameElement.GuiRenderBuilder defaultBlockElement(PartialModel partial) {
        return GuiGameElement.of(partial).lighting(DEFAULT_LIGHTING);
    }

    public static float getCurrentAngle() {
        return AnimationTickHolder.getRenderTime() * 4.0F % 360.0F;
    }

    protected BlockState shaft(Direction.Axis axis) {
        return (BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(BlockStateProperties.AXIS, axis);
    }

    protected PartialModel cogwheel() {
        return AllPartialModels.SHAFTLESS_COGWHEEL;
    }

    protected GuiGameElement.GuiRenderBuilder blockElement(BlockState state) {
        return defaultBlockElement(state);
    }

    protected GuiGameElement.GuiRenderBuilder blockElement(PartialModel partial) {
        return defaultBlockElement(partial);
    }

    @Override
    public int getWidth() {
        return 50;
    }

    @Override
    public int getHeight() {
        return 50;
    }
}