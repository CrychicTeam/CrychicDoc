package snownee.lychee.mixin;

import java.util.List;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ StatePropertiesPredicate.class })
public interface StatePropertiesPredicateAccess {

    @Accessor
    List<StatePropertiesPredicate.PropertyMatcher> getProperties();
}