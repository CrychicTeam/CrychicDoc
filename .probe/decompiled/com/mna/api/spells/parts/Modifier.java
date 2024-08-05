package com.mna.api.spells.parts;

import com.google.common.collect.ImmutableList;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.recipes.IMARecipe;
import com.mna.api.spells.SpellCraftingContext;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.base.SpellBlacklistResult;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Modifier implements ISpellComponent {

    private final ResourceLocation guiIcon;

    private ResourceLocation _registryNameCached;

    private ImmutableList<Attribute> governed;

    private int xpToRote;

    private Integer tier;

    public Modifier(ResourceLocation guiIcon, int xpToRote, Attribute... modified_attributes) {
        this.governed = ImmutableList.copyOf(modified_attributes);
        this.guiIcon = guiIcon;
        this.xpToRote = xpToRote;
    }

    @Override
    public void onRegistered() {
    }

    @Override
    public ResourceLocation getRegistryName() {
        if (this._registryNameCached == null) {
            this._registryNameCached = ManaAndArtificeMod.getModifierRegistry().getKey(this);
        }
        return this._registryNameCached;
    }

    public final boolean modifiesType(Attribute governing_attribute) {
        return this.governed.contains(governing_attribute);
    }

    public final ImmutableList<Attribute> getModifiedAttributes() {
        return this.governed;
    }

    @Override
    public final ResourceLocation getGuiIcon() {
        return this.guiIcon;
    }

    @Override
    public boolean isCraftable(SpellCraftingContext context) {
        return true;
    }

    @Override
    public boolean isUseableByPlayers() {
        return true;
    }

    @Override
    public int requiredXPForRote() {
        return this.xpToRote;
    }

    @Override
    public SpellBlacklistResult canBeCastAt(Level world, Vec3 position) {
        return SpellBlacklistResult.ALLOWED;
    }

    @Override
    public final SpellPartTags getUseTag() {
        return SpellPartTags.NEUTRAL;
    }

    @Override
    public int getTier(Level world) {
        if (this.tier == null) {
            if (this.isSilverSpell()) {
                this.tier = 5;
            } else {
                Optional<? extends Recipe<?>> recipe = world.getRecipeManager().byKey(this.getRegistryName());
                if (recipe.isPresent() && recipe.get() instanceof IMARecipe) {
                    this.tier = ((IMARecipe) recipe.get()).getTier();
                } else {
                    this.tier = 0;
                }
                if (this.tier < 0) {
                    this.tier = 0;
                }
            }
        }
        return this.tier;
    }

    public static class PhantomModifier extends Modifier {

        private static final ResourceLocation _default = new ResourceLocation("mna:modifier_default");

        public PhantomModifier() {
            super(_default, -1);
        }
    }
}