package noppes.npcs.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.packets.server.SPacketGuiOpen;

public class ItemNpcCloner extends Item {

    public ItemNpcCloner() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            SPacketGuiOpen.sendOpenGui(context.getPlayer(), EnumGuiType.MobSpawner, null, context.getClickedPos());
        }
        return InteractionResult.SUCCESS;
    }
}