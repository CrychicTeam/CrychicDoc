package com.simibubi.create.foundation.gui;

import com.mojang.blaze3d.platform.Lighting;

public interface ILightingSettings {

    ILightingSettings DEFAULT_3D = () -> Lighting.setupFor3DItems();

    ILightingSettings DEFAULT_FLAT = () -> Lighting.setupForFlatItems();

    void applyLighting();
}