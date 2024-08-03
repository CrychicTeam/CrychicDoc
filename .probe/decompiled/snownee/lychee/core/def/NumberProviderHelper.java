package snownee.lychee.core.def;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public class NumberProviderHelper {

    public static void requireConstant(NumberProvider numberProvider) {
        if (numberProvider != null) {
            Preconditions.checkArgument(numberProvider.getType() == NumberProviders.CONSTANT);
        }
    }

    public static Integer toConstant(NumberProvider numberProvider) {
        return numberProvider == null ? null : ((ConstantValue) numberProvider).m_142683_(null);
    }

    public static ConstantValue fromConstant(Integer integer) {
        return integer == null ? null : ConstantValue.exactly((float) integer.intValue());
    }
}