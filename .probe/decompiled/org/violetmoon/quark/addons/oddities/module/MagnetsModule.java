package org.violetmoon.quark.addons.oddities.module;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.violetmoon.quark.addons.oddities.block.MagnetBlock;
import org.violetmoon.quark.addons.oddities.block.MovingMagnetizedBlock;
import org.violetmoon.quark.addons.oddities.block.be.MagnetBlockEntity;
import org.violetmoon.quark.addons.oddities.block.be.MagnetizedBlockBlockEntity;
import org.violetmoon.quark.addons.oddities.client.render.be.MagnetizedBlockRenderer;
import org.violetmoon.quark.addons.oddities.magnetsystem.MagnetSystem;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.ZLevelTick;
import org.violetmoon.zeta.event.play.ZRecipeCrawl;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.handler.ToolInteractionHandler;

@ZetaLoadModule(category = "oddities")
public class MagnetsModule extends ZetaModule {

    public static BlockEntityType<MagnetBlockEntity> magnetType;

    public static BlockEntityType<MagnetizedBlockBlockEntity> magnetizedBlockType;

    public static SimpleParticleType attractorParticle;

    public static SimpleParticleType repulsorParticle;

    @Config(description = "Any items you place in this list will be derived so that any block made of it will become magnetizable")
    public static List<String> magneticDerivationList = Lists.newArrayList(new String[] { "minecraft:iron_ingot", "minecraft:copper_ingot", "minecraft:exposed_copper", "minecraft:weathered_copper", "minecraft:oxidized_copper", "minecraft:raw_iron", "minecraft:raw_copper", "minecraft:iron_ore", "minecraft:deepslate_iron_ore", "minecraft:copper_ore", "minecraft:deepslate_copper_ore", "quark:gravisand" });

    @Config(description = "Block/Item IDs to force-allow magnetism on, regardless of their crafting recipe")
    public static List<String> magneticWhitelist = Lists.newArrayList(new String[] { "minecraft:chipped_anvil", "minecraft:damaged_anvil", "minecraft:iron_horse_armor", "minecraft:chainmail_helmet", "minecraft:chainmail_boots", "minecraft:chainmail_leggings", "minecraft:chainmail_chestplate", "#minecraft:cauldrons" });

    @Config(description = "Block/Item IDs to force-disable magnetism on, regardless of their crafting recipe")
    public static List<String> magneticBlacklist = Lists.newArrayList(new String[] { "minecraft:tripwire_hook", "minecraft:map" });

    @Config(flag = "magnet_pre_end")
    public static boolean usePreEndRecipe = false;

    @Config(flag = "magnetic_entities", description = "Allows magnets to push and pull entities in the 'affected_by_magnets' tag (edit it with datapack). Turning off can reduce lag")
    public static boolean affectEntities = true;

    @Config(flag = "magnetic_armor", description = "Allows magnets to push and pull entities having magnetic armor. Requires 'magnetic_entities' config ON")
    public static boolean affectsArmor = true;

    @Config(description = "Determines how fast entities are pulled by magnets. Still follows the inverse square law")
    public static double entitiesPullForce = 0.18F;

    @Config(description = "Stonecutters pulled by magnets will silk touch the blocks they cut.")
    public static boolean stoneCutterSilkTouch = true;

    public static final TagKey<EntityType<?>> magneticEntities = TagKey.create(Registries.ENTITY_TYPE, Quark.asResource("affected_by_magnets"));

    @Hint
    public static Block magnet;

    public static Block magnetized_block;

    @LoadEvent
    public final void register(ZRegister event) {
        magnet = new MagnetBlock(this);
        magnetized_block = new MovingMagnetizedBlock(this);
        ToolInteractionHandler.registerWaxedBlockBooleanProperty(this, magnet, MagnetBlock.WAXED);
        magnetType = BlockEntityType.Builder.<MagnetBlockEntity>of(MagnetBlockEntity::new, magnet).build(null);
        event.getRegistry().register(magnetType, "magnet", Registries.BLOCK_ENTITY_TYPE);
        magnetizedBlockType = BlockEntityType.Builder.<MagnetizedBlockBlockEntity>of(MagnetizedBlockBlockEntity::new, magnetized_block).build(null);
        event.getRegistry().register(magnetizedBlockType, "magnetized_block", Registries.BLOCK_ENTITY_TYPE);
        attractorParticle = new SimpleParticleType(false);
        event.getRegistry().register(attractorParticle, "attractor", Registries.PARTICLE_TYPE);
        repulsorParticle = new SimpleParticleType(false);
        event.getRegistry().register(repulsorParticle, "repulsor", Registries.PARTICLE_TYPE);
    }

    @LoadEvent
    public final void clientSetup(ZClientSetup event) {
        BlockEntityRenderers.register(magnetizedBlockType, MagnetizedBlockRenderer::new);
    }

    @PlayEvent
    public void tickStart(ZLevelTick.Start event) {
        MagnetSystem.tick(true, event.getLevel());
    }

    @PlayEvent
    public void tickEnd(ZLevelTick.End event) {
        MagnetSystem.tick(false, event.getLevel());
    }

    @PlayEvent
    public void crawlReset(ZRecipeCrawl.Reset event) {
        MagnetSystem.onRecipeReset();
    }

    @PlayEvent
    public void crawlDigest(ZRecipeCrawl.Digest event) {
        MagnetSystem.onDigest(event);
    }
}