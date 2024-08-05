package dev.ftb.mods.ftbquests.client;

import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ImageResourceConfig;
import dev.ftb.mods.ftblibrary.config.ItemStackConfig;
import dev.ftb.mods.ftblibrary.config.ui.SelectImageResourceScreen;
import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ConfigIconItemStack extends ItemStackConfig {

    public ConfigIconItemStack() {
        super(false, true);
    }

    @Override
    public void onClicked(Widget clickedWidget, MouseButton button, ConfigCallback callback) {
        if (this.getCanEdit()) {
            if (button.isRight()) {
                ImageResourceConfig imageConfig = new ImageResourceConfig();
                new SelectImageResourceScreen(imageConfig, accepted -> {
                    if (accepted) {
                        if (!imageConfig.getValue().equals(ImageResourceConfig.NONE)) {
                            ItemStack stack = new ItemStack((ItemLike) FTBQuestsItems.CUSTOM_ICON.get());
                            stack.addTagElement("Icon", StringTag.valueOf(imageConfig.getValue().toString()));
                            this.setCurrentValue(stack);
                        } else {
                            this.setCurrentValue(ItemStack.EMPTY);
                        }
                    }
                    callback.save(accepted);
                }).openGui();
            } else {
                new SelectItemStackScreen(this, callback).openGui();
            }
        }
    }
}