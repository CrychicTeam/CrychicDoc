package com.simibubi.create.foundation.render;

import com.jozufozu.flywheel.core.shader.GameStateProvider;
import com.jozufozu.flywheel.core.shader.ShaderConstants;
import com.simibubi.create.content.kinetics.KineticDebugger;
import javax.annotation.Nonnull;

public enum RainbowDebugStateProvider implements GameStateProvider {

    INSTANCE;

    public boolean isTrue() {
        return KineticDebugger.isActive();
    }

    public void alterConstants(@Nonnull ShaderConstants constants) {
        constants.define("DEBUG_RAINBOW");
    }
}