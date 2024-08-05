package net.minecraft.network.chat.contents;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;

public class TranslatableContents implements ComponentContents {

    public static final Object[] NO_ARGS = new Object[0];

    private static final FormattedText TEXT_PERCENT = FormattedText.of("%");

    private static final FormattedText TEXT_NULL = FormattedText.of("null");

    private final String key;

    @Nullable
    private final String fallback;

    private final Object[] args;

    @Nullable
    private Language decomposedWith;

    private List<FormattedText> decomposedParts = ImmutableList.of();

    private static final Pattern FORMAT_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public TranslatableContents(String string0, @Nullable String string1, Object[] object2) {
        this.key = string0;
        this.fallback = string1;
        this.args = object2;
    }

    private void decompose() {
        Language $$0 = Language.getInstance();
        if ($$0 != this.decomposedWith) {
            this.decomposedWith = $$0;
            String $$1 = this.fallback != null ? $$0.getOrDefault(this.key, this.fallback) : $$0.getOrDefault(this.key);
            try {
                Builder<FormattedText> $$2 = ImmutableList.builder();
                this.decomposeTemplate($$1, $$2::add);
                this.decomposedParts = $$2.build();
            } catch (TranslatableFormatException var4) {
                this.decomposedParts = ImmutableList.of(FormattedText.of($$1));
            }
        }
    }

    private void decomposeTemplate(String string0, Consumer<FormattedText> consumerFormattedText1) {
        Matcher $$2 = FORMAT_PATTERN.matcher(string0);
        try {
            int $$3 = 0;
            int $$4 = 0;
            while ($$2.find($$4)) {
                int $$5 = $$2.start();
                int $$6 = $$2.end();
                if ($$5 > $$4) {
                    String $$7 = string0.substring($$4, $$5);
                    if ($$7.indexOf(37) != -1) {
                        throw new IllegalArgumentException();
                    }
                    consumerFormattedText1.accept(FormattedText.of($$7));
                }
                String $$8 = $$2.group(2);
                String $$9 = string0.substring($$5, $$6);
                if ("%".equals($$8) && "%%".equals($$9)) {
                    consumerFormattedText1.accept(TEXT_PERCENT);
                } else {
                    if (!"s".equals($$8)) {
                        throw new TranslatableFormatException(this, "Unsupported format: '" + $$9 + "'");
                    }
                    String $$10 = $$2.group(1);
                    int $$11 = $$10 != null ? Integer.parseInt($$10) - 1 : $$3++;
                    consumerFormattedText1.accept(this.getArgument($$11));
                }
                $$4 = $$6;
            }
            if ($$4 < string0.length()) {
                String $$12 = string0.substring($$4);
                if ($$12.indexOf(37) != -1) {
                    throw new IllegalArgumentException();
                }
                consumerFormattedText1.accept(FormattedText.of($$12));
            }
        } catch (IllegalArgumentException var12) {
            throw new TranslatableFormatException(this, var12);
        }
    }

    private FormattedText getArgument(int int0) {
        if (int0 >= 0 && int0 < this.args.length) {
            Object $$1 = this.args[int0];
            if ($$1 instanceof Component) {
                return (Component) $$1;
            } else {
                return $$1 == null ? TEXT_NULL : FormattedText.of($$1.toString());
            }
        } else {
            throw new TranslatableFormatException(this, int0);
        }
    }

    @Override
    public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> formattedTextStyledContentConsumerT0, Style style1) {
        this.decompose();
        for (FormattedText $$2 : this.decomposedParts) {
            Optional<T> $$3 = $$2.visit(formattedTextStyledContentConsumerT0, style1);
            if ($$3.isPresent()) {
                return $$3;
            }
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> visit(FormattedText.ContentConsumer<T> formattedTextContentConsumerT0) {
        this.decompose();
        for (FormattedText $$1 : this.decomposedParts) {
            Optional<T> $$2 = $$1.visit(formattedTextContentConsumerT0);
            if ($$2.isPresent()) {
                return $$2;
            }
        }
        return Optional.empty();
    }

    @Override
    public MutableComponent resolve(@Nullable CommandSourceStack commandSourceStack0, @Nullable Entity entity1, int int2) throws CommandSyntaxException {
        Object[] $$3 = new Object[this.args.length];
        for (int $$4 = 0; $$4 < $$3.length; $$4++) {
            Object $$5 = this.args[$$4];
            if ($$5 instanceof Component) {
                $$3[$$4] = ComponentUtils.updateForEntity(commandSourceStack0, (Component) $$5, entity1, int2);
            } else {
                $$3[$$4] = $$5;
            }
        }
        return MutableComponent.create(new TranslatableContents(this.key, this.fallback, $$3));
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            if (object0 instanceof TranslatableContents $$1 && Objects.equals(this.key, $$1.key) && Objects.equals(this.fallback, $$1.fallback) && Arrays.equals(this.args, $$1.args)) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        int $$0 = Objects.hashCode(this.key);
        $$0 = 31 * $$0 + Objects.hashCode(this.fallback);
        return 31 * $$0 + Arrays.hashCode(this.args);
    }

    public String toString() {
        return "translation{key='" + this.key + "'" + (this.fallback != null ? ", fallback='" + this.fallback + "'" : "") + ", args=" + Arrays.toString(this.args) + "}";
    }

    public String getKey() {
        return this.key;
    }

    @Nullable
    public String getFallback() {
        return this.fallback;
    }

    public Object[] getArgs() {
        return this.args;
    }
}