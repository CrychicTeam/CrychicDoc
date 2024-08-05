package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.network.message.MessageItemSound;
import com.rekindled.embers.util.MultiblockSoundType;
import com.rekindled.embers.util.sound.ItemUseSound;
import com.rekindled.embers.util.sound.MachineSound;
import net.minecraft.client.Minecraft;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;

public class EmbersSounds extends SoundDefinitionsProvider {

    public static final RegistryObject<SoundEvent> ALCHEMY_FAIL = registerSound("block.alchemy.fail");

    public static final RegistryObject<SoundEvent> ALCHEMY_SUCCESS = registerSound("block.alchemy.success");

    public static final RegistryObject<SoundEvent> ALCHEMY_LOOP = registerSound("block.alchemy.loop");

    public static final RegistryObject<SoundEvent> ALCHEMY_START = registerSound("block.alchemy.start");

    public static final RegistryObject<SoundEvent> PEDESTAL_LOOP = registerSound("block.pedestal.loop");

    public static final RegistryObject<SoundEvent> BEAM_CANNON_FIRE = registerSound("block.beam_cannon.fire");

    public static final RegistryObject<SoundEvent> BEAM_CANNON_HIT = registerSound("block.beam_cannon.hit");

    public static final RegistryObject<SoundEvent> CRYSTAL_CELL_LOOP = registerSound("block.crystal_cell.loop");

    public static final RegistryObject<SoundEvent> CRYSTAL_CELL_GROW = registerSound("block.crystal_cell.grow");

    public static final RegistryObject<SoundEvent> GENERATOR_LOOP = registerSound("block.generator.loop");

    public static final RegistryObject<SoundEvent> ACTIVATOR = registerSound("block.activator.plume");

    public static final RegistryObject<SoundEvent> PRESSURE_REFINERY = registerSound("block.boiler.plume");

    public static final RegistryObject<SoundEvent> IGNEM_REACTOR = registerSound("block.ignem_reactor.plume");

    public static final RegistryObject<SoundEvent> BORE_START = registerSound("block.bore.start");

    public static final RegistryObject<SoundEvent> BORE_STOP = registerSound("block.bore.stop");

    public static final RegistryObject<SoundEvent> BORE_LOOP = registerSound("block.bore.loop");

    public static final RegistryObject<SoundEvent> BORE_LOOP_MINE = registerSound("block.bore.loop_mine");

    public static final RegistryObject<SoundEvent> BORE_LOOP_SUPERSPEED = registerSound("block.bore.loop.superspeed");

    public static final RegistryObject<SoundEvent> BORE_LOOP_MINE_SUPERSPEED = registerSound("block.bore.loop_mine.superspeed");

    public static final RegistryObject<SoundEvent> CATALYTIC_PLUG_START = registerSound("block.catalytic_plug.start");

    public static final RegistryObject<SoundEvent> CATALYTIC_PLUG_STOP = registerSound("block.catalytic_plug.stop");

    public static final RegistryObject<SoundEvent> CATALYTIC_PLUG_LOOP = registerSound("block.catalytic_plug.loop");

    public static final RegistryObject<SoundEvent> CATALYTIC_PLUG_LOOP_READY = registerSound("block.catalytic_plug.loop_ready");

    public static final RegistryObject<SoundEvent> STAMPER_DOWN = registerSound("block.stamper.down");

    public static final RegistryObject<SoundEvent> STAMPER_UP = registerSound("block.stamper.up");

    public static final RegistryObject<SoundEvent> HEATCOIL_HIGH = registerSound("block.heat_coil.high_loop");

    public static final RegistryObject<SoundEvent> HEATCOIL_MID = registerSound("block.heat_coil.mid_loop");

    public static final RegistryObject<SoundEvent> HEATCOIL_LOW = registerSound("block.heat_coil.low_loop");

    public static final RegistryObject<SoundEvent> HEATCOIL_COOK = registerSound("block.heat_coil.cooking_loop");

    public static final RegistryObject<SoundEvent> PLINTH_LOOP = registerSound("block.melter.loopblock.plinth.loop");

    public static final RegistryObject<SoundEvent> MELTER_LOOP = registerSound("block.melter.loop");

    public static final RegistryObject<SoundEvent> MIXER_LOOP = registerSound("block.mixer.loop");

    public static final RegistryObject<SoundEvent> COPPER_CHARGER_LOOP = registerSound("block.copper_charger.loop");

    public static final RegistryObject<SoundEvent> COPPER_CHARGER_SIPHON_LOOP = registerSound("block.copper_charger_siphon.loop");

    public static final RegistryObject<SoundEvent> INJECTOR_LOOP = registerSound("block.injector.loop");

    public static final RegistryObject<SoundEvent> METAL_SEED_LOOP = registerSound("block.metal_seed.loop");

    public static final RegistryObject<SoundEvent> METAL_SEED_PING = registerSound("block.metal_seed.ping");

    public static final RegistryObject<SoundEvent> INFERNO_FORGE_FAIL = registerSound("block.inferno_forge.fail");

    public static final RegistryObject<SoundEvent> INFERNO_FORGE_SUCCESS = registerSound("block.inferno_forge.success");

    public static final RegistryObject<SoundEvent> INFERNO_FORGE_LOOP = registerSound("block.inferno_forge.loop");

    public static final RegistryObject<SoundEvent> INFERNO_FORGE_START = registerSound("block.inferno_forge.start");

    public static final RegistryObject<SoundEvent> INFERNO_FORGE_OPEN = registerSound("block.inferno_forge.open");

    public static final RegistryObject<SoundEvent> INFERNO_FORGE_CLOSE = registerSound("block.inferno_forge.close");

    public static final RegistryObject<SoundEvent> FIELD_CHART_LOOP = registerSound("block.field_chart.loop");

    public static final RegistryObject<SoundEvent> ENTROPIC_ENUMERATOR_TURN = registerSound("block.entropic_enumerator.turn");

    public static final RegistryObject<SoundEvent> EMBER_EMIT = registerSound("block.ember_transfer.emit.small");

    public static final RegistryObject<SoundEvent> EMBER_EMIT_BIG = registerSound("block.ember_transfer.emit.big");

    public static final RegistryObject<SoundEvent> EMBER_RECEIVE = registerSound("block.ember_transfer.receive.small");

    public static final RegistryObject<SoundEvent> EMBER_RECEIVE_BIG = registerSound("block.ember_transfer.receive.big");

    public static final RegistryObject<SoundEvent> EMBER_RELAY = registerSound("block.ember_transfer.relay");

    public static final RegistryObject<SoundEvent> STEAM_ENGINE_START_STEAM = registerSound("block.steam_engine.start_steam");

    public static final RegistryObject<SoundEvent> STEAM_ENGINE_START_BURN = registerSound("block.steam_engine.start_burn");

    public static final RegistryObject<SoundEvent> STEAM_ENGINE_LOOP_STEAM = registerSound("block.steam_engine.steam_loop");

    public static final RegistryObject<SoundEvent> STEAM_ENGINE_LOOP_BURN = registerSound("block.steam_engine.burn_loop");

    public static final RegistryObject<SoundEvent> STEAM_ENGINE_STOP = registerSound("block.steam_engine.stop");

    public static final RegistryObject<SoundEvent> MINI_BOILER_RUPTURE = registerSound("block.mini_boiler.rupture");

    public static final RegistryObject<SoundEvent> MINI_BOILER_LOOP_SLOW = registerSound("block.mini_boiler.loop_slow");

    public static final RegistryObject<SoundEvent> MINI_BOILER_LOOP_MID = registerSound("block.mini_boiler.loop_mid");

    public static final RegistryObject<SoundEvent> MINI_BOILER_LOOP_FAST = registerSound("block.mini_boiler.loop_fast");

    public static final RegistryObject<SoundEvent> MINI_BOILER_PRESSURE_LOW = registerSound("block.mini_boiler.pressure_loop_low");

    public static final RegistryObject<SoundEvent> MINI_BOILER_PRESSURE_MID = registerSound("block.mini_boiler.pressure_loop_mid");

    public static final RegistryObject<SoundEvent> MINI_BOILER_PRESSURE_HIGH = registerSound("block.mini_boiler.pressure_loop_high");

    public static final RegistryObject<SoundEvent> PUMP_SLOW = registerSound("block.pump.slow");

    public static final RegistryObject<SoundEvent> PUMP_MID = registerSound("block.pump.mid");

    public static final RegistryObject<SoundEvent> PUMP_FAST = registerSound("block.pump.fast");

    public static final RegistryObject<SoundEvent> PIPE_CONNECT = registerSound("block.pipe.connect");

    public static final RegistryObject<SoundEvent> PIPE_DISCONNECT = registerSound("block.pipe.disconnect");

    public static final RegistryObject<SoundEvent> PIPE_VENT_LOOP = registerSound("block.pipe.vent");

    public static final RegistryObject<SoundEvent> FIREBALL_BIG = registerSound("fireball.big.fire");

    public static final RegistryObject<SoundEvent> FIREBALL_BIG_HIT = registerSound("fireball.big.hit");

    public static final RegistryObject<SoundEvent> FIREBALL = registerSound("fireball.small.fire");

    public static final RegistryObject<SoundEvent> FIREBALL_HIT = registerSound("fireball.small.hit");

    public static final RegistryObject<SoundEvent> BLAZING_RAY_FIRE = registerSound("item.blazing_ray.fire");

    public static final RegistryObject<SoundEvent> BLAZING_RAY_EMPTY = registerSound("item.blazing_ray.empty");

    public static final RegistryObject<SoundEvent> CINDER_STAFF_CHARGE = registerSound("item.cinder_staff.charge");

    public static final RegistryObject<SoundEvent> CINDER_STAFF_FAIL = registerSound("item.cinder_staff.fail");

    public static final RegistryObject<SoundEvent> CINDER_STAFF_LOOP = registerSound("item.cinder_staff.loop");

    public static final RegistryObject<SoundEvent> ULTRASYD_7F_PATTERNS = registerSound("music_disc.7f_patterns");

    public static final RegistryObject<SoundEvent> EXPLOSION_CHARM_ABSORB = registerSound("item.explosion_charm.absorb");

    public static final RegistryObject<SoundEvent> EXPLOSION_CHARM_RECHARGE = registerSound("item.explosion_charm.recharge");

    public static final RegistryObject<SoundEvent> ASHEN_AMULET_BURN = registerSound("item.ash_amulet.burn");

    public static final RegistryObject<SoundEvent> HEATED_ITEM_LEVELUP = registerSound("item.heated.level_up");

    public static final RegistryObject<SoundEvent> RESONATING_BELL = registerSound("item.resonating_bell.ring");

    public static final RegistryObject<SoundEvent> CINDER_JET = registerSound("item.cinder_jet.boost");

    public static final RegistryObject<SoundEvent> INFLICTOR_GEM = registerSound("item.inflictor_gem.absorb");

    public static final RegistryObject<SoundEvent> METALLURGIC_DUST = registerSound("item.metallurgic_dust.convert");

    public static final RegistryObject<SoundEvent> METALLURGIC_DUST_FAIL = registerSound("item.metallurgic_dust.fail");

    public static final RegistryObject<SoundEvent> CODEX_OPEN = registerSound("item.codex.open");

    public static final RegistryObject<SoundEvent> CODEX_CLOSE = registerSound("item.codex.close");

    public static final RegistryObject<SoundEvent> CODEX_CATEGORY_OPEN = registerSound("item.codex.category.open");

    public static final RegistryObject<SoundEvent> CODEX_CATEGORY_CLOSE = registerSound("item.codex.category.close");

    public static final RegistryObject<SoundEvent> CODEX_CATEGORY_SELECT = registerSound("item.codex.category.select");

    public static final RegistryObject<SoundEvent> CODEX_CATEGORY_UNSELECT = registerSound("item.codex.category.unselect");

    public static final RegistryObject<SoundEvent> CODEX_CATEGORY_SWITCH = registerSound("item.codex.category.switch");

    public static final RegistryObject<SoundEvent> CODEX_PAGE_OPEN = registerSound("item.codex.page.open");

    public static final RegistryObject<SoundEvent> CODEX_PAGE_CLOSE = registerSound("item.codex.page.close");

    public static final RegistryObject<SoundEvent> CODEX_PAGE_SWITCH = registerSound("item.codex.page.switch");

    public static final RegistryObject<SoundEvent> CODEX_CHECK = registerSound("item.codex.check");

    public static final RegistryObject<SoundEvent> CODEX_UNCHECK = registerSound("item.codex.uncheck");

    public static final RegistryObject<SoundEvent> CODEX_LOCK = registerSound("item.codex.lock");

    public static final RegistryObject<SoundEvent> CODEX_UNLOCK = registerSound("item.codex.unlock");

    public static final RegistryObject<SoundEvent> BAUBLE_EQUIP = registerSound("item.bauble.equip");

    public static final RegistryObject<SoundEvent> BAUBLE_UNEQUIP = registerSound("item.bauble.unequip");

    public static final RegistryObject<SoundEvent> TYRFING_HIT = registerSound("item.tyrfing.hit");

    public static final RegistryObject<SoundEvent> SHIFTING_SCALES_BREAK = registerSound("item.scale.break");

    public static final RegistryObject<SoundEvent> WINDING_GEARS_SPRING = registerSound("item.windup.spring");

    public static final RegistryObject<SoundEvent> ANCIENT_GOLEM_STEP = registerSound("entity.ancient_golem.step");

    public static final RegistryObject<SoundEvent> ANCIENT_GOLEM_HURT = registerSound("entity.ancient_golem.hurt");

    public static final RegistryObject<SoundEvent> ANCIENT_GOLEM_PUNCH = registerSound("entity.ancient_golem.punch");

    public static final RegistryObject<SoundEvent> ANCIENT_GOLEM_DEATH = registerSound("entity.ancient_golem.death");

    public static final RegistryObject<SoundEvent> MULTIBLOCK_PACK = registerSound("block.multiblock.pack");

    public static final RegistryObject<SoundEvent> MULTIBLOCK_UNPACK = registerSound("block.multiblock.unpack");

    public static final RegistryObject<SoundEvent> CAMINITE_BREAK = registerSound("block.caminite.break");

    public static final RegistryObject<SoundEvent> CAMINITE_STEP = registerSound("block.caminite.step");

    public static final RegistryObject<SoundEvent> CAMINITE_PLACE = registerSound("block.caminite.place");

    public static final RegistryObject<SoundEvent> CAMINITE_HIT = registerSound("block.caminite.hit");

    public static final RegistryObject<SoundEvent> CAMINITE_FALL = registerSound("block.caminite.fall");

    public static final RegistryObject<SoundEvent> ASHEN_STONE_BREAK = registerSound("block.ashen_stone.break");

    public static final RegistryObject<SoundEvent> ASHEN_STONE_STEP = registerSound("block.ashen_stone.step");

    public static final RegistryObject<SoundEvent> ASHEN_STONE_PLACE = registerSound("block.ashen_stone.place");

    public static final RegistryObject<SoundEvent> ASHEN_STONE_HIT = registerSound("block.ashen_stone.hit");

    public static final RegistryObject<SoundEvent> ASHEN_STONE_FALL = registerSound("block.ashen_stone.fall");

    public static final RegistryObject<SoundEvent> SOLID_METAL_BREAK = registerSound("block.solid_metal.break");

    public static final RegistryObject<SoundEvent> SOLID_METAL_PLACE = registerSound("block.solid_metal.place");

    public static final RegistryObject<SoundEvent> MACHINE_BREAK = registerSound("block.machine.break");

    public static final RegistryObject<SoundEvent> MACHINE_PLACE = registerSound("block.machine.place");

    public static final SoundType CAMINITE = new ForgeSoundType(1.0F, 1.0F, CAMINITE_BREAK, CAMINITE_STEP, CAMINITE_PLACE, CAMINITE_HIT, CAMINITE_FALL);

    public static final SoundType ASHEN_STONE = new ForgeSoundType(1.0F, 1.0F, ASHEN_STONE_BREAK, ASHEN_STONE_STEP, ASHEN_STONE_PLACE, ASHEN_STONE_HIT, ASHEN_STONE_FALL);

    public static final SoundType LIGHT_METAL = new ForgeSoundType(1.0F, 1.5F, () -> SoundEvents.NETHERITE_BLOCK_BREAK, () -> SoundEvents.NETHERITE_BLOCK_STEP, () -> SoundEvents.NETHERITE_BLOCK_PLACE, () -> SoundEvents.NETHERITE_BLOCK_HIT, () -> SoundEvents.NETHERITE_BLOCK_FALL);

    public static final SoundType SOLID_METAL = new ForgeSoundType(1.0F, 1.0F, SOLID_METAL_BREAK, () -> SoundEvents.NETHERITE_BLOCK_STEP, SOLID_METAL_PLACE, () -> SoundEvents.NETHERITE_BLOCK_HIT, () -> SoundEvents.NETHERITE_BLOCK_FALL);

    public static final SoundType MACHINE = new ForgeSoundType(1.0F, 1.0F, MACHINE_BREAK, () -> SoundEvents.NETHERITE_BLOCK_STEP, MACHINE_PLACE, () -> SoundEvents.NETHERITE_BLOCK_HIT, () -> SoundEvents.NETHERITE_BLOCK_FALL);

    public static final SoundType LIGHT_MACHINE = new ForgeSoundType(1.0F, 1.5F, MACHINE_BREAK, () -> SoundEvents.NETHERITE_BLOCK_STEP, MACHINE_PLACE, () -> SoundEvents.NETHERITE_BLOCK_HIT, () -> SoundEvents.NETHERITE_BLOCK_FALL);

    public static final SoundType MULTIBLOCK_CENTER = new ForgeSoundType(1.0F, 1.0F, MULTIBLOCK_PACK, () -> SoundEvents.NETHERITE_BLOCK_STEP, MULTIBLOCK_UNPACK, () -> SoundEvents.NETHERITE_BLOCK_HIT, () -> SoundEvents.NETHERITE_BLOCK_FALL);

    public static final SoundType CAMINITE_MULTIBLOCK_CENTER = new ForgeSoundType(1.0F, 1.0F, MULTIBLOCK_PACK, CAMINITE_STEP, MULTIBLOCK_UNPACK, () -> SoundEvents.NETHERITE_BLOCK_HIT, () -> SoundEvents.NETHERITE_BLOCK_FALL);

    public static final SoundType ARCHAIC_MULTIBLOCK_CENTER = new ForgeSoundType(1.0F, 1.0F, MULTIBLOCK_PACK, () -> SoundEvents.NETHER_BRICKS_STEP, MULTIBLOCK_UNPACK, () -> SoundEvents.NETHER_BRICKS_HIT, () -> SoundEvents.NETHER_BRICKS_FALL);

    public static final SoundType MULTIBLOCK_EXTRA = new MultiblockSoundType(SoundType.NETHERITE_BLOCK);

    public static final SoundType CAMINITE_MULTIBLOCK_EXTRA = new MultiblockSoundType(CAMINITE);

    public static final SoundType ARCHAIC_MULTIBLOCK_EXTRA = new MultiblockSoundType(SoundType.NETHER_BRICKS);

    public static void init() {
    }

    public EmbersSounds(PackOutput generator, ExistingFileHelper helper) {
        super(generator, "embers", helper);
    }

    public static RegistryObject<SoundEvent> registerSound(String name) {
        return RegistryManager.SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("embers", name)));
    }

    @OnlyIn(Dist.CLIENT)
    public static void playMachineSound(BlockEntity tile, int id, SoundEvent soundIn, SoundSource categoryIn, boolean repeat, float volume, float pitch, float xIn, float yIn, float zIn) {
        Minecraft.getInstance().getSoundManager().play(new MachineSound(tile, id, soundIn, categoryIn, repeat, volume, pitch, xIn, yIn, zIn));
    }

    @OnlyIn(Dist.CLIENT)
    public static void playItemSoundClient(LivingEntity entity, Item item, SoundEvent soundIn, SoundSource categoryIn, boolean repeat, float volume, float pitch) {
        Minecraft.getInstance().getSoundManager().play(new ItemUseSound(entity, item, soundIn, categoryIn, repeat, volume, pitch));
    }

    public static void playItemSound(LivingEntity entity, Item item, SoundEvent soundIn, SoundSource categoryIn, boolean repeat, float volume, float pitch) {
        PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MessageItemSound(entity, item, soundIn, categoryIn, repeat, volume, pitch));
    }

    @Override
    public void registerSounds() {
        this.withSubtitle(ALCHEMY_FAIL, definition().with(sound(this.resource("alchemy_tablet/fail"))));
        this.withSubtitle(ALCHEMY_SUCCESS, definition().with(sound(this.resource("alchemy_tablet/success"))));
        this.add(ALCHEMY_LOOP, definition().with(sound(this.resource("alchemy_tablet/loop"))));
        this.withSubtitle(ALCHEMY_START, definition().with(sound(this.resource("alchemy_tablet/start"))));
        this.add(PEDESTAL_LOOP, definition().with(sound(this.resource("pedestal_loop"))));
        this.withSubtitle(BEAM_CANNON_FIRE, definition().with(sound(this.resource("beam_cannon/fire"))));
        this.withSubtitle(BEAM_CANNON_HIT, definition().with(sound(this.resource("beam_cannon/hit1")), sound(this.resource("beam_cannon/hit2"))));
        this.add(CRYSTAL_CELL_LOOP, definition().with(sound(this.resource("crystal_cell/loop"))));
        this.withSubtitle(CRYSTAL_CELL_GROW, definition().with(sound(this.resource("crystal_cell/zap1")), sound(this.resource("crystal_cell/zap2")), sound(this.resource("crystal_cell/zap3")), sound(this.resource("crystal_cell/zap4"))));
        this.add(GENERATOR_LOOP, definition().with(sound(this.resource("generator_hold_embers"))));
        this.withSubtitle(ACTIVATOR, definition().with(sound(this.resource("activator/plume1")), sound(this.resource("activator/plume2"))));
        this.withSubtitle(PRESSURE_REFINERY, definition().with(sound(this.resource("boiler/plume1")), sound(this.resource("boiler/plume2"))));
        this.withSubtitle(IGNEM_REACTOR, definition().with(sound(this.resource("ignem_reactor/plume1")), sound(this.resource("ignem_reactor/plume2"))));
        this.withSubtitle(BORE_START, definition().with(sound(this.resource("ember_bore/start"))));
        this.withSubtitle(BORE_STOP, definition().with(sound(this.resource("ember_bore/stop"))));
        this.add(BORE_LOOP, definition().with(sound(this.resource("ember_bore/run_loop"))));
        this.add(BORE_LOOP_MINE, definition().with(sound(this.resource("ember_bore/mine_loop"))));
        this.add(BORE_LOOP_SUPERSPEED, definition().with(sound(this.resource("ember_bore/superspeed_run_loop"))));
        this.add(BORE_LOOP_MINE_SUPERSPEED, definition().with(sound(this.resource("ember_bore/superspeed_mine_loop"))));
        this.withSubtitle(CATALYTIC_PLUG_START, definition().with(sound(this.resource("catalytic_plug/start"))));
        this.withSubtitle(CATALYTIC_PLUG_STOP, definition().with(sound(this.resource("catalytic_plug/stop"))));
        this.add(CATALYTIC_PLUG_LOOP, definition().with(sound(this.resource("catalytic_plug/run_loop"))));
        this.add(CATALYTIC_PLUG_LOOP_READY, definition().with(sound(this.resource("catalytic_plug/ready_loop"))));
        this.withSubtitle(STAMPER_DOWN, definition().with(sound(this.resource("stamper/down1")), sound(this.resource("stamper/down2")), sound(this.resource("stamper/down3"))));
        this.withSubtitle(STAMPER_UP, definition().with(sound(this.resource("stamper/up1")), sound(this.resource("stamper/up2")), sound(this.resource("stamper/up3"))));
        this.add(HEATCOIL_HIGH, definition().with(sound(this.resource("heat_coil/high_loop"))));
        this.add(HEATCOIL_MID, definition().with(sound(this.resource("heat_coil/mid_loop"))));
        this.add(HEATCOIL_LOW, definition().with(sound(this.resource("heat_coil/low_loop"))));
        this.add(HEATCOIL_COOK, definition().with(sound(this.resource("heat_coil/cooking_loop"))));
        this.add(PLINTH_LOOP, definition().with(sound(this.resource("cinder_plinth_loop"))));
        this.add(MELTER_LOOP, definition().with(sound(this.resource("melter_loop"))));
        this.add(MIXER_LOOP, definition().with(sound(this.resource("mixer_loop"))));
        this.add(COPPER_CHARGER_LOOP, definition().with(sound(this.resource("copper_charger_loop"))));
        this.add(COPPER_CHARGER_SIPHON_LOOP, definition().with(sound(this.resource("charger_siphon_loop"))));
        this.add(INJECTOR_LOOP, definition().with(sound(this.resource("injector_loop"))));
        this.add(METAL_SEED_LOOP, definition().with(sound(this.resource("metal_seed/loop"))));
        this.withSubtitle(METAL_SEED_PING, definition().with(sound(this.resource("metal_seed/ping1")), sound(this.resource("metal_seed/ping2")), sound(this.resource("metal_seed/ping3"))));
        this.withSubtitle(INFERNO_FORGE_FAIL, definition().with(sound(this.resource("inferno_forge/fail"))));
        this.withSubtitle(INFERNO_FORGE_SUCCESS, definition().with(sound(this.resource("inferno_forge/success"))));
        this.add(INFERNO_FORGE_LOOP, definition().with(sound(this.resource("inferno_forge/loop"))));
        this.withSubtitle(INFERNO_FORGE_START, definition().with(sound(this.resource("inferno_forge/start"))));
        this.withSubtitle(INFERNO_FORGE_OPEN, definition().with(sound(this.resource("inferno_forge/open"))));
        this.withSubtitle(INFERNO_FORGE_CLOSE, definition().with(sound(this.resource("inferno_forge/close"))));
        this.add(FIELD_CHART_LOOP, definition().with(sound(this.resource("field_chart_loop"))));
        this.withSubtitle(ENTROPIC_ENUMERATOR_TURN, definition().with(sound(this.resource("entropic_enumerator/turn1")), sound(this.resource("entropic_enumerator/turn2")), sound(this.resource("entropic_enumerator/turn3")), sound(this.resource("entropic_enumerator/turn4")), sound(this.resource("entropic_enumerator/turn5")), sound(this.resource("entropic_enumerator/turn6")), sound(this.resource("entropic_enumerator/turn7"))));
        this.withSubtitle(EMBER_EMIT, definition().with(sound(this.resource("ember_transmission/emit1")), sound(this.resource("ember_transmission/emit2")), sound(this.resource("ember_transmission/emit3")), sound(this.resource("ember_transmission/emit4"))));
        this.withSubtitle(EMBER_EMIT_BIG, definition().with(sound(this.resource("ember_transmission/emit_big1")), sound(this.resource("ember_transmission/emit_big2")), sound(this.resource("ember_transmission/emit_big3")), sound(this.resource("ember_transmission/emit_big4"))));
        this.withSubtitle(EMBER_RECEIVE, definition().with(sound(this.resource("ember_transmission/recep1")), sound(this.resource("ember_transmission/recep2")), sound(this.resource("ember_transmission/recep3")), sound(this.resource("ember_transmission/recep4"))));
        this.withSubtitle(EMBER_RECEIVE_BIG, definition().with(sound(this.resource("ember_transmission/recep_big1")), sound(this.resource("ember_transmission/recep_big2")), sound(this.resource("ember_transmission/recep_big3")), sound(this.resource("ember_transmission/recep_big4"))));
        this.withSubtitle(EMBER_RELAY, definition().with(sound(this.resource("ember_transmission/relay1")), sound(this.resource("ember_transmission/relay2")), sound(this.resource("ember_transmission/relay3")), sound(this.resource("ember_transmission/relay4"))));
        this.withSubtitle(STEAM_ENGINE_START_STEAM, definition().with(sound(this.resource("steam_engine/start_steam"))));
        this.withSubtitle(STEAM_ENGINE_START_BURN, definition().with(sound(this.resource("steam_engine/start_burn"))));
        this.add(STEAM_ENGINE_LOOP_STEAM, definition().with(sound(this.resource("steam_engine/steam_loop"))));
        this.add(STEAM_ENGINE_LOOP_BURN, definition().with(sound(this.resource("steam_engine/burn_loop"))));
        this.withSubtitle(STEAM_ENGINE_STOP, definition().with(sound(this.resource("steam_engine/stop"))));
        this.withSubtitle(MINI_BOILER_RUPTURE, definition().with(sound(this.resource("mini_boiler/rupture"))));
        this.add(MINI_BOILER_LOOP_SLOW, definition().with(sound(this.resource("mini_boiler/slow_loop"))));
        this.add(MINI_BOILER_LOOP_MID, definition().with(sound(this.resource("mini_boiler/mid_loop"))));
        this.add(MINI_BOILER_LOOP_FAST, definition().with(sound(this.resource("mini_boiler/fast_loop"))));
        this.add(MINI_BOILER_PRESSURE_LOW, definition().with(sound(this.resource("mini_boiler/pressure_loop1"))));
        this.add(MINI_BOILER_PRESSURE_MID, definition().with(sound(this.resource("mini_boiler/pressure_loop2"))));
        this.add(MINI_BOILER_PRESSURE_HIGH, definition().with(sound(this.resource("mini_boiler/pressure_loop3"))));
        this.withSubtitle(PUMP_SLOW, definition().with(sound(this.resource("pump/slow"))));
        this.withSubtitle(PUMP_MID, definition().with(sound(this.resource("pump/mid1")), sound(this.resource("pump/mid2")), sound(this.resource("pump/mid3"))));
        this.withSubtitle(PUMP_FAST, definition().with(sound(this.resource("pump/fast1")), sound(this.resource("pump/fast2")), sound(this.resource("pump/fast3"))));
        this.withSubtitle(PIPE_CONNECT, definition().with(sound(this.resource("pipe_connect1")), sound(this.resource("pipe_connect2"))));
        this.withSubtitle(PIPE_DISCONNECT, definition().with(sound(this.resource("pipe_disconnect1")), sound(this.resource("pipe_disconnect2"))));
        this.add(PIPE_VENT_LOOP, definition().with(sound(this.resource("pipe_vent"))));
        this.withSubtitle(FIREBALL_BIG, definition().with(sound(this.resource("fireball_big_launch1")), sound(this.resource("fireball_big_launch2"))));
        this.withSubtitle(FIREBALL_BIG_HIT, definition().with(sound(this.resource("fireball_big_hit1")), sound(this.resource("fireball_big_hit2"))));
        this.withSubtitle(FIREBALL, definition().with(sound(this.resource("fireball_small_launch1")), sound(this.resource("fireball_small_launch2"))));
        this.withSubtitle(FIREBALL_HIT, definition().with(sound(this.resource("fireball_small_hit1")), sound(this.resource("fireball_small_hit2"))));
        this.withSubtitle(BLAZING_RAY_FIRE, definition().with(sound(this.resource("ignition_cannon/fire1")), sound(this.resource("ignition_cannon/fire2"))));
        this.withSubtitle(BLAZING_RAY_EMPTY, definition().with(sound(this.resource("ignition_cannon/empty"))));
        this.withSubtitle(CINDER_STAFF_CHARGE, definition().with(sound(this.resource("cinder_staff/charge"))));
        this.withSubtitle(CINDER_STAFF_FAIL, definition().with(sound(this.resource("cinder_staff/fail"))));
        this.add(CINDER_STAFF_LOOP, definition().with(sound(this.resource("cinder_staff/loop"))));
        this.add(ULTRASYD_7F_PATTERNS, definition().with(sound(this.resource("7f_patterns")).stream()));
        this.withSubtitle(EXPLOSION_CHARM_ABSORB, definition().with(sound(this.resource("explosion_charm/absorb1")), sound(this.resource("explosion_charm/absorb2"))));
        this.add(EXPLOSION_CHARM_RECHARGE, definition().with(sound(this.resource("explosion_charm/recharge"))));
        this.withSubtitle(ASHEN_AMULET_BURN, definition().with(sound(this.resource("ash_amulet/burn1")), sound(this.resource("ash_amulet/burn2"))));
        this.withSubtitle(HEATED_ITEM_LEVELUP, definition().with(sound(this.resource("heated_tool_level"))));
        this.withSubtitle(RESONATING_BELL, definition().with(sound(this.resource("resonating_bell1")), sound(this.resource("resonating_bell2"))));
        this.withSubtitle(CINDER_JET, definition().with(sound(this.resource("cinder_jet1")), sound(this.resource("cinder_jet2"))));
        this.withSubtitle(INFLICTOR_GEM, definition().with(sound(this.resource("inflictor_gem_absorb"))));
        this.withSubtitle(METALLURGIC_DUST, definition().with(sound(this.resource("metallurgic_dust/convert1")), sound(this.resource("metallurgic_dust/convert2"))));
        this.withSubtitle(METALLURGIC_DUST_FAIL, definition().with(sound(this.resource("metallurgic_dust/fail1")), sound(this.resource("metallurgic_dust/fail2"))));
        this.add(CODEX_OPEN, definition().with(sound(this.resource("ancient_codex/open"))));
        this.add(CODEX_CLOSE, definition().with(sound(this.resource("ancient_codex/close"))));
        this.add(CODEX_CATEGORY_OPEN, definition().with(sound(this.resource("ancient_codex/category_open"))));
        this.add(CODEX_CATEGORY_CLOSE, definition().with(sound(this.resource("ancient_codex/category_close"))));
        this.add(CODEX_CATEGORY_SELECT, definition().with(sound(this.resource("ancient_codex/category_scrape_up1")), sound(this.resource("ancient_codex/category_scrape_up2")), sound(this.resource("ancient_codex/category_scrape_up3"))));
        this.add(CODEX_CATEGORY_UNSELECT, definition().with(sound(this.resource("ancient_codex/category_scrape_down1")), sound(this.resource("ancient_codex/category_scrape_down2")), sound(this.resource("ancient_codex/category_scrape_down3"))));
        this.add(CODEX_CATEGORY_SWITCH, definition().with(sound(this.resource("ancient_codex/flip1")), sound(this.resource("ancient_codex/flip2"))));
        this.add(CODEX_PAGE_OPEN, definition().with(sound(this.resource("ancient_codex/page_open"))));
        this.add(CODEX_PAGE_CLOSE, definition().with(sound(this.resource("ancient_codex/page_close"))));
        this.add(CODEX_PAGE_SWITCH, definition().with(sound(this.resource("ancient_codex/flip1")), sound(this.resource("ancient_codex/flip2"))));
        this.add(CODEX_CHECK, definition().with(sound(this.resource("ancient_codex/check"))));
        this.add(CODEX_UNCHECK, definition().with(sound(this.resource("ancient_codex/uncheck"))));
        this.add(CODEX_LOCK, definition().with(sound(this.resource("ancient_codex/lock"))));
        this.add(CODEX_UNLOCK, definition().with(sound(this.resource("ancient_codex/unlock"))));
        this.add(BAUBLE_EQUIP, definition().with(sound(this.resource("bauble_equip"))));
        this.add(BAUBLE_UNEQUIP, definition().with(sound(this.resource("bauble_unequip"))));
        this.add(TYRFING_HIT, definition().with(sound(this.resource("tyrfing_hit1")), sound(this.resource("tyrfing_hit2"))));
        this.add(SHIFTING_SCALES_BREAK, definition().with(sound(this.resource("scale_heart_break1")), sound(this.resource("scale_heart_break2")), sound(this.resource("scale_heart_break3")), sound(this.resource("scale_heart_break4"))));
        this.add(WINDING_GEARS_SPRING, definition().with(sound(this.resource("spring_launch1")), sound(this.resource("spring_launch2"))));
        this.withSubtitle(ANCIENT_GOLEM_STEP, definition().with(sound(this.resource("agolem/step1")), sound(this.resource("agolem/step2")), sound(this.resource("agolem/step3")), sound(this.resource("agolem/step4"))));
        this.withSubtitle(ANCIENT_GOLEM_HURT, definition().with(sound(this.resource("agolem/hit1")), sound(this.resource("agolem/hit2")), sound(this.resource("agolem/hit3")), sound(this.resource("agolem/hit4"))));
        this.withSubtitle(ANCIENT_GOLEM_PUNCH, definition().with(sound(this.resource("agolem/punch1")), sound(this.resource("agolem/punch2")), sound(this.resource("agolem/punch3")), sound(this.resource("agolem/punch4"))));
        this.withSubtitle(ANCIENT_GOLEM_DEATH, definition().with(sound(this.resource("agolem/die"))));
        this.add(MULTIBLOCK_PACK, definition().subtitle("subtitles.block.generic.break").with(sound(this.resource("multiblock_pack"))));
        this.add(MULTIBLOCK_UNPACK, definition().subtitle("subtitles.block.generic.place").with(sound(this.resource("multiblock_unpack"))));
        this.add(CAMINITE_BREAK, definition().subtitle("subtitles.block.generic.break").with(sound(this.resource("block/caminite/break1")), sound(this.resource("block/caminite/break2")), sound(this.resource("block/caminite/break3")), sound(this.resource("block/caminite/break4"))));
        this.add(CAMINITE_STEP, definition().subtitle("subtitles.block.generic.footsteps").with(sound(this.resource("block/caminite/step1")), sound(this.resource("block/caminite/step2")), sound(this.resource("block/caminite/step3")), sound(this.resource("block/caminite/step4"))));
        this.add(CAMINITE_PLACE, definition().subtitle("subtitles.block.generic.place").with(sound(this.resource("block/caminite/break1")), sound(this.resource("block/caminite/break2")), sound(this.resource("block/caminite/break3")), sound(this.resource("block/caminite/break4"))));
        this.add(CAMINITE_HIT, definition().subtitle("subtitles.block.generic.hit").with(sound(this.resource("block/caminite/step1")), sound(this.resource("block/caminite/step2")), sound(this.resource("block/caminite/step3")), sound(this.resource("block/caminite/step4"))));
        this.add(CAMINITE_FALL, definition().with(sound(this.resource("block/caminite/step1")), sound(this.resource("block/caminite/step2")), sound(this.resource("block/caminite/step3")), sound(this.resource("block/caminite/step4"))));
        this.add(ASHEN_STONE_BREAK, definition().subtitle("subtitles.block.generic.break").with(sound(this.resource("block/ashen_stone/break1")), sound(this.resource("block/ashen_stone/break2")), sound(this.resource("block/ashen_stone/break3")), sound(this.resource("block/ashen_stone/break4"))));
        this.add(ASHEN_STONE_STEP, definition().subtitle("subtitles.block.generic.footsteps").with(sound(this.resource("block/ashen_stone/step1")), sound(this.resource("block/ashen_stone/step2")), sound(this.resource("block/ashen_stone/step3")), sound(this.resource("block/ashen_stone/step4"))));
        this.add(ASHEN_STONE_PLACE, definition().subtitle("subtitles.block.generic.place").with(sound(this.resource("block/ashen_stone/break1")), sound(this.resource("block/ashen_stone/break2")), sound(this.resource("block/ashen_stone/break3")), sound(this.resource("block/ashen_stone/break4"))));
        this.add(ASHEN_STONE_HIT, definition().subtitle("subtitles.block.generic.hit").with(sound(this.resource("block/ashen_stone/step1")), sound(this.resource("block/ashen_stone/step2")), sound(this.resource("block/ashen_stone/step3")), sound(this.resource("block/ashen_stone/step4"))));
        this.add(ASHEN_STONE_FALL, definition().with(sound(this.resource("block/ashen_stone/step1")), sound(this.resource("block/ashen_stone/step2")), sound(this.resource("block/ashen_stone/step3")), sound(this.resource("block/ashen_stone/step4"))));
        this.add(SOLID_METAL_BREAK, definition().subtitle("subtitles.block.generic.break").with(sound(this.resource("block/solid_metal/break1")), sound(this.resource("block/solid_metal/break2")), sound(this.resource("block/solid_metal/break3")), sound(this.resource("block/solid_metal/break4"))));
        this.add(SOLID_METAL_PLACE, definition().subtitle("subtitles.block.generic.place").with(sound(this.resource("block/solid_metal/break1")), sound(this.resource("block/solid_metal/break2")), sound(this.resource("block/solid_metal/break3")), sound(this.resource("block/solid_metal/break4"))));
        this.add(MACHINE_BREAK, definition().subtitle("subtitles.block.generic.break").with(sound(this.resource("block/machine/break1")), sound(this.resource("block/machine/break2")), sound(this.resource("block/machine/break3")), sound(this.resource("block/machine/break4"))));
        this.add(MACHINE_PLACE, definition().subtitle("subtitles.block.generic.place").with(sound(this.resource("block/machine/break1")), sound(this.resource("block/machine/break2")), sound(this.resource("block/machine/break3")), sound(this.resource("block/machine/break4"))));
    }

    public void withSubtitle(RegistryObject<SoundEvent> soundEvent, SoundDefinition definition) {
        this.add(soundEvent, definition.subtitle("subtitles.embers." + soundEvent.getId().getPath()));
    }

    public ResourceLocation resource(String path) {
        return new ResourceLocation("embers", path);
    }
}