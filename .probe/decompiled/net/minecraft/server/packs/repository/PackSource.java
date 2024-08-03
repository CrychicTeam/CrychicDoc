package net.minecraft.server.packs.repository;

import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public interface PackSource {

    UnaryOperator<Component> NO_DECORATION = UnaryOperator.identity();

    PackSource DEFAULT = create(NO_DECORATION, true);

    PackSource BUILT_IN = create(decorateWithSource("pack.source.builtin"), true);

    PackSource FEATURE = create(decorateWithSource("pack.source.feature"), false);

    PackSource WORLD = create(decorateWithSource("pack.source.world"), true);

    PackSource SERVER = create(decorateWithSource("pack.source.server"), true);

    Component decorate(Component var1);

    boolean shouldAddAutomatically();

    static PackSource create(final UnaryOperator<Component> unaryOperatorComponent0, final boolean boolean1) {
        return new PackSource() {

            @Override
            public Component decorate(Component p_251609_) {
                return (Component) unaryOperatorComponent0.apply(p_251609_);
            }

            @Override
            public boolean shouldAddAutomatically() {
                return boolean1;
            }
        };
    }

    private static UnaryOperator<Component> decorateWithSource(String string0) {
        Component $$1 = Component.translatable(string0);
        return p_10539_ -> Component.translatable("pack.nameAndSource", p_10539_, $$1).withStyle(ChatFormatting.GRAY);
    }
}