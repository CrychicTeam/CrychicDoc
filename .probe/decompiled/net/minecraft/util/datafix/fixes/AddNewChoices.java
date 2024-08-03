package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TaggedChoice.TaggedChoiceType;
import java.util.Locale;

public class AddNewChoices extends DataFix {

    private final String name;

    private final TypeReference type;

    public AddNewChoices(Schema schema0, String string1, TypeReference typeReference2) {
        super(schema0, true);
        this.name = string1;
        this.type = typeReference2;
    }

    public TypeRewriteRule makeRule() {
        TaggedChoiceType<?> $$0 = this.getInputSchema().findChoiceType(this.type);
        TaggedChoiceType<?> $$1 = this.getOutputSchema().findChoiceType(this.type);
        return this.cap(this.name, $$0, $$1);
    }

    protected final <K> TypeRewriteRule cap(String string0, TaggedChoiceType<K> taggedChoiceTypeK1, TaggedChoiceType<?> taggedChoiceType2) {
        if (taggedChoiceTypeK1.getKeyType() != taggedChoiceType2.getKeyType()) {
            throw new IllegalStateException("Could not inject: key type is not the same");
        } else {
            return this.fixTypeEverywhere(string0, taggedChoiceTypeK1, taggedChoiceType2, p_14636_ -> p_145061_ -> {
                if (!taggedChoiceType2.hasType(p_145061_.getFirst())) {
                    throw new IllegalArgumentException(String.format(Locale.ROOT, "Unknown type %s in %s ", p_145061_.getFirst(), this.type));
                } else {
                    return p_145061_;
                }
            });
        }
    }
}