package dev.xkmc.l2hostility.compat.curios;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2tabs.compat.BaseCuriosListMenu;
import dev.xkmc.l2tabs.compat.CuriosEventHandler;
import dev.xkmc.l2tabs.compat.CuriosWrapper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

public class EntityCuriosListMenu extends BaseCuriosListMenu<EntityCuriosListMenu> {

    @Nullable
    public static EntityCuriosListMenu fromNetwork(MenuType<EntityCuriosListMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        int id = buf.readInt();
        int page = buf.readInt();
        ClientLevel level = Proxy.getClientWorld();
        assert level != null;
        if (level.getEntity(id) instanceof LivingEntity le && CuriosApi.getCuriosInventory(le).resolve().isPresent()) {
            return new EntityCuriosListMenu(type, wid, plInv, new CuriosWrapper(le, page));
        }
        return null;
    }

    protected EntityCuriosListMenu(MenuType<?> type, int wid, Inventory plInv, CuriosWrapper curios) {
        super(type, wid, plInv, curios);
    }

    public void switchPage(ServerPlayer player, int i) {
        if (CuriosApi.getCuriosInventory(this.curios.entity).resolve().isPresent()) {
            EntityCuriosMenuPvd pvd = new EntityCuriosMenuPvd(this.curios.entity, i);
            CuriosEventHandler.openMenuWrapped(player, () -> NetworkHooks.openScreen(player, pvd, pvd::writeBuffer));
        }
    }
}