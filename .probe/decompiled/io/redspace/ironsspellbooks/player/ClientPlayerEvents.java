package io.redspace.ironsspellbooks.player;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.attribute.IMagicAttribute;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.effect.AbyssalShroudEffect;
import io.redspace.ironsspellbooks.effect.AscensionEffect;
import io.redspace.ironsspellbooks.effect.CustomDescriptionMobEffect;
import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltManager;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingMusicManager;
import io.redspace.ironsspellbooks.item.CastingItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.IMultihandWeapon;
import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.spells.blood.RayOfSiphoningSpell;
import io.redspace.ironsspellbooks.spells.ender.RecallSpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber({ Dist.CLIENT })
public class ClientPlayerEvents {

    @SubscribeEvent
    public static void onPlayerLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
        IronsSpellbooks.LOGGER.debug("ClientPlayerNetworkEvent onPlayerLogOut");
        DeadKingMusicManager.hardStop();
        GuidingBoltManager.handleClientLogout();
        ClientMagicData.spellSelectionManager = null;
        if (event.getPlayer() != null) {
            ClientMagicData.resetClientCastState(event.getPlayer().m_20148_());
        }
    }

    @SubscribeEvent
    public static void onPlayerOpenScreen(ScreenEvent.Opening event) {
        if (ClientMagicData.isCasting()) {
            Messages.sendToServer(new ServerboundCancelCast(SpellRegistry.getSpell(ClientMagicData.getCastingSpellId()).getCastType() == CastType.CONTINUOUS));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient() && event.phase == TickEvent.Phase.END && event.player == Minecraft.getInstance().player) {
            ClientLevel level = Minecraft.getInstance().level;
            ClientMagicData.getRecasts().tickRecasts();
            ClientMagicData.getCooldowns().tick(1);
            if (ClientMagicData.getCastDuration() > 0) {
                ClientMagicData.handleCastDuration();
            }
            if (level != null) {
                List<Entity> spellcasters = level.m_6249_((Entity) null, event.player.m_20191_().inflate(64.0), mob -> mob instanceof Player || mob instanceof IMagicEntity);
                spellcasters.forEach(entity -> {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    SyncedSpellData spellData = ClientMagicData.getSyncedSpellData(livingEntity);
                    if (spellData.hasEffect(8L)) {
                        AbyssalShroudEffect.ambientParticles(level, livingEntity);
                    }
                    if (spellData.hasEffect(16L)) {
                        AscensionEffect.ambientParticles(level, livingEntity);
                    }
                    if (livingEntity.isAutoSpinAttack() && spellData.getSpinAttackType() == SpinAttackType.FIRE) {
                        BurningDashSpell.ambientParticles(level, livingEntity);
                    }
                    if (spellData.isCasting()) {
                        if (spellData.getCastingSpellId().equals(SpellRegistry.RAY_OF_SIPHONING_SPELL.get().getSpellId())) {
                            Vec3 impact = Utils.raycastForEntity(entity.level, entity, RayOfSiphoningSpell.getRange(0), true).getLocation().subtract(0.0, 0.25, 0.0);
                            for (int i = 0; i < 8; i++) {
                                Vec3 motion = new Vec3(Utils.getRandomScaled(0.2F), Utils.getRandomScaled(0.2F), Utils.getRandomScaled(0.2F));
                                entity.level.addParticle(ParticleHelper.SIPHON, impact.x + motion.x, impact.y + motion.y, impact.z + motion.z, motion.x, motion.y, motion.z);
                            }
                        } else if (spellData.getCastingSpellId().equals(SpellRegistry.RECALL_SPELL.get().getSpellId())) {
                            RecallSpell.ambientParticles(livingEntity, spellData);
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof LocalPlayer player) {
            ClientMagicData.spellSelectionManager = new SpellSelectionManager(player);
        }
    }

    @SubscribeEvent
    public static void beforeLivingRender(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            LivingEntity livingEntity = event.getEntity();
            if (livingEntity instanceof Player || livingEntity instanceof IMagicEntity) {
                SyncedSpellData syncedData = ClientMagicData.getSyncedSpellData(livingEntity);
                if (syncedData.hasEffect(32L) && livingEntity.m_20177_(player)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void afterLivingRender(RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player) {
            SyncedSpellData syncedData = ClientMagicData.getSyncedSpellData(livingEntity);
            if (syncedData.isCasting()) {
                SpellRenderingHelper.renderSpellHelper(syncedData, livingEntity, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
            }
        }
    }

    @SubscribeEvent
    public static void imbuedWeaponTooltips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof Scroll)) {
            MinecraftInstanceHelper.ifPlayerPresent(player1 -> {
                LocalPlayer player = (LocalPlayer) player1;
                List<Component> lines = event.getToolTip();
                boolean advanced = event.getFlags().isAdvanced();
                if (stack.getItem() instanceof CastingItem) {
                    handleCastingImplementTooltip(stack, player, lines, advanced);
                }
                if (ISpellContainer.isSpellContainer(stack) && !(stack.getItem() instanceof SpellBook)) {
                    handleImbuedSpellTooltip(stack, player, lines, advanced);
                }
                if (ISpellContainer.isSpellContainer(stack) && Utils.canImbue(stack)) {
                    ISpellContainer spellContainer = ISpellContainer.get(stack);
                    lines.add(1, Component.translatable("tooltip.irons_spellbooks.can_be_imbued_frame", Component.translatable("tooltip.irons_spellbooks.can_be_imbued_number", spellContainer.getActiveSpellCount(), spellContainer.getMaxSpellCount()).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GOLD));
                }
                if (stack.getItem() instanceof IMultihandWeapon) {
                    if (ServerConfigs.APPLY_ALL_MULTIHAND_ATTRIBUTES.get()) {
                        int i = TooltipsUtils.indexOfComponent(lines, "item.modifiers.mainhand");
                        if (i >= 0) {
                            lines.set(i, Component.translatable("tooltip.irons_spellbooks.modifiers.multihand").withStyle(((Component) lines.get(i)).getStyle()));
                        }
                    } else {
                        int i = TooltipsUtils.indexOfComponent(lines, "item.modifiers.mainhand");
                        if (i >= 0) {
                            int endIndex = 0;
                            List<Integer> linesToGrab = new ArrayList();
                            for (int j = i; j < lines.size(); j++) {
                                ComponentContents contents = ((Component) lines.get(j)).getContents();
                                if (contents instanceof TranslatableContents) {
                                    TranslatableContents translatableContents = (TranslatableContents) contents;
                                    if (translatableContents.getKey().startsWith("attribute.modifier")) {
                                        endIndex = j;
                                        for (Object arg : translatableContents.getArgs()) {
                                            if (arg instanceof Component) {
                                                Component component = (Component) arg;
                                                ComponentContents patt12244$temp = component.getContents();
                                                if (patt12244$temp instanceof TranslatableContents) {
                                                    TranslatableContents translatableContents2 = (TranslatableContents) patt12244$temp;
                                                    if (getAttributeForDescriptionId(translatableContents2.getKey()) instanceof IMagicAttribute) {
                                                        linesToGrab.add(j);
                                                    }
                                                }
                                            }
                                        }
                                    } else if (i != j && translatableContents.getKey().startsWith("item.modifiers")) {
                                        break;
                                    }
                                } else {
                                    for (Component line : ((Component) lines.get(j)).getSiblings()) {
                                        ComponentContents patt13367$temp = line.getContents();
                                        if (patt13367$temp instanceof TranslatableContents) {
                                            TranslatableContents translatableContents = (TranslatableContents) patt13367$temp;
                                            if (translatableContents.getKey().startsWith("attribute.modifier")) {
                                                endIndex = j;
                                            }
                                        }
                                    }
                                }
                            }
                            if (!linesToGrab.isEmpty()) {
                                lines.add(++endIndex, Component.empty());
                                lines.add(++endIndex, Component.translatable("tooltip.irons_spellbooks.modifiers.multihand").withStyle(((Component) lines.get(i)).getStyle()));
                                for (Integer index : linesToGrab) {
                                    lines.add(++endIndex, (Component) lines.get(index));
                                }
                                for (int jx = linesToGrab.size() - 1; jx >= 0; jx--) {
                                    lines.remove((Integer) linesToGrab.get(jx));
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private static void handleImbuedSpellTooltip(ItemStack stack, LocalPlayer player, List<Component> lines, boolean advanced) {
        ISpellContainer spellContainer = ISpellContainer.get(stack);
        int i = advanced ? TooltipsUtils.indexOfAdvancedText(lines, stack) : lines.size();
        if (!spellContainer.isEmpty()) {
            ArrayList<Component> additionalLines = new ArrayList();
            spellContainer.getActiveSpells().forEach(spellSlot -> {
                List<MutableComponent> spellTooltip = TooltipsUtils.formatActiveSpellTooltip(stack, spellSlot, CastSource.SWORD, player);
                spellTooltip.set(1, Component.literal(" ").append((Component) spellTooltip.get(1)));
                additionalLines.addAll(spellTooltip);
            });
            additionalLines.add(1, Component.translatable("tooltip.irons_spellbooks.imbued_tooltip").withStyle(ChatFormatting.GRAY));
            lines.addAll(i < 0 ? lines.size() : i, additionalLines);
        }
    }

    private static void handleCastingImplementTooltip(ItemStack stack, LocalPlayer player, List<Component> lines, boolean advanced) {
        SpellData spellSlot = ClientMagicData.getSpellSelectionManager().getSelectedSpellData();
        if (spellSlot != SpellData.EMPTY) {
            List<MutableComponent> additionalLines = TooltipsUtils.formatActiveSpellTooltip(stack, spellSlot, CastSource.SWORD, player);
            additionalLines.add(1, Component.translatable("tooltip.irons_spellbooks.casting_implement_tooltip").withStyle(ChatFormatting.GRAY));
            additionalLines.set(2, Component.literal(" ").append((Component) additionalLines.get(2)));
            additionalLines.add(Component.literal(" ").append(Component.translatable("tooltip.irons_spellbooks.press_to_cast_active", Component.keybind("key.use")).withStyle(ChatFormatting.GOLD)));
            int i = advanced ? TooltipsUtils.indexOfAdvancedText(lines, stack) : lines.size();
            lines.addAll(i < 0 ? lines.size() : i, additionalLines);
        }
    }

    private static Attribute getAttributeForDescriptionId(String descriptionId) {
        return (Attribute) ForgeRegistries.ATTRIBUTES.getValues().stream().filter(attribute -> attribute.getDescriptionId().equals(descriptionId)).findFirst().orElse(null);
    }

    @SubscribeEvent
    public static void customPotionTooltips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<MobEffectInstance> mobEffects = PotionUtils.getMobEffects(stack);
        if (mobEffects.size() > 0) {
            for (MobEffectInstance mobEffectInstance : mobEffects) {
                if (mobEffectInstance.getEffect() instanceof CustomDescriptionMobEffect customDescriptionMobEffect) {
                    CustomDescriptionMobEffect.handleCustomPotionTooltip(stack, event.getToolTip(), event.getFlags().isAdvanced(), mobEffectInstance, customDescriptionMobEffect);
                }
            }
        }
    }

    @SubscribeEvent
    public static void changeFogColor(ViewportEvent.ComputeFogColor event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.m_21023_(MobEffectRegistry.PLANAR_SIGHT.get())) {
            int color = MobEffectRegistry.PLANAR_SIGHT.get().getColor();
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            f += (float) (color >> 16 & 0xFF) / 255.0F;
            f1 += (float) (color >> 8 & 0xFF) / 255.0F;
            f2 += (float) (color >> 0 & 0xFF) / 255.0F;
            event.setRed(f * 0.15F);
            event.setGreen(f1 * 0.15F);
            event.setBlue(f2 * 0.15F);
        }
    }
}