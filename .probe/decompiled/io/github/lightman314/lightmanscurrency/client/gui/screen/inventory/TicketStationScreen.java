package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.easy.rendering.Sprite;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.crafting.TicketStationRecipe;
import io.github.lightman314.lightmanscurrency.common.menus.TicketStationMenu;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.util.NonNullSupplier;

public class TicketStationScreen extends EasyMenuScreen<TicketStationMenu> implements IScrollable {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/ticket_machine.png");

    public static final Sprite SPRITE_ARROW = Sprite.SimpleSprite(GUI_TEXTURE, 176, 0, 24, 16);

    private static final ScreenArea SELECTION_AREA = ScreenArea.of(153, 7, 16, 16);

    private TicketStationRecipe selectedRecipe = null;

    public List<TicketStationRecipe> getMatchingRecipes() {
        return ((TicketStationMenu) this.f_97732_).getAllRecipes().stream().filter(r -> r.matches(((TicketStationMenu) this.f_97732_).blockEntity.getStorage(), ((TicketStationMenu) this.f_97732_).blockEntity.m_58904_())).toList();
    }

    public TicketStationScreen(TicketStationMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.resize(176, 138);
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.renderNormalBackground(GUI_TEXTURE, this);
        gui.drawString(this.f_96539_, 8, 6, 4210752);
        gui.drawString(this.f_169604_, 8, this.getYSize() - 94, 4210752);
        if (this.selectedRecipe != null) {
            gui.renderItem(this.selectedRecipe.peekAtResult(((TicketStationMenu) this.f_97732_).blockEntity.getStorage()), SELECTION_AREA.pos);
        }
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        if (this.selectedRecipe != null && SELECTION_AREA.offsetPosition(this.getCorner()).isMouseInArea(gui.mousePos)) {
            List<Component> tooltip = new ArrayList();
            tooltip.add(LCText.TOOLTIP_TICKET_STATION_RECIPE_INFO.get(this.selectedRecipe.peekAtResult(((TicketStationMenu) this.f_97732_).blockEntity.getStorage()).getHoverName()));
            if (this.getMatchingRecipes().size() > 1) {
                tooltip.add(LCText.TOOLTIP_TICKET_STATION_SELECT_RECIPE.get());
            }
            gui.renderComponentTooltip(tooltip);
        }
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        this.addChild(new PlainButton(screenArea.x + 79, screenArea.y + 21, this::craftTicket, SPRITE_ARROW).withAddons(EasyAddonHelper.visibleCheck((NonNullSupplier<Boolean>) (() -> ((TicketStationMenu) this.f_97732_).validInputs() && this.selectedRecipe != null && ((TicketStationMenu) this.f_97732_).roomForOutput(this.selectedRecipe))), EasyAddonHelper.tooltip(this::getArrowTooltip)));
        this.addChild(new ScrollListener(SELECTION_AREA.offsetPosition(screenArea.pos), this));
        this.validateSelectedRecipe();
    }

    @Override
    protected void screenTick() {
        this.validateSelectedRecipe();
    }

    private Component getArrowTooltip() {
        return this.selectedRecipe != null ? LCText.TOOLTIP_TICKET_STATION_CRAFT.get(this.selectedRecipe.peekAtResult(((TicketStationMenu) this.f_97732_).blockEntity.getStorage()).getHoverName()) : EasyText.empty();
    }

    private void validateSelectedRecipe() {
        List<TicketStationRecipe> matchingRecipes = this.getMatchingRecipes();
        if (this.selectedRecipe != null && !matchingRecipes.contains(this.selectedRecipe)) {
            if (!matchingRecipes.isEmpty()) {
                this.selectedRecipe = (TicketStationRecipe) matchingRecipes.get(0);
            } else {
                this.selectedRecipe = null;
            }
        } else {
            if (this.selectedRecipe == null && !matchingRecipes.isEmpty()) {
                this.selectedRecipe = (TicketStationRecipe) matchingRecipes.get(0);
            }
        }
    }

    private void craftTicket(EasyButton button) {
        this.validateSelectedRecipe();
        if (this.selectedRecipe != null) {
            ((TicketStationMenu) this.f_97732_).SendCraftTicketsMessage(Screen.hasShiftDown(), this.selectedRecipe.m_6423_());
        }
    }

    @Override
    public int currentScroll() {
        this.validateSelectedRecipe();
        return this.selectedRecipe == null ? 0 : this.getMatchingRecipes().indexOf(this.selectedRecipe);
    }

    @Override
    public void setScroll(int newScroll) {
        List<TicketStationRecipe> matchingRecipes = this.getMatchingRecipes();
        if (matchingRecipes.isEmpty()) {
            this.selectedRecipe = null;
        } else if (newScroll >= 0 && newScroll < matchingRecipes.size()) {
            this.selectedRecipe = (TicketStationRecipe) matchingRecipes.get(newScroll);
        } else {
            this.setScroll(0);
        }
    }

    @Override
    public int getMaxScroll() {
        return this.getMatchingRecipes().size() - 1;
    }
}