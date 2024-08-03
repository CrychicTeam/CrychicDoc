package de.keksuccino.fancymenu.customization.placeholder.placeholders.gui;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.editor.AbstractEditorElement;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementHeightPlaceholder extends Placeholder {

    private static final Logger LOGGER = LogManager.getLogger();

    public ElementHeightPlaceholder() {
        super("elementheight");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        if (Minecraft.getInstance().screen == null) {
            return "1";
        } else {
            String id = (String) dps.values.get("id");
            if (id != null) {
                AbstractElement element = this.findElement(id);
                if (element != null) {
                    return element.getAbsoluteHeight() + "";
                }
                LOGGER.error("[FANCYMENU] Unable to get height of element via placeholder! Element not found: " + id);
            }
            return "1";
        }
    }

    private AbstractElement findElement(String id) {
        if (Minecraft.getInstance().screen != null) {
            if (Minecraft.getInstance().screen instanceof LayoutEditorScreen editor) {
                AbstractEditorElement e = editor.getElementByInstanceIdentifier(id);
                if (e != null) {
                    return e.element;
                }
            } else {
                ScreenCustomizationLayer mh = ScreenCustomizationLayerHandler.getLayerOfScreen(Minecraft.getInstance().screen);
                if (mh != null) {
                    return mh.getElementByInstanceIdentifier(id);
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        List<String> l = new ArrayList();
        l.add("id");
        return l;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.helper.placeholder.elementheight");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.helper.placeholder.elementheight.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.gui");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        dps.values.put("id", "some.element.id");
        return dps;
    }
}