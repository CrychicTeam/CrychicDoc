package noppes.npcs.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumGuiType;

public class ItemNpcScripter extends Item {

    public ItemNpcScripter() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            CustomNpcs.proxy.openGui(player, EnumGuiType.ScriptPlayers);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
        }
    }
}