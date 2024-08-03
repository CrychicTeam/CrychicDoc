package com.mna.gui.block;

import com.google.common.collect.Lists;
import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.faction.IFaction;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.sound.SFX;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerOcculus;
import com.mna.items.ItemInit;
import com.mna.network.ClientMessageDispatcher;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.progression.ProgressionCondition;
import com.mna.recipes.spells.ISpellComponentRecipe;
import com.mna.tools.render.GuiRenderUtils;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.IForgeRegistry;

public class GuiOcculus extends AbstractContainerScreen<ContainerOcculus> {

    private static final int ICON_WIDTH = 50;

    private static final int ICON_HEIGHT = 40;

    private static final int ICON_SPACING = 10;

    private static final int ICON_SHEET_SIZE = 256;

    float backgroundPositionX = 0.0F;

    float backgroundPositionY = 0.0F;

    int backgroundWidth = 1024;

    int backgroundHeight = 1024;

    int categoryButtonOffset = 0;

    int startTooltipY = 52;

    int tooltipYPos = 52;

    int minTooltipY = this.startTooltipY;

    float backgroundPositionXMax = 1000.0F;

    HashMap<GuiOcculus.IconTypes, GuiOcculus.OcculusEntryList> lists;

    GuiOcculus.OcculusEntryList currentList;

    List<Component> currentTooltip = new ArrayList();

    ArrayList<GuiOcculus.TextFormatData> currentProgressionTooltip = new ArrayList();

    IPlayerProgression progression;

    IPlayerMagic magic;

    IPlayerRoteSpells rote;

    GuiOcculus.TextFormatData tierConditions = null;

    public GuiOcculus(ContainerOcculus screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
        this.lists = new HashMap();
    }

    @Override
    protected void init() {
        super.init();
        ClientMessageDispatcher.sendRequestWellspringNetworkSyncMessage(false);
        this.m_169413_();
        this.categoryButtonOffset = 0;
        Minecraft mc = Minecraft.getInstance();
        RecipeManager recipeManager = mc.level.getRecipeManager();
        List<GuiOcculus.ListEntry<AMRecipeBase>> spell_parts = new ArrayList();
        this.getRecipes(recipeManager, GuiOcculus.IconTypes.SHAPE, RecipeInit.SHAPE_TYPE.get(), spell_parts, null);
        this.getRecipes(recipeManager, GuiOcculus.IconTypes.COMPONENT, RecipeInit.COMPONENT_TYPE.get(), spell_parts, null);
        this.getRecipes(recipeManager, GuiOcculus.IconTypes.MODIFIER, RecipeInit.MODIFIER_TYPE.get(), spell_parts, null);
        this.setupTierBadges(GuiOcculus.IconTypes.PROGRESSION, new ArrayList(), new ItemStack(ItemInit.GUIDE_BOOK.get()).setHoverName(Component.translatable("mechanic.mna:progression")));
        this.setupTierBadges(GuiOcculus.IconTypes.FACTION_AFFINITY, new ArrayList(), new ItemStack(ItemInit.MOTE_ARCANE.get()).setHoverName(Component.translatable("mechanic.mna:faction_affinity")));
        this.setupTierBadges(GuiOcculus.IconTypes.SHAPE, spell_parts, new ItemStack(ItemInit.VELLUM.get()).setHoverName(Component.translatable("mechanic.mna:sorcery")));
        this.setupTierBadges(GuiOcculus.IconTypes.RITUAL, this.getRecipes(recipeManager, GuiOcculus.IconTypes.RITUAL, RecipeInit.RITUAL_TYPE.get(), null), new ItemStack(ItemInit.WIZARD_CHALK.get()).setHoverName(Component.translatable("mechanic.mna:rituals")));
        this.setupTierBadges(GuiOcculus.IconTypes.MANAWEAVE_PATTERN, this.getRecipes(recipeManager, GuiOcculus.IconTypes.MANAWEAVE_PATTERN, RecipeInit.MANAWEAVING_PATTERN_TYPE.get(), null), new ItemStack(ItemInit.MANAWEAVER_WAND.get()).setHoverName(Component.translatable("mechanic.mna:manaweave_patterns")));
        this.setupTierBadges(GuiOcculus.IconTypes.MANAWEAVE_ALTAR, this.getRecipes(recipeManager, GuiOcculus.IconTypes.MANAWEAVE_ALTAR, RecipeInit.MANAWEAVING_RECIPE_TYPE.get(), r -> !r.isEnchantment() && !(r.getResultItem().getItem() instanceof ItemConstructPart)), new ItemStack(BlockInit.MANAWEAVING_ALTAR.get()).setHoverName(Component.translatable("mechanic.mna:manaweave_crafting")));
        this.setupTierBadges(GuiOcculus.IconTypes.ELDRIN_ALTAR, this.getRecipes(recipeManager, GuiOcculus.IconTypes.ELDRIN_ALTAR, RecipeInit.ELDRIN_ALTAR_TYPE.get(), r -> !(r.getResultItem().getItem() instanceof ItemConstructPart)), new ItemStack(BlockInit.ELDRIN_ALTAR.get()).setHoverName(Component.translatable("mechanic.mna:eldrin_altar")));
        this.setupTierBadges(GuiOcculus.IconTypes.RUNESCRIBING, this.getRecipes(recipeManager, GuiOcculus.IconTypes.RUNESCRIBING, RecipeInit.RUNESCRIBING_TYPE.get(), null), new ItemStack(BlockInit.RUNESCRIBING_TABLE.get()).setHoverName(Component.translatable("mechanic.mna:runescribing")));
        this.setupTierBadges(GuiOcculus.IconTypes.RUNIC_ANVIL, this.getRecipes(recipeManager, GuiOcculus.IconTypes.RUNIC_ANVIL, RecipeInit.RUNEFORGING_TYPE.get(), null), new ItemStack(BlockInit.RUNIC_ANVIL.get()).setHoverName(Component.translatable("mechanic.mna:runesmithing")));
        List<GuiOcculus.ListEntry<AMRecipeBase>> construct_recipes = this.getRecipes(recipeManager, GuiOcculus.IconTypes.CONSTRUCTS, RecipeInit.MANAWEAVING_RECIPE_TYPE.get(), r -> !r.isEnchantment() && r.getResultItem().getItem() instanceof ItemConstructPart);
        construct_recipes.addAll(this.getRecipes(recipeManager, GuiOcculus.IconTypes.CONSTRUCTS, RecipeInit.ELDRIN_ALTAR_TYPE.get(), r -> r.getResultItem().getItem() instanceof ItemConstructPart));
        this.setupTierBadges(GuiOcculus.IconTypes.CONSTRUCTS, construct_recipes, new ItemStack(ItemInit.CONSTRUCT_BASIC_HEAD_OBSIDIAN.get()).setHoverName(Component.translatable("mechanic.mna:constructs")));
        this.setupTierBadges(GuiOcculus.IconTypes.ENCHANTMENT, this.getRecipes(recipeManager, GuiOcculus.IconTypes.ENCHANTMENT, RecipeInit.MANAWEAVING_RECIPE_TYPE.get(), r -> r.isEnchantment()), new ItemStack(Items.ENCHANTED_BOOK).setHoverName(Component.translatable("mechanic.mna:enchantments")));
        this.finalizeLists();
        this.setActiveList(GuiOcculus.IconTypes.PROGRESSION);
        this.setActiveButton((Button) this.m_6702_().stream().filter(e -> e instanceof GuiOcculus.ImageItemStackButton).map(e -> (GuiOcculus.ImageItemStackButton) e).findFirst().get());
        this.progression = (IPlayerProgression) this.f_96541_.player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        this.magic = (IPlayerMagic) this.f_96541_.player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        this.rote = (IPlayerRoteSpells) this.f_96541_.player.getCapability(PlayerRoteSpellsProvider.ROTE).orElse(null);
        this.currentProgressionTooltip.clear();
        if (this.progression != null) {
            List<ProgressionCondition> conditions = ProgressionCondition.get(this.f_96541_.level, this.progression.getTier(), new ArrayList());
            Collections.sort(conditions, new Comparator<ProgressionCondition>() {

                public int compare(ProgressionCondition o1, ProgressionCondition o2) {
                    MutableComponent ttc1 = Component.translatable(o1.m_6423_().toString());
                    MutableComponent ttc2 = Component.translatable(o2.m_6423_().toString());
                    return ttc1.getString().compareTo(ttc2.getString());
                }
            });
            List<ResourceLocation> completedConditions = this.progression.getCompletedProgressionSteps();
            this.tierConditions = this.progression.getTier() < 5 ? new GuiOcculus.TextFormatData(Component.translatable("gui.mna.tier_progress", ProgressionCondition.getCompletionRequirementForTier(this.f_96541_.level, this.progression.getTier()), conditions.size()), ChatFormatting.WHITE.getColor()) : null;
            for (ProgressionCondition cond : conditions) {
                if (completedConditions.contains(cond.m_6423_())) {
                    this.currentProgressionTooltip.add(new GuiOcculus.TextFormatData(cond.getDescription(), ChatFormatting.GREEN.getColor()));
                } else {
                    this.currentProgressionTooltip.add(new GuiOcculus.TextFormatData(cond.getDescription(), ChatFormatting.RED.getColor()));
                }
            }
            if (this.progression.getTierProgress(this.f_96541_.level) >= 1.0F) {
                int advancementColor = ChatFormatting.YELLOW.getColor();
                this.currentProgressionTooltip.add(new GuiOcculus.TextFormatData(Component.literal(""), ChatFormatting.WHITE.getColor()));
                int tier = this.progression.getTier();
                if (tier == 1) {
                    this.currentProgressionTooltip.add(new GuiOcculus.TextFormatData(Component.translatable("mna:progresscondition.t1_complete"), advancementColor));
                } else if (tier == 2) {
                    this.currentProgressionTooltip.add(new GuiOcculus.TextFormatData(Component.translatable("mna:progresscondition.t2_complete"), advancementColor));
                    ((IForgeRegistry) Registries.Factions.get()).forEach(f -> this.currentProgressionTooltip.add(new GuiOcculus.TextFormatData(f.getOcculusTaskPrompt(tier), advancementColor)));
                } else {
                    this.currentProgressionTooltip.add(new GuiOcculus.TextFormatData(Component.translatable("mna:progresscondition.t3+_complete"), advancementColor));
                    this.currentProgressionTooltip.add(new GuiOcculus.TextFormatData(this.progression.getAlliedFaction().getOcculusTaskPrompt(tier), advancementColor));
                }
            }
        }
    }

    private <T extends AMRecipeBase> List<GuiOcculus.ListEntry<AMRecipeBase>> getRecipes(RecipeManager recipeManager, GuiOcculus.IconTypes iconType, RecipeType<T> recipeType, Function<T, Boolean> predicate) {
        return this.getRecipes(recipeManager, iconType, recipeType, null, predicate);
    }

    private <T extends AMRecipeBase> List<GuiOcculus.ListEntry<AMRecipeBase>> getRecipes(RecipeManager recipeManager, GuiOcculus.IconTypes iconType, RecipeType<T> recipeType, @Nullable List<GuiOcculus.ListEntry<AMRecipeBase>> appendTo, @Nullable Function<T, Boolean> predicate) {
        List<T> recipes = recipeManager.getAllRecipesFor(recipeType);
        if (appendTo == null) {
            appendTo = new ArrayList();
        }
        for (T recipe : recipes) {
            if (predicate == null || (Boolean) predicate.apply(recipe)) {
                appendTo.add(new GuiOcculus.ListEntry(recipe, iconType));
            }
        }
        return appendTo;
    }

    private void setupViewButton(GuiOcculus.IconTypes iconType, ItemStack displayStack) {
        this.m_142416_(new GuiOcculus.ImageItemStackButton(this.f_97735_ - 22, this.f_97736_ + this.categoryButtonOffset, 22, 22, 51, 232, -22, GuiTextures.Blocks.OCCULUS_BORDER, 256, 256, button -> {
            this.setActiveList(iconType);
            this.setActiveButton(button);
        }, displayStack, this.f_96541_.getItemRenderer(), false));
        this.categoryButtonOffset += 25;
    }

    private void setupTierBadges(GuiOcculus.IconTypes iconType, List<GuiOcculus.ListEntry<AMRecipeBase>> recipes, ItemStack displayStack) {
        GuiOcculus.OcculusEntryList oel = new GuiOcculus.OcculusEntryList(this.f_96541_, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_);
        this.lists.put(iconType, oel);
        Collections.sort(recipes, new Comparator<GuiOcculus.ListEntry<AMRecipeBase>>() {

            public int compare(GuiOcculus.ListEntry<AMRecipeBase> o1, GuiOcculus.ListEntry<AMRecipeBase> o2) {
                Integer tier1 = o1.getTier();
                Integer tier2 = o2.getTier();
                int tierComp = tier1.compareTo(tier2);
                if (tierComp != 0) {
                    return tierComp;
                } else {
                    String name1 = o1.getRecipe().getGuiRepresentationStack().getHoverName().getString();
                    String name2 = o2.getRecipe().getGuiRepresentationStack().getHoverName().getString();
                    return name1.compareTo(name2);
                }
            }
        });
        for (GuiOcculus.ListEntry<? extends AMRecipeBase> entry : recipes) {
            oel.addIcon(entry.getRecipe(), entry.getIcon());
        }
        this.m_7787_(oel);
        this.setupViewButton(iconType, displayStack);
    }

    private void finalizeLists() {
        for (GuiOcculus.OcculusEntryList list : this.lists.values()) {
            list.finalize();
        }
    }

    private void setActiveList(GuiOcculus.IconTypes type) {
        this.currentList = (GuiOcculus.OcculusEntryList) this.lists.get(type);
        this.currentList.m_93410_(0.0);
    }

    private void setActiveButton(Button btn) {
        this.m_6702_().stream().forEach(l -> {
            if (l instanceof GuiOcculus.ImageItemStackButton) {
                ((GuiOcculus.ImageItemStackButton) l).setCurrent(l == btn);
            }
        });
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        return mouseX < (double) guiLeftIn || mouseY < (double) guiTopIn || mouseX >= (double) (guiLeftIn + this.f_97726_) || mouseY >= (double) (guiTopIn + this.f_97727_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX < (double) this.f_97735_) {
            for (GuiEventListener b : this.m_6702_()) {
                if (b instanceof GuiOcculus.ImageItemStackButton && b.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTick, int pMouseX, int pMouseY) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        if (this.progression == null || this.currentList != this.lists.get(GuiOcculus.IconTypes.PROGRESSION) && this.currentList != this.lists.get(GuiOcculus.IconTypes.FACTION_AFFINITY)) {
            pGuiGraphics.blit(GuiTextures.Blocks.OCCULUS_BACKGROUND, i + 2, j + 2, this.backgroundPositionX, this.backgroundPositionY, this.f_97726_ - 4, this.f_97727_ - 4, 100, 100);
            this.backgroundPositionX = (float) ((double) this.backgroundPositionX + Math.cos((double) ((float) this.f_96541_.level.m_46467_() / 200.0F)) * 0.2F * (double) partialTick);
            this.backgroundPositionY = (float) ((double) this.backgroundPositionY - Math.sin((double) ((float) this.f_96541_.level.m_46467_() / 200.0F)) * 0.2F * (double) partialTick);
        } else {
            pGuiGraphics.fillGradient(i + 2, j + 2, i + this.f_97726_, j + this.f_97727_, FastColor.ARGB32.color(255, 34, 0, 61), FastColor.ARGB32.color(255, 0, 0, 0));
        }
        if (this.currentList != null) {
            this.currentList.render(pGuiGraphics, pMouseX, pMouseY, partialTick);
        }
        if (this.progression != null) {
            int height = 210;
            pGuiGraphics.blit(GuiTextures.Blocks.OCCULUS_BORDER, i + this.f_97726_, j, 232.0F, 0.0F, 11, height, 256, 256);
            float fillPct = this.progression.getTierProgress(ManaAndArtifice.instance.proxy.getClientWorld());
            pGuiGraphics.blit(GuiTextures.Blocks.OCCULUS_BORDER, i + this.f_97726_, (int) ((float) j + (float) height * (1.0F - fillPct)), 221.0F, 0.0F, 11, (int) ((float) height * fillPct), 256, 256);
            pGuiGraphics.blit(GuiTextures.Blocks.OCCULUS_BORDER, i + this.f_97726_, j, 210.0F, 0.0F, 11, this.f_97727_, 256, 256);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int i = 0;
        int j = 0;
        int tooltipYStart = 30;
        int tooltipX = 10;
        int tooltipY = this.tooltipYPos;
        int maxWidth = this.f_97726_ - 20;
        int totalTooltipHeight = 0;
        if (this.progression != null && this.currentList == this.lists.get(GuiOcculus.IconTypes.PROGRESSION)) {
            String gui_player_tier = I18n.get("gui.mna.your_tier", this.progression.getTier());
            pGuiGraphics.drawString(this.f_96547_, gui_player_tier, 10, 10, 16777215, false);
            if (this.tierConditions != null) {
                for (FormattedText line : this.f_96547_.getSplitter().splitLines(this.tierConditions.itc.getString(), maxWidth, Style.EMPTY)) {
                    pGuiGraphics.drawString(this.f_96547_, line.getString(), tooltipX, tooltipYStart, this.tierConditions.color, false);
                    tooltipYStart += 9;
                }
            } else {
                tooltipYStart += 9 * 2;
            }
            pGuiGraphics.blit(GuiTextures.Blocks.OCCULUS_BORDER, i, j + tooltipYStart, 0.0F, 0.0F, this.f_97726_, 2, 310, 312);
            if (this.progression.getTier() >= 5) {
                pGuiGraphics.drawString(this.f_96547_, Component.translatable("gui.mna.your_tier_max").getString(), tooltipX, tooltipY, ChatFormatting.WHITE.getColor(), false);
            } else {
                for (GuiOcculus.TextFormatData tfd : this.currentProgressionTooltip) {
                    for (FormattedText line : this.f_96547_.getSplitter().splitLines(tfd.itc.getString(), maxWidth, Style.EMPTY)) {
                        if (tooltipY >= tooltipYStart && tooltipY <= this.f_97727_ - 9) {
                            pGuiGraphics.drawString(this.f_96547_, line.getString(), tooltipX, tooltipY, tfd.color, false);
                        }
                        tooltipY += 9;
                        totalTooltipHeight += 9;
                    }
                    tooltipY += 2;
                    totalTooltipHeight += 2;
                }
            }
        } else if (this.progression != null && this.currentList == this.lists.get(GuiOcculus.IconTypes.FACTION_AFFINITY)) {
            int y = 10;
            int x = 10;
            String gui_player_faction = I18n.get("gui.mna.your_faction");
            pGuiGraphics.drawString(this.f_96547_, gui_player_faction, x, y, 16777215, false);
            x += this.f_96547_.width(gui_player_faction) + 3;
            if (this.progression.hasAlliedFaction()) {
                IFaction faction = this.progression.getAlliedFaction();
                pGuiGraphics.blit(faction.getFactionIcon(), x, y, 0.0F, 0.0F, faction.getFactionIconTextureSize(), faction.getFactionIconTextureSize(), faction.getFactionIconTextureSize(), faction.getFactionIconTextureSize());
            } else {
                gui_player_faction = I18n.get("gui.mna.your_faction.none");
                pGuiGraphics.drawString(this.f_96547_, gui_player_faction, x, y, 16777215, false);
            }
            int var20 = 30;
            int var24 = 10;
            String gui_player_affinity = I18n.get("gui.mna.affinity");
            pGuiGraphics.drawString(this.f_96547_, gui_player_affinity, var24, var20, 16777215, false);
            var20 += 9;
            for (Affinity aff : Affinity.values()) {
                ItemStack affStack = (ItemStack) GuiTextures.affinityIcons.get(aff);
                if (!affStack.isEmpty()) {
                    pGuiGraphics.renderItem(affStack, var24, var20);
                    var24 += 20;
                    pGuiGraphics.drawString(this.f_96547_, String.format("%.1f%%", this.magic.getAffinityDepth(aff)), var24, var20 + 5, 16777215, false);
                    var24 = 10;
                    var20 += 20;
                }
            }
            this.f_96541_.level.getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                int posY = 30;
                int posX = this.getXSize() - 30;
                String gui_power_network = I18n.get("gui.mna.power_network");
                pGuiGraphics.drawString(this.f_96547_, gui_power_network, posX - this.f_96547_.width(gui_power_network) + 16, posY, 16777215, false);
                posY += 9;
                if (ManaAndArtifice.instance.proxy.getGameTicks() % 20L == 0L) {
                    ClientMessageDispatcher.sendRequestWellspringNetworkSyncMessage(false);
                }
                HashMap<Affinity, Float> nodeStrengths = m.getWellspringRegistry().getNodeNetworkStrengthFor(this.f_96541_.player);
                HashMap<Affinity, Float> nodeAmounts = m.getWellspringRegistry().getNodeNetworkAmountFor(this.f_96541_.player);
                for (Affinity affx : Affinity.values()) {
                    ItemStack affStackx = (ItemStack) GuiTextures.affinityIcons.get(affx);
                    if (!affStackx.isEmpty() && nodeStrengths.containsKey(affx)) {
                        pGuiGraphics.renderItem(affStackx, posX, posY);
                        String value = String.format("(%.1f)", nodeStrengths.get(affx));
                        int valueWidth = this.f_96547_.width(value);
                        pGuiGraphics.drawString(this.f_96547_, value, posX - valueWidth - 5, posY + 5, 16777215, false);
                        if (nodeAmounts.containsKey(affx)) {
                            String amtvalue = String.format("%.1f", nodeAmounts.get(affx));
                            int amtValueWidth = this.f_96547_.width(amtvalue);
                            pGuiGraphics.drawString(this.f_96547_, amtvalue, posX - valueWidth - amtValueWidth - 10, posY + 5, 16777215, false);
                        }
                        posY += 20;
                    }
                }
            });
        }
        pGuiGraphics.blit(GuiTextures.Blocks.OCCULUS_BORDER, i, j, 0.0F, 0.0F, this.f_97726_, this.f_97727_, 310, 312);
        if (totalTooltipHeight <= this.tooltipYPos - tooltipY + this.f_97727_ - tooltipYStart + 20) {
            this.minTooltipY = 52;
        } else {
            this.minTooltipY = this.tooltipYPos - tooltipY + this.f_97727_ - tooltipYStart + 20;
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.currentTooltip.clear();
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (!this.currentTooltip.isEmpty()) {
            pGuiGraphics.renderTooltip(this.f_96547_, Lists.transform(this.currentTooltip, Component::m_7532_), pMouseX, pMouseY);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.backgroundPositionX = (float) ((double) this.backgroundPositionX - deltaX);
        this.backgroundPositionY = (float) ((double) this.backgroundPositionY - deltaY);
        for (GuiOcculus.OcculusEntryList list : this.lists.values()) {
            list.m_6050_(mouseX, mouseY, deltaY / 20.0);
        }
        if (this.currentList == this.lists.get(GuiOcculus.IconTypes.PROGRESSION)) {
            this.tooltipYPos = this.tooltipYPos + (int) Math.ceil(deltaY);
            if (this.tooltipYPos > 52) {
                this.tooltipYPos = 52;
            }
            if (this.tooltipYPos < this.minTooltipY) {
                this.tooltipYPos = this.minTooltipY;
            }
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        for (GuiOcculus.OcculusEntryList list : this.lists.values()) {
            list.m_6050_(mouseX, mouseY, delta);
        }
        if (this.currentList == this.lists.get(GuiOcculus.IconTypes.PROGRESSION)) {
            this.tooltipYPos = this.tooltipYPos + (int) Math.ceil(delta) * 10;
            if (this.tooltipYPos > 52) {
                this.tooltipYPos = 52;
            }
            if (this.tooltipYPos < this.minTooltipY) {
                this.tooltipYPos = this.minTooltipY;
            }
        }
        return true;
    }

    static enum IconTypes {

        PROGRESSION(0, 0),
        FACTION_AFFINITY(0, 0),
        MANAWEAVE_PATTERN(0, 0),
        MANAWEAVE_ALTAR(50, 0),
        ELDRIN_ALTAR(50, 0),
        SHAPE(100, 0),
        COMPONENT(150, 0),
        MODIFIER(200, 0),
        RUNESCRIBING(0, 80),
        RUNIC_ANVIL(50, 80),
        ENCHANTMENT(100, 80),
        RITUAL(150, 80),
        CONSTRUCTS(200, 80);

        private int texStartX;

        private int texStartY;

        private IconTypes(int texStartX, int texStartY) {
            this.texStartX = texStartX;
            this.texStartY = texStartY;
        }

        public int getTexX() {
            return this.texStartX;
        }

        public int getTexY() {
            return this.texStartY;
        }
    }

    public class ImageItemStackButton extends ImageButton {

        final ItemStack iconStack;

        final ItemRenderer itemRenderer;

        final boolean includeTooltip;

        private boolean current;

        public ImageItemStackButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int hoverOffset, ResourceLocation textureFile, int texWidth, int texHeight, Button.OnPress clickHandler, ItemStack displayStack, ItemRenderer itemRenderer) {
            this(x, y, width, height, xTexStart, yTexStart, hoverOffset, textureFile, texWidth, texHeight, clickHandler, displayStack, itemRenderer, true);
        }

        public ImageItemStackButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int hoverOffset, ResourceLocation textureFile, int texWidth, int texHeight, Button.OnPress clickHandler, ItemStack displayStack, ItemRenderer itemRenderer, boolean displayFullTooltip) {
            super(x, y, width, height, xTexStart, yTexStart, hoverOffset, textureFile, texWidth, texHeight, clickHandler, displayStack.getHoverName());
            this.iconStack = displayStack;
            this.itemRenderer = itemRenderer;
            this.includeTooltip = displayFullTooltip;
        }

        @Override
        public boolean isHoveredOrFocused() {
            return this.current;
        }

        public void setCurrent(boolean current) {
            this.current = current;
        }

        @Override
        protected boolean clicked(double mouseX, double mouseY) {
            return super.m_93680_(mouseX, mouseY);
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            pGuiGraphics.renderItem(this.iconStack, this.m_252754_() + 3, this.m_252907_() + 3);
            if (this.f_93623_ && this.f_93622_) {
                List<Component> lines;
                if (this.includeTooltip) {
                    lines = this.iconStack.getTooltipLines(GuiOcculus.this.f_96541_.player, TooltipFlag.Default.f_256752_);
                } else {
                    lines = new ArrayList();
                    lines.add(this.iconStack.getHoverName());
                }
                List<Component> tt = new ArrayList();
                for (Component comp : lines) {
                    if (!this.includeTooltip) {
                        String unformatted = ChatFormatting.stripFormatting(comp.getString());
                        MutableComponent stc = Component.literal(unformatted);
                        stc.withStyle(ChatFormatting.ITALIC);
                        tt.add(stc);
                    } else {
                        tt.add(comp);
                    }
                }
                GuiOcculus.this.currentTooltip = tt;
            }
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            soundHandler.play(SimpleSoundInstance.forUI(SFX.Gui.PAGE_FLIP, (float) (0.8 + Math.random() * 0.4)));
        }
    }

    class ListEntry<T extends AMRecipeBase> {

        private T recipe;

        private GuiOcculus.IconTypes iconType;

        public ListEntry(T recipe, GuiOcculus.IconTypes icon) {
            this.recipe = recipe;
            this.iconType = icon;
        }

        public int getTier() {
            return this.recipe.getTier();
        }

        public GuiOcculus.IconTypes getIcon() {
            return this.iconType;
        }

        public T getRecipe() {
            return this.recipe;
        }
    }

    class OcculusEntryList extends ObjectSelectionList<GuiOcculus.OcculusEntryList.OcculusEntry> {

        private int x;

        private GuiOcculus.OcculusEntryList.OcculusEntry current;

        public <T extends AMRecipeBase> OcculusEntryList(Minecraft mcIn, int x, int y, int widthIn, int heightIn) {
            super(mcIn, widthIn, heightIn, y, y + heightIn, 50);
            this.x = x;
            this.m_93471_(false);
            this.m_93473_(false, 50);
        }

        public <T extends AMRecipeBase> void addIcon(T recipe, GuiOcculus.IconTypes type) {
            if (this.current == null || this.current.countIcons() == 4 || recipe.getTier() > this.current.getTier()) {
                if (recipe instanceof ISpellComponentRecipe) {
                    this.current = new GuiOcculus.OcculusEntryList.OcculusSpellEntry(recipe.getTier());
                } else if (recipe instanceof ManaweavingPattern) {
                    this.current = new GuiOcculus.OcculusEntryList.OcculusManaweaveEntry(recipe.getTier());
                } else {
                    this.current = new GuiOcculus.OcculusEntryList.OcculusEntry(recipe.getTier());
                }
                this.m_7085_(this.current);
            }
            if (this.current.getTier() > recipe.getTier()) {
                throw new InvalidParameterException("Cannot add a recipe for a lower tier.  Sort recipes ascending by tier for this to work.");
            } else {
                this.current.addOutputItem(recipe, type);
            }
        }

        public void finalize() {
            for (GuiOcculus.OcculusEntryList.OcculusEntry e : this.m_6702_()) {
                e.finalize();
            }
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            int xPos = this.getRowLeft();
            int yPos = this.f_93390_ - (int) this.m_93517_();
            this.renderList(pGuiGraphics, xPos, yPos, pPartialTick);
            int lastTier = 0;
            int tierItemCount = 0;
            for (Iterator var9 = this.m_6702_().iterator(); var9.hasNext(); yPos += 50) {
                for (GuiOcculus.OcculusEntryList.OcculusEntry entry = (GuiOcculus.OcculusEntryList.OcculusEntry) var9.next(); lastTier < entry.getTier(); yPos += 10) {
                    lastTier++;
                    if (yPos + 15 > this.f_93390_ && yPos + 15 + 9 < this.f_93391_) {
                        String tierStr = String.format("Tier %d", lastTier);
                        if (tierItemCount == 0 && lastTier < entry.getTier()) {
                            tierStr = tierStr + " (No Items)";
                        }
                        pGuiGraphics.drawString(this.f_93386_.font, tierStr, xPos, yPos + 15, 16777215, false);
                    }
                    tierItemCount = 0;
                }
                tierItemCount++;
            }
        }

        @Override
        protected void renderList(GuiGraphics pGuiGraphics, int p_238478_4_, int p_238478_5_, float partialTicks) {
            int count = this.m_5773_();
            for (int i = 0; i < count; i++) {
                int top = this.getRowTop(i);
                int bottom = top + this.f_93387_;
                if (top >= this.f_93390_ && bottom <= this.f_93391_) {
                    int j1 = this.f_93387_ - 4;
                    GuiOcculus.OcculusEntryList.OcculusEntry e = (GuiOcculus.OcculusEntryList.OcculusEntry) this.m_93500_(i);
                    e.render(pGuiGraphics, i, top, this.getRowLeft(), this.getRowWidth(), j1, p_238478_4_, p_238478_5_, false, partialTicks);
                }
            }
        }

        @Override
        public int getRowWidth() {
            return this.f_93388_ - 40;
        }

        @Override
        public int getRowLeft() {
            return this.x + 12;
        }

        @Override
        protected int getRowTop(int p_230962_1_) {
            GuiOcculus.OcculusEntryList.OcculusEntry oe = (GuiOcculus.OcculusEntryList.OcculusEntry) this.m_6702_().get(p_230962_1_);
            return this.f_93390_ + 15 - (int) this.m_93517_() + p_230962_1_ * this.f_93387_ + oe.getTier() * 10;
        }

        @Override
        protected int getMaxPosition() {
            return (this.m_5773_() + 1) * this.f_93387_ + 50;
        }

        public class OcculusEntry extends ObjectSelectionList.Entry<GuiOcculus.OcculusEntryList.OcculusEntry> {

            private static final int MAX_ITEMS = 4;

            private ArrayList<ItemStack> outputItems;

            private ArrayList<AMRecipeBase> outputRecipes;

            private ArrayList<GuiOcculus.IconTypes> iconTypes;

            protected ItemRenderer itemRenderer = OcculusEntryList.this.f_93386_.getItemRenderer();

            protected Font fontRenderer = OcculusEntryList.this.f_93386_.font;

            protected int tier;

            public OcculusEntry(int tier) {
                this.outputItems = new ArrayList();
                this.outputRecipes = new ArrayList();
                this.iconTypes = new ArrayList();
                this.tier = tier;
            }

            public void addOutputItem(AMRecipeBase recipe, GuiOcculus.IconTypes type) {
                if (this.outputItems.size() != 4) {
                    if (!recipe.getGuiRepresentationStack().isEmpty()) {
                        this.outputItems.add(recipe.getGuiRepresentationStack());
                        this.outputRecipes.add(recipe);
                        this.iconTypes.add(type);
                    }
                }
            }

            public void finalize() {
                while (this.outputItems.size() < 4) {
                    this.outputItems.add(ItemStack.EMPTY);
                }
            }

            public int countIcons() {
                return this.outputItems.size();
            }

            public int getTier() {
                return this.tier;
            }

            protected IFaction getFaction(int index) {
                return index < this.outputRecipes.size() && this.outputRecipes.get(index) != null ? ((AMRecipeBase) this.outputRecipes.get(index)).getFactionRequirement() : null;
            }

            @Override
            public void render(GuiGraphics pGuiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovered, float partialTicks) {
                for (int i = 0; i < 4 && !((ItemStack) this.outputItems.get(i)).isEmpty(); i++) {
                    if (GuiOcculus.this.progression == null) {
                        return;
                    }
                    boolean tierMet = this.tier <= GuiOcculus.this.progression.getTier();
                    if (!tierMet) {
                        pGuiGraphics.setColor(0.5F, 0.0F, 0.0F, 1.0F);
                    } else {
                        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                    }
                    this.renderBackground(pGuiGraphics, left + i * 58, top, (GuiOcculus.IconTypes) this.iconTypes.get(i), i, tierMet);
                    this.renderForeground(pGuiGraphics, left + i * 58, top, partialTicks, (ItemStack) this.outputItems.get(i), i);
                }
            }

            protected void renderBackground(GuiGraphics pGuiGraphics, int x, int y, GuiOcculus.IconTypes type, int index, boolean tierMet) {
                pGuiGraphics.blit(GuiTextures.Blocks.OCCULUS_ICONS, x + 2, y + 2, (float) type.getTexX(), (float) type.getTexY(), 50, 40, 256, 256);
                IFaction faction = this.getFaction(index);
                if (faction != null) {
                    pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                    pGuiGraphics.blit(faction.getFactionIcon(), x + 50 - faction.getFactionIconTextureSize(), y + 40 - faction.getFactionIconTextureSize(), 0.0F, 0.0F, faction.getFactionIconTextureSize(), faction.getFactionIconTextureSize(), faction.getFactionIconTextureSize(), faction.getFactionIconTextureSize());
                    if (!tierMet) {
                        pGuiGraphics.setColor(0.5F, 0.0F, 0.0F, 1.0F);
                    }
                }
            }

            public void renderForeground(GuiGraphics pGuiGraphics, int x, int y, float partialTick, ItemStack stack, int index) {
                pGuiGraphics.renderItem(stack, x + 19, y + 3);
                this.renderTextLines(pGuiGraphics, stack, x, y);
            }

            protected void renderTextLines(GuiGraphics pGuiGraphics, ItemStack stack, int x, int y) {
                List<Component> tooltip = new ArrayList();
                tooltip.add(stack.getHoverName());
                if (GuiOcculus.this.progression != null) {
                    float scale = 0.7F;
                    pGuiGraphics.pose().pushPose();
                    pGuiGraphics.pose().scale(scale, scale, 1.0F);
                    int yCoord = y + 22;
                    for (Component tc : tooltip) {
                        for (FormattedText line : this.fontRenderer.getSplitter().splitLines(tc, (int) (47.0F / scale), Style.EMPTY)) {
                            int len = (int) ((float) this.fontRenderer.width(line.getString()) * scale);
                            int xCoord = x + 2 + 25 - len / 2;
                            pGuiGraphics.drawString(OcculusEntryList.this.f_93386_.font, line.getString(), (int) ((float) xCoord / scale), (int) ((float) yCoord / scale), 16777215, false);
                            yCoord = (int) ((float) yCoord + 9.0F * scale);
                        }
                    }
                    pGuiGraphics.pose().popPose();
                }
            }

            @Override
            public Component getNarration() {
                return Component.translatable("gui.mna.occulus.select");
            }
        }

        public class OcculusManaweaveEntry extends GuiOcculus.OcculusEntryList.OcculusEntry {

            private ArrayList<ManaweavingPattern> patterns = new ArrayList();

            public OcculusManaweaveEntry(int tier) {
                super(tier);
            }

            @Override
            public void addOutputItem(AMRecipeBase recipe, GuiOcculus.IconTypes type) {
                super.addOutputItem(recipe, type);
                if (recipe instanceof ManaweavingPattern) {
                    this.patterns.add((ManaweavingPattern) recipe);
                }
            }

            @Override
            public void renderForeground(GuiGraphics pGuiGraphics, int x, int y, float partialTick, ItemStack stack, int index) {
                float scale = 0.1F;
                GuiRenderUtils.renderManaweavePattern(pGuiGraphics, (int) ((float) (x + 33) / scale), (int) ((float) (y + 4) / scale), scale, (IManaweavePattern) this.patterns.get(index));
                List<Component> tooltip = new ArrayList();
                tooltip.add(stack.getHoverName());
                if (GuiOcculus.this.progression != null) {
                    scale = 0.7F;
                    pGuiGraphics.pose().pushPose();
                    pGuiGraphics.pose().scale(scale, scale, 1.0F);
                    int yCoord = y + 22;
                    for (Component tc : tooltip) {
                        for (FormattedText line : this.fontRenderer.getSplitter().splitLines(tc, (int) (47.0F / scale), Style.EMPTY)) {
                            int len = (int) ((float) this.fontRenderer.width(line.getString()) * scale);
                            int xCoord = x + 2 + 25 - len / 2;
                            pGuiGraphics.drawString(this.fontRenderer, line.getString(), (int) ((float) xCoord / scale), (int) ((float) yCoord / scale), 16777215, false);
                            yCoord = (int) ((float) yCoord + 9.0F * scale);
                        }
                    }
                    pGuiGraphics.pose().popPose();
                }
            }

            @Override
            public Component getNarration() {
                return Component.translatable("gui.mna.occulus.select");
            }
        }

        public class OcculusSpellEntry extends GuiOcculus.OcculusEntryList.OcculusEntry {

            ArrayList<ISpellComponent> spellParts = new ArrayList();

            public OcculusSpellEntry(int tier) {
                super(tier);
            }

            @Override
            public void addOutputItem(AMRecipeBase recipe, GuiOcculus.IconTypes type) {
                super.addOutputItem(recipe, type);
                if (recipe instanceof ISpellComponentRecipe) {
                    ISpellComponent comp = ((ISpellComponentRecipe) recipe).getComponent();
                    ResourceLocation spellRLoc = comp != null ? comp.getRegistryName() : new ResourceLocation("");
                    if (((IForgeRegistry) Registries.Shape.get()).containsKey(spellRLoc)) {
                        this.spellParts.add((ISpellComponent) ((IForgeRegistry) Registries.Shape.get()).getValue(spellRLoc));
                    } else if (((IForgeRegistry) Registries.SpellEffect.get()).containsKey(spellRLoc)) {
                        this.spellParts.add((ISpellComponent) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(spellRLoc));
                    } else if (((IForgeRegistry) Registries.Modifier.get()).containsKey(spellRLoc)) {
                        this.spellParts.add((ISpellComponent) ((IForgeRegistry) Registries.Modifier.get()).getValue(spellRLoc));
                    }
                }
            }

            @Override
            protected void renderBackground(GuiGraphics pGuiGraphics, int x, int y, GuiOcculus.IconTypes type, int index, boolean tierMet) {
                super.renderBackground(pGuiGraphics, x, y, type, index, tierMet);
                if (index < this.spellParts.size()) {
                    ISpellComponent comp = (ISpellComponent) this.spellParts.get(index);
                    if (comp != null && GuiOcculus.this.rote != null) {
                        int color = GuiOcculus.this.rote.isRote(comp) ? FastColor.ARGB32.color(255, 0, 255, 0) : FastColor.ARGB32.color(255, 237, 230, 9);
                        float prog = GuiOcculus.this.rote.getRoteProgression(comp);
                        if (prog > 0.0F && prog < 0.02F) {
                            prog = 0.02F;
                        }
                        int width = (int) (prog * 50.0F);
                        pGuiGraphics.fill(x + 2, y + 40, x + 2 + width, y + 40 + 2, color);
                    }
                }
            }

            @Override
            public void renderForeground(GuiGraphics pGuiGraphics, int x, int y, float partialTick, ItemStack stack, int index) {
                if (index >= 0 && index < this.spellParts.size()) {
                    ISpellComponent part = (ISpellComponent) this.spellParts.get(index);
                    if (part != null) {
                        pGuiGraphics.blit(part.getGuiIcon(), x + 19, y + 3, 0.0F, 0.0F, 16, 16, 16, 16);
                        if (part.isSilverSpell()) {
                            GuiRenderUtils.renderSilverSpellBorder(pGuiGraphics, x + 19, y + 3, 16, 16);
                        }
                    }
                }
                this.renderTextLines(pGuiGraphics, stack, x, y);
            }

            @Override
            protected IFaction getFaction(int index) {
                return index < this.spellParts.size() && this.spellParts.get(index) != null ? ((ISpellComponent) this.spellParts.get(index)).getFactionRequirement() : null;
            }
        }
    }

    class TextFormatData {

        public Component itc;

        public int color;

        public TextFormatData(Component itc, int color) {
            this.itc = itc;
            this.color = color;
        }
    }
}