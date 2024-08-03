package io.github.lightman314.lightmanscurrency.client.gui.widget;

import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.misc.player.OwnerData;
import io.github.lightman314.lightmanscurrency.api.ownership.Owner;
import io.github.lightman314.lightmanscurrency.api.ownership.listing.PotentialOwner;
import io.github.lightman314.lightmanscurrency.api.ownership.listing.PotentialOwnerList;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.OwnerSelectButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.resources.ResourceLocation;

public class OwnerSelectionWidget extends EasyWidgetWithChildren implements IScrollable {

    public static final ResourceLocation SEARCH_BOX_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/item_edit.png");

    private final Supplier<OwnerData> currentOwner;

    private final Consumer<Owner> setOwner;

    private final int rows;

    public final PotentialOwnerList list;

    private EditBox searchBox;

    private int scroll = 0;

    public OwnerSelectionWidget(int x, int y, int width, int rows, @Nonnull Supplier<OwnerData> currentOwner, @Nonnull Consumer<Owner> setOwner, @Nullable OwnerSelectionWidget oldWidget) {
        this(ScreenPosition.of(x, y), width, rows, currentOwner, setOwner, oldWidget);
    }

    public OwnerSelectionWidget(@Nonnull ScreenPosition pos, int width, int rows, @Nonnull Supplier<OwnerData> currentOwner, @Nonnull Consumer<Owner> setOwner, @Nullable OwnerSelectionWidget oldWidget) {
        super(pos, width, rows * 20 + 12);
        this.currentOwner = currentOwner;
        this.setOwner = setOwner;
        this.rows = rows;
        if (oldWidget != null) {
            this.scroll = oldWidget.scroll;
            this.searchBox = oldWidget.searchBox;
            this.list = oldWidget.list;
        } else {
            this.list = new PotentialOwnerList(Minecraft.getInstance().player, this.currentOwner);
        }
    }

    public OwnerSelectionWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    protected void renderTick() {
        this.list.tick();
    }

    @Override
    protected void renderWidget(@Nonnull EasyGuiGraphics gui) {
        gui.blit(SEARCH_BOX_TEXTURE, this.f_93618_ - 90, 0, 18, 0, 90, 12);
    }

    @Override
    public void addChildren() {
        this.searchBox = this.addChild(new EditBox(Minecraft.getInstance().font, this.m_252754_() + this.f_93618_ - 88, this.m_252907_() + 2, 79, 9, this.searchBox, EasyText.empty()));
        this.searchBox.setBordered(false);
        this.searchBox.setResponder(this::modifySearch);
        this.addChild(new ScrollBarWidget(this.getPosition().offset(this.m_5711_(), 12), this.m_93694_() - 12, this).withAddons(EasyAddonHelper.visibleCheck(this::isVisible)));
        this.addChild(new ScrollListener(this.getArea(), this));
        for (int i = 0; i < this.rows; i++) {
            int index = i;
            this.addChild(new OwnerSelectButton(this.getPosition().offset(0, i * 20 + 12), this.f_93618_, () -> this.setOwner(index), this.currentOwner, () -> this.getOwner(index), this::isVisible));
        }
    }

    private void modifySearch(@Nonnull String newSearch) {
        this.list.updateCache(newSearch);
        this.validateScroll();
    }

    @Nullable
    private PotentialOwner getOwner(int buttonIndex) {
        List<PotentialOwner> list = this.list.getOwners();
        int index = buttonIndex + this.scroll;
        return index >= 0 && index < list.size() ? (PotentialOwner) list.get(index) : null;
    }

    private void setOwner(int buttonIndex) {
        PotentialOwner owner = this.getOwner(buttonIndex);
        if (owner != null) {
            this.setOwner.accept(owner.asOwner());
        }
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.scroll = newScroll;
    }

    @Override
    public int getMaxScroll() {
        return IScrollable.calculateMaxScroll(this.rows, this.list.getOwners().size());
    }
}