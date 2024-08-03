package net.minecraft.network.chat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringDecomposer;

public class SubStringSource {

    private final String plainText;

    private final List<Style> charStyles;

    private final Int2IntFunction reverseCharModifier;

    private SubStringSource(String string0, List<Style> listStyle1, Int2IntFunction intIntFunction2) {
        this.plainText = string0;
        this.charStyles = ImmutableList.copyOf(listStyle1);
        this.reverseCharModifier = intIntFunction2;
    }

    public String getPlainText() {
        return this.plainText;
    }

    public List<FormattedCharSequence> substring(int int0, int int1, boolean boolean2) {
        if (int1 == 0) {
            return ImmutableList.of();
        } else {
            List<FormattedCharSequence> $$3 = Lists.newArrayList();
            Style $$4 = (Style) this.charStyles.get(int0);
            int $$5 = int0;
            for (int $$6 = 1; $$6 < int1; $$6++) {
                int $$7 = int0 + $$6;
                Style $$8 = (Style) this.charStyles.get($$7);
                if (!$$8.equals($$4)) {
                    String $$9 = this.plainText.substring($$5, $$7);
                    $$3.add(boolean2 ? FormattedCharSequence.backward($$9, $$4, this.reverseCharModifier) : FormattedCharSequence.forward($$9, $$4));
                    $$4 = $$8;
                    $$5 = $$7;
                }
            }
            if ($$5 < int0 + int1) {
                String $$10 = this.plainText.substring($$5, int0 + int1);
                $$3.add(boolean2 ? FormattedCharSequence.backward($$10, $$4, this.reverseCharModifier) : FormattedCharSequence.forward($$10, $$4));
            }
            return boolean2 ? Lists.reverse($$3) : $$3;
        }
    }

    public static SubStringSource create(FormattedText formattedText0) {
        return create(formattedText0, p_178527_ -> p_178527_, p_178529_ -> p_178529_);
    }

    public static SubStringSource create(FormattedText formattedText0, Int2IntFunction intIntFunction1, UnaryOperator<String> unaryOperatorString2) {
        StringBuilder $$3 = new StringBuilder();
        List<Style> $$4 = Lists.newArrayList();
        formattedText0.visit((p_131249_, p_131250_) -> {
            StringDecomposer.iterateFormatted(p_131250_, p_131249_, (p_178533_, p_178534_, p_178535_) -> {
                $$3.appendCodePoint(p_178535_);
                int $$5 = Character.charCount(p_178535_);
                for (int $$6 = 0; $$6 < $$5; $$6++) {
                    $$4.add(p_178534_);
                }
                return true;
            });
            return Optional.empty();
        }, Style.EMPTY);
        return new SubStringSource((String) unaryOperatorString2.apply($$3.toString()), $$4, intIntFunction1);
    }
}