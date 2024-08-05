package com.mna.apibridge;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.config.ISpellConfigHelper;
import com.mna.config.SpellConfig;
import com.mna.config.SpellConfigProvider;
import net.minecraft.resources.ResourceLocation;

public class ConfigHelper implements ISpellConfigHelper {

    @Override
    public void initForPart(ISpellComponent shape, AttributeValuePair... pairs) {
        SpellConfigProvider.initForPart(SpellConfig.SPELLS_CONFIG_BUILDER, shape, pairs);
    }

    @Override
    public boolean isDimensionBlacklisted(ISpellComponent part, ResourceLocation dimensionID) {
        return SpellConfig.isDimensionBlacklisted(part, dimensionID);
    }

    @Override
    public boolean isBiomeBlacklisted(ISpellComponent part, ResourceLocation biomeID) {
        return SpellConfig.isBiomeBlacklisted(part, biomeID);
    }

    @Override
    public float getConfiguredValue(ISpellComponent part, Attribute attribute, ISpellConfigHelper.Value value, float defaultValue) {
        return SpellConfig.getConfiguredValue(part, attribute, value, defaultValue);
    }

    @Override
    public boolean isPartInitialized(ISpellComponent part) {
        return SpellConfig.isPartInitialized(part);
    }
}