package com.mna.api.spells.parts;

import com.google.common.collect.ImmutableList;
import com.mna.ManaAndArtifice;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.recipes.IMARecipe;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
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
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class SpellEffect implements IModifiable<SpellEffect>, ISpellComponent {

    private final ResourceLocation guiIcon;

    private ResourceLocation _registryNameCached;

    private final ImmutableList<AttributeValuePair> modifiableAttributes;

    private Integer tier;

    private ArrayList<SpellReagent> _reagents = null;

    public SpellEffect(ResourceLocation guiIcon, AttributeValuePair... attributeValuePairs) {
        ArrayList<AttributeValuePair> list = new ArrayList(Arrays.asList(attributeValuePairs));
        if (!list.stream().anyMatch(m -> m.getAttribute() == Attribute.DELAY)) {
            list.add(new AttributeValuePair(Attribute.DELAY, 0.0F, 0.0F, 3.0F, 0.1F, 0.5F));
        }
        this.modifiableAttributes = ImmutableList.copyOf(list);
        this.guiIcon = guiIcon;
    }

    @Override
    public void onRegistered() {
        this.initializeConfigs((AttributeValuePair[]) this.modifiableAttributes.toArray(new AttributeValuePair[0]));
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

    public abstract ComponentApplicationResult ApplyEffect(SpellSource var1, SpellTarget var2, IModifiedSpellPart<SpellEffect> var3, SpellContext var4);

    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.ARCANE;
    }

    @OnlyIn(Dist.CLIENT)
    public void SpawnParticles(Level world, Vec3 impact_position, int age, @Nullable Player caster, @Nullable ISpellDefinition recipe) {
        this.SpawnParticles(world, impact_position, Vec3.ZERO, age, caster, recipe);
    }

    @OnlyIn(Dist.CLIENT)
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, @Nullable LivingEntity caster, @Nullable ISpellDefinition recipe) {
    }

    public abstract Affinity getAffinity();

    @Override
    public final ResourceLocation getGuiIcon() {
        return this.guiIcon;
    }

    @Override
    public ResourceLocation getRegistryName() {
        if (this._registryNameCached == null) {
            this._registryNameCached = ManaAndArtificeMod.getComponentRegistry().getKey(this);
        }
        return this._registryNameCached;
    }

    @Override
    public boolean isCraftable(SpellCraftingContext context) {
        return true;
    }

    public abstract float initialComplexity();

    public int baselineCooldown() {
        return 0;
    }

    public boolean canBeChanneled() {
        return true;
    }

    public boolean targetsEntities() {
        return true;
    }

    public boolean targetsBlocks() {
        return true;
    }

    public boolean isHellfireBoosted(Attribute attr) {
        return true;
    }

    public Direction defaultBlockFace() {
        return Direction.UP;
    }

    @Override
    public boolean isUseableByPlayers() {
        return true;
    }

    public boolean applyAtChanneledEntityPos(boolean clientSide) {
        return false;
    }

    @Nullable
    public List<SpellReagent> getRequiredReagents(@Nullable Player caster, @Nullable InteractionHand hand) {
        return this._reagents == null ? null : this._reagents.stream().filter(r -> !r.isIgnoredBy(caster)).toList();
    }

    public void addReagent(ItemStack reagentStack, boolean compareNBT, boolean ignoreDurability, boolean consume, IFaction... ignoredBy) {
        if (this._reagents == null) {
            this._reagents = new ArrayList();
        }
        this._reagents.add(new SpellReagent(this, reagentStack, compareNBT, ignoreDurability, consume, false, ignoredBy));
    }

    public void addReagent(ItemStack reagentStack, IFaction... ignoredBy) {
        this.addReagent(reagentStack, false, false, true, ignoredBy);
    }

    public void addOptionalReagent(ItemStack reagentStack, boolean compareNBT, boolean ignoreDurability, boolean consume, IFaction... ignoredBy) {
        if (this._reagents == null) {
            this._reagents = new ArrayList();
        }
        this._reagents.add(new SpellReagent(this, reagentStack, compareNBT, ignoreDurability, consume, true, ignoredBy));
    }

    public void addOptionalReagent(ItemStack reagentStack, IFaction... ignoredBy) {
        this.addOptionalReagent(reagentStack, false, false, true, ignoredBy);
    }

    public boolean autoConsumeReagents() {
        return true;
    }

    public void addReagentTooltip(Player player, @Nullable InteractionHand hand, List<Component> tooltip, SpellReagent reagent) {
        MutableComponent c = Component.literal(String.format("%d x ", reagent.getReagentStack().getCount())).append(reagent.getReagentStack().getHoverName());
        if (reagent.getOptional()) {
            c.append(Component.literal(" (")).append(Component.translatable("item.mna.spell.tooltip.optional")).append(Component.literal(")"));
        }
        tooltip.add(c);
    }

    public SpellEffect.BlockPlaceResult tryPlaceBlock(@Nullable Player player, ServerLevel world, Block block, BlockPos pos, Direction face, boolean checkOtherDirections, @Nullable TriPredicate<Level, BlockPos, BlockPos> additionalPredicate) {
        List<Direction> dirs = new ArrayList();
        dirs.add(face);
        if (checkOtherDirections) {
            for (Direction d : Direction.values()) {
                if (d != face) {
                    dirs.add(d);
                }
            }
        }
        for (Direction dx : dirs) {
            BlockPos targetPos = pos.offset(dx.getNormal());
            if (additionalPredicate == null || additionalPredicate.test(world, pos, targetPos)) {
                BlockState existing = world.m_8055_(targetPos);
                Player bpcPlayer = player;
                if (player == null) {
                    bpcPlayer = FakePlayerFactory.getMinecraft(world);
                    bpcPlayer.m_146884_(Vec3.atCenterOf(targetPos));
                    bpcPlayer.m_7618_(EntityAnchorArgument.Anchor.FEET, Vec3.atCenterOf(targetPos));
                }
                BlockPlaceContext bpc = new BlockPlaceContext(bpcPlayer, InteractionHand.MAIN_HAND, ItemStack.EMPTY, new BlockHitResult(Vec3.atCenterOf(targetPos), dx, targetPos, false));
                if (existing.m_60629_(bpc)) {
                    BlockState placementState = block.getStateForPlacement(bpc);
                    if (placementState != null && block.m_7898_(placementState, world, targetPos)) {
                        if (!ForgeEventFactory.onBlockPlace(player, BlockSnapshot.create(world.m_46472_(), world, targetPos), dx)) {
                            world.m_46597_(targetPos, placementState);
                        }
                        return new SpellEffect.BlockPlaceResult(true, targetPos, dx);
                    }
                }
            }
        }
        return new SpellEffect.BlockPlaceResult(false, null, null);
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

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.NEUTRAL;
    }

    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.EARTH, Affinity.ENDER, Affinity.FIRE, Affinity.WATER, Affinity.WIND, Affinity.ICE, Affinity.LIGHTNING);
    }

    protected boolean casterTeamCheck(SpellSource source, SpellTarget target) {
        if (source.getCaster() == target.getEntity()) {
            return true;
        } else if (!target.isEntity()) {
            return false;
        } else if (source.isPlayerCaster() && target.getLivingEntity() instanceof Player) {
            Player caster = source.getPlayer();
            Player tgt = (Player) target.getLivingEntity();
            return ManaAndArtificeMod.getSummonHelper().isEntityFriendly(tgt, caster);
        } else {
            return true;
        }
    }

    public class BlockPlaceResult {

        public final boolean success;

        @Nullable
        public final BlockPos position;

        @Nullable
        public final Direction placedAgainst;

        public BlockPlaceResult(boolean success, BlockPos position, Direction placedAgainst) {
            this.success = success;
            this.position = position;
            this.placedAgainst = placedAgainst;
        }
    }

    public static class PhantomComponent extends SpellEffect {

        public static SpellEffect.PhantomComponent instance = new SpellEffect.PhantomComponent();

        private static final ResourceLocation _default = new ResourceLocation("mna:component_default");

        public PhantomComponent() {
            super(_default);
        }

        @Override
        public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
            return ComponentApplicationResult.SUCCESS;
        }

        @Override
        public Affinity getAffinity() {
            return Affinity.UNKNOWN;
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