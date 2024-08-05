package noppes.npcs.items;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;

public class ItemSoulstoneFilled extends Item {

    public ItemSoulstoneFilled() {
        super(new Item.Properties().stacksTo(1));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag) {
        CompoundTag compound = stack.getTag();
        if (compound != null && compound.contains("Entity", 10)) {
            Component name = Component.translatable(compound.getString("Name"));
            if (compound.contains("DisplayName")) {
                name = Component.translatable(compound.getString("DisplayName")).append(" (").append(name).append(")");
            }
            list.add(Component.literal(ChatFormatting.BLUE + "").append(name));
            if (stack.getTag().contains("ExtraText")) {
                MutableComponent text = Component.literal("");
                String[] split = compound.getString("ExtraText").split(",");
                for (String s : split) {
                    text.append(Component.translatable(s));
                }
                list.add(text);
            }
        } else {
            list.add(Component.literal(ChatFormatting.RED + "Error"));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack stack = context.getItemInHand();
            if (Spawn(context.getPlayer(), stack, context.getLevel(), context.getClickedPos()) == null) {
                return InteractionResult.FAIL;
            } else {
                if (!context.getPlayer().getAbilities().instabuild) {
                    stack.split(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    public static Entity Spawn(Player player, ItemStack stack, Level level, BlockPos pos) {
        if (level.isClientSide) {
            return null;
        } else if (stack.getTag() != null && stack.getTag().contains("Entity", 10)) {
            CompoundTag compound = stack.getTag().getCompound("Entity");
            Entity entity = (Entity) EntityType.create(compound, level).orElse(null);
            if (entity == null) {
                return null;
            } else {
                entity.setPos((double) pos.m_123341_() + 0.5, (double) ((float) (pos.m_123342_() + 1) + 0.2F), (double) pos.m_123343_() + 0.5);
                if (entity instanceof EntityNPCInterface npc) {
                    npc.ais.setStartPos(pos);
                    npc.m_21153_(npc.m_21233_());
                    npc.m_6034_((double) ((float) pos.m_123341_() + 0.5F), npc.getStartYPos(), (double) ((float) pos.m_123343_() + 0.5F));
                    if (npc.role.getType() == 6 && player != null) {
                        PlayerData data = PlayerData.get(player);
                        if (data.hasCompanion()) {
                            return null;
                        }
                        ((RoleCompanion) npc.role).setOwner(player);
                        data.setCompanion(npc);
                    }
                    if (npc.role.getType() == 2 && player != null) {
                        ((RoleFollower) npc.role).setOwner(player);
                    }
                }
                if (!level.m_7967_(entity)) {
                    player.m_213846_(Component.translatable("error.failedToSpawn"));
                    return null;
                } else {
                    return entity;
                }
            }
        } else {
            return null;
        }
    }
}