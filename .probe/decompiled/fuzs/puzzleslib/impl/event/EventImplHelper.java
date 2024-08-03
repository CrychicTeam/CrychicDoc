package fuzs.puzzleslib.impl.event;

import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.event.v1.data.DefaultedDouble;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingEvents;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class EventImplHelper {

    private EventImplHelper() {
    }

    public static void onLivingJump(LivingEvents.Jump callback, LivingEntity entity) {
        Vec3 deltaMovement = entity.m_20184_();
        DefaultedDouble jumpPower = DefaultedDouble.fromValue(deltaMovement.y);
        OptionalDouble newJumpPower;
        if (callback.onLivingJump(entity, jumpPower).isInterrupt()) {
            newJumpPower = OptionalDouble.of(0.0);
        } else {
            newJumpPower = jumpPower.getAsOptionalDouble();
        }
        if (newJumpPower.isPresent()) {
            entity.m_20334_(deltaMovement.x, newJumpPower.getAsDouble(), deltaMovement.z);
        }
    }

    public static Optional<Player> getGrindstoneUsingPlayer(ItemStack topInput, ItemStack bottomInput) {
        MinecraftServer minecraftServer = CommonAbstractions.INSTANCE.getMinecraftServer();
        Optional<Player> optional = Optional.empty();
        for (ServerPlayer player : minecraftServer.getPlayerList().getPlayers()) {
            if (player.f_36096_ instanceof GrindstoneMenu menu) {
                optional = Optional.of(player);
                if (menu.m_38853_(0).getItem() == topInput && menu.m_38853_(1).getItem() == bottomInput) {
                    break;
                }
            }
        }
        return optional;
    }

    public static Optional<Player> getPlayerFromContainerMenu(AbstractContainerMenu menu) {
        for (Slot slot : menu.slots) {
            if (slot.container instanceof Inventory inventory) {
                return Optional.of(inventory.player);
            }
        }
        MinecraftServer minecraftServer = CommonAbstractions.INSTANCE.getMinecraftServer();
        for (ServerPlayer player : minecraftServer.getPlayerList().getPlayers()) {
            if (player.f_36096_ == menu) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
}