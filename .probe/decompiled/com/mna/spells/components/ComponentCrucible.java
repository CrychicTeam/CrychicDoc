package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.factions.Factions;
import com.mna.tools.BlockUtils;
import com.mna.tools.ContainerTools;
import com.mna.tools.EnchantmentUtils;
import com.mna.tools.InventoryUtilities;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;

public class ComponentCrucible extends SpellEffect {

    public ComponentCrucible(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 10.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.isPlayerCaster()) {
            if (target.isBlock()) {
                int harvestLevel = (int) modificationData.getValue(Attribute.MAGNITUDE);
                Tier harvestTier = BlockUtils.tierFromHarvestLevel(harvestLevel - 1);
                if (BlockUtils.canDestroyBlock(source.getCaster(), context.getLevel(), target.getBlock(), harvestTier)) {
                    int fortune_level = EnchantmentUtils.getFortuneLevel(source.getPlayer());
                    Pair<Boolean, Boolean> captureRedirect = InventoryUtilities.getCaptureAndRedirect(source.getPlayer());
                    List<ItemStack> stacks = BlockUtils.destroyBlockCaptureDrops(source.getPlayer(), context.getLevel(), target.getBlock(), false, fortune_level, harvestTier);
                    ArrayList<ItemStack> drops = new ArrayList();
                    for (ItemStack stack : stacks) {
                        drops.add(this.getFurnace(context.getLevel(), stack));
                    }
                    InventoryUtilities.redirectCaptureOrDrop(source.getPlayer(), context.getServerLevel(), drops, (Boolean) captureRedirect.getSecond());
                }
            } else if (target.getLivingEntity() == source.getCaster()) {
                ItemStack offhand = source.getPlayer().m_21120_(source.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                if (!offhand.isEmpty()) {
                    ItemStack output = this.getFurnace(context.getLevel(), offhand);
                    offhand.shrink(1);
                    if (!source.getPlayer().addItem(output)) {
                        source.getPlayer().drop(output, true);
                    }
                }
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    private ItemStack getFurnace(Level level, ItemStack input) {
        CraftingContainer inv = ContainerTools.createTemporaryContainer(input);
        Optional<SmeltingRecipe> furnace_recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inv, level);
        if (furnace_recipe.isPresent()) {
            ItemStack output = ((SmeltingRecipe) furnace_recipe.get()).m_5874_(inv, level.registryAccess());
            if (!output.isEmpty()) {
                return output;
            }
        }
        return input;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.DEMONS;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}