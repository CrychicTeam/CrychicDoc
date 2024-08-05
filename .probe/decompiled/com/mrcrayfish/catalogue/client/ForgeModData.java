package com.mrcrayfish.catalogue.client;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.VersionChecker.CheckResult;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;

public class ForgeModData implements IModData {

    private final IModInfo info;

    public ForgeModData(IModInfo info) {
        this.info = info;
    }

    @Override
    public IModData.Type getType() {
        return IModData.Type.DEFAULT;
    }

    @Override
    public String getModId() {
        return this.info.getModId();
    }

    @Override
    public String getDisplayName() {
        return this.info.getDisplayName();
    }

    @Override
    public String getVersion() {
        return this.info.getVersion().toString();
    }

    @Override
    public String getDescription() {
        return this.info.getDescription();
    }

    @Nullable
    @Override
    public String getItemIcon() {
        String itemIcon = (String) this.info.getModProperties().get("catalogueItemIcon");
        if (itemIcon == null) {
            itemIcon = (String) ((ModInfo) this.info).getConfigElement(new String[] { "itemIcon" }).orElse(null);
        }
        return itemIcon;
    }

    @Nullable
    @Override
    public String getImageIcon() {
        return this.info.getModProperties().get("catalogueImageIcon") instanceof String s ? s : null;
    }

    @Override
    public String getLicense() {
        return this.info.getOwningFile().getLicense();
    }

    @Override
    public String getCredits() {
        return this.getConfigString("credits");
    }

    @Nullable
    @Override
    public String getAuthors() {
        return this.getConfigString("authors");
    }

    @Nullable
    @Override
    public String getHomepage() {
        return this.getConfigString("displayURL");
    }

    @Nullable
    @Override
    public String getIssueTracker() {
        return this.getConfigString("issueTrackerURL");
    }

    @Nullable
    @Override
    public String getBanner() {
        return (String) this.info.getLogoFile().orElse(null);
    }

    @Nullable
    @Override
    public String getBackground() {
        return this.info.getModProperties().get("catalogueBackground") instanceof String s ? s : null;
    }

    @Override
    public IModData.Update getUpdate() {
        CheckResult result = VersionChecker.getResult(this.info);
        return result.status().shouldDraw() ? new IModData.Update(result.status().isAnimated(), result.url(), result.status().getSheetOffset()) : null;
    }

    @Override
    public boolean hasConfig() {
        return ConfigScreenHandler.getScreenFactoryFor(this.info).isPresent();
    }

    @Override
    public boolean isLogoSmooth() {
        return this.info.getLogoBlur();
    }

    @Override
    public void openConfigScreen(Screen parent) {
        ConfigScreenHandler.getScreenFactoryFor(this.info).map(f -> (Screen) f.apply(Minecraft.getInstance(), parent)).ifPresent(newScreen -> Minecraft.getInstance().setScreen(newScreen));
    }

    @Nullable
    private String getConfigString(String key) {
        return (String) ((ModInfo) this.info).getConfigElement(new String[] { key }).map(Object::toString).orElse(null);
    }
}