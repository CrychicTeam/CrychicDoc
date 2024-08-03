package org.violetmoon.quark.content.tools.module;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.tools.entity.ParrotEgg;
import org.violetmoon.quark.content.tools.item.ParrotEggItem;
import org.violetmoon.zeta.advancement.ManualTrigger;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.living.ZLivingTick;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerInteract;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tools")
public class ParrotEggsModule extends ZetaModule {

    private static final ResourceLocation KOTO = new ResourceLocation("quark", "textures/model/entity/variants/kotobirb.png");

    private static final String EGG_TIMER = "quark:parrot_egg_timer";

    private static final List<String> NAMES = List.of("red_blue", "blue", "green", "yellow_blue", "grey");

    public static EntityType<ParrotEgg> parrotEggType;

    public static TagKey<Item> feedTag;

    @Hint(key = "parrot_eggs")
    public static List<Item> parrotEggs;

    @Config(description = "The chance feeding a parrot will produce an egg")
    public static double chance = 0.05;

    @Config(description = "How long it takes to create an egg")
    public static int eggTime = 12000;

    @Config(name = "Enable Special Awesome Parrot")
    public static boolean enableKotobirb = true;

    private static boolean isEnabled;

    public static ManualTrigger throwParrotEggTrigger;

    @LoadEvent
    public final void register(ZRegister event) {
        parrotEggType = EntityType.Builder.of(ParrotEgg::new, MobCategory.MISC).sized(0.4F, 0.4F).clientTrackingRange(64).updateInterval(10).setCustomClientFactory((spawnEntity, world) -> new ParrotEgg(parrotEggType, world)).build("parrot_egg");
        Quark.ZETA.registry.register(parrotEggType, "parrot_egg", Registries.ENTITY_TYPE);
        CreativeTabManager.daisyChain();
        parrotEggs = new ArrayList();
        for (final int i = 0; i < 5; i++) {
            Item parrotEgg = new ParrotEggItem((String) NAMES.get(i), i, this).setCreativeTab(CreativeModeTabs.INGREDIENTS, Items.EGG, false);
            parrotEggs.add(parrotEgg);
            DispenserBlock.registerBehavior(parrotEgg, new AbstractProjectileDispenseBehavior() {

                @NotNull
                @Override
                protected Projectile getProjectile(@NotNull Level world, @NotNull Position pos, @NotNull ItemStack stack) {
                    return Util.make(new ParrotEgg(world, pos.x(), pos.y(), pos.z()), parrotEgg -> {
                        parrotEgg.m_37446_(stack);
                        parrotEgg.setVariant(i);
                    });
                }
            });
        }
        CreativeTabManager.endDaisyChain();
        throwParrotEggTrigger = event.getAdvancementModifierRegistry().registerManualTrigger("throw_parrot_egg");
    }

    @LoadEvent
    public final void setup(ZCommonSetup event) {
        feedTag = ItemTags.create(new ResourceLocation("quark", "parrot_feed"));
    }

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        isEnabled = this.enabled;
    }

    @LoadEvent
    public final void clientSetup(ZClientSetup event) {
        EntityRenderers.register(parrotEggType, ThrownItemRenderer::new);
    }

    @PlayEvent
    public void entityInteract(ZPlayerInteract.EntityInteract event) {
        Entity e = event.getTarget();
        Player player = event.getEntity();
        if (e instanceof Parrot parrot) {
            ItemStack stack = player.m_21205_();
            if (stack.isEmpty() || !stack.is(feedTag)) {
                stack = player.m_21206_();
            }
            if (!stack.isEmpty() && stack.is(feedTag)) {
                if (e.getPersistentData().getInt("quark:parrot_egg_timer") <= 0) {
                    if (!parrot.m_21824_()) {
                        return;
                    }
                    event.setCanceled(true);
                    if (parrot.m_9236_().isClientSide || event.getHand() == InteractionHand.OFF_HAND) {
                        return;
                    }
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    if (parrot.m_9236_() instanceof ServerLevel ws) {
                        ws.m_6263_(null, parrot.m_20185_(), parrot.m_20186_(), parrot.m_20189_(), SoundEvents.PARROT_EAT, SoundSource.NEUTRAL, 1.0F, 1.0F + (ws.f_46441_.nextFloat() - ws.f_46441_.nextFloat()) * 0.2F);
                        if (ws.f_46441_.nextDouble() < chance) {
                            parrot.getPersistentData().putInt("quark:parrot_egg_timer", eggTime);
                            ws.sendParticles(ParticleTypes.HAPPY_VILLAGER, parrot.m_20185_(), parrot.m_20186_(), parrot.m_20189_(), 10, (double) parrot.m_20205_(), (double) parrot.m_20206_(), (double) parrot.m_20205_(), 0.0);
                        } else {
                            ws.sendParticles(ParticleTypes.SMOKE, parrot.m_20185_(), parrot.m_20186_(), parrot.m_20189_(), 10, (double) parrot.m_20205_(), (double) parrot.m_20206_(), (double) parrot.m_20205_(), 0.0);
                        }
                    }
                } else if (parrot.m_9236_() instanceof ServerLevel wsx) {
                    wsx.sendParticles(ParticleTypes.HEART, parrot.m_20185_(), parrot.m_20186_(), parrot.m_20189_(), 1, (double) parrot.m_20205_(), (double) parrot.m_20206_(), (double) parrot.m_20205_(), 0.0);
                }
            }
        }
    }

    @PlayEvent
    public void entityUpdate(ZLivingTick event) {
        Entity e = event.getEntity();
        if (e instanceof Parrot parrot) {
            int time = parrot.getPersistentData().getInt("quark:parrot_egg_timer");
            if (time > 0) {
                if (time == 1) {
                    e.playSound(QuarkSounds.ENTITY_PARROT_EGG, 1.0F, (parrot.m_9236_().random.nextFloat() - parrot.m_9236_().random.nextFloat()) * 0.2F + 1.0F);
                    e.spawnAtLocation(new ItemStack((ItemLike) parrotEggs.get(this.getResultingEggColor(parrot))), 0.0F);
                }
                e.getPersistentData().putInt("quark:parrot_egg_timer", time - 1);
            }
        }
    }

    private int getResultingEggColor(Parrot parrot) {
        int color = parrot.getVariant().getId();
        RandomSource rand = parrot.m_9236_().random;
        return rand.nextBoolean() ? color : rand.nextInt(5);
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends ParrotEggsModule {

        @Nullable
        public static ResourceLocation getTextureForParrot(Parrot parrot) {
            if (ParrotEggsModule.isEnabled && enableKotobirb) {
                UUID uuid = parrot.m_20148_();
                return parrot.getVariant().getId() == 4 && uuid.getLeastSignificantBits() % 20L == 0L ? ParrotEggsModule.KOTO : null;
            } else {
                return null;
            }
        }
    }
}