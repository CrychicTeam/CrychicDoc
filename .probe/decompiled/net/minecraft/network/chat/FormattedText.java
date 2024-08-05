package net.minecraft.network.chat;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.Unit;

public interface FormattedText {

    Optional<Unit> STOP_ITERATION = Optional.of(Unit.INSTANCE);

    FormattedText EMPTY = new FormattedText() {

        @Override
        public <T> Optional<T> visit(FormattedText.ContentConsumer<T> p_130779_) {
            return Optional.empty();
        }

        @Override
        public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> p_130781_, Style p_130782_) {
            return Optional.empty();
        }
    };

    <T> Optional<T> visit(FormattedText.ContentConsumer<T> var1);

    <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> var1, Style var2);

    static FormattedText of(final String string0) {
        return new FormattedText() {

            @Override
            public <T> Optional<T> visit(FormattedText.ContentConsumer<T> p_130787_) {
                return p_130787_.accept(string0);
            }

            @Override
            public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> p_130789_, Style p_130790_) {
                return p_130789_.accept(p_130790_, string0);
            }
        };
    }

    static FormattedText of(final String string0, final Style style1) {
        return new FormattedText() {

            @Override
            public <T> Optional<T> visit(FormattedText.ContentConsumer<T> p_130797_) {
                return p_130797_.accept(string0);
            }

            @Override
            public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> p_130799_, Style p_130800_) {
                return p_130799_.accept(style1.applyTo(p_130800_), string0);
            }
        };
    }

    static FormattedText composite(FormattedText... formattedText0) {
        return composite(ImmutableList.copyOf(formattedText0));
    }

    static FormattedText composite(final List<? extends FormattedText> listExtendsFormattedText0) {
        return new FormattedText() {

            @Override
            public <T> Optional<T> visit(FormattedText.ContentConsumer<T> p_130805_) {
                for (FormattedText $$1 : listExtendsFormattedText0) {
                    Optional<T> $$2 = $$1.visit(p_130805_);
                    if ($$2.isPresent()) {
                        return $$2;
                    }
                }
                return Optional.empty();
            }

            @Override
            public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> p_130807_, Style p_130808_) {
                for (FormattedText $$2 : listExtendsFormattedText0) {
                    Optional<T> $$3 = $$2.visit(p_130807_, p_130808_);
                    if ($$3.isPresent()) {
                        return $$3;
                    }
                }
                return Optional.empty();
            }
        };
    }

    default String getString() {
        StringBuilder $$0 = new StringBuilder();
        this.visit(p_130767_ -> {
            $$0.append(p_130767_);
            return Optional.empty();
        });
        return $$0.toString();
    }

    public interface ContentConsumer<T> {

        Optional<T> accept(String var1);
    }

    public interface StyledContentConsumer<T> {

        Optional<T> accept(Style var1, String var2);
    }
}