package io.github.lightman314.lightmanscurrency.api.money.input;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.types.CurrencyType;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.rendering.Sprite;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.dropdown.DropdownWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class MoneyValueWidget extends EasyWidgetWithChildren {

    public static final int HEIGHT = 69;

    public static final int WIDTH = 176;

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/coinvalueinput.png");

    public static final Sprite SPRITE_FREE_TOGGLE = Sprite.SimpleSprite(GUI_TEXTURE, 40, 69, 10, 10);

    public static final Sprite SPRITE_UP_ARROW = Sprite.SimpleSprite(GUI_TEXTURE, 0, 69, 20, 10);

    public static final Sprite SPRITE_DOWN_ARROW = Sprite.SimpleSprite(GUI_TEXTURE, 20, 69, 20, 10);

    public static final Sprite SPRITE_LEFT_ARROW = Sprite.SimpleSprite(GUI_TEXTURE, 50, 69, 10, 20);

    public static final Sprite SPRITE_RIGHT_ARROW = Sprite.SimpleSprite(GUI_TEXTURE, 60, 69, 10, 20);

    public static final Consumer<MoneyValue> EMPTY_CONSUMER = v -> {
    };

    private static String lastSelectedHandler = "lightmanscurrency:coins!main";

    public boolean drawBG = true;

    public boolean allowFreeInput = true;

    private boolean locked = false;

    private final Font font;

    private final Map<String, MoneyInputHandler> availableHandlers;

    private final List<String> handlerKeys;

    private MoneyInputHandler currentHandler;

    private MoneyValue currentValue;

    private final Consumer<MoneyValue> changeHandler;

    private Consumer<MoneyValueWidget> handlerChangeConsumer;

    private final MoneyValueWidget oldWidget;

    private DropdownWidget dropdown;

    private EasyButton freeToggle;

    public boolean isLocked() {
        return this.locked;
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    public final Font getFont() {
        return this.font;
    }

    @Nonnull
    public String getCurrentHandlerType() {
        return this.currentHandler == null ? "" : this.currentHandler.getUniqueName();
    }

    @Nullable
    public MoneyInputHandler getCurrentHandler() {
        return this.currentHandler;
    }

    @Nonnull
    public final MoneyValue getCurrentValue() {
        return this.currentValue;
    }

    public void setHandlerChangeListener(@Nonnull Consumer<MoneyValueWidget> consumer) {
        this.handlerChangeConsumer = consumer;
    }

    public MoneyValueWidget(int x, int y, @Nullable MoneyValueWidget oldWidget, @Nonnull MoneyValue startingValue, @Nonnull Consumer<MoneyValue> changeHandler) {
        this(ScreenPosition.of(x, y), oldWidget, startingValue, changeHandler);
    }

    public MoneyValueWidget(@Nonnull ScreenPosition pos, @Nullable MoneyValueWidget oldWidget, @Nonnull MoneyValue startingValue, @Nonnull Consumer<MoneyValue> changeHandler) {
        super(pos, 176, 69);
        this.font = Minecraft.getInstance().font;
        this.handlerKeys = new ArrayList();
        this.currentHandler = null;
        this.handlerChangeConsumer = w -> {
        };
        this.dropdown = null;
        this.freeToggle = null;
        this.changeHandler = changeHandler;
        this.currentValue = oldWidget != null ? oldWidget.currentValue : startingValue;
        this.availableHandlers = this.setupHandlers();
        this.oldWidget = oldWidget;
    }

    private Map<String, MoneyInputHandler> setupHandlers() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Map<String, MoneyInputHandler> handlers = new HashMap();
        for (CurrencyType type : MoneyAPI.API.AllCurrencyTypes()) {
            for (Object h : type.getInputHandlers(player)) {
                if (h instanceof MoneyInputHandler handler) {
                    handlers.put(handler.getUniqueName(), handler);
                    handler.setup(this, x$0 -> this.addChild(x$0), x$0 -> this.removeChild(x$0), this::onHandlerChangeValue);
                    this.handlerKeys.add(handler.getUniqueName());
                }
            }
        }
        if (handlers.isEmpty()) {
            throw new RuntimeException("No valid MoneyInputHandlers are included in the registered CurrencyTypes!");
        } else {
            return handlers;
        }
    }

    private MoneyInputHandler findDefaultHandler() {
        if (this.oldWidget != null && this.oldWidget.currentHandler != null && this.availableHandlers.containsKey(this.oldWidget.currentHandler.getUniqueName())) {
            return (MoneyInputHandler) this.availableHandlers.get(this.oldWidget.currentHandler.getUniqueName());
        } else {
            MoneyValue value = this.currentValue;
            if (!value.isEmpty() && !value.isFree()) {
                String id = value.getUniqueName();
                if (this.availableHandlers.containsKey(id)) {
                    return (MoneyInputHandler) this.availableHandlers.get(id);
                }
            } else if (this.availableHandlers.containsKey(lastSelectedHandler)) {
                return (MoneyInputHandler) this.availableHandlers.get(lastSelectedHandler);
            }
            return (MoneyInputHandler) this.availableHandlers.values().stream().toList().get(0);
        }
    }

    public MoneyValueWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void addChildren() {
        this.setHandler(this.findDefaultHandler());
        this.freeToggle = this.addChild(new PlainButton(this.m_252754_() + this.f_93618_ - 14, this.m_252907_() + 4, this::toggleFree, SPRITE_FREE_TOGGLE));
        this.dropdown = this.addChild(new DropdownWidget(this.m_252754_() + 10, this.m_252907_() + 4, 64, this.handlerKeys.indexOf(this.currentHandler.getUniqueName()), this::selectHandler, this.handlerNames()));
    }

    private void checkHandler() {
        if (!this.currentValue.isFree() && !this.currentValue.isEmpty() && !this.currentValue.getUniqueName().equals(this.currentHandler.getUniqueName())) {
            if (this.availableHandlers.containsKey(this.currentValue.getUniqueName())) {
                this.setHandler((MoneyInputHandler) this.availableHandlers.get(this.currentValue.getUniqueName()));
            }
        }
    }

    private void setHandler(@Nonnull MoneyInputHandler handler) {
        if (this.currentHandler != handler) {
            if (this.currentHandler != null) {
                this.removeChild(this.currentHandler);
                this.currentHandler.close();
            }
            this.currentHandler = handler;
            this.addChild(this.currentHandler);
            this.currentHandler.initialize(this.getArea());
            lastSelectedHandler = this.currentHandler.getUniqueName();
            this.handlerChangeConsumer.accept(this);
        }
    }

    private List<Component> handlerNames() {
        List<Component> names = new ArrayList();
        for (String key : this.handlerKeys) {
            names.add(((MoneyInputHandler) this.availableHandlers.get(key)).inputName());
        }
        return names;
    }

    private void selectHandler(int handlerIndex) {
        if (handlerIndex >= 0 && handlerIndex < this.handlerKeys.size()) {
            MoneyInputHandler handler = (MoneyInputHandler) this.availableHandlers.get(this.handlerKeys.get(handlerIndex));
            if (handler != null) {
                this.setHandler(handler);
            }
        }
    }

    @Override
    protected void renderTick() {
        this.freeToggle.f_93624_ = this.allowFreeInput && this.isVisible();
        this.dropdown.f_93624_ = this.isVisible() && this.availableHandlers.size() > 1;
        if (this.currentHandler != null) {
            this.currentHandler.renderTick();
        }
    }

    @Override
    protected void renderWidget(@Nonnull EasyGuiGraphics gui) {
        if (this.drawBG) {
            gui.blit(GUI_TEXTURE, 0, 0, 0, 0, 176, 69);
        }
        if (this.currentHandler != null) {
            this.currentHandler.renderBG(gui);
        }
        int priceWidth = gui.font.width(this.currentValue.getString());
        int freeButtonOffset = this.allowFreeInput ? 15 : 5;
        gui.drawString(this.currentValue.getText(), this.f_93618_ - freeButtonOffset - priceWidth, 5, 4210752);
    }

    private void toggleFree(EasyButton button) {
        if (this.allowFreeInput && this.currentValue.isFree()) {
            this.onHandlerChangeValue(MoneyValue.empty());
        } else if (this.allowFreeInput) {
            this.onHandlerChangeValue(MoneyValue.free());
        }
    }

    private void onHandlerChangeValue(MoneyValue newValue) {
        if (newValue == null) {
            newValue = MoneyValue.empty();
        }
        if (newValue.isFree() && !this.allowFreeInput) {
            newValue = MoneyValue.empty();
        }
        this.currentValue = newValue;
        this.changeHandler.accept(newValue);
    }

    public void changeValue(@Nonnull MoneyValue newValue) {
        if (newValue.isFree() && !this.allowFreeInput) {
            newValue = MoneyValue.empty();
        }
        this.currentValue = newValue;
        this.checkHandler();
        if (this.currentHandler != null) {
            this.currentHandler.onValueChanged(newValue);
        }
    }

    @Override
    public boolean hideFromMouse() {
        return true;
    }
}