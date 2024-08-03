package com.mna.events;

import com.mna.KeybindInit;
import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.WellspringNode;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.items.IPositionalItem;
import com.mna.api.items.IRelic;
import com.mna.api.items.ITieredItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.effects.EffectHelper;
import com.mna.effects.EffectInit;
import com.mna.effects.interfaces.IDoubleTapEndEarly;
import com.mna.effects.interfaces.IInputDisable;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mna.entities.constructs.BubbleBoat;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.constructs.animated.ConstructDiagnostics;
import com.mna.factions.Factions;
import com.mna.gui.GuiTextures;
import com.mna.gui.HUDOverlayRenderer;
import com.mna.gui.radial.BellOfBiddingRadialSelect;
import com.mna.gui.radial.EnderDiscPatternRadialSelect;
import com.mna.gui.radial.ItemstackRadialSelect;
import com.mna.gui.radial.ManaweavingWandRadialSelect;
import com.mna.gui.radial.ModifierRadialSelect;
import com.mna.gui.radial.RitualKitRadialSelect;
import com.mna.gui.radial.SpellRadialSelect;
import com.mna.gui.widgets.PlayerInventoryButton;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.items.base.IRadialInventorySelect;
import com.mna.items.constructs.BellOfBidding;
import com.mna.items.manaweaving.ItemManaweaverWand;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.sorcery.ItemSpellBook;
import com.mna.network.ClientMessageDispatcher;
import com.mna.network.messages.to_server.PossessionInputMessage;
import com.mna.particles.types.movers.ParticleBezierMover;
import com.mna.particles.types.movers.ParticleOrbitMover;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.DidYouKnowHelper;
import com.mna.tools.PossessionHelper;
import com.mna.tools.math.MathUtils;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.MultiblockRenderer;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ClientEventHandler {

    private static boolean toolMenuKeyWasDown;

    private static boolean uiModifierKeyWasDown;

    private static boolean spellChordModifierKeyWasUp;

    private static boolean[] slotKeysWereDown = new boolean[] { false, false, false, false, false, false, false, false, false, false };

    public static boolean shiftPressed = false;

    private static float fogAmount = 0.0F;

    private static float fogDecay = 0.01F;

    public static int decrementFog = 0;

    public static float[] fogColor = new float[] { 1.0F, 1.0F, 1.0F };

    private static long spaceTapTimer = 0L;

    private static boolean lastTickJump = false;

    private static Entity cachedRenderEntity = null;

    private static final ResourceLocation oredict_ores = new ResourceLocation("forge", "ores");

    public static void setFogAmount(float amount) {
        fogAmount = amount;
        decrementFog = 20;
    }

    public static float getFogAmount() {
        return fogAmount;
    }

    public static void wipeOpen() {
        while (((KeyMapping) KeybindInit.RadialMenuOpen.get()).consumeClick()) {
        }
    }

    @SubscribeEvent
    public static void handleKeys(TickEvent.ClientTickEvent ev) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) {
            handleRadialKeyDown();
            handleUIModifierKeyDown();
            handleSpellBookChordsKeyDown();
        } else {
            toolMenuKeyWasDown = true;
            uiModifierKeyWasDown = true;
            spellChordModifierKeyWasUp = true;
            for (int i = 0; i < slotKeysWereDown.length; i++) {
                slotKeysWereDown[i] = true;
            }
        }
    }

    private static void handleSpellBookChordsKeyDown() {
        Minecraft mc = Minecraft.getInstance();
        boolean spellChordModifierKeyIsDown = ((KeyMapping) KeybindInit.SpellBookChord.get()).isDown();
        if (spellChordModifierKeyIsDown) {
            if (spellChordModifierKeyWasUp) {
                HUDOverlayRenderer.instance.isRenderingSpellBookChords = !HUDOverlayRenderer.instance.isRenderingSpellBookChords;
                spellChordModifierKeyWasUp = false;
            }
        } else {
            spellChordModifierKeyWasUp = true;
        }
        if (HUDOverlayRenderer.instance.isRenderingSpellBookChords) {
            KeyMapping[] slotKeyBinds = mc.options.keyHotbarSlots;
            for (int i = 0; i < slotKeyBinds.length; i++) {
                boolean slotKeyDown = slotKeyBinds[i].isDown();
                if (slotKeyDown != slotKeysWereDown[i]) {
                    while (slotKeyBinds[i].consumeClick()) {
                    }
                    ItemStack mainHandItem = mc.player.m_21205_();
                    ItemStack offHandItem = mc.player.m_21206_();
                    boolean offhand = false;
                    ItemStack inHand = ItemStack.EMPTY;
                    if (mainHandItem.getItem() instanceof ItemSpellBook) {
                        inHand = mainHandItem;
                    } else if (offHandItem.getItem() instanceof ItemSpellBook) {
                        inHand = offHandItem;
                        offhand = true;
                    }
                    if (!inHand.isEmpty()) {
                        ItemSpellBook.setSlot(mc.player, inHand, i, offhand, true);
                    }
                    HUDOverlayRenderer.instance.isRenderingSpellBookChords = false;
                    spellChordModifierKeyWasUp = true;
                }
                slotKeysWereDown[i] = slotKeyDown;
            }
        }
    }

    private static void handleUIModifierKeyDown() {
        Minecraft mc = Minecraft.getInstance();
        boolean uiModifierKeyIsDown = ((KeyMapping) KeybindInit.InventoryItemOpen.get()).isDown();
        if (uiModifierKeyIsDown != uiModifierKeyWasDown) {
            ClientMessageDispatcher.sendItemUIOpenMessage(uiModifierKeyIsDown);
            mc.player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.setModifierPressed(uiModifierKeyIsDown));
        }
        uiModifierKeyWasDown = uiModifierKeyIsDown;
    }

    private static void handleRadialKeyDown() {
        Minecraft mc = Minecraft.getInstance();
        boolean toolMenuKeyIsDown = ((KeyMapping) KeybindInit.RadialMenuOpen.get()).isDown();
        if (toolMenuKeyIsDown && !toolMenuKeyWasDown) {
            while (((KeyMapping) KeybindInit.RadialMenuOpen.get()).consumeClick()) {
                if (mc.screen == null) {
                    ItemStack inHand = mc.player.m_21205_();
                    boolean checkOffhand = true;
                    if (inHand.getItem() instanceof ItemSpellBook) {
                        mc.setScreen(new SpellRadialSelect(false));
                        checkOffhand = false;
                    } else if (inHand.getItem() == ItemInit.MODIFIER_BOOK.get()) {
                        mc.setScreen(new ModifierRadialSelect(false));
                        checkOffhand = false;
                    } else if (inHand.getItem() == ItemInit.ENDER_DISK.get()) {
                        mc.setScreen(new EnderDiscPatternRadialSelect(false));
                        checkOffhand = false;
                    } else if (inHand.getItem() instanceof ItemPractitionersPouch) {
                        mc.setScreen(new RitualKitRadialSelect(false));
                        checkOffhand = false;
                    } else if (inHand.getItem() instanceof BellOfBidding) {
                        mc.setScreen(new BellOfBiddingRadialSelect(false));
                        checkOffhand = false;
                    } else if (inHand.getItem() instanceof ItemManaweaverWand) {
                        mc.setScreen(new ManaweavingWandRadialSelect(false));
                        checkOffhand = false;
                    } else if (inHand.getItem() instanceof IRadialInventorySelect) {
                        mc.setScreen(new ItemstackRadialSelect(false).setOverrideStackCount(((IRadialInventorySelect) inHand.getItem()).capacity()));
                        checkOffhand = false;
                    }
                    if (checkOffhand) {
                        inHand = mc.player.m_21206_();
                        if (inHand.getItem() instanceof ItemSpellBook) {
                            mc.setScreen(new SpellRadialSelect(true));
                        } else if (inHand.getItem() == ItemInit.MODIFIER_BOOK.get()) {
                            mc.setScreen(new ModifierRadialSelect(true));
                        } else if (inHand.getItem() == ItemInit.ENDER_DISK.get()) {
                            mc.setScreen(new EnderDiscPatternRadialSelect(true));
                        } else if (inHand.getItem() instanceof ItemPractitionersPouch) {
                            mc.setScreen(new RitualKitRadialSelect(true));
                        } else if (inHand.getItem() instanceof BellOfBidding) {
                            mc.setScreen(new BellOfBiddingRadialSelect(true));
                            checkOffhand = false;
                        } else if (inHand.getItem() instanceof ItemManaweaverWand) {
                            mc.setScreen(new ManaweavingWandRadialSelect(true));
                            checkOffhand = false;
                        } else if (inHand.getItem() instanceof IRadialInventorySelect) {
                            mc.setScreen(new ItemstackRadialSelect(true).setOverrideStackCount(((IRadialInventorySelect) inHand.getItem()).capacity()));
                        }
                    }
                }
            }
        }
        toolMenuKeyWasDown = toolMenuKeyIsDown;
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getInstance().isPaused()) {
            Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
            if (player != null) {
                DidYouKnowHelper.CheckAndShowDidYouKnow(player, "helptip.mna.pretty_models");
                Level world = ManaAndArtifice.instance.proxy.getClientWorld();
                renderWellsprings(world, player);
                if (player.m_21023_(EffectInit.SNOWBLIND.get())) {
                    fogDecay = 0.01F;
                    if (fogAmount < fogDecay * 2.0F) {
                        fogAmount = fogDecay * 2.0F;
                    }
                    Vec3 pos = player.m_20182_();
                    Vec3 lastPos = ManaAndArtifice.instance.proxy.getClientLastTickPosition();
                    lastPos = new Vec3(lastPos.x, 0.0, lastPos.z);
                    pos = new Vec3(pos.x, 0.0, pos.z);
                    if (lastPos != Vec3.ZERO) {
                        double dist = pos.distanceToSqr(lastPos);
                        if (dist > 0.078) {
                            double amount = (dist - 0.078) * 10.0;
                            if ((double) fogAmount < amount) {
                                fogAmount = (float) amount;
                            }
                        }
                    }
                    if (fogAmount > 2.0F) {
                        fogAmount = 2.0F;
                    }
                } else {
                    fogDecay = 0.01F;
                }
            }
            if (fogAmount > 0.0F && decrementFog-- < 0) {
                fogAmount = fogAmount - fogDecay;
            }
            ManaAndArtifice.instance.proxy.incrementTick();
            if (player != null && player.m_21023_(EffectInit.CHOOSING_WELLSPRING.get())) {
                Level world = ManaAndArtifice.instance.proxy.getClientWorld();
                world.getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                    int distx = GeneralConfigValues.WellspringDistance;
                    int halfDist = distx / 2;
                    HashMap<BlockPos, WellspringNode> nodes = m.getWellspringRegistry().getNearbyNodes(player.m_20183_(), 50, distx + halfDist);
                    Vec3 start = player.m_20182_();
                    for (Entry<BlockPos, WellspringNode> entry : nodes.entrySet()) {
                        BlockPos targetPos = (BlockPos) entry.getKey();
                        Vec3 targetVec3d = new Vec3((double) targetPos.m_123341_(), player.m_20186_() + 1.0, (double) targetPos.m_123343_());
                        Vec3 dir = targetVec3d.subtract(start).normalize();
                        WellspringNode node = (WellspringNode) entry.getValue();
                        MAParticleType particle = null;
                        if (node.isClaimed()) {
                            int[] color = node.getAffinity().getShiftAffinity().getColor();
                            particle = new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()).setColor(color[0], color[1], color[2]);
                        } else {
                            particle = new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()).setColor(255, 255, 255);
                        }
                        particle.setScale(0.05F);
                        world.addParticle(particle, player.m_20185_() + dir.x() + Math.random() * 0.2 - 0.1, player.m_20186_() + 1.0 + dir.y() + Math.random() * 0.2 - 0.1, player.m_20189_() + dir.z() + Math.random() * 0.2 - 0.1, dir.x, dir.y, dir.z);
                    }
                });
            }
            if (player != null && player.m_21023_(EffectInit.COLD_DARK.get())) {
                fogAmount = 0.95F;
                fogColor[0] = 0.05F;
                fogColor[1] = 0.05F;
                fogColor[2] = 0.05F;
            }
        }
    }

    private static void renderWellsprings(Level world, Player player) {
        world.getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.getWellspringRegistry().getNearbyNodes(player.m_20183_(), 0, 32).forEach((pos, node) -> {
            if (node.isClaimed() || player.m_21023_(EffectInit.ELDRIN_SIGHT.get()) || player.m_21023_(EffectInit.WELLSPRING_SIGHT.get())) {
                if (!node.hasForcedYLevel() || !(Math.abs(player.m_20186_() - (double) node.getYLevel()) > 32.0)) {
                    Affinity aff = node.getAffinity().getShiftAffinity();
                    double yPos = node.hasForcedYLevel() ? (double) node.getYLevel() : player.m_20186_();
                    int[] colors = aff.getColor();
                    int[] sparkleColors = null;
                    float scale = 1.0F;
                    float strength = node.getStrength();
                    float spread = strength / 25.0F / 2.0F;
                    float lightSpread = spread;
                    int affPfxAmount = 2;
                    float lightScale = 1.0F;
                    float beamVelUp = 0.1F;
                    int lightLifetime = 20;
                    float[] velocities = new float[] { 0.0F, 0.2F, 0.0F };
                    Vec3 llPos = new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5);
                    MAParticleType type = null;
                    switch(aff) {
                        case FIRE:
                        case HELLFIRE:
                            type = ParticleInit.FLAME.get();
                            scale = 0.3F;
                            break;
                        case WATER:
                        case ICE:
                            type = ParticleInit.WATER.get();
                            scale = 0.02F + (float) Math.random() * 0.04F;
                            spread = (float) ((double) spread + 0.35);
                            lightScale = 0.75F;
                            break;
                        case EARTH:
                            type = ParticleInit.DUST.get();
                            scale = 0.2F + (float) (Math.random() * 0.2F) * spread;
                            velocities = new float[] { 0.0F, -0.13F, 0.0F };
                            sparkleColors = new int[] { 30, 14, 10 };
                            colors = new int[] { 30, 14, 10 };
                            affPfxAmount = 3;
                            lightScale = 0.625F * spread + 0.1F;
                            lightLifetime = 20;
                            beamVelUp = 0.01F;
                            lightSpread = 0.02F * spread;
                            if (Math.random() < (double) (spread + 0.25F)) {
                                world.addParticle(new MAParticleType(ParticleInit.EARTH.get()).setPhysics(false).setMaxAge(60).setScale(0.1F).setMover(new ParticleOrbitMover(llPos.x(), yPos, llPos.z(), 0.1, -0.025, (double) (1.0F * spread))), llPos.x(), yPos - 3.0 + Math.random() * 10.0, llPos.z(), 0.0, 0.0, 0.0);
                            }
                            break;
                        case WIND:
                            type = ParticleInit.AIR_ORBIT.get();
                            affPfxAmount = (int) Math.ceil((double) (8.0F * spread));
                            scale = 0.2F;
                            sparkleColors = new int[] { 20, 20, 20 };
                            colors = new int[] { 40, 40, 40 };
                            velocities = new float[] { 0.3F, 0.2F, spread * 2.0F };
                            lightScale = 0.5F * spread + 0.15F;
                            beamVelUp = 0.2F;
                            spread *= 0.0F;
                            break;
                        case ENDER:
                            type = ParticleInit.ENDER_VELOCITY.get();
                            scale = 0.3F;
                            lightScale = 0.4F;
                            lightSpread = 0.5F - spread;
                            spread = 0.5F - spread;
                            world.addParticle(new MAParticleType(ParticleInit.ENDER_VELOCITY.get()).setPhysics(false).setMaxAge(60).setScale(0.1F).setMover(new ParticleOrbitMover(llPos.x(), yPos, llPos.z(), Math.random() * 0.1, Math.random() * 0.07, 0.4)), llPos.x(), yPos - 3.0 + Math.random() * 10.0, llPos.z(), 0.0, 0.0, 0.0);
                            break;
                        case ARCANE:
                            type = ParticleInit.ARCANE_MAGELIGHT.get();
                            scale = 0.15F;
                            affPfxAmount = 3;
                            colors = new int[] { 102, 32, 232 };
                            lightSpread = 0.05F;
                            if (player.m_9236_().getGameTime() % 20L == 0L) {
                                world.addParticle(new MAParticleType(ParticleInit.TRAIL_ORBIT.get()).setColor(colors[0], colors[1], colors[2]).setPhysics(false).setMaxAge(60), llPos.x(), yPos - 4.0, llPos.z(), 0.25, 0.25, (double) spread);
                            }
                    }
                    if (aff != Affinity.UNKNOWN) {
                        for (int i = 0; i < 15; i++) {
                            world.addParticle(new MAParticleType(ParticleInit.LIGHT_VELOCITY.get()).setScale(lightScale).setColor(colors[0], colors[1], colors[2]).setMaxAge(lightLifetime), llPos.x() - (double) lightSpread + Math.random() * (double) lightSpread * 2.0, yPos - 3.0 + Math.random() * 10.0, llPos.z() - (double) lightSpread + Math.random() * (double) lightSpread * 2.0, 0.0, (double) beamVelUp, 0.0);
                        }
                    }
                    if (type != null) {
                        for (int i = 0; i < affPfxAmount; i++) {
                            MAParticleType particle = new MAParticleType(type).setScale(scale).setPhysics(false);
                            if (sparkleColors != null) {
                                particle.setColor(sparkleColors[0], sparkleColors[1], sparkleColors[2]);
                            }
                            world.addParticle(particle, llPos.x() - (double) spread + Math.random() * (double) spread * 2.0, yPos - 3.0 + Math.random() * 10.0, llPos.z() - (double) spread + Math.random() * (double) spread * 2.0, (double) velocities[0], (double) velocities[1], (double) velocities[2]);
                        }
                    }
                    if (player.m_9236_().getGameTime() % 20L == 0L) {
                        if (!node.hasForcedYLevel()) {
                            world.addParticle(new MAParticleType(ParticleInit.TRAIL_ORBIT.get()).setColor(colors[0], colors[1], colors[2]).setScale(0.2F), llPos.x(), yPos - 1.0, llPos.z(), 0.25, 0.25, 0.65);
                        } else {
                            int rnd = (int) (Math.random() * 4.0);
                            Vec3 start = new Vec3(llPos.x() - 0.5, yPos - 2.0, llPos.z() - 0.5);
                            Vec3 end = null;
                            Vec3 control_a = null;
                            Vec3 control_b = null;
                            switch(rnd) {
                                case 0:
                                    end = new Vec3(llPos.x(), yPos + 2.0, llPos.z() + 5.0);
                                    control_a = new Vec3(llPos.x(), yPos + 4.0, llPos.z() + 1.0);
                                    control_b = new Vec3(llPos.x(), yPos + 4.0, llPos.z() + 3.0);
                                    break;
                                case 1:
                                    end = new Vec3(llPos.x() + 5.0, yPos + 2.0, llPos.z());
                                    control_a = new Vec3(llPos.x() + 1.0, yPos + 4.0, llPos.z());
                                    control_b = new Vec3(llPos.x() + 3.0, yPos + 4.0, llPos.z());
                                    break;
                                case 2:
                                    end = new Vec3(llPos.x() - 5.0, yPos + 2.0, llPos.z());
                                    control_a = new Vec3(llPos.x() - 1.0, yPos + 4.0, llPos.z());
                                    control_b = new Vec3(llPos.x() - 3.0, yPos + 4.0, llPos.z());
                                    break;
                                case 3:
                                    end = new Vec3(llPos.x(), yPos + 2.0, llPos.z() - 5.0);
                                    control_a = new Vec3(llPos.x(), yPos + 4.0, llPos.z() - 1.0);
                                    control_b = new Vec3(llPos.x(), yPos + 4.0, llPos.z() - 3.0);
                            }
                            world.addParticle(new MAParticleType(ParticleInit.TRAIL_BEZIER.get()).setColor(colors[0], colors[1], colors[2]).setMaxAge(30).setAgePadding(5).setScale(0.2F).setMover(new ParticleBezierMover(start, end, control_a, control_b)), start.x, start.y, start.z, end.x, end.y, end.z);
                        }
                    }
                }
            }
        }));
    }

    @SubscribeEvent
    public static void entityTick(LivingEvent.LivingTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.player != null && event.getEntity() != null) {
            if (event.getEntity() != mc.player) {
                if (!(event.getEntity().m_20280_(mc.player) > 256.0)) {
                    MobEffectInstance existingEffect = event.getEntity().getEffect(MobEffects.GLOWING);
                    if (mc.player.m_21023_(EffectInit.ELDRIN_SIGHT.get()) && (existingEffect == null || existingEffect.getDuration() < 5)) {
                        event.getEntity().addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 0, false, false));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            MobEffectInstance posession = mc.player.m_21124_(EffectInit.POSSESSION.get());
            if (event.isAttack() && posession != null && posession.getAmplifier() > 0) {
                event.setCanceled(true);
                ClientMessageDispatcher.sendPosessionClickInput();
            }
        }
    }

    @SubscribeEvent
    public static void onMouseWheel(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if (mc.player.isUsingItem()) {
                ItemStack using = mc.player.m_21211_();
                ISpellDefinition recipe = null;
                if (using.getItem() instanceof ItemSpellBook) {
                    recipe = SpellRecipe.fromNBT(((ItemSpellBook) using.getItem()).getSpellCompound(using, mc.player));
                } else if (using.getItem() instanceof ICanContainSpell) {
                    recipe = ((ICanContainSpell) using.getItem()).getSpell(using);
                }
                if (recipe != null && recipe.isValid() && recipe.isChanneled()) {
                    event.setCanceled(true);
                    float maximum = recipe.getShape().getValue(Attribute.RANGE);
                    mc.player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                        float delta = (float) event.getScrollDelta();
                        m.offsetFocusDistance(delta, maximum);
                        ClientMessageDispatcher.sendPlayerFocusDistanceChange(delta, maximum);
                    });
                    event.setResult(Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerInputEvent(MovementInputUpdateEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if (spaceTapTimer > 0L) {
                spaceTapTimer--;
            }
            if (mc.player.m_21023_(EffectInit.CONFUSION.get()) && GeneralConfigValues.ConfuseAffectsPlayers) {
                event.getInput().forwardImpulse = -event.getInput().forwardImpulse;
                event.getInput().leftImpulse = -event.getInput().leftImpulse;
            }
            MobEffectInstance posession = mc.player.m_21124_(EffectInit.POSSESSION.get());
            if (posession != null && posession.getAmplifier() > 0) {
                ClientMessageDispatcher.sendPosessionMovementInput(event.getInput().up ? 1.0F : (event.getInput().down ? -1.0F : 0.0F), event.getInput().left ? 1.0F : (event.getInput().right ? -1.0F : 0.0F), event.getInput().jumping, event.getInput().shiftKeyDown, mc.player.m_146908_(), mc.player.f_20885_, mc.player.m_146909_());
                PossessionInputMessage msg = PossessionInputMessage.movement(event.getInput().up ? 1.0F : (event.getInput().down ? -1.0F : 0.0F), event.getInput().left ? 1.0F : (event.getInput().right ? -1.0F : 0.0F), event.getInput().jumping, event.getInput().shiftKeyDown, mc.player.m_146908_(), mc.player.f_20885_, mc.player.m_146909_());
                if (mc.player.getPersistentData().contains("posessed_entity_id")) {
                    int id = mc.player.getPersistentData().getInt("posessed_entity_id");
                    Entity e = mc.player.m_9236_().getEntity(id);
                    if (e != null && e instanceof Mob) {
                        PossessionHelper.handleRemoteInput(msg, mc.player, (Mob) e);
                    }
                }
            }
            if (!mc.player.m_20096_() && event.getInput().jumping && mc.player.f_20954_ == 0) {
                ItemStack boots = mc.player.m_6844_(EquipmentSlot.FEET);
                int enchLevel = boots.getEnchantmentLevel(EnchantmentInit.LEAPING.get());
                if (!boots.isEmpty() && enchLevel > 0) {
                    mc.player.f_20954_ = 10;
                    ClientMessageDispatcher.sendPlayerJump();
                }
            }
            if (!lastTickJump && event.getInput().jumping && mc.player.m_21220_().stream().anyMatch(e -> e.getEffect() instanceof IDoubleTapEndEarly)) {
                if (spaceTapTimer == 0L) {
                    spaceTapTimer = 7L;
                } else {
                    EffectHelper.removeDoubleTapEvents(mc.player);
                    ClientMessageDispatcher.sendRequestEndControlEffectEarlyMessage();
                    spaceTapTimer = 0L;
                }
            }
            lastTickJump = event.getInput().jumping;
            int mask = InputDisabler.getDisableInputMask(event.getEntity());
            IPlayerMagic magic = (IPlayerMagic) mc.player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            boolean teleporting = magic == null ? false : magic.getIsTeleporting() || magic.getTeleportElapsedTicks() > 0;
            if ((mask & IInputDisable.InputMask.MOVEMENT.mask()) != 0 || teleporting) {
                event.getInput().up = false;
                event.getInput().down = false;
                event.getInput().left = false;
                event.getInput().right = false;
                event.getInput().jumping = false;
                event.getInput().forwardImpulse = 0.0F;
                event.getInput().leftImpulse = 0.0F;
                event.getInput().shiftKeyDown = false;
            } else if ((mask & IInputDisable.InputMask.JUMP.mask()) != 0 || teleporting) {
                event.getInput().jumping = false;
                event.getInput().shiftKeyDown = false;
            }
        }
    }

    public static boolean isKeyDown(KeyMapping keybind) {
        if (keybind.isUnbound()) {
            return false;
        } else {
            boolean isDown = false;
            switch(keybind.getKey().getType()) {
                case KEYSYM:
                    isDown = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue());
                    break;
                case MOUSE:
                    isDown = GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue()) == 1;
            }
            return isDown && keybind.getKeyConflictContext().isActive() && keybind.getKeyModifier().isActive(keybind.getKeyConflictContext());
        }
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelStageEvent rwl) {
        if (rwl.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            PoseStack matrixStack = rwl.getPoseStack();
            Minecraft mc = Minecraft.getInstance();
            Vec3 projectedView = mc.gameRenderer.getMainCamera().getPosition();
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
            matrixStack.pushPose();
            matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
            renderMarkingRunes(mc, matrixStack, projectedView, buffers);
            renderConstructWorldDiagnostics(mc, matrixStack, projectedView, buffers);
            MobEffectInstance inst = mc.player.m_21124_(EffectInit.DIVINATION.get());
            if (inst != null) {
                renderNearbyOres(mc, matrixStack, projectedView, buffers, inst.getAmplifier() + 2);
            }
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
            renderDistantWellsprings(mc.level, mc.getFrameTime(), matrixStack, buffers, mc.player);
            matrixStack.popPose();
            buffers.endBatch();
        }
    }

    private static void renderDistantWellsprings(Level world, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, Player player) {
        world.getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.getWellspringRegistry().getNearbyNodes(player.m_20183_(), 0, 128).forEach((pos, node) -> {
            if (node.isClaimed() || player.m_21023_(EffectInit.ELDRIN_SIGHT.get()) || player.m_21023_(EffectInit.WELLSPRING_SIGHT.get())) {
                if (!node.hasForcedYLevel() || !(Math.abs(player.m_20186_() - (double) node.getYLevel()) > 32.0)) {
                    BlockPos adjPos = new BlockPos(pos.m_123341_(), player.m_20183_().m_123342_(), pos.m_123343_());
                    double dist = Math.sqrt(player.m_20183_().m_123331_(adjPos));
                    Affinity aff = node.getAffinity().getShiftAffinity();
                    int alpha = 176;
                    int distFalloff = 12;
                    int distFalloffStart = 38;
                    int height = 13;
                    if (dist < (double) distFalloffStart && aff != Affinity.ENDER && aff != Affinity.UNKNOWN) {
                        double distAdjusted = (double) distFalloffStart - dist;
                        double distPct = 1.0 - distAdjusted / (double) distFalloff;
                        alpha = (int) ((double) alpha * distPct);
                        if (alpha < 0) {
                            alpha = 0;
                        }
                    }
                    if (!node.hasForcedYLevel()) {
                        height = 255;
                    }
                    int[] color = aff.getColor();
                    double yPos = node.hasForcedYLevel() ? (double) (node.getYLevel() - 4) : 0.0;
                    Vec3 llPos = new Vec3((double) pos.m_123341_() + 0.5, yPos, (double) pos.m_123343_() + 0.5);
                    matrixStack.pushPose();
                    matrixStack.translate(llPos.x, llPos.y, llPos.z);
                    WorldRenderUtils.renderBeam(world, partialTicks, matrixStack, buffer, 15728640, llPos, llPos.add(0.0, (double) height, 0.0), 1.0F, color, alpha, 0.25F, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
                    matrixStack.popPose();
                }
            }
        }));
    }

    private static void renderConstructWorldDiagnostics(Minecraft mc, PoseStack matrixStack, Vec3 projectedView, MultiBufferSource.BufferSource buffers) {
        Construct c = HUDOverlayRenderer.instance.getTrackedConstruct();
        if (c != null) {
            LinkedList<ConstructDiagnostics.TaskHistoryEntry> taskHistory = c.getDiagnostics().getTaskHistory();
            if (taskHistory.size() != 0) {
                int drawDist = 4096;
                float opacity = 1.0F;
                float opacityDecrease = 0.9F / (float) taskHistory.size();
                RenderSystem.disableDepthTest();
                for (int i = taskHistory.size() - 1; i >= 0; i--) {
                    matrixStack.pushPose();
                    ConstructDiagnostics.TaskHistoryEntry mostRecentTask = (ConstructDiagnostics.TaskHistoryEntry) taskHistory.get(i);
                    float[] color = mostRecentTask.colorFromStatusCode();
                    if (mostRecentTask.getEntityID() > 0) {
                        if (cachedRenderEntity == null || cachedRenderEntity.getId() != mostRecentTask.getEntityID()) {
                            cachedRenderEntity = mc.level.getEntity(mostRecentTask.getEntityID());
                        }
                        if (cachedRenderEntity != null && cachedRenderEntity.isAlive() && mc.player.m_20280_(cachedRenderEntity) < (double) drawDist) {
                            LevelRenderer.renderLineBox(matrixStack, buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK), new AABB(cachedRenderEntity.blockPosition()).deflate(0.375), color[0], color[1], color[2], opacity);
                        }
                    }
                    if (mostRecentTask.getArea() != null && mc.player.m_20238_(mostRecentTask.getArea().getCenter()) < (double) drawDist + Math.pow(mostRecentTask.getArea().getSize() / 2.0, 2.0)) {
                        LevelRenderer.renderLineBox(matrixStack, buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK), mostRecentTask.getArea(), color[0], color[1], color[2], opacity);
                    }
                    if (mostRecentTask.getPos() != null && mc.player.m_20238_(mostRecentTask.getPos()) < (double) drawDist) {
                        LevelRenderer.renderLineBox(matrixStack, buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK), new AABB(BlockPos.containing(mostRecentTask.getPos())).deflate(0.375), color[0], color[1], color[2], opacity);
                    }
                    opacity -= opacityDecrease;
                    matrixStack.popPose();
                }
                Vec3 targetPos = c.getDiagnostics().getMovePos();
                if (targetPos != null && mc.player.m_20238_(targetPos) < (double) drawDist && mc.player.m_20280_(c) < (double) drawDist) {
                    VertexConsumer consumer = buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK);
                    BlockPos blockPos = BlockPos.containing(targetPos);
                    LevelRenderer.renderLineBox(matrixStack, consumer, new AABB(blockPos).deflate(0.375), 1.0F, 1.0F, 1.0F, 1.0F);
                    targetPos = Vec3.atCenterOf(blockPos);
                    Matrix4f positions = matrixStack.last().pose();
                    Matrix3f normals = matrixStack.last().normal();
                    Vec3 cPos = c.m_20182_();
                    consumer.vertex(positions, (float) cPos.x, (float) cPos.y, (float) cPos.z).color(1, 1, 1, 1).normal(normals, 1.0F, 0.0F, 0.0F).endVertex();
                    consumer.vertex(positions, (float) targetPos.x, (float) targetPos.y, (float) targetPos.z).color(1, 1, 1, 1).normal(normals, 1.0F, 0.0F, 0.0F).endVertex();
                }
                RenderSystem.enableDepthTest();
            }
        }
    }

    private static void renderMarkingRunes(Minecraft mc, PoseStack matrixStack, Vec3 projectedView, MultiBufferSource.BufferSource buffers) {
        int drawDist = 4096;
        ItemStack mainHand = mc.player.m_21205_();
        ItemStack offHand = mc.player.m_21206_();
        BlockPos mainHandPos = null;
        BlockPos offhandPos = null;
        boolean render = false;
        if (mainHand.getItem() instanceof IPositionalItem) {
            mainHandPos = ((IPositionalItem) mainHand.getItem()).getLocation(mainHand);
            render = true;
        }
        if (offHand.getItem() instanceof IPositionalItem) {
            offhandPos = ((IPositionalItem) offHand.getItem()).getLocation(offHand);
            render = true;
        }
        if (render) {
            boolean drawMainHand = false;
            boolean drawOffHand = false;
            RenderSystem.disableDepthTest();
            if (mainHandPos != null && mc.player.m_20275_((double) mainHandPos.m_123341_(), (double) mainHandPos.m_123342_(), (double) mainHandPos.m_123343_()) < (double) drawDist) {
                LevelRenderer.renderLineBox(matrixStack, buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK), new AABB(mainHandPos).deflate(0.25), 0.0F, 1.0F, 0.0F, 1.0F);
                Direction face = ItemInit.RUNE_MARKING.get().getFace(mainHand);
                float offsetAmt = 0.4F;
                Vec3 offsetVec = new Vec3((double) ((float) face.getStepX() * offsetAmt), (double) ((float) face.getStepY() * offsetAmt), (double) ((float) face.getStepZ() * offsetAmt));
                LevelRenderer.renderLineBox(matrixStack, buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK), new AABB(mainHandPos).move(offsetVec).deflate(0.49).contract(-offsetVec.x, -offsetVec.y, -offsetVec.z), 1.0F, 1.0F, 1.0F, 1.0F);
                drawMainHand = true;
            }
            if (offhandPos != null && mc.player.m_20275_((double) offhandPos.m_123341_(), (double) offhandPos.m_123342_(), (double) offhandPos.m_123343_()) < (double) drawDist) {
                LevelRenderer.renderLineBox(matrixStack, buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK), new AABB(offhandPos).deflate(0.25), 1.0F, 0.0F, 0.0F, 1.0F);
                Direction face = ItemInit.RUNE_MARKING.get().getFace(offHand);
                float offsetAmt = 0.4F;
                Vec3 offsetVec = new Vec3((double) ((float) face.getStepX() * offsetAmt), (double) ((float) face.getStepY() * offsetAmt), (double) ((float) face.getStepZ() * offsetAmt));
                LevelRenderer.renderLineBox(matrixStack, buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK), new AABB(offhandPos).move(offsetVec).deflate(0.49).contract(-offsetVec.x, -offsetVec.y, -offsetVec.z), 1.0F, 1.0F, 1.0F, 1.0F);
                drawOffHand = true;
            }
            if (drawMainHand && drawOffHand) {
                LevelRenderer.renderLineBox(matrixStack, buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK), MathUtils.createInclusiveBB(mainHandPos, offhandPos), 1.0F, 1.0F, 0.0F, 1.0F);
            }
            RenderSystem.enableDepthTest();
        }
    }

    private static void renderNearbyOres(Minecraft mc, PoseStack matrixStack, Vec3 projectedView, MultiBufferSource.BufferSource buffers, int radius) {
        ItemStack offhand = mc.player.m_21206_();
        boolean searchOffhand = offhand.getItem() instanceof BlockItem;
        BlockPos offset = BlockPos.containing(mc.player.m_20156_().normalize().scale((double) radius));
        BlockPos origin = mc.player.m_20183_().offset(offset);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos target = origin.offset(x, y, z);
                    BlockState state = mc.level.m_8055_(target);
                    if (searchOffhand && state.m_60734_() == ((BlockItem) offhand.getItem()).getBlock() || !searchOffhand && (state.m_60734_() instanceof DropExperienceBlock || MATags.isBlockIn(state.m_60734_(), oredict_ores))) {
                        LevelRenderer.renderLineBox(matrixStack, buffers.getBuffer(MARenderTypes.MARKING_RUNE_MARK), new AABB(target), 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderBlockScreenEffectEvent event) {
        if (event.getBlockState().m_60734_() == Blocks.FIRE) {
            Entity ridingEntity = event.getPlayer().m_20202_();
            if (ridingEntity != null && ridingEntity instanceof BubbleBoat && ((BubbleBoat) ridingEntity).isBrimstone()) {
                event.setCanceled(true);
            } else if (event.getPlayer().getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemInit.BRIMSTONE_BOAT.get()) {
                event.setCanceled(true);
            } else if (CuriosApi.getCuriosHelper().findFirstCurio(event.getPlayer(), ItemInit.EMBERGLOW_BRACELET.get()).isPresent()) {
                event.setCanceled(true);
            } else if (ItemInit.DEMON_ARMOR_CHEST.get().isSetEquipped(event.getPlayer())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onInventoryGuiInit(ScreenEvent.Init.Post evt) {
        Screen screen = evt.getScreen();
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            boolean isCreative = screen instanceof CreativeModeInventoryScreen;
            Tuple<Integer, Integer> offsets = PlayerInventoryButton.getOffsets(isCreative);
            int x = offsets.getA();
            int y = offsets.getB();
            int textureOffsetX = 6;
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            if (HUDOverlayRenderer.instance.getPinnedrecipe() != null || HUDOverlayRenderer.instance.getConstructDisplay().isValid()) {
                evt.addListener(new PlayerInventoryButton(gui, gui.getGuiLeft() + x, gui.f_96544_ / 2 + y, 5, 10, textureOffsetX, 27, 10, GuiTextures.Widgets.WIDGETS, button -> {
                    HUDOverlayRenderer.instance.setPinnedRecipe(null);
                    if (HUDOverlayRenderer.instance.getTrackedConstruct() != null) {
                        HUDOverlayRenderer.instance.getTrackedConstruct().setRequestingDiagnostics(false);
                        HUDOverlayRenderer.instance.setTrackedConstruct(null);
                    }
                    button.f_93623_ = false;
                    button.f_93624_ = false;
                }, Component.translatable("gui.mna.clear_pinned_prompt")));
            }
            if (MultiblockRenderer.hasMultiblock) {
                evt.addListener(new PlayerInventoryButton(gui, gui.getGuiLeft() + x, gui.f_96544_ / 2 + y + 10, 5, 5, textureOffsetX, 47, 5, GuiTextures.Widgets.WIDGETS, button -> MultiblockRenderer.setMultiblock(null, null, true), Component.translatable("gui.mna.clear_multiblock_prompt")));
                evt.addListener(new PlayerInventoryButton(gui, gui.getGuiLeft() + x, gui.f_96544_ / 2 + y + 20, 5, 5, textureOffsetX, 57, 5, GuiTextures.Widgets.WIDGETS, button -> MultiblockRenderer.toggleLowestLayerMode(), Component.translatable("gui.mna.multiblock_mode_prompt")));
            }
        }
    }

    @SubscribeEvent
    public static void onFogRenderEvent(ViewportEvent.RenderFog event) {
        if (event.getCamera().getFluidInCamera() == FogType.LAVA) {
            Minecraft mc = Minecraft.getInstance();
            if (ItemInit.DEMON_ARMOR_CHEST.get().isSetEquipped(mc.player) || CuriosInterop.IsItemInCurioSlot(ItemInit.EMBERGLOW_BRACELET.get(), mc.player, SlotTypePreset.BRACELET)) {
                event.setNearPlaneDistance(-8.0F);
                event.setFarPlaneDistance(192.0F);
                event.setCanceled(true);
            }
        }
        if (event.getCamera().getFluidInCamera() == FogType.WATER) {
            Minecraft mc = Minecraft.getInstance();
            mc.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (p.getAlliedFaction() == Factions.UNDEAD) {
                    event.setNearPlaneDistance(-8.0F);
                    event.setFarPlaneDistance(192.0F);
                    event.setCanceled(true);
                }
            });
        }
        if (fogAmount > 0.0F) {
            float f1 = Mth.lerp(Math.min(1.0F, fogAmount), event.getFarPlaneDistance(), 5.0F);
            float f2 = 0.0F;
            float f3 = f1 * 0.8F;
            RenderSystem.setShaderFogStart(f2);
            RenderSystem.setShaderFogEnd(f3);
        }
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (fogAmount > 0.0F) {
            event.setRed(fogColor[0]);
            event.setGreen(fogColor[1]);
            event.setBlue(fogColor[2]);
        }
    }

    @SubscribeEvent
    public static void onTooltipRender(ItemTooltipEvent event) {
        Level world = ManaAndArtifice.instance.proxy.getClientWorld();
        if (world != null) {
            if (event.getItemStack().getItem() instanceof IRelic relic) {
                Component c = relic.getHoverAddition(event.getItemStack(), world, event.getToolTip(), event.getFlags());
                if (c != null) {
                    event.getToolTip().add(1, c);
                }
            }
            if (event.getItemStack().getItem() instanceof ITieredItem item) {
                if (event.getItemStack().hasTag() && event.getItemStack().getTag().contains("hideTier")) {
                    return;
                }
                int tier = item.getTier(world, event.getItemStack());
                if (tier == -2) {
                    event.getToolTip().add(Component.translatable("gui.mna.item-tier.none").withStyle(ChatFormatting.GREEN));
                } else {
                    MutableBoolean metTier = new MutableBoolean(false);
                    if (tier > 0) {
                        Player clientPlayer = ManaAndArtifice.instance.proxy.getClientPlayer();
                        if (clientPlayer != null) {
                            clientPlayer.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                                if (p.getTier() >= tier) {
                                    metTier.setTrue();
                                }
                            });
                        }
                        event.getToolTip().add(Component.translatable("gui.mna.item-tier", tier).withStyle(metTier.getValue() ? ChatFormatting.GREEN : ChatFormatting.RED));
                    } else {
                        event.getToolTip().add(Component.translatable("gui.mna.item-tier.none").withStyle(ChatFormatting.GREEN));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRecipesUpdated(RecipesUpdatedEvent event) {
        ItemInit.ITEMS.getEntries().forEach(i -> {
            Item item = (Item) i.get();
            if (item instanceof ITieredItem tiered) {
                tiered.setCachedTier(tiered.resolveTier(ManaAndArtifice.instance.proxy.getClientWorld(), new ItemStack(item)));
            }
        });
    }
}