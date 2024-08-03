package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class BlockEntityCustomNameToComponentFix extends DataFix {

    public BlockEntityCustomNameToComponentFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        OpticFinder<String> $$0 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
        return this.fixTypeEverywhereTyped("BlockEntityCustomNameToComponentFix", this.getInputSchema().getType(References.BLOCK_ENTITY), p_14821_ -> p_14821_.update(DSL.remainderFinder(), p_145133_ -> {
            Optional<String> $$3 = p_14821_.getOptional($$0);
            return $$3.isPresent() && Objects.equals($$3.get(), "minecraft:command_block") ? p_145133_ : EntityCustomNameToComponentFix.fixTagCustomName(p_145133_);
        }));
    }
}