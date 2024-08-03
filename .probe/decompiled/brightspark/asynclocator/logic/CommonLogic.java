package brightspark.asynclocator.logic;

import brightspark.asynclocator.mixins.MapItemAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class CommonLogic {

    private static final String MAP_HOVER_NAME_KEY = "menu.working";

    private static final String KEY_LOCATING = "asynclocator.locating";

    private CommonLogic() {
    }

    public static ItemStack createEmptyMap() {
        ItemStack stack = new ItemStack(Items.FILLED_MAP);
        stack.setHoverName(Component.translatable("menu.working"));
        stack.addTagElement("asynclocator.locating", ByteTag.ONE);
        return stack;
    }

    public static boolean isEmptyPendingMap(ItemStack stack) {
        return stack.is(Items.FILLED_MAP) && stack.hasTag() && stack.getTag().contains("asynclocator.locating");
    }

    public static void updateMap(ItemStack mapStack, ServerLevel level, BlockPos pos, int scale, MapDecoration.Type destinationType) {
        updateMap(mapStack, level, pos, scale, destinationType, (Component) null);
    }

    public static void updateMap(ItemStack mapStack, ServerLevel level, BlockPos pos, int scale, MapDecoration.Type destinationType, String displayName) {
        updateMap(mapStack, level, pos, scale, destinationType, Component.translatable(displayName));
    }

    public static void updateMap(ItemStack mapStack, ServerLevel level, BlockPos pos, int scale, MapDecoration.Type destinationType, Component displayName) {
        MapItemAccess.callCreateAndStoreSavedData(mapStack, level, pos.m_123341_(), pos.m_123343_(), scale, true, true, level.m_46472_());
        MapItem.renderBiomePreviewMap(level, mapStack);
        MapItemSavedData.addTargetDecoration(mapStack, pos, "+", destinationType);
        if (displayName != null) {
            mapStack.setHoverName(displayName);
        }
        mapStack.removeTagKey("asynclocator.locating");
    }

    public static void broadcastChestChanges(ServerLevel level, BlockEntity be) {
        if (be instanceof ChestBlockEntity) {
            level.players().forEach(player -> {
                if (player.f_36096_ instanceof ChestMenu chestMenu && chestMenu.getContainer() == be) {
                    chestMenu.m_38946_();
                }
            });
        }
    }
}