package snownee.jade.api;

import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.ui.IElement;

public interface Accessor<T extends HitResult> {

    Level getLevel();

    Player getPlayer();

    @NotNull
    CompoundTag getServerData();

    T getHitResult();

    boolean isServerConnected();

    ItemStack getPickedResult();

    boolean showDetails();

    Object getTarget();

    Class<? extends Accessor<?>> getAccessorType();

    void toNetwork(FriendlyByteBuf var1);

    boolean verifyData(CompoundTag var1);

    public interface ClientHandler<T extends Accessor<?>> {

        boolean shouldDisplay(T var1);

        boolean shouldRequestData(T var1);

        void requestData(T var1);

        IElement getIcon(T var1);

        void gatherComponents(T var1, Function<IJadeProvider, ITooltip> var2);
    }
}