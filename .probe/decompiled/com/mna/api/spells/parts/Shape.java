package com.mna.api.spells.parts;

import com.google.common.collect.ImmutableList;
import com.mna.ManaAndArtifice;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.recipes.IMARecipe;
import com.mna.api.spells.SpellCraftingContext;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiable;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.base.SpellBlacklistResult;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public abstract class Shape implements IModifiable<Shape>, ISpellComponent {

    private final ResourceLocation guiIcon;

    private ResourceLocation _registryNameCached;

    private Integer tier = null;

    private final ImmutableList<AttributeValuePair> modifiableAttributes;

    public Shape(ResourceLocation guiIcon, AttributeValuePair... attributeValuePairs) {
        this.guiIcon = guiIcon;
        this.modifiableAttributes = ImmutableList.copyOf(attributeValuePairs);
    }

    @Override
    public final ImmutableList<AttributeValuePair> getModifiableAttributes() {
        this.lookupAttributeConfig();
        return AttributeValuePair.deepCopy(this.modifiableAttributes);
    }

    public final void lookupAttributeConfig() {
        this.modifiableAttributes.forEach(m -> m.lookupConfig(this));
    }

    @Override
    public void onRegistered() {
        this.initializeConfigs((AttributeValuePair[]) this.modifiableAttributes.toArray(new AttributeValuePair[0]));
    }

    @Override
    public ResourceLocation getRegistryName() {
        if (this._registryNameCached == null) {
            this._registryNameCached = ManaAndArtificeMod.getShapeRegistry().getKey(this);
        }
        return this._registryNameCached;
    }

    public abstract List<SpellTarget> Target(SpellSource var1, Level var2, IModifiedSpellPart<Shape> var3, ISpellDefinition var4);

    public List<SpellTarget> TargetNPCCast(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, SpellTarget targetHint) {
        return Arrays.asList(SpellTarget.NPC_CAST_ASSIST_NOT_IMPLEMENTED);
    }

    public boolean spawnsTargetEntity() {
        return false;
    }

    @Override
    public boolean isCraftable(SpellCraftingContext context) {
        return true;
    }

    public boolean isChanneled() {
        return false;
    }

    public boolean grantComponentRoteXP() {
        return true;
    }

    public abstract float initialComplexity();

    @Override
    public SpellBlacklistResult canBeCastAt(Level world, Vec3 position) {
        if (world.isClientSide) {
            return SpellBlacklistResult.ALLOWED;
        } else {
            try {
                Registry<Biome> biomeRegistry = ((ServerLevel) world).m_9598_().registryOrThrow(Registries.BIOME);
                BlockPos bp = BlockPos.containing(position);
                Biome biome = (Biome) world.m_204166_(bp).value();
                if (ManaAndArtificeMod.getConfigHelper().isDimensionBlacklisted(this, world.dimension().location())) {
                    return SpellBlacklistResult.DIMENSION_BLOCKED;
                }
                if (ManaAndArtificeMod.getConfigHelper().isBiomeBlacklisted(this, biomeRegistry.getKey(biome))) {
                    return SpellBlacklistResult.BIOME_BLOCKED;
                }
            } catch (Throwable var6) {
                ManaAndArtifice.LOGGER.error("Failed to resolve biome at " + position.toString());
            }
            return SpellBlacklistResult.ALLOWED;
        }
    }

    public int baselineCooldown() {
        return 0;
    }

    public int maxChannelTime(IModifiedSpellPart<Shape> shape) {
        return (int) shape.getValue(Attribute.DURATION) * 20;
    }

    @Override
    public boolean isUseableByPlayers() {
        return true;
    }

    @Override
    public final ResourceLocation getGuiIcon() {
        return this.guiIcon;
    }

    @Nullable
    public List<SpellReagent> getRequiredReagents(@Nullable Player caster) {
        return null;
    }

    public boolean affectsCaster() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
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

    public static class PhantomShape extends Shape {

        public static Shape.PhantomShape instance = new Shape.PhantomShape();

        private static final ResourceLocation _default = new ResourceLocation("mna:shape_default");

        public PhantomShape() {
            super(_default);
        }

        @Override
        public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
            return Arrays.asList(SpellTarget.NONE);
        }

        @Override
        public float initialComplexity() {
            return 0.0F;
        }

        @Override
        public int requiredXPForRote() {
            return -1;
        }

        @Override
        public SpellPartTags getUseTag() {
            return SpellPartTags.NEUTRAL;
        }
    }
}