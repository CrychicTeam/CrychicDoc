package net.mehvahdjukaar.moonlight.api.integration.configured;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.configured.api.IConfigEntry;
import com.mrcrayfish.configured.api.IConfigValue;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.api.ValueEntry;
import com.mrcrayfish.configured.client.screen.ConfigScreen;
import com.mrcrayfish.configured.client.screen.ListMenuScreen;
import com.mrcrayfish.configured.client.screen.widget.IconButton;
import com.mrcrayfish.configured.impl.forge.ForgeConfig;
import com.mrcrayfish.configured.impl.forge.ForgeValue;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.client.util.RenderUtil;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.forge.ConfigSpecWrapper;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

public abstract class CustomConfigScreen extends ConfigScreen {

    @Nullable
    private static final Field BUTTON_ON_PRESS = findFieldOrNull(Button.class, "onPress");

    @Nullable
    private static final Field FOLDER_ENTRY = findFieldOrNull(ConfigScreen.class, "folderEntry");

    @Nullable
    private static final Method SAVE_CONFIG = findMethodOrNull(ConfigScreen.class, "saveConfig");

    @Nullable
    private static final Field CONFIG_VALUE_HOLDER = findFieldOrNull(ConfigScreen.ConfigItem.class, "holder");

    @Nullable
    private static final Field BOOLEAN_ITEM_BUTTON = findFieldOrNull(ConfigScreen.BooleanItem.class, "button");

    protected final String modId;

    protected final Map<String, ItemStack> icons = new HashMap();

    public final ItemStack mainIcon;

    private int ticks = 0;

    @Nullable
    static Method findMethodOrNull(Class<?> c, String methodName) {
        Method field = null;
        try {
            field = ObfuscationReflectionHelper.findMethod(c, methodName, new Class[0]);
        } catch (Exception var4) {
        }
        return field;
    }

    @Nullable
    static Field findFieldOrNull(Class<?> c, String fieldName) {
        Field field = null;
        try {
            field = ObfuscationReflectionHelper.findField(c, fieldName);
        } catch (Exception var4) {
        }
        return field;
    }

    protected CustomConfigScreen(CustomConfigSelectScreen parent, IModConfig config) {
        this(parent.getModId(), parent.getMainIcon(), parent.getBackgroundTexture(), parent.m_96636_(), parent, config);
    }

    protected CustomConfigScreen(CustomConfigSelectScreen parent, ModConfig config) {
        this(parent.getModId(), parent.getMainIcon(), parent.getBackgroundTexture(), parent.m_96636_(), parent, config);
    }

    protected CustomConfigScreen(String modId, ItemStack mainIcon, ResourceLocation background, Component title, Screen parent, ModConfig config) {
        this(modId, mainIcon, background, title, parent, new ForgeConfig(config, (ForgeConfigSpec) config.getSpec()));
    }

    protected CustomConfigScreen(String modId, ItemStack mainIcon, ResourceLocation background, Component title, Screen parent, IModConfig config) {
        super(parent, title, config, CustomConfigSelectScreen.ensureNotNull(background));
        this.modId = modId;
        this.mainIcon = mainIcon;
    }

    @Override
    protected void constructEntries(List<ListMenuScreen.Item> entries) {
        super.constructEntries(entries);
        List<ListMenuScreen.Item> copy = new ArrayList(entries);
        entries.clear();
        ListIterator<ListMenuScreen.Item> iter = copy.listIterator();
        while (iter.hasNext()) {
            ListMenuScreen.Item e = (ListMenuScreen.Item) iter.next();
            if (e.getLabel().toLowerCase(Locale.ROOT).equals(this.getEnabledKeyword())) {
                iter.remove();
                entries.add(e);
            }
        }
        entries.addAll(copy);
    }

    public ItemStack getIcon(String... path) {
        String last = path[path.length - 1];
        if (path.length > 1 && last.equals(this.getEnabledKeyword())) {
            last = path[path.length - 2];
        }
        last = last.toLowerCase(Locale.ROOT).replace("_", " ");
        if (!this.icons.containsKey(last)) {
            String formatted = last.toLowerCase(Locale.ROOT).replace(" ", "_");
            Optional<net.minecraft.world.item.Item> item = BuiltInRegistries.ITEM.m_6612_(new ResourceLocation(this.modId, formatted));
            item.ifPresent(value -> this.addIcon(last, value.asItem().getDefaultInstance()));
        }
        return (ItemStack) this.icons.getOrDefault(last, ItemStack.EMPTY);
    }

    private void addIcon(String s, ItemStack i) {
        this.icons.put(s, i);
    }

    @Override
    protected void init() {
        super.init();
        this.list.replaceEntries(this.replaceItems(this.list.m_6702_()));
        Collection<ListMenuScreen.Item> temp = this.replaceItems(this.entries);
        this.entries = new ArrayList(temp);
        if (this.saveButton != null && SAVE_CONFIG != null && BUTTON_ON_PRESS != null) {
            try {
                Button.OnPress press = this::saveButtonAction;
                BUTTON_ON_PRESS.set(this.saveButton, press);
            } catch (Exception var3) {
            }
        }
    }

    private Collection<ListMenuScreen.Item> replaceItems(Collection<ListMenuScreen.Item> originals) {
        ArrayList<ListMenuScreen.Item> newList = new ArrayList();
        for (ListMenuScreen.Item c : originals) {
            if (c instanceof ConfigScreen.FolderItem f) {
                CustomConfigScreen.FolderWrapper wrapper = this.wrapFolderItem(f);
                if (wrapper != null) {
                    newList.add(wrapper);
                    continue;
                }
            } else if (c instanceof ConfigScreen.BooleanItem b) {
                CustomConfigScreen.BooleanWrapper wrapper = this.wrapBooleanItem(b);
                if (wrapper != null) {
                    newList.add(wrapper);
                    continue;
                }
            }
            newList.add(c);
        }
        return newList;
    }

    private void saveButtonAction(Button button) {
        if (this.config != null) {
            try {
                SAVE_CONFIG.invoke(this);
            } catch (Exception var3) {
            }
            if (this.isChanged(this.folderEntry)) {
                this.onSave();
            }
        }
        this.f_96541_.setScreen(this.parent);
    }

    public abstract void onSave();

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        Lighting.setupFor3DItems();
        int titleWidth = this.f_96547_.width(this.f_96539_) + 35;
        graphics.renderFakeItem(this.mainIcon, this.f_96543_ / 2 + titleWidth / 2 - 17, 2);
        graphics.renderFakeItem(this.mainIcon, this.f_96543_ / 2 - titleWidth / 2, 2);
    }

    @Override
    public void tick() {
        super.m_86600_();
        this.ticks++;
    }

    @Nullable
    public CustomConfigScreen.FolderWrapper wrapFolderItem(ConfigScreen.FolderItem old) {
        try {
            String oldName = old.getLabel();
            IConfigEntry found = null;
            for (IConfigEntry e : this.folderEntry.getChildren()) {
                if (!(e instanceof ValueEntry)) {
                    String n = Component.literal(ConfigScreen.createLabel(e.getEntryName())).getString();
                    if (n.equals(oldName)) {
                        found = e;
                        break;
                    }
                }
            }
            if (found != null) {
                return new CustomConfigScreen.FolderWrapper(found, oldName);
            }
        } catch (Exception var7) {
            Moonlight.LOGGER.error("error", var7);
        }
        return null;
    }

    public abstract CustomConfigScreen createSubScreen(Component var1);

    public String getEnabledKeyword() {
        return "enabled";
    }

    public List<ConfigSpec> getCustomSpecs() {
        return List.of();
    }

    @Nullable
    public CustomConfigScreen.BooleanWrapper wrapBooleanItem(ConfigScreen.BooleanItem old) {
        try {
            IConfigValue<Boolean> holder = (IConfigValue<Boolean>) CONFIG_VALUE_HOLDER.get(old);
            ValueEntry found = null;
            for (IConfigEntry e : this.folderEntry.getChildren()) {
                if (e instanceof ValueEntry) {
                    ValueEntry value = (ValueEntry) e;
                    if (holder == value.getValue()) {
                        found = value;
                    }
                }
            }
            if (found != null) {
                String[] path = (String[]) ((ForgeValue) holder).configValue.getPath().toArray(String[]::new);
                ItemStack icon = this.getIcon(path);
                return new CustomConfigScreen.BooleanWrapper(holder, icon);
            }
        } catch (Exception var7) {
            Moonlight.LOGGER.error("error");
        }
        return null;
    }

    private static void rotateItem(int ticks, float partialTicks, PoseStack s, BakedModel m) {
        if (ticks != 0) {
            if (m.usesBlockLight()) {
                s.mulPose(Axis.YP.rotation(((float) ticks + partialTicks) * (float) (Math.PI / 180.0) * 10.0F));
            } else {
                float scale = 1.0F + 0.1F * Mth.sin(((float) ticks + partialTicks) * (float) (Math.PI / 180.0) * 20.0F);
                s.scale(scale, scale, scale);
            }
        }
    }

    private class BooleanWrapper extends ConfigScreen.BooleanItem {

        private static final int ICON_SIZE = 12;

        private final ItemStack item;

        protected final int iconOffset;

        protected final boolean needsGameRestart;

        protected boolean doesNeedsGameRestart = false;

        protected Button button;

        private int ticks = 0;

        private int lastTick = 1;

        public BooleanWrapper(IConfigValue<Boolean> holder, ItemStack item) {
            super(holder);
            try {
                this.button = (Button) CustomConfigScreen.BOOLEAN_ITEM_BUTTON.get(this);
            } catch (Exception var5) {
            }
            this.button.m_93666_(Component.literal(""));
            this.needsGameRestart = this.hackyCheckIfValueNeedsGameRestart(holder);
            this.item = item;
            this.iconOffset = item.isEmpty() ? 0 : 7;
        }

        public BooleanWrapper(IConfigValue<Boolean> holder) {
            this(holder, ItemStack.EMPTY);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean r = super.m_6375_(mouseX, mouseY, button);
            this.doesNeedsGameRestart = !this.doesNeedsGameRestart;
            return r;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            this.button.m_93666_(Component.literal(""));
            super.render(graphics, index, top, left, width, height, mouseX, mouseY, hovered, partialTicks);
            hovered = this.button.m_5953_((double) mouseX, (double) mouseY);
            if (this.lastTick < CustomConfigScreen.this.ticks) {
                this.ticks = Math.max(0, this.ticks + (hovered ? 1 : -2)) % 36;
                if (!hovered && this.ticks > 17) {
                    this.ticks %= 18;
                }
            }
            this.lastTick = CustomConfigScreen.this.ticks;
            if (this.doesNeedsGameRestart) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                graphics.blit(IconButton.ICONS, left - 18, top + 5, 11, 11, 51.0F, 22.0F, 11, 11, 64, 64);
                if (MthUtils.isWithinRectangle(left - 18, top + 5, 11, 11, mouseX, mouseY)) {
                    String translationKey = "configured.gui.requires_game_restart";
                    int outline = -1438090048;
                    CustomConfigScreen.this.setActiveTooltip(Component.translatable(translationKey), outline);
                }
            }
            RenderSystem.setShader(GameRenderer::m_172820_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            int iconX = this.iconOffset + (int) ((double) this.button.m_252754_() + Math.ceil((double) ((float) (this.button.m_5711_() - 12) / 2.0F)));
            int iconY = (int) ((double) this.button.m_252907_() + Math.ceil((double) ((float) (this.button.m_93694_() - 12) / 2.0F)));
            boolean on = this.holder.get();
            int u = on ? 12 : 0;
            graphics.blit(CustomConfigSelectScreen.MISC_ICONS, iconX, iconY, 0, (float) u, 0.0F, 12, 12, 64, 64);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (!this.item.isEmpty()) {
                int light = on ? 15728880 : 0;
                int center = (int) ((float) this.button.m_252754_() + (float) this.button.m_5711_() / 2.0F);
                ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
                RenderUtil.renderGuiItemRelative(graphics.pose(), this.item, center - 8 - this.iconOffset, top + 2, renderer, (s, m) -> CustomConfigScreen.rotateItem(this.ticks, partialTicks, s, m), light, OverlayTexture.NO_OVERLAY);
            }
        }

        private boolean hackyCheckIfValueNeedsGameRestart(IConfigValue<Boolean> value) {
            for (ConfigSpec v : CustomConfigScreen.this.getCustomSpecs()) {
                if (v.getFileName().equals(CustomConfigScreen.this.config.getFileName())) {
                    return ((ConfigSpecWrapper) v).requiresGameRestart(((ForgeValue) value).configValue);
                }
            }
            return false;
        }

        @Override
        public void onResetValue() {
            this.button.m_93666_(Component.literal(""));
        }
    }

    private class FolderWrapper extends ConfigScreen.FolderItem {

        private final ItemStack icon;

        protected final Button button;

        protected boolean light;

        private int ticks = 0;

        private int lastTick = 1;

        private FolderWrapper(IConfigEntry folderEntry, String label) {
            super(folderEntry);
            this.button = Button.builder(Component.literal(label).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.WHITE), onPress -> {
                Component newTitle = CustomConfigScreen.this.f_96539_.plainCopy().append(" > " + label);
                CustomConfigScreen sc = CustomConfigScreen.this.createSubScreen(newTitle);
                try {
                    CustomConfigScreen.FOLDER_ENTRY.set(sc, folderEntry);
                } catch (Exception var7) {
                }
                CustomConfigScreen.this.f_96541_.setScreen(sc);
            }).bounds(10, 5, 44, 20).build();
            ItemStack i = CustomConfigScreen.this.getIcon(label.toLowerCase(Locale.ROOT));
            this.icon = i.isEmpty() ? CustomConfigScreen.this.mainIcon : i;
            this.light = this.getFolderEnabledValue(folderEntry);
        }

        private boolean getFolderEnabledValue(IConfigEntry entry) {
            for (IConfigEntry c : entry.getChildren()) {
                IConfigValue<?> value = c.getValue();
                if (value != null && value.getName().equals(CustomConfigScreen.this.getEnabledKeyword()) && value.get() instanceof Boolean b) {
                    return b;
                }
            }
            return true;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.button);
        }

        @Override
        public void render(GuiGraphics graphics, int x, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            int light = this.light ? 15728880 : 0;
            if (this.lastTick < CustomConfigScreen.this.ticks) {
                this.ticks = Math.max(0, this.ticks + (hovered ? 1 : -2)) % 36;
            }
            this.lastTick = CustomConfigScreen.this.ticks;
            this.button.m_252865_(left - 1);
            this.button.m_253211_(top);
            this.button.m_93674_(width);
            this.button.m_88315_(graphics, mouseX, mouseY, partialTicks);
            int center = this.button.m_252754_() + width / 2;
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            RenderUtil.renderGuiItemRelative(graphics.pose(), this.icon, center + 95 - 17, top + 2, renderer, (s, m) -> CustomConfigScreen.rotateItem(this.ticks, partialTicks, s, m), light, OverlayTexture.NO_OVERLAY);
            RenderUtil.renderGuiItemRelative(graphics.pose(), this.icon, center - 95, top + 2, renderer, (s, m) -> CustomConfigScreen.rotateItem(this.ticks, partialTicks, s, m), light, OverlayTexture.NO_OVERLAY);
        }
    }
}