package mezz.jei.common.platform;

import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IPlatformItemStackHelper {

    int getBurnTime(ItemStack var1);

    boolean isBookEnchantable(ItemStack var1, ItemStack var2);

    Optional<String> getCreatorModId(ItemStack var1);

    List<Component> getTestTooltip(@Nullable Player var1, ItemStack var2);
}