package fr.frinn.custommachinery.api.upgrade;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public interface IRecipeModifier {

    boolean shouldApply(RequirementType<?> var1, RequirementIOMode var2, @Nullable String var3);

    double apply(double var1, int var3);

    Component getTooltip();

    Component getDefaultTooltip();

    public static enum OPERATION {

        ADDITION, MULTIPLICATION, EXPONENTIAL;

        public static final NamedCodec<IRecipeModifier.OPERATION> CODEC = NamedCodec.enumCodec(IRecipeModifier.OPERATION.class);

        public static IRecipeModifier.OPERATION value(String value) {
            return valueOf(value.toUpperCase(Locale.ROOT));
        }
    }
}