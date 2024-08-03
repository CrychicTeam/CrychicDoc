package com.mna.cantrips;

import com.google.common.collect.Lists;
import com.mna.ManaAndArtifice;
import com.mna.api.cantrips.ICantrip;
import com.mna.api.cantrips.ICantripRegistry;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.api.tools.RLoc;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.utility.PresentItem;
import com.mna.entities.utility.SpellFX;
import com.mna.items.ItemInit;
import com.mna.spells.SpellCaster;
import com.mna.spells.components.ComponentGust;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.BlockUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.util.TriConsumer;

public class CantripRegistry implements ICantripRegistry {

    private static ItemStack IGNITE_CANTRIP_SPELL = new ItemStack(ItemInit.SPELL.get());

    private static ItemStack TRANSMUTE_CANTRIP_SPELL = new ItemStack(ItemInit.SPELL.get());

    private List<ICantrip> cantrips = new ArrayList();

    public static final CantripRegistry INSTANCE = new CantripRegistry();

    public CantripRegistry() {
        SpellRecipe recipe = new SpellRecipe();
        recipe.setShape(Shapes.TOUCH);
        recipe.addComponent(Components.FIRE_DAMAGE);
        recipe.changeComponentAttributeValue(0, Attribute.DAMAGE, 0.0F);
        recipe.writeToNBT(IGNITE_CANTRIP_SPELL.getOrCreateTag());
        IGNITE_CANTRIP_SPELL.getTag().putBoolean("default_cantrip", true);
        recipe = new SpellRecipe();
        recipe.setShape(Shapes.TOUCH);
        recipe.addComponent(Components.TRANSMUTE);
        recipe.changeShapeAttributeValue(Attribute.RADIUS, 1.0F);
        recipe.writeToNBT(TRANSMUTE_CANTRIP_SPELL.getOrCreateTag());
        TRANSMUTE_CANTRIP_SPELL.getTag().putBoolean("default_cantrip", true);
        this.registerCantrip(RLoc.create("ignite"), RLoc.create("textures/gui/cantrips/ignite.png"), 1, CantripRegistry::applySpellAtTargetOnDelay, IGNITE_CANTRIP_SPELL, RLoc.create("manaweave_patterns/square"), RLoc.create("manaweave_patterns/triangle")).setRequiredAdvancement(RLoc.create("tier_1/advance_tier_2"));
        this.registerCantrip(RLoc.create("transmute"), RLoc.create("textures/gui/cantrips/transmute.png"), 1, CantripRegistry::applySpellAtTargetOnDelay, TRANSMUTE_CANTRIP_SPELL, RLoc.create("manaweave_patterns/square"), RLoc.create("manaweave_patterns/square"));
        this.registerCantrip(RLoc.create("halp"), RLoc.create("textures/gui/cantrips/halp.png"), 1, CantripRegistry::firework, ItemStack.EMPTY, RLoc.create("manaweave_patterns/square"), RLoc.create("manaweave_patterns/slash")).dynamicItem(Items.FIREWORK_ROCKET);
        this.registerCantrip(RLoc.create("gust"), RLoc.create("textures/gui/cantrips/gust.png"), 1, CantripRegistry::gust, ItemStack.EMPTY, RLoc.create("manaweave_patterns/slash"), RLoc.create("manaweave_patterns/backslash"));
        this.registerCantrip(RLoc.create("dispel"), RLoc.create("textures/gui/cantrips/dispel.png"), 2, CantripRegistry::dispel, ItemStack.EMPTY, RLoc.create("manaweave_patterns/circle"), RLoc.create("manaweave_patterns/triangle"));
        this.registerCantrip(RLoc.create("drought"), RLoc.create("textures/gui/cantrips/drought.png"), 2, CantripRegistry::drought, ItemStack.EMPTY, RLoc.create("manaweave_patterns/circle"), RLoc.create("manaweave_patterns/circle"), RLoc.create("manaweave_patterns/circle"));
        this.registerCantrip(RLoc.create("grimoire"), RLoc.create("textures/gui/cantrips/grimoire.png"), 3, CantripRegistry::summonGrimoire, ItemStack.EMPTY, RLoc.create("manaweave_patterns/triangle"), RLoc.create("manaweave_patterns/inverted_triangle"));
        this.registerCantrip(RLoc.create("faction_grimoire"), RLoc.create("textures/gui/cantrips/grimoire2.png"), 4, CantripRegistry::summonFactionGrimoire, ItemStack.EMPTY, RLoc.create("manaweave_patterns/hourglass"), RLoc.create("manaweave_patterns/triangle"), RLoc.create("manaweave_patterns/inverted_triangle"));
        this.registerCantrip(RLoc.create("freeform_spell_a"), RLoc.create("textures/gui/cantrips/chevron_1.png"), 3, CantripRegistry::applySpellAtTargetOnDelay, ItemStack.EMPTY, RLoc.create("manaweave_patterns/triangle"), RLoc.create("manaweave_patterns/circle")).dynamicItem(ItemInit.SPELL.get());
        this.registerCantrip(RLoc.create("freeform_spell_b"), RLoc.create("textures/gui/cantrips/chevron_2.png"), 4, CantripRegistry::applySpellAtTargetOnDelay, ItemStack.EMPTY, RLoc.create("manaweave_patterns/diamond"), RLoc.create("manaweave_patterns/square")).dynamicItem(ItemInit.SPELL.get());
        this.registerCantrip(RLoc.create("freeform_spell_c"), RLoc.create("textures/gui/cantrips/chevron_3.png"), 5, CantripRegistry::applySpellAtTargetOnDelay, ItemStack.EMPTY, RLoc.create("manaweave_patterns/knot2"), RLoc.create("manaweave_patterns/split_triangle")).dynamicItem(ItemInit.SPELL.get());
    }

    @Override
    public ICantrip registerCantrip(ResourceLocation id, ResourceLocation icon, int tier, TriConsumer<Player, ICantrip, InteractionHand> effector, ItemStack spell, ResourceLocation... defaultCombination) {
        if (this.cantrips.stream().anyMatch(cx -> cx.getId().equals(id))) {
            ManaAndArtifice.LOGGER.error("Attempted to register duplicate cantrips with id " + id);
            return null;
        } else {
            ICantrip c = new Cantrip(id, effector, spell, defaultCombination).setTier(tier).setDelay(60).setSound(SFX.Event.Player.MANAWEAVE_PATTERN_MATCH).setIcon(icon);
            this.cantrips.add(c);
            return c;
        }
    }

    public static void applySpellAtTargetOnDelay(Player player, ICantrip cantrip, InteractionHand hand) {
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCantripData().getCantrip(cantrip.getId()).ifPresent(c -> {
            SpellRecipe spell = SpellRecipe.fromNBT(c.getStack().getTag());
            if (spell.isValid()) {
                DelayedEventQueue.pushEvent(player.m_9236_(), new TimedDelayedEvent<>(player.m_20148_().toString() + "cantrip", 60, Triple.of(player, cantrip, hand), CantripRegistry::applySpellAtTarget));
            }
        }));
    }

    public static void applySpellAtTarget(String id, Triple<Player, ICantrip, InteractionHand> data) {
        if (data.getLeft() != null && data.getMiddle() != null) {
            InteractionHand useHand = ((Player) data.getLeft()).m_7655_();
            ((Player) data.getLeft()).getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCantripData().getCantrip(((ICantrip) data.getMiddle()).getId()).ifPresent(c -> {
                CompoundTag tag = c.getStack().getTag();
                boolean isDefaultCantrip = tag.getBoolean("default_cantrip");
                SpellRecipe recipe = SpellRecipe.fromNBT(tag);
                if (recipe.isValid()) {
                    if (!isDefaultCantrip) {
                        recipe.getShape().getContainedAttributes().forEach(a -> recipe.getShape().resetValueToDefault(a));
                        recipe.iterateComponents(comp -> comp.getContainedAttributes().forEach(a -> comp.resetValueToDefault(a)));
                    }
                    if (!recipe.getShape().getPart().isChanneled() && recipe.getReagents((Player) data.getLeft(), null, null).size() == 0) {
                        SpellCaster.Affect(c.getStack(), recipe, ((Player) data.getLeft()).m_9236_(), new SpellSource((LivingEntity) data.getLeft(), useHand));
                    }
                }
            }));
        }
    }

    public static void summonGrimoire(Player player, ICantrip cantrip, InteractionHand hand) {
        Vec3 vec = player.m_20299_(0.0F).add(player.m_20154_().scale(2.0));
        PresentItem entity = new PresentItem(player.m_9236_(), vec.x, vec.y - 0.5, vec.z, new ItemStack(ItemInit.GRIMOIRE.get()));
        entity.m_32060_();
        player.m_9236_().m_7967_(entity);
    }

    public static void summonFactionGrimoire(Player player, ICantrip cantrip, InteractionHand hand) {
        player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
            IFaction f = p.getAlliedFaction();
            ItemStack stack = f.getFactionGrimoire();
            if (stack.isEmpty()) {
                player.m_213846_(Component.translatable("cantrip.mna.faction_grimoire_nofaction"));
            } else {
                Vec3 vec = player.m_20299_(0.0F).add(player.m_20154_().scale(2.0));
                PresentItem entity = new PresentItem(player.m_9236_(), vec.x, vec.y - 0.5, vec.z, stack);
                entity.m_32060_();
                player.m_9236_().m_7967_(entity);
            }
        });
    }

    public static void dispel(Player player, ICantrip cantrip, InteractionHand hand) {
        player.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
        SpellRecipe recipe = new SpellRecipe();
        recipe.setShape(Shapes.SELF);
        recipe.addComponent(Components.TRUE_INVISIBILITY);
        SpellFX fx = new SpellFX(EntityInit.SPELL_FX.get(), player.m_9236_());
        fx.setCasterUUID(player);
        fx.setRecipe(recipe);
        fx.m_6034_(player.m_20185_(), player.m_20186_(), player.m_20189_());
        player.m_9236_().m_7967_(fx);
    }

    public static void drought(Player player, ICantrip cantrip, InteractionHand hand) {
        Level world = player.m_9236_();
        BlockPos pos = player.m_20183_();
        Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
        queue.add(new Tuple<>(pos, 0));
        int i = 0;
        while (!queue.isEmpty()) {
            Tuple<BlockPos, Integer> tuple = (Tuple<BlockPos, Integer>) queue.poll();
            BlockPos blockpos = tuple.getA();
            int j = tuple.getB();
            for (Direction direction : Direction.values()) {
                BlockPos blockpos1 = blockpos.relative(direction);
                BlockState blockstate = world.getBlockState(blockpos1);
                FluidState fluidstate = world.getFluidState(blockpos1);
                if (fluidstate.is(FluidTags.WATER)) {
                    if (blockstate.m_60734_() instanceof BucketPickup && ((BucketPickup) blockstate.m_60734_()).pickupBlock(world, blockpos1, blockstate) != ItemStack.EMPTY) {
                        i++;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (blockstate.m_60734_() instanceof LiquidBlock) {
                        world.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
                        i++;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (ComponentGust.BlockStateIsGustable(blockstate)) {
                        BlockEntity tileentity = blockstate.m_155947_() ? world.getBlockEntity(blockpos1) : null;
                        Block.dropResources(blockstate, world, blockpos1, tileentity);
                        world.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
                        i++;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    }
                }
            }
            if (i > 64) {
                break;
            }
        }
    }

    public static void gust(Player player, ICantrip cantrip, InteractionHand hand) {
        if (!player.m_9236_().isClientSide()) {
            int radius = 4;
            int height = 2;
            for (int i = -radius; i <= radius; i++) {
                for (int k = -radius; k <= radius; k++) {
                    for (int j = 0; j < height; j++) {
                        BlockPos pos = player.m_20183_().offset(i, j, k);
                        BlockState state = player.m_9236_().getBlockState(pos);
                        if (ComponentGust.BlockStateIsGustable(state) && BlockUtils.destroyBlock(player, player.m_9236_(), pos, true, Tiers.WOOD)) {
                            BlockUtils.updateBlockState(player.m_9236_(), pos);
                        }
                    }
                }
            }
        }
    }

    public static void firework(Player player, ICantrip cantrip, InteractionHand hand) {
        Vec3 vec = player.m_20182_().add(player.m_20156_().normalize().scale(4.0)).add(0.0, 1.0, 0.0);
        MutableObject<ItemStack> fireworkStack = new MutableObject(ItemStack.EMPTY);
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCantripData().getCantrip(cantrip.getId()).ifPresent(c -> fireworkStack.setValue(c.getStack())));
        if (((ItemStack) fireworkStack.getValue()).isEmpty() || ((ItemStack) fireworkStack.getValue()).getItem() != Items.FIREWORK_ROCKET) {
            ItemStack defaultStack = new ItemStack(Items.FIREWORK_ROCKET);
            CompoundTag explosion = new CompoundTag();
            CompoundTag fw = defaultStack.getOrCreateTagElement("Fireworks");
            DyeColor explosionColor = DyeColor.values()[(int) (Math.random() * (double) DyeColor.values().length)];
            DyeColor explosionFade = DyeColor.values()[(int) (Math.random() * (double) DyeColor.values().length)];
            explosion.putByte("Type", (byte) 0);
            explosion.putIntArray("Colors", new int[] { explosionColor.getFireworkColor() });
            explosion.putIntArray("FadeColors", new int[] { explosionFade.getFireworkColor() });
            fw.putByte("Flight", (byte) 2);
            ListTag explosions = new ListTag();
            explosions.add(explosion);
            fw.put("Explosions", explosions);
            fireworkStack.setValue(defaultStack);
        }
        FireworkRocketEntity firework = new FireworkRocketEntity(player.m_9236_(), null, vec.x, vec.y, vec.z, (ItemStack) fireworkStack.getValue());
        player.m_9236_().m_7967_(firework);
        player.m_7292_(new MobEffectInstance(MobEffects.GLOWING, 600, 1, false, false));
        player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
            if (p.hasAlliedFaction() && p.getAllyCooldown() <= 0) {
                p.summonRandomAlly(player);
            }
        });
    }

    @Override
    public int countRegisteredCantrips() {
        return this.cantrips.size();
    }

    @Nullable
    @Override
    public Optional<ICantrip> getCantrip(ResourceLocation cantripID) {
        return this.cantrips.stream().filter(c -> c.getId().equals(cantripID)).findFirst();
    }

    @Override
    public List<ICantrip> getCantrips() {
        return this.cantrips;
    }

    @Override
    public List<ICantrip> getCantrips(Player player) {
        int tier = 0;
        IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (progression != null) {
            tier = progression.getTier();
        }
        int resolvedTier = tier;
        return (List<ICantrip>) this.getCantrips().stream().filter(c -> c.getTier() > resolvedTier ? false : c.getRequiredAdvancement() == null || ManaAndArtifice.instance.proxy.playerHasAdvancement(player, c.getRequiredAdvancement())).collect(Collectors.toList());
    }

    @Override
    public List<ICantrip> getCantrips(int tier) {
        return (List<ICantrip>) this.getCantrips().stream().filter(c -> c.getTier() <= tier && c.getRequiredAdvancement() == null).collect(Collectors.toList());
    }
}