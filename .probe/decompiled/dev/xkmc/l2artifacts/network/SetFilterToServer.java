package dev.xkmc.l2artifacts.network;

import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.augment.AugmentMenu;
import dev.xkmc.l2artifacts.content.search.common.ArtifactChestMenuPvd;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveMenu;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenu;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenu;
import dev.xkmc.l2artifacts.content.search.shape.ShapeMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenu;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SetFilterToServer extends SerialPacketBase {

    @SerialField
    private int slot;

    @SerialField
    private CompoundTag filter;

    @SerialField
    @Nullable
    private SetFilterToServer.Type type;

    @Deprecated
    public SetFilterToServer() {
    }

    public SetFilterToServer(ArtifactChestToken token, @Nullable SetFilterToServer.Type type) {
        this.slot = token.invSlot;
        this.filter = TagCodec.toTag(new CompoundTag(), token);
        ArtifactChestItem.setFilter(Proxy.getClientPlayer().m_150109_().getItem(this.slot), this.filter);
        this.type = type;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null) {
            ItemStack stack = player.m_150109_().getItem(this.slot);
            if (stack.getItem() instanceof ArtifactChestItem) {
                ArtifactChestItem.setFilter(stack, this.filter);
                if (this.type == null) {
                    player.doCloseContainer();
                } else {
                    if (player.f_36096_ instanceof IFilterMenu) {
                        ItemStack carried = player.f_36096_.getCarried();
                        player.f_36096_.setCarried(ItemStack.EMPTY);
                        new ArtifactChestMenuPvd(this.type.factory, player, this.slot, stack).open();
                        player.f_36096_.setCarried(carried);
                    } else {
                        new ArtifactChestMenuPvd(this.type.factory, player, this.slot, stack).open();
                    }
                }
            }
        }
    }

    public static enum Type {

        FILTER(FilteredMenu::new),
        RECYCLE(RecycleMenu::new),
        UPGRADE(UpgradeMenu::new),
        DISSOLVE(DissolveMenu::new),
        AUGMENT(AugmentMenu::new),
        SHAPE(ShapeMenu::new);

        private final ArtifactChestMenuPvd.Factory factory;

        private Type(ArtifactChestMenuPvd.Factory factory) {
            this.factory = factory;
        }
    }
}