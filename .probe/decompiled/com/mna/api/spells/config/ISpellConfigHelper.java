package com.mna.api.spells.config;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.ISpellComponent;
import net.minecraft.resources.ResourceLocation;

public interface ISpellConfigHelper {

    void initForPart(ISpellComponent var1, AttributeValuePair... var2);

    boolean isPartInitialized(ISpellComponent var1);

    boolean isDimensionBlacklisted(ISpellComponent var1, ResourceLocation var2);

    boolean isBiomeBlacklisted(ISpellComponent var1, ResourceLocation var2);

    float getConfiguredValue(ISpellComponent var1, Attribute var2, ISpellConfigHelper.Value var3, float var4);

    public static enum Value {

        MINIMUM, MAXIMUM, DEFAULT, STEP, COMPLEXITY
    }
}