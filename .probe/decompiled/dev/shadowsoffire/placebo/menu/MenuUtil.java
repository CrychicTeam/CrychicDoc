package dev.shadowsoffire.placebo.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.network.NetworkHooks;

public class MenuUtil {

    public static <T extends AbstractContainerMenu> MenuType<T> type(MenuType.MenuSupplier<T> factory) {
        return new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> bufType(IContainerFactory<T> factory) {
        return new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> posType(MenuUtil.PosFactory<T> factory) {
        return new MenuType<>(factory(factory), FeatureFlags.DEFAULT_FLAGS);
    }

    public static <M extends AbstractContainerMenu> InteractionResult openGui(Player player, BlockPos pos, MenuUtil.PosFactory<M> factory) {
        if (player.m_9236_().isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            NetworkHooks.openScreen((ServerPlayer) player, new SimplerMenuProvider<>(player.m_9236_(), pos, factory), pos);
            return InteractionResult.CONSUME;
        }
    }

    public static int split(int value, boolean upper) {
        return upper ? value >> 16 : value & 65535;
    }

    public static int merge(int current, int value, boolean upper) {
        return upper ? current & 65535 | value << 16 : current & -65536 | value & 65535;
    }

    public static <T extends AbstractContainerMenu> IContainerFactory<T> factory(MenuUtil.PosFactory<T> factory) {
        return (id, inv, buf) -> factory.create(id, inv, buf.readBlockPos());
    }

    public interface PosFactory<T> {

        T create(int var1, Inventory var2, BlockPos var3);
    }
}