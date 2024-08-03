package mezz.jei.common.platform;

import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.common.input.keys.IJeiKeyMappingCategoryBuilder;
import net.minecraft.client.KeyMapping;

public interface IPlatformInputHelper {

    boolean isActiveAndMatches(KeyMapping var1, InputConstants.Key var2);

    IJeiKeyMappingCategoryBuilder createKeyMappingCategoryBuilder(String var1);
}