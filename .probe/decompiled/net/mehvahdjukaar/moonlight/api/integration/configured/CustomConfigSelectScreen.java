package net.mehvahdjukaar.moonlight.api.integration.configured;

import com.mojang.blaze3d.platform.Lighting;
import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.screen.ListMenuScreen;
import com.mrcrayfish.configured.client.screen.ModConfigSelectionScreen;
import com.mrcrayfish.configured.client.screen.widget.IconButton;
import com.mrcrayfish.configured.impl.forge.ForgeConfig;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.forge.ConfigSpecWrapper;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;

public class CustomConfigSelectScreen extends ModConfigSelectionScreen {

    public static final ResourceLocation MISC_ICONS = Moonlight.res("textures/gui/misc_icons.png");

    private static final Field FILE_ITEM_BUTTON = CustomConfigScreen.findFieldOrNull(ModConfigSelectionScreen.FileItem.class, "modifyButton");

    private static final Field FILE_ITEM_CONFIG = CustomConfigScreen.findFieldOrNull(ModConfigSelectionScreen.FileItem.class, "config");

    private final BiFunction<CustomConfigSelectScreen, IModConfig, CustomConfigScreen> configScreenFactory;

    private final ItemStack mainIcon;

    private final String modId;

    private final String modURL;

    public CustomConfigSelectScreen(String modId, ItemStack mainIcon, String displayName, ResourceLocation background, Screen parent, BiFunction<CustomConfigSelectScreen, IModConfig, CustomConfigScreen> configScreenFactory, ConfigSpec... specs) {
        this(modId, mainIcon, displayName, background, parent, configScreenFactory, createConfigMap(specs));
    }

    public CustomConfigSelectScreen(String modId, ItemStack mainIcon, String displayName, ResourceLocation background, Screen parent, BiFunction<CustomConfigSelectScreen, IModConfig, CustomConfigScreen> configScreenFactory, Map<ConfigType, Set<IModConfig>> configMap) {
        super(parent, Component.literal(displayName), ensureNotNull(background), configMap);
        this.configScreenFactory = configScreenFactory;
        this.mainIcon = mainIcon;
        this.modId = modId;
        ModContainer container = (ModContainer) ModList.get().getModContainerById(modId).get();
        this.modURL = (String) container.getModInfo().getModURL().map(URL::getPath).orElse(null);
    }

    public static ResourceLocation ensureNotNull(ResourceLocation background) {
        return background == null ? new ResourceLocation("minecraft:textures/gui/options_background.png") : background;
    }

    public ItemStack getMainIcon() {
        return this.mainIcon;
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return super.getBackgroundTexture();
    }

    public String getModId() {
        return this.modId;
    }

    public static void registerConfigScreen(String modId, Function<Screen, CustomConfigSelectScreen> screenSelectFactory) {
        ModContainer container = (ModContainer) ModList.get().getModContainerById(modId).get();
        container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((m, s) -> (Screen) screenSelectFactory.apply(s)));
    }

    private static Map<ConfigType, Set<IModConfig>> createConfigMap(ConfigSpec... specs) {
        Map<ConfigType, Set<IModConfig>> modConfigMap = new EnumMap(ConfigType.class);
        for (ConfigSpec ss : specs) {
            ConfigSpecWrapper s = (ConfigSpecWrapper) ss;
            ModConfig modConfig = s.getModConfig();
            ForgeConfig forgeConfig = new ForgeConfig(modConfig, ((ConfigSpecWrapper) ss).getSpec());
            Set<IModConfig> set = (Set<IModConfig>) modConfigMap.computeIfAbsent(forgeConfig.getType(), a -> new HashSet());
            set.add(forgeConfig);
        }
        return modConfigMap;
    }

    private static ConfigType getType(ConfigSpecWrapper s) {
        net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType t = s.getConfigType();
        if (t == net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType.CLIENT) {
            return ConfigType.CLIENT;
        } else {
            return t == net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType.COMMON ? ConfigType.UNIVERSAL : ConfigType.UNIVERSAL;
        }
    }

    @Override
    protected void constructEntries(List<ListMenuScreen.Item> entries) {
        super.constructEntries(entries);
        for (ListMenuScreen.Item i : entries) {
            if (i instanceof ModConfigSelectionScreen.FileItem) {
                ModConfigSelectionScreen.FileItem item = (ModConfigSelectionScreen.FileItem) i;
                try {
                    FILE_ITEM_BUTTON.setAccessible(true);
                    FILE_ITEM_CONFIG.setAccessible(true);
                    FILE_ITEM_BUTTON.set(i, this.createModifyButton((IModConfig) FILE_ITEM_CONFIG.get(item)));
                } catch (IllegalAccessException var6) {
                }
            }
        }
    }

    private Button createModifyButton(IModConfig config) {
        String langKey = "configured.gui.modify";
        return new IconButton(0, 0, 33, 0, 60, Component.translatable(langKey), onPress -> Minecraft.getInstance().setScreen((Screen) this.configScreenFactory.apply(this, config)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        Lighting.setupFor3DItems();
        int titleWidth = this.f_96547_.width(this.f_96539_) + 35;
        graphics.renderFakeItem(this.mainIcon, this.f_96543_ / 2 + titleWidth / 2 - 17, 2);
        graphics.renderFakeItem(this.mainIcon, this.f_96543_ / 2 - titleWidth / 2, 2);
        if (this.modURL != null && MthUtils.isWithinRectangle(this.f_96543_ / 2 - 90, 2, 180, 16, mouseX, mouseY)) {
            graphics.renderTooltip(this.f_96547_, this.f_96547_.split(Component.translatable("gui.moonlight.open_mod_page", this.modId), 200), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.modURL != null && MthUtils.isWithinRectangle(this.f_96543_ / 2 - 90, 2, 180, 16, (int) mouseX, (int) mouseY)) {
            Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.modURL));
            this.m_5561_(style);
            return true;
        } else {
            return super.m_6375_(mouseX, mouseY, button);
        }
    }
}