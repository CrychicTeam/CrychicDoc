package com.blamejared.controlling.client;

import com.blamejared.controlling.ControllingConstants;
import com.blamejared.controlling.api.DisplayMode;
import com.blamejared.controlling.api.SortOrder;
import com.blamejared.controlling.mixin.AccessKeyBindsScreen;
import com.blamejared.controlling.mixin.AccessKeyMapping;
import com.blamejared.controlling.platform.Services;
import com.blamejared.searchables.api.autcomplete.AutoCompletingEditBox;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;

public class NewKeyBindsScreen extends KeyBindsScreen {

    private AutoCompletingEditBox<KeyBindsList.Entry> search;

    private DisplayMode displayMode;

    private SortOrder sortOrder = SortOrder.NONE;

    private Button buttonNone;

    private Button buttonConflicting;

    private Button buttonSort;

    private final DisplayableBoolean confirmingReset = new DisplayableBoolean(false, ControllingConstants.COMPONENT_OPTIONS_CONFIRM_RESET, ControllingConstants.COMPONENT_CONTROLS_RESET_ALL);

    private boolean showFree;

    private Supplier<NewKeyBindsList> newKeyList;

    private Supplier<FreeKeysList> freeKeyList;

    private final Button.OnPress PRESS_RESET = btn -> {
        Minecraft minecraft = (Minecraft) Objects.requireNonNull(this.f_96541_);
        if (!this.confirmingReset.toggle()) {
            for (KeyMapping keybinding : minecraft.options.keyMappings) {
                Services.PLATFORM.setToDefault(minecraft.options, keybinding);
            }
            this.getKeyBindsList().resetMappingAndUpdateButtons();
        }
        btn.m_93666_(this.confirmingReset.currentDisplay());
    };

    private final Button.OnPress PRESS_NONE = btn -> {
        if (this.displayMode == DisplayMode.NONE) {
            this.buttonNone.m_93666_(ControllingConstants.COMPONENT_OPTIONS_SHOW_NONE);
            this.displayMode = DisplayMode.ALL;
        } else {
            this.displayMode = DisplayMode.NONE;
            this.buttonNone.m_93666_(ControllingConstants.COMPONENT_OPTIONS_SHOW_ALL);
            this.buttonConflicting.m_93666_(ControllingConstants.COMPONENT_OPTIONS_SHOW_CONFLICTS);
        }
        this.filterKeys();
    };

    private final Button.OnPress PRESS_SORT = btn -> {
        this.sortOrder = this.sortOrder.cycle();
        btn.m_93666_(this.sortOrder.getDisplay());
        this.filterKeys();
    };

    private final Button.OnPress PRESS_CONFLICTING = btn -> {
        if (this.displayMode == DisplayMode.CONFLICTING) {
            this.buttonConflicting.m_93666_(ControllingConstants.COMPONENT_OPTIONS_SHOW_CONFLICTS);
            this.displayMode = DisplayMode.ALL;
        } else {
            this.displayMode = DisplayMode.CONFLICTING;
            this.buttonConflicting.m_93666_(ControllingConstants.COMPONENT_OPTIONS_SHOW_ALL);
            this.buttonNone.m_93666_(ControllingConstants.COMPONENT_OPTIONS_SHOW_NONE);
        }
        this.filterKeys();
    };

    private final Button.OnPress PRESS_FREE = btn -> {
        this.m_169411_(this.getKeyBindsList());
        if (this.showFree) {
            this.buttonSort.f_93623_ = true;
            this.buttonNone.f_93623_ = true;
            this.buttonConflicting.f_93623_ = true;
            this.resetButton().f_93623_ = true;
            this.setKeyBindsList((KeyBindsList) this.newKeyList.get());
        } else {
            ((FreeKeysList) this.freeKeyList.get()).recalculate();
            this.buttonSort.f_93623_ = false;
            this.buttonNone.f_93623_ = false;
            this.buttonConflicting.f_93623_ = false;
            this.resetButton().f_93623_ = false;
            this.setKeyBindsList((KeyBindsList) this.freeKeyList.get());
        }
        this.filterKeys();
        this.m_7787_(this.getKeyBindsList());
        this.m_7522_(this.getKeyBindsList());
        this.showFree = !this.showFree;
    };

    public NewKeyBindsScreen(Screen screen, Options settings) {
        super(screen, settings);
    }

    @Override
    protected void init() {
        super.init();
        int searchX = this.getKeyBindsList().getRowWidth();
        int btnWidth = 74;
        int groupPadding = 5;
        int centerX = this.f_96543_ / 2;
        int leftX = centerX - 150 - groupPadding;
        int rightX = centerX + groupPadding;
        int bottomY = this.f_96544_ - 29;
        int rowSpacing = 24;
        int topRowY = bottomY - rowSpacing;
        Supplier<List<KeyBindsList.Entry>> listSupplier = () -> this.getCustomList().getAllEntries();
        this.search = (AutoCompletingEditBox<KeyBindsList.Entry>) this.m_142416_(new AutoCompletingEditBox<>(this.f_96547_, centerX - searchX / 2, 22, searchX, 20, this.search, Component.translatable("selectWorld.search"), ControllingConstants.SEARCHABLE_KEYBINDINGS, listSupplier));
        this.search.addResponder(this::filterKeys);
        this.m_169394_(this.search.autoComplete());
        this.newKeyList = Suppliers.memoize(() -> new NewKeyBindsList(this, this.f_96541_));
        this.freeKeyList = Suppliers.memoize(() -> new FreeKeysList(this, this.f_96541_));
        this.m_169411_(this.getKeyBindsList());
        this.setKeyBindsList(this.showFree ? (KeyBindsList) this.freeKeyList.get() : (KeyBindsList) this.newKeyList.get());
        this.m_7787_(this.getKeyBindsList());
        this.m_169411_(this.resetButton());
        this.resetButton((Button) this.m_142416_(Button.builder(this.confirmingReset.currentDisplay(), this.PRESS_RESET).bounds(leftX, bottomY, 150, 20).build()));
        this.m_142416_(Button.builder(ControllingConstants.COMPONENT_OPTIONS_TOGGLE_FREE, this.PRESS_FREE).bounds(leftX, topRowY, btnWidth, 20).build());
        this.buttonSort = (Button) this.m_142416_(Button.builder(this.sortOrder.getDisplay(), this.PRESS_SORT).bounds(leftX + btnWidth + 2, topRowY, btnWidth, 20).build());
        this.buttonNone = (Button) this.m_142416_(Button.builder(ControllingConstants.COMPONENT_OPTIONS_SHOW_NONE, this.PRESS_NONE).bounds(rightX, topRowY, btnWidth, 20).build());
        this.buttonConflicting = (Button) this.m_142416_(Button.builder(ControllingConstants.COMPONENT_OPTIONS_SHOW_CONFLICTS, this.PRESS_CONFLICTING).bounds(rightX + btnWidth + 2, topRowY, btnWidth, 20).build());
        this.displayMode = DisplayMode.ALL;
        this.m_264313_(this.search);
        this.search.m_94188_(0);
        this.m_6702_().sort(Comparator.comparingInt(value -> value.getRectangle().top()).thenComparingInt(listener -> listener.getRectangle().left()));
    }

    public Button resetButton() {
        return this.getAccess().controlling$getResetButton();
    }

    public void resetButton(Button button) {
        this.getAccess().controlling$setResetButton(button);
    }

    public void filterKeys() {
        this.filterKeys(this.search.m_94155_());
    }

    public void filterKeys(String lastSearch) {
        this.getKeyBindsList().m_6702_().clear();
        this.getKeyBindsList().m_93410_(0.0);
        if (lastSearch.isEmpty() && this.displayMode == DisplayMode.ALL && this.sortOrder == SortOrder.NONE) {
            this.getKeyBindsList().m_6702_().addAll(this.getCustomList().getAllEntries());
        } else {
            Predicate<KeyBindsList.Entry> extraPredicate = entry -> true;
            Consumer<List<KeyBindsList.Entry>> postConsumer = entries -> {
            };
            CustomList list = this.getCustomList();
            if (list instanceof NewKeyBindsList) {
                extraPredicate = this.displayMode.getPredicate();
                postConsumer = entries -> this.sortOrder.sort(entries);
            }
            list.m_6702_().addAll(ControllingConstants.SEARCHABLE_KEYBINDINGS.filterEntries(list.getAllEntries(), lastSearch, extraPredicate));
            postConsumer.accept(list.m_6702_());
        }
    }

    @Override
    public void tick() {
        this.search.m_94120_();
    }

    @Override
    public boolean mouseScrolled(double xpos, double ypos, double delta) {
        return this.search.autoComplete().mouseScrolled(xpos, ypos, delta) ? true : super.m_6050_(xpos, ypos, delta);
    }

    @Override
    public boolean keyPressed(int key, int scancode, int mods) {
        if (!this.search.m_93696_() && this.f_193975_ == null && m_96637_() && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 70)) {
            this.search.m_93692_(true);
            return true;
        } else if (this.search.m_93696_() && key == 256) {
            this.search.m_93692_(false);
            return true;
        } else if (this.f_193975_ != null) {
            if (key == 256) {
                Services.PLATFORM.setKey(this.f_96282_, this.f_193975_, InputConstants.UNKNOWN);
            } else {
                Services.PLATFORM.setKey(this.f_96282_, this.f_193975_, InputConstants.getKey(key, scancode));
            }
            if (!Services.PLATFORM.isKeyCodeModifier(((AccessKeyMapping) this.f_193975_).controlling$getKey())) {
                this.f_193975_ = null;
            }
            this.f_193976_ = Util.getMillis();
            this.getKeyBindsList().resetMappingAndUpdateButtons();
            return true;
        } else {
            return super.keyPressed(key, scancode, mods);
        }
    }

    private CustomList getCustomList() {
        KeyBindsList var2 = this.getKeyBindsList();
        if (var2 instanceof CustomList) {
            return (CustomList) var2;
        } else {
            throw new IllegalStateException("keyBindsList('%s') was not an instance of CustomList! You're either too early or another mod is messing with things.".formatted(this.getKeyBindsList().getClass()));
        }
    }

    private KeyBindsList getKeyBindsList() {
        return this.getAccess().controlling$getKeyBindsList();
    }

    private void setKeyBindsList(KeyBindsList newList) {
        this.getAccess().controlling$setKeyBindsList(newList);
    }

    private AccessKeyBindsScreen getAccess() {
        return (AccessKeyBindsScreen) this;
    }
}