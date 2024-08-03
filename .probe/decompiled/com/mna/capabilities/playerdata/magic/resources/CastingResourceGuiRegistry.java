package com.mna.capabilities.playerdata.magic.resources;

import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.capabilities.resource.ICastingResourceGuiProvider;
import com.mna.api.capabilities.resource.ICastingResourceGuiRegistry;
import com.mna.gui.GuiTextures;
import com.mna.items.ItemInit;
import java.security.InvalidParameterException;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CastingResourceGuiRegistry implements ICastingResourceGuiRegistry {

    private HashMap<ResourceLocation, ICastingResourceGuiProvider> _guiProviders = new HashMap();

    public static final CastingResourceGuiRegistry Instance = new CastingResourceGuiRegistry();

    private static final ItemStack HUD_BRIMSTONE_COLD = new ItemStack(ItemInit.MOTE_FIRE.get());

    private static final ItemStack HUD_BRIMSTONE_WARM = new ItemStack(ItemInit.GREATER_MOTE_FIRE.get());

    private static final ItemStack HUD_BRIMSTONE_HOT = new ItemStack(ItemInit.GREATER_MOTE_HELLFIRE.get());

    private static final ItemStack HUD_COUNCIL = new ItemStack(ItemInit.COUNCIL_HUD_BADGE.get());

    private static final ItemStack HUD_FEY_SUMMER = new ItemStack(ItemInit.FEY_SUMMER_HUD_BADGE.get());

    private static final ItemStack HUD_FEY_WINTER = new ItemStack(ItemInit.FEY_WINTER_HUD_BADGE.get());

    private static final ItemStack HUD_UNDEAD = new ItemStack(ItemInit.UNDEAD_HUD_BADGE.get());

    public void registerDefaults() {
        Instance.registerResourceGui(CastingResourceIDs.MANA, new ICastingResourceGuiProvider() {

            @Override
            public ResourceLocation getTexture() {
                return GuiTextures.Hud.BARS;
            }

            @Override
            public int getXPBarColor() {
                return -12039016;
            }

            @Override
            public int getBarColor() {
                return -7504641;
            }

            @Override
            public int getBarManaCostEstimateColor() {
                return -13357731;
            }

            @Override
            public int getResourceNumericTextColor() {
                return -5394;
            }

            @Override
            public int getBadgeSize() {
                return 64;
            }

            @Override
            public int getFrameU() {
                return 0;
            }

            @Override
            public int getFrameV() {
                return 0;
            }

            @Override
            public int getFrameWidth() {
                return 153;
            }

            @Override
            public int getFrameHeight() {
                return 24;
            }

            @Override
            public int getLevelDisplayY() {
                return this.getFrameHeight() - 6;
            }
        });
        Instance.registerResourceGui(CastingResourceIDs.COUNCIL_MANA, new ICastingResourceGuiProvider() {

            @Override
            public ResourceLocation getTexture() {
                return GuiTextures.Hud.BARS;
            }

            @Override
            public int getXPBarColor() {
                return -12039016;
            }

            @Override
            public int getBarColor() {
                return -7504641;
            }

            @Override
            public int getBarManaCostEstimateColor() {
                return -13357731;
            }

            @Override
            public int getResourceNumericTextColor() {
                return -5394;
            }

            @Override
            public int getBadgeSize() {
                return 64;
            }

            @Override
            public int getFrameU() {
                return 0;
            }

            @Override
            public int getFrameV() {
                return 24;
            }

            @Override
            public int getFrameWidth() {
                return 153;
            }

            @Override
            public int getFrameHeight() {
                return 24;
            }

            @Override
            public ItemStack getBadgeItem() {
                return CastingResourceGuiRegistry.HUD_COUNCIL;
            }

            @Override
            public int getBadgeItemOffsetY() {
                return 9;
            }

            @Override
            public int getLevelDisplayY() {
                return this.getFrameHeight() - 2;
            }
        });
        Instance.registerResourceGui(CastingResourceIDs.SOULS, new ICastingResourceGuiProvider() {

            @Override
            public ResourceLocation getTexture() {
                return GuiTextures.Hud.BARS;
            }

            @Override
            public int getXPBarColor() {
                return -9666636;
            }

            @Override
            public int getBarColor() {
                return -12497052;
            }

            @Override
            public int getBarManaCostEstimateColor() {
                return -12929864;
            }

            @Override
            public int getResourceNumericTextColor() {
                return -1909793;
            }

            @Override
            public int getBadgeSize() {
                return 64;
            }

            @Override
            public int getFrameU() {
                return 0;
            }

            @Override
            public int getFrameV() {
                return 169;
            }

            @Override
            public int getFrameWidth() {
                return 153;
            }

            @Override
            public int getFrameHeight() {
                return 26;
            }

            @Override
            public ItemStack getBadgeItem() {
                return CastingResourceGuiRegistry.HUD_UNDEAD;
            }

            @Override
            public int getBadgeItemOffsetY() {
                return 12;
            }

            @Override
            public int getXPBarOffsetY() {
                return 18;
            }

            @Override
            public int getFillStartY() {
                return 8;
            }

            @Override
            public int getResourceNumericOffsetY() {
                return 11;
            }

            @Override
            public int getLevelDisplayY() {
                return this.getFrameHeight() - 2;
            }

            @Override
            public int getLowChargeOffsetY() {
                return 0;
            }
        });
        Instance.registerResourceGui(CastingResourceIDs.BRIMSTONE, new ICastingResourceGuiProvider() {

            @Override
            public ResourceLocation getTexture() {
                return GuiTextures.Hud.BARS;
            }

            @Override
            public int getXPBarColor() {
                return FastColor.ARGB32.color(255, 128, 32, 32);
            }

            @Override
            public int getBarColor() {
                return -3917309;
            }

            @Override
            public int getBarManaCostEstimateColor() {
                return FastColor.ARGB32.color(255, 245, 126, 32);
            }

            @Override
            public int getResourceNumericTextColor() {
                return -2927;
            }

            @Override
            public int getBadgeSize() {
                return 64;
            }

            @Override
            public int getFrameU() {
                return 0;
            }

            @Override
            public int getFrameV() {
                Minecraft mc = Minecraft.getInstance();
                Holder<Biome> biome = mc.level.m_204166_(mc.player.m_20183_());
                if (((Biome) biome.get()).coldEnoughToSnow(mc.player.m_20183_())) {
                    return 96;
                } else {
                    return biome.is(BiomeTags.SNOW_GOLEM_MELTS) ? 144 : 120;
                }
            }

            @Override
            public int getFrameWidth() {
                return 153;
            }

            @Override
            public int getFrameHeight() {
                return 24;
            }

            @Override
            public ItemStack getBadgeItem() {
                Minecraft mc = Minecraft.getInstance();
                Holder<Biome> biome = mc.level.m_204166_(mc.player.m_20183_());
                if (((Biome) biome.get()).coldEnoughToSnow(mc.player.m_20183_())) {
                    return CastingResourceGuiRegistry.HUD_BRIMSTONE_COLD;
                } else {
                    return biome.is(BiomeTags.SNOW_GOLEM_MELTS) ? CastingResourceGuiRegistry.HUD_BRIMSTONE_HOT : CastingResourceGuiRegistry.HUD_BRIMSTONE_WARM;
                }
            }

            @Override
            public int getBadgeItemOffsetY() {
                Minecraft mc = Minecraft.getInstance();
                Biome biome = (Biome) mc.level.m_204166_(mc.player.m_20183_()).value();
                return biome.coldEnoughToSnow(mc.player.m_20183_()) ? 10 : ICastingResourceGuiProvider.super.getBadgeItemOffsetY();
            }
        });
        Instance.registerResourceGui(CastingResourceIDs.SUMMER_FIRE, new ICastingResourceGuiProvider() {

            @Override
            public ResourceLocation getTexture() {
                return GuiTextures.Hud.BARS;
            }

            @Override
            public int getXPBarColor() {
                return -10833371;
            }

            @Override
            public int getBarColor() {
                return -1405640;
            }

            @Override
            public int getBarManaCostEstimateColor() {
                return -8896481;
            }

            @Override
            public int getResourceNumericTextColor() {
                return 16711634;
            }

            @Override
            public int getBadgeSize() {
                return 64;
            }

            @Override
            public int getFrameU() {
                return 0;
            }

            @Override
            public int getFrameWidth() {
                return 153;
            }

            @Override
            public int getFrameHeight() {
                return 24;
            }

            @Override
            public int getFrameV() {
                return 48;
            }

            @Override
            public ItemStack getBadgeItem() {
                return CastingResourceGuiRegistry.HUD_FEY_SUMMER;
            }

            @Override
            public int getBadgeItemOffsetY() {
                return 8;
            }

            @Override
            public int getFillWidth() {
                return 127;
            }

            @Override
            public int getLevelDisplayY() {
                return this.getFrameHeight() - 3;
            }
        });
        Instance.registerResourceGui(CastingResourceIDs.WINTER_ICE, new ICastingResourceGuiProvider() {

            @Override
            public ResourceLocation getTexture() {
                return GuiTextures.Hud.BARS;
            }

            @Override
            public int getXPBarColor() {
                return -10390889;
            }

            @Override
            public int getBarColor() {
                return -4924696;
            }

            @Override
            public int getBarManaCostEstimateColor() {
                return -8482116;
            }

            @Override
            public int getResourceNumericTextColor() {
                return -14667956;
            }

            @Override
            public int getBadgeSize() {
                return 64;
            }

            @Override
            public int getFrameU() {
                return 0;
            }

            @Override
            public int getFrameWidth() {
                return 153;
            }

            @Override
            public int getFrameHeight() {
                return 24;
            }

            @Override
            public int getFrameV() {
                return 72;
            }

            @Override
            public ItemStack getBadgeItem() {
                return CastingResourceGuiRegistry.HUD_FEY_WINTER;
            }

            @Override
            public int getBadgeItemOffsetY() {
                return 9;
            }

            @Override
            public int getFillWidth() {
                return 127;
            }

            @Override
            public int getLevelDisplayY() {
                return this.getFrameHeight() - 1;
            }
        });
    }

    @Override
    public void registerResourceGui(ResourceLocation identifier, ICastingResourceGuiProvider guiProvider) {
        if (identifier != null) {
            if (this._guiProviders.containsKey(identifier)) {
                throw new InvalidParameterException("The Casting Resource Identifier " + identifier.toString() + " is already in use.");
            } else if (CastingResourceRegistry.Instance.getRegisteredClass(identifier) == null) {
                throw new InvalidParameterException("The Casting Resource Identifier " + identifier.toString() + " was not registered in the CastingResourceRegistrationEvent event.  Subscribe to this and map the resource to a class.");
            } else {
                this._guiProviders.put(identifier, guiProvider);
            }
        }
    }

    public ICastingResourceGuiProvider getGuiProvider(ResourceLocation key) {
        return (ICastingResourceGuiProvider) this._guiProviders.getOrDefault(key, (ICastingResourceGuiProvider) this._guiProviders.get(CastingResourceIDs.MANA));
    }
}