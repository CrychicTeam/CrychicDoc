package icyllis.modernui.mc;

import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.Matrix4;
import icyllis.arc3d.core.Rect2i;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.CustomDrawable;
import icyllis.modernui.graphics.RectF;
import icyllis.modernui.view.View;
import javax.annotation.Nonnull;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerMenuView extends View implements CustomDrawable {

    private AbstractContainerMenu mContainerMenu;

    private final int mItemSize = this.dp(32.0F);

    public ContainerMenuView(Context context) {
        super(context);
    }

    public void setContainerMenu(AbstractContainerMenu containerMenu) {
        this.mContainerMenu = containerMenu;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(@Nonnull Canvas canvas) {
        AbstractContainerMenu menu = this.mContainerMenu;
        if (menu != null) {
            for (int i = 0; i < menu.slots.size(); i++) {
                Slot slot = menu.slots.get(i);
                if (slot.isActive()) {
                    this.drawSlot(canvas, slot);
                }
            }
        }
    }

    protected void drawSlot(@Nonnull Canvas canvas, @Nonnull Slot slot) {
        ItemStack item = slot.getItem();
        if (!item.isEmpty()) {
            int x = this.dp((float) (slot.x * 2));
            int y = this.dp((float) (slot.y * 2));
            ContainerDrawHelper.drawItem(canvas, item, (float) x, (float) y, 0.0F, (float) this.mItemSize, x + y * this.getWidth());
        }
    }

    @Override
    public CustomDrawable.DrawHandler snapDrawHandler(int backendApi, Matrix4 viewMatrix, Rect2i clipBounds, ImageInfo targetInfo) {
        return null;
    }

    @Override
    public RectF getBounds() {
        return null;
    }
}