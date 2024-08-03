package snownee.jade.gui;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.gui.config.OptionsList;
import snownee.jade.gui.config.value.OptionValue;
import snownee.jade.impl.ObjectDataCenter;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.impl.config.entry.ConfigEntry;
import snownee.jade.util.ModIdentification;

public class PluginsConfigScreen extends BaseOptionsScreen {

    private final MutableObject<OptionsList.Entry> jumpToEntry = new MutableObject();

    private String jumpTo;

    public PluginsConfigScreen(Screen parent) {
        super(parent, Component.translatable("gui.jade.plugin_settings"));
        this.saver = PluginConfig.INSTANCE::save;
        this.canceller = PluginConfig.INSTANCE::reload;
    }

    public static Screen createPluginConfigScreen(@Nullable Screen parent, @Nullable String namespace, boolean dontSave) {
        PluginsConfigScreen screen = new PluginsConfigScreen(parent);
        screen.jumpTo = namespace;
        return screen;
    }

    @Override
    public OptionsList createOptions() {
        OptionsList options = new OptionsList(this, this.f_96541_, this.f_96543_ - 120, this.f_96544_, 0, this.f_96544_ - 32, 26, PluginConfig.INSTANCE::save);
        boolean noteServerFeature = Minecraft.getInstance().level == null || IWailaConfig.get().getGeneral().isDebug() || !ObjectDataCenter.serverConnected;
        PluginConfig.INSTANCE.getNamespaces().forEach(namespace -> {
            String translationKey = "plugin_" + namespace;
            Optional<String> modName = ModIdentification.getModName(namespace);
            MutableComponent title;
            if (!"jade".equals(namespace) && modName.isPresent()) {
                title = Component.literal((String) modName.get());
            } else {
                title = Component.translatable(OptionsList.Entry.makeKey(translationKey));
            }
            if (namespace.equals(this.jumpTo)) {
                this.jumpToEntry.setValue(options.add(new OptionsList.Title(title)));
            } else {
                options.add(new OptionsList.Title(title));
            }
            Set<ResourceLocation> keys = PluginConfig.INSTANCE.getKeys(namespace);
            MutableObject<OptionValue<?>> lastPrimary = new MutableObject();
            keys.stream().sorted(Comparator.comparingInt(WailaCommonRegistration.INSTANCE.priorities.getSortedList()::indexOf)).forEach(i -> {
                ConfigEntry<?> configEntry = PluginConfig.INSTANCE.getEntry(i);
                OptionValue<?> entry = configEntry.createUI(options, translationKey + "." + i.getPath());
                if (configEntry.isSynced()) {
                    entry.setDisabled(true);
                    entry.appendDescription(ChatFormatting.DARK_RED + I18n.get("gui.jade.forced_plugin_config"));
                } else if (noteServerFeature && !WailaClientRegistration.INSTANCE.isClientFeature(i)) {
                    entry.serverFeature = true;
                }
                if (i.getPath().contains(".")) {
                    if (lastPrimary.getValue() != null) {
                        entry.parent((OptionsList.Entry) lastPrimary.getValue());
                    }
                } else {
                    lastPrimary.setValue(entry);
                }
            });
        });
        this.jumpTo = null;
        return options;
    }

    @Override
    protected void init() {
        super.init();
        if (this.jumpToEntry.getValue() != null) {
            this.options.showOnTop((OptionsList.Entry) this.jumpToEntry.getValue());
            this.jumpToEntry.setValue(null);
        }
    }
}