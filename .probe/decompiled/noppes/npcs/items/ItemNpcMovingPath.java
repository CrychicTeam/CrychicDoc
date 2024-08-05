package noppes.npcs.items;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;

public class ItemNpcMovingPath extends Item {

    public ItemNpcMovingPath() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (!level.isClientSide && CustomNpcsPermissions.hasPermission((ServerPlayer) player, CustomNpcsPermissions.TOOL_PATHER)) {
            EntityNPCInterface npc = this.getNpc(itemstack, level);
            if (npc != null) {
                NoppesUtilServer.sendOpenGui(player, EnumGuiType.MovingPath, npc);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide && CustomNpcsPermissions.hasPermission((ServerPlayer) context.getPlayer(), CustomNpcsPermissions.TOOL_PATHER)) {
            ItemStack stack = context.getItemInHand();
            EntityNPCInterface npc = this.getNpc(stack, context.getLevel());
            if (npc == null) {
                return InteractionResult.PASS;
            } else {
                List<int[]> list = npc.ais.getMovingPath();
                int[] pos = (int[]) list.get(list.size() - 1);
                int x = context.getClickedPos().m_123341_();
                int y = context.getClickedPos().m_123342_();
                int z = context.getClickedPos().m_123343_();
                list.add(new int[] { x, y, z });
                double d3 = (double) (x - pos[0]);
                double d4 = (double) (y - pos[1]);
                double d5 = (double) (z - pos[2]);
                double distance = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                context.getPlayer().m_213846_(Component.translatable("message.pather.added", x, y, z, npc.getName()));
                if (distance > (double) CustomNpcs.NpcNavRange) {
                    ((ServerPlayer) context.getPlayer()).sendSystemMessage(Component.translatable("message.pather.farwarning", CustomNpcs.NpcNavRange));
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.FAIL;
        }
    }

    private EntityNPCInterface getNpc(ItemStack item, Level level) {
        if (!level.isClientSide && item.getTag() != null) {
            Entity entity = level.getEntity(item.getTag().getInt("NPCID"));
            return entity != null && entity instanceof EntityNPCInterface ? (EntityNPCInterface) entity : null;
        } else {
            return null;
        }
    }
}