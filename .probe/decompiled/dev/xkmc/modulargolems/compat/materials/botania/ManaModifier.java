package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.Consumer;

public class ManaModifier extends GolemModifier {

    public ManaModifier(StatFilterType type, int maxLevel) {
        super(type, maxLevel);
    }

    @Override
    public void onRegisterFlag(Consumer<GolemFlags> addFlag) {
        addFlag.accept(GolemFlags.BOTANIA);
    }
}