package dev.xkmc.modulargolems.content.modifier.base;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import java.util.function.Consumer;

public class SimpleFlagModifier extends GolemModifier {

    private final GolemFlags flag;

    public SimpleFlagModifier(StatFilterType type, GolemFlags flag) {
        super(type, 1);
        this.flag = flag;
    }

    @Override
    public void onRegisterFlag(Consumer<GolemFlags> addFlag) {
        addFlag.accept(this.flag);
    }
}