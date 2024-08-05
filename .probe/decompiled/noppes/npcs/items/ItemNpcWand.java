package noppes.npcs.items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import noppes.npcs.CustomEntities;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.util.CustomNPCsScheduler;

public class ItemNpcWand extends Item {

    public ItemNpcWand() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (!level.isClientSide) {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
        } else {
            CustomNpcs.proxy.openGui(player, EnumGuiType.NpcRemote);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
        }
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (CustomNpcs.OpsOnly && !context.getPlayer().m_20194_().getPlayerList().isOp(context.getPlayer().getGameProfile())) {
                context.getPlayer().m_213846_(Component.translatable("availability.permission"));
            } else if (CustomNpcsPermissions.hasPermission((ServerPlayer) context.getPlayer(), CustomNpcsPermissions.NPC_CREATE)) {
                EntityCustomNpc npc = new EntityCustomNpc(CustomEntities.entityCustomNpc, context.getLevel());
                npc.ais.setStartPos(context.getClickedPos().above());
                npc.m_7678_((double) ((float) context.getClickedPos().m_123341_() + 0.5F), npc.getStartYPos(), (double) ((float) context.getClickedPos().m_123343_() + 0.5F), context.getPlayer().m_146908_(), context.getPlayer().m_146909_());
                context.getLevel().m_7967_(npc);
                npc.m_21153_(npc.m_21233_());
                CustomNPCsScheduler.runTack(() -> NoppesUtilServer.sendOpenGui(context.getPlayer(), EnumGuiType.MainMenuDisplay, npc), 100);
            } else {
                ((ServerPlayer) context.getPlayer()).sendSystemMessage(Component.translatable("availability.permission"));
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity playerIn) {
        return stack;
    }
}