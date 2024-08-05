package snownee.jade;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.util.CommonProxy;

public final class JadeCommonConfig {

    private static final Set<String> inventoryBlacklist = Sets.newHashSet();

    public static boolean bypassLockedContainer = false;

    private static boolean onlyShowVanilla = false;

    private static final Set<String> modBlacklist = Sets.newHashSet();

    public static boolean shouldIgnoreTE(String id) {
        return inventoryBlacklist.contains(id);
    }

    public static boolean shouldShowCustomName(BlockEntity t) {
        String modid = CommonProxy.getId(t.getType()).getNamespace();
        return onlyShowVanilla ? "minecraft".equals(modid) : !modBlacklist.contains(modid);
    }
}