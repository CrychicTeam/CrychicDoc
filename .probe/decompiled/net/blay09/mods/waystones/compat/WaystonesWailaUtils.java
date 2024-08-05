package net.blay09.mods.waystones.compat;

import java.util.function.Consumer;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.WarpPlateBlock;
import net.blay09.mods.waystones.block.entity.WarpPlateBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WaystonesWailaUtils {

    public static final ResourceLocation WAYSTONE_UID = new ResourceLocation("waystones", "waystone");

    public static void appendTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer) {
        if (blockEntity instanceof WarpPlateBlockEntity warpPlate) {
            IWaystone waystone = warpPlate.getWaystone();
            tooltipConsumer.accept(WarpPlateBlock.getGalacticName(waystone));
        } else if (blockEntity instanceof WaystoneBlockEntityBase waystoneBlockEntity) {
            IWaystone waystone = waystoneBlockEntity.getWaystone();
            boolean isActivated = !waystone.getWaystoneType().equals(WaystoneTypes.WAYSTONE) || PlayerWaystoneManager.isWaystoneActivated(player, waystone);
            if (isActivated && waystone.hasName() && waystone.isValid()) {
                tooltipConsumer.accept(Component.literal(waystone.getName()));
            } else {
                tooltipConsumer.accept(Component.translatable("tooltip.waystones.undiscovered"));
            }
        }
    }

    private WaystonesWailaUtils() {
    }
}