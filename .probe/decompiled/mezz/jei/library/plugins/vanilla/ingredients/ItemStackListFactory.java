package mezz.jei.library.plugins.vanilla.ingredients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.common.util.StackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ItemStackListFactory {

    private static final Logger LOGGER = LogManager.getLogger();

    public static List<ItemStack> create(StackHelper stackHelper) {
        List<ItemStack> itemList = new ArrayList();
        Set<String> itemNameSet = new HashSet();
        Minecraft minecraft = Minecraft.getInstance();
        FeatureFlagSet features = (FeatureFlagSet) Optional.of(minecraft).map(m -> m.player).map(p -> p.connection).map(ClientPacketListener::m_247016_).orElse(FeatureFlagSet.of());
        boolean hasPermissions = minecraft.options.operatorItemsTab().get() || (Boolean) Optional.of(minecraft).map(m -> m.player).map(Player::m_36337_).orElse(false);
        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("minecraft.level must be set before JEI fetches ingredients");
        } else {
            RegistryAccess registryAccess = level.m_9598_();
            CreativeModeTab.ItemDisplayParameters displayParameters = new CreativeModeTab.ItemDisplayParameters(features, hasPermissions, registryAccess);
            Iterator var9 = CreativeModeTabs.allTabs().iterator();
            while (true) {
                CreativeModeTab itemGroup;
                Collection<ItemStack> creativeTabItemStacks;
                while (true) {
                    while (true) {
                        if (!var9.hasNext()) {
                            return itemList;
                        }
                        itemGroup = (CreativeModeTab) var9.next();
                        if (itemGroup.getType() == CreativeModeTab.Type.CATEGORY) {
                            try {
                                itemGroup.buildContents(displayParameters);
                                break;
                            } catch (LinkageError | RuntimeException var14) {
                                LOGGER.error("Item Group crashed while building contents.Items from this group will be missing from the JEI ingredient list. {}", itemGroup, var14);
                            }
                        }
                    }
                    try {
                        creativeTabItemStacks = itemGroup.getSearchTabDisplayItems();
                        break;
                    } catch (LinkageError | RuntimeException var15) {
                        LOGGER.error("Item Group crashed while getting display items.Some items from this group will be missing from the JEI ingredient list. {}", itemGroup, var15);
                    }
                }
                for (ItemStack itemStack : creativeTabItemStacks) {
                    if (itemStack.isEmpty()) {
                        LOGGER.error("Found an empty itemStack from creative tab: {}", itemGroup);
                    } else {
                        addItemStack(stackHelper, itemStack, itemList, itemNameSet);
                    }
                }
            }
        }
    }

    private static void addItemStack(StackHelper stackHelper, ItemStack stack, List<ItemStack> itemList, Set<String> itemNameSet) {
        if (stack.getItem() != Items.PLAYER_HEAD) {
            String itemKey;
            try {
                itemKey = stackHelper.getUniqueIdentifierForStack(stack, UidContext.Ingredient);
            } catch (LinkageError | RuntimeException var7) {
                String stackInfo = ErrorUtil.getItemStackInfo(stack);
                LOGGER.error("Couldn't get unique name for itemStack {}", stackInfo, var7);
                return;
            }
            if (!itemNameSet.contains(itemKey)) {
                itemNameSet.add(itemKey);
                itemList.add(stack);
            }
        }
    }
}