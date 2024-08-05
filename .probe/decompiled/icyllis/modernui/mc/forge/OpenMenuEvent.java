package icyllis.modernui.mc.forge;

import icyllis.modernui.fragment.Fragment;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus.Internal;

@Cancelable
@Internal
public final class OpenMenuEvent extends Event implements IModBusEvent {

    @Nonnull
    private final AbstractContainerMenu mMenu;

    private Fragment mFragment;

    OpenMenuEvent(@Nonnull AbstractContainerMenu menu) {
        this.mMenu = menu;
    }

    @Nonnull
    public AbstractContainerMenu getMenu() {
        return this.mMenu;
    }

    public void set(@Nonnull Fragment fragment) {
        this.mFragment = fragment;
        if (!this.isCanceled()) {
            this.setCanceled(true);
        }
    }

    @Nullable
    Fragment getFragment() {
        return this.mFragment;
    }

    static {
        assert FMLEnvironment.dist.isClient();
    }
}