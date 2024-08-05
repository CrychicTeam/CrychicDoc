package io.github.lightman314.lightmanscurrency.client.gui.widget.button.inventory;

import io.github.lightman314.lightmanscurrency.client.gui.easy.rendering.Sprite;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraftforge.common.util.NonNullSupplier;

public abstract class InventoryButton extends PlainButton {

    protected final boolean isCreativeScreen;

    protected final AbstractContainerScreen<?> inventoryScreen;

    private ScreenPosition getScreenCorner() {
        return ScreenPosition.getScreenCorner(this.inventoryScreen);
    }

    protected InventoryButton(@Nonnull AbstractContainerScreen<?> inventoryScreen, @Nonnull Consumer<EasyButton> pressable, @Nonnull Sprite sprite) {
        this(inventoryScreen, pressable, () -> sprite);
    }

    protected InventoryButton(@Nonnull AbstractContainerScreen<?> inventoryScreen, @Nonnull Consumer<EasyButton> pressable, @Nonnull NonNullSupplier<Sprite> sprite) {
        super(0, 0, pressable, sprite);
        this.inventoryScreen = inventoryScreen;
        this.isCreativeScreen = this.inventoryScreen instanceof CreativeModeInventoryScreen;
    }

    protected abstract ScreenPosition getPositionOffset(boolean var1);

    protected boolean canShow() {
        return true;
    }

    @Override
    protected void renderTick() {
        if (this.inventoryScreen instanceof CreativeModeInventoryScreen cs) {
            this.f_93624_ = cs.isInventoryOpen() && this.canShow();
        } else {
            this.f_93624_ = this.canShow();
        }
        if (this.f_93624_) {
            this.setPosition(this.getScreenCorner().offset(this.getPositionOffset(this.isCreativeScreen)));
        }
    }
}