package dev.xkmc.modulargolems.content.item.wand;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.compat.curio.CurioCompatRegistry;
import dev.xkmc.modulargolems.content.client.outline.BlockOutliner;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.mode.GolemMode;
import dev.xkmc.modulargolems.content.entity.mode.GolemModes;
import dev.xkmc.modulargolems.content.item.card.ConfigCard;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsMenuPvd;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class CommandWandItem extends BaseWandItem implements GolemInteractItem, IGlowingTarget {

    private static final int RANGE = 64;

    public CommandWandItem(Item.Properties properties, @Nullable ItemEntry<? extends BaseWandItem> base) {
        super(properties, MGLangData.WAND_COMMAND_RIGHT, MGLangData.WAND_COMMAND_SHIFT, base);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide() && selected && entity instanceof Player player) {
            RayTraceUtil.clientUpdateTarget(player, 64.0);
            if (ModList.get().isLoaded("create") && RayTraceUtil.serverGetTarget(player) instanceof AbstractGolemEntity<?, ?> golem && golem.getMode() == GolemModes.ROUTE) {
                BlockOutliner.drawOutlines(player, golem.getPatrolList());
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide()) {
            LivingEntity target = RayTraceUtil.serverGetTarget(player);
            if (target != null) {
                this.interactLivingEntity(stack, player, target, hand);
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public int getDistance(ItemStack itemStack) {
        return 64;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (target instanceof AbstractGolemEntity<?, ?> golem) {
            return command(target.m_9236_(), user, (AbstractGolemEntity<?, ?>) Wrappers.cast(golem)) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        } else {
            if (user instanceof ServerPlayer sp) {
                if (target instanceof OwnableEntity ownable && ownable.getOwner() == user) {
                    CurioCompatRegistry.tryOpen(sp, target);
                    return InteractionResult.SUCCESS;
                }
                this.hurtEnemy(stack, target, user);
            }
            return InteractionResult.SUCCESS;
        }
    }

    private static boolean command(Level level, Player user, AbstractGolemEntity<?, ?> golem) {
        if (!ConfigCard.getFilter(user).test(golem)) {
            return false;
        } else if (!golem.canModify(user)) {
            return false;
        } else if (level.isClientSide()) {
            return true;
        } else if (user.m_6144_()) {
            new EquipmentsMenuPvd(golem).open((ServerPlayer) user);
            return true;
        } else {
            GolemMode mode = GolemModes.nextMode(golem.getMode());
            golem.setMode(mode.getID(), mode.hasPos() ? golem.m_20183_() : BlockPos.ZERO);
            return true;
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            List list = target.m_9236_().getEntities(EntityTypeTest.forClass(AbstractGolemEntity.class), attacker.m_20191_().inflate(32.0), ex -> true);
            int size = 0;
            for (AbstractGolemEntity e : list) {
                if (!ConfigCard.getFilter(player).test(e)) {
                    return false;
                }
                if (!e.canModify(player)) {
                    return false;
                }
                if (e.getOwner() == attacker) {
                    size++;
                    e.resetTarget(target);
                }
            }
            if (attacker instanceof ServerPlayer pl) {
                pl.sendSystemMessage(MGLangData.CALL_ATTACK.get(size, target.m_5446_()), true);
            }
            return false;
        } else {
            return false;
        }
    }
}