package com.github.alexthe666.alexsmobs.entity.util;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.item.ItemRainbowJelly;
import com.github.alexthe666.alexsmobs.misc.AMSimplexNoise;
import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import java.awt.Color;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class RainbowUtil {

    private static final String RAINBOW_TYPE = "RainbowTypeAlexsMobs";

    public static void setRainbowType(LivingEntity fabulous, int type) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(fabulous);
        tag.putInt("RainbowTypeAlexsMobs", type);
        CitadelEntityData.setCitadelTag(fabulous, tag);
        if (!fabulous.m_9236_().isClientSide) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", tag, fabulous.m_19879_()));
        } else {
            Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", tag, fabulous.m_19879_()));
        }
    }

    public static int getRainbowType(LivingEntity entity) {
        CompoundTag lassoedTag = CitadelEntityData.getOrCreateCitadelTag(entity);
        return lassoedTag.contains("RainbowTypeAlexsMobs") ? lassoedTag.getInt("RainbowTypeAlexsMobs") : 0;
    }

    public static int getRainbowTypeFromStack(ItemStack stack) {
        String name = stack.getDisplayName().getString().toLowerCase(Locale.ROOT);
        return ItemRainbowJelly.RainbowType.getFromString(name).ordinal() + 1;
    }

    public static int calculateGlassColor(BlockPos pos) {
        float f = (float) AMConfig.rainbowGlassFidelity;
        float f1 = (float) ((AMSimplexNoise.noise((double) (((float) pos.m_123341_() + f) / f), (double) (((float) pos.m_123342_() + f) / f), (double) (((float) pos.m_123343_() + f) / f)) + 1.0) * 0.5);
        return Color.HSBtoRGB(f1, 1.0F, 1.0F);
    }
}