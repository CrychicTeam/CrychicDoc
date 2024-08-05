package net.blay09.mods.waystones.block.entity;

import java.util.List;
import java.util.stream.Collectors;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.SharestoneBlock;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.ModMenus;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.blay09.mods.waystones.menu.WaystoneSettingsMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class SharestoneBlockEntity extends WaystoneBlockEntityBase {

    public SharestoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.sharestone.get(), pos, state);
    }

    @Override
    protected ResourceLocation getWaystoneType() {
        return WaystoneTypes.getSharestone(((SharestoneBlock) this.m_58900_().m_60734_()).getColor());
    }

    @Override
    public MenuProvider getMenuProvider() {
        return new BalmMenuProvider() {

            @Override
            public Component getDisplayName() {
                return Component.translatable("container.waystones.waystone_selection");
            }

            @Override
            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                return WaystoneSelectionMenu.createSharestoneSelection(playerEntity.m_20194_(), i, SharestoneBlockEntity.this.getWaystone(), SharestoneBlockEntity.this.m_58900_());
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                SharestoneBlock block = (SharestoneBlock) SharestoneBlockEntity.this.m_58900_().m_60734_();
                ResourceLocation waystoneType = WaystoneTypes.getSharestone(block.getColor());
                List<IWaystone> waystones = (List<IWaystone>) WaystoneManager.get(player.server).getWaystonesByType(waystoneType).collect(Collectors.toList());
                buf.writeBlockPos(SharestoneBlockEntity.this.f_58858_);
                buf.writeShort(waystones.size());
                for (IWaystone waystone : waystones) {
                    Waystone.write(buf, waystone);
                }
            }
        };
    }

    public BalmMenuProvider getSettingsMenuProvider() {
        return new BalmMenuProvider() {

            @Override
            public Component getDisplayName() {
                return Component.translatable("container.waystones.waystone_settings");
            }

            @Override
            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                return new WaystoneSettingsMenu(ModMenus.waystoneSettings.get(), SharestoneBlockEntity.this.getWaystone(), i);
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                Waystone.write(buf, SharestoneBlockEntity.this.getWaystone());
            }
        };
    }
}