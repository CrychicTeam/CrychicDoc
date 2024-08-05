package io.redspace.ironsspellbooks.player;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.gui.EldritchResearchScreen;
import io.redspace.ironsspellbooks.network.ClientboundCastErrorMessage;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.spells.ice.FrostStepSpell;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ClientSpellCastHelper {

    private static boolean suppressRightClicks;

    public static boolean shouldSuppressRightClicks() {
        return suppressRightClicks;
    }

    public static void setSuppressRightClicks(boolean suppressRightClicks) {
        ClientSpellCastHelper.suppressRightClicks = suppressRightClicks;
    }

    public static void openEldritchResearchScreen(InteractionHand hand) {
        Minecraft.getInstance().setScreen(new EldritchResearchScreen(Component.empty(), hand));
    }

    public static void handleCastErrorMessage(ClientboundCastErrorMessage packet) {
        AbstractSpell spell = SpellRegistry.getSpell(packet.spellId);
        if (packet.errorType == ClientboundCastErrorMessage.ErrorType.COOLDOWN) {
            if (ClientInputEvents.hasReleasedSinceCasting) {
                Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("ui.irons_spellbooks.cast_error_cooldown", spell.getDisplayName(Minecraft.getInstance().player)).withStyle(ChatFormatting.RED), false);
            }
        } else {
            Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("ui.irons_spellbooks.cast_error_mana", spell.getDisplayName(Minecraft.getInstance().player)).withStyle(ChatFormatting.RED), false);
        }
    }

    public static void handleClientboundBloodSiphonParticles(Vec3 pos1, Vec3 pos2) {
        if (Minecraft.getInstance().player != null) {
            Level level = Minecraft.getInstance().player.f_19853_;
            Vec3 direction = pos2.subtract(pos1).scale(0.1F);
            for (int i = 0; i < 40; i++) {
                Vec3 scaledDirection = direction.scale(1.0 + Utils.getRandomScaled(0.35));
                Vec3 random = new Vec3(Utils.getRandomScaled(0.08F), Utils.getRandomScaled(0.08F), Utils.getRandomScaled(0.08F));
                level.addParticle(ParticleHelper.BLOOD, pos1.x, pos1.y, pos1.z, scaledDirection.x + random.x, scaledDirection.y + random.y, scaledDirection.z + random.z);
            }
        }
    }

    public static void handleClientboundShockwaveParticle(Vec3 pos, float radius, ParticleType<?> particleType) {
        if (Minecraft.getInstance().player != null && particleType instanceof ParticleOptions) {
            Level level = Minecraft.getInstance().player.f_19853_;
            int count = (int) ((float) (Math.PI * 2) * radius) * 2;
            float angle = 360.0F / (float) count * (float) (Math.PI / 180.0);
            for (int i = 0; i < count; i++) {
                Vec3 motion = new Vec3((double) (Mth.cos(angle * (float) i) * radius), 0.0, (double) (Mth.sin(angle * (float) i) * radius)).scale((double) Utils.random.nextIntBetweenInclusive(50, 70) * 0.00155);
                level.addParticle((ParticleOptions) particleType, pos.x + motion.x * 4.0, pos.y, pos.z + motion.z * 4.0, motion.x, motion.y, motion.z);
            }
        }
    }

    public static void handleClientsideHealParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = Minecraft.getInstance().player.f_19853_;
            int i = PotionUtils.getColor(Potion.byName("healing"));
            double d0 = (double) (i >> 16 & 0xFF) / 255.0;
            double d1 = (double) (i >> 8 & 0xFF) / 255.0;
            double d2 = (double) (i >> 0 & 0xFF) / 255.0;
            for (int j = 0; j < 15; j++) {
                level.addParticle(ParticleTypes.ENTITY_EFFECT, pos.x + Utils.getRandomScaled(0.25), pos.y + Utils.getRandomScaled(1.0) + 1.0, pos.z + Utils.getRandomScaled(0.25), d0, d1, d2);
            }
        }
    }

    public static void handleClientsideAbsorptionParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = Minecraft.getInstance().player.f_19853_;
            int i = 16239960;
            double d0 = (double) (i >> 16 & 0xFF) / 255.0;
            double d1 = (double) (i >> 8 & 0xFF) / 255.0;
            double d2 = (double) (i >> 0 & 0xFF) / 255.0;
            for (int j = 0; j < 15; j++) {
                level.addParticle(ParticleTypes.ENTITY_EFFECT, pos.x + Utils.getRandomScaled(0.25), pos.y + Utils.getRandomScaled(1.0), pos.z + Utils.getRandomScaled(0.25), d0, d1, d2);
            }
        }
    }

    public static void handleClientboundOakskinParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        RandomSource randomsource = player.m_217043_();
        for (int i = 0; i < 50; i++) {
            double d0 = (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
            double d1 = (double) Mth.randomBetween(randomsource, 0.0F, 2.0F);
            double d2 = (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
            ParticleOptions particleType = (ParticleOptions) (randomsource.nextFloat() < 0.1F ? ParticleHelper.FIREFLY : new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_WOOD.defaultBlockState()));
            player.f_19853_.addParticle(particleType, pos.x + d0, pos.y + d1, pos.z + d2, d0 * 0.05, 0.05, d2 * 0.05);
        }
    }

    public static void handleClientsideRegenCloudParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = player.f_19853_;
            int ySteps = 16;
            int xSteps = 48;
            float yDeg = 180.0F / (float) ySteps * (float) (Math.PI / 180.0);
            float xDeg = 360.0F / (float) xSteps * (float) (Math.PI / 180.0);
            for (int x = 0; x < xSteps; x++) {
                for (int y = 0; y < ySteps; y++) {
                    Vec3 offset = new Vec3(0.0, 0.0, 5.0).yRot((float) y * yDeg).xRot((float) x * xDeg).zRot((float) (-Math.PI / 2)).multiply(1.0, 0.85F, 1.0);
                    level.addParticle(DustParticleOptions.REDSTONE, pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    public static void handleClientsideFortifyAreaParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = player.f_19853_;
            int ySteps = 128;
            float yDeg = 180.0F / (float) ySteps * (float) (Math.PI / 180.0);
            for (int y = 0; y < ySteps; y++) {
                Vec3 offset = new Vec3(0.0, 0.0, 16.0).yRot((float) y * yDeg);
                Vec3 motion = new Vec3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).scale(0.1);
                level.addParticle(ParticleHelper.WISP, pos.x + offset.x, 1.0 + pos.y + offset.y, pos.z + offset.z, motion.x, motion.y, motion.z);
            }
        }
    }

    private static void animatePlayerStart(Player player, ResourceLocation resourceLocation) {
        KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(resourceLocation);
        if (keyframeAnimation != null) {
            ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(SpellAnimations.ANIMATION_RESOURCE);
            if (animation != null) {
                KeyframeAnimationPlayer castingAnimationPlayer = new KeyframeAnimationPlayer(keyframeAnimation);
                ClientMagicData.castingAnimationPlayerLookup.put(player.m_20148_(), castingAnimationPlayer);
                Boolean armsFlag = ClientConfigs.SHOW_FIRST_PERSON_ARMS.get();
                Boolean itemsFlag = ClientConfigs.SHOW_FIRST_PERSON_ITEMS.get();
                if (!armsFlag && !itemsFlag) {
                    castingAnimationPlayer.setFirstPersonMode(FirstPersonMode.DISABLED);
                } else {
                    castingAnimationPlayer.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                    castingAnimationPlayer.setFirstPersonConfiguration(new FirstPersonConfiguration(armsFlag, armsFlag, itemsFlag, itemsFlag));
                }
                animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(2, Ease.INOUTSINE), castingAnimationPlayer, true);
            }
        }
    }

    public static void handleClientboundOnClientCast(String spellId, int level, CastSource castSource, ICastData castData) {
        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        spell.onClientCast(Minecraft.getInstance().player.f_19853_, level, Minecraft.getInstance().player, castData);
    }

    public static void handleClientboundTeleport(Vec3 pos1, Vec3 pos2) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = Minecraft.getInstance().player.f_19853_;
            TeleportSpell.particleCloud(level, pos1);
            TeleportSpell.particleCloud(level, pos2);
        }
    }

    public static void handleClientboundFieryExplosion(Vec3 pos, float radius) {
        MinecraftInstanceHelper.ifPlayerPresent(player -> {
            Level level = player.f_19853_;
            double x = pos.x;
            double y = pos.y;
            double z = pos.z;
            level.addParticle(new BlastwaveParticleOptions(new Vector3f(1.0F, 0.6F, 0.3F), radius + 1.0F), x, y, z, 0.0, 0.0, 0.0);
            int c = (int) (6.28 * (double) radius) * 3;
            float step = 360.0F / (float) c * (float) (Math.PI / 180.0);
            float speed = 0.06F + 0.01F * radius;
            for (int i = 0; i < c; i++) {
                Vec3 vec3 = new Vec3((double) Mth.cos(step * (float) i), 0.0, (double) Mth.sin(step * (float) i)).scale((double) speed);
                Vec3 posOffset = Utils.getRandomVec3(0.5).add(vec3.scale(10.0));
                vec3 = vec3.add(Utils.getRandomVec3(0.01));
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x + posOffset.x, y + posOffset.y, z + posOffset.z, vec3.x, vec3.y, vec3.z);
            }
            int cloudDensity = 50 + (int) (25.0F * radius);
            for (int i = 0; i < cloudDensity; i++) {
                Vec3 posOffset = Utils.getRandomVec3(1.0).scale((double) (radius * 0.4F));
                Vec3 motion = posOffset.normalize().scale((double) (speed * 0.5F));
                posOffset = posOffset.add(motion.scale(Utils.getRandomScaled(1.0)));
                motion = motion.add(Utils.getRandomVec3(0.02));
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x + posOffset.x, y + posOffset.y, z + posOffset.z, motion.x, motion.y, motion.z);
            }
            for (int i = 0; i < cloudDensity; i += 2) {
                Vec3 posOffset = Utils.getRandomVec3(1.0).scale((double) (radius * 0.4F));
                Vec3 motion = posOffset.normalize().scale((double) (speed * 0.5F));
                motion = motion.add(Utils.getRandomVec3(0.25));
                level.addParticle(ParticleHelper.EMBERS, true, x + posOffset.x, y + posOffset.y, z + posOffset.z, motion.x, motion.y, motion.z);
                level.addParticle(ParticleHelper.FIRE, x + posOffset.x * 0.5, y + posOffset.y * 0.5, z + posOffset.z * 0.5, motion.x, motion.y, motion.z);
            }
            for (int i = 0; i < cloudDensity; i += 2) {
                Vec3 posOffset = Utils.getRandomVec3((double) radius).scale(0.2F);
                Vec3 motion = posOffset.normalize().scale(0.6);
                motion = motion.add(Utils.getRandomVec3(0.18));
                level.addParticle(ParticleHelper.FIERY_SPARKS, x + posOffset.x * 0.5, y + posOffset.y * 0.5, z + posOffset.z * 0.5, motion.x, motion.y, motion.z);
            }
        });
    }

    public static void handleClientboundFrostStep(Vec3 pos1, Vec3 pos2) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = Minecraft.getInstance().player.f_19853_;
            FrostStepSpell.particleCloud(level, pos1);
            FrostStepSpell.particleCloud(level, pos2);
        }
    }

    public static void handleClientBoundOnCastStarted(UUID castingEntityId, String spellId, int spellLevel) {
        Player player = Minecraft.getInstance().player.f_19853_.m_46003_(castingEntityId);
        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        spell.getCastStartAnimation().getForPlayer().ifPresent(resourceLocation -> animatePlayerStart(player, resourceLocation));
        spell.onClientPreCast(player.f_19853_, spellLevel, player, player.m_7655_(), null);
    }

    public static void handleClientBoundOnCastFinished(UUID castingEntityId, String spellId, boolean cancelled) {
        ClientMagicData.resetClientCastState(castingEntityId);
        Player player = Minecraft.getInstance().player.f_19853_.m_46003_(castingEntityId);
        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        AnimationHolder finishAnimation = spell.getCastFinishAnimation();
        if (finishAnimation.getForPlayer().isPresent() && !cancelled) {
            animatePlayerStart(player, (ResourceLocation) finishAnimation.getForPlayer().get());
        } else if (finishAnimation != AnimationHolder.pass() || cancelled) {
            KeyframeAnimationPlayer animationPlayer = (KeyframeAnimationPlayer) ClientMagicData.castingAnimationPlayerLookup.getOrDefault(castingEntityId, null);
            if (animationPlayer != null) {
                animationPlayer.stop();
            }
        }
        if (cancelled && spell.stopSoundOnCancel()) {
            spell.getCastStartSound().ifPresent(soundEvent -> Minecraft.getInstance().getSoundManager().stop(soundEvent.getLocation(), null));
        }
        if (castingEntityId.equals(Minecraft.getInstance().player.m_20148_()) && ClientInputEvents.isUseKeyDown) {
            ClientInputEvents.hasReleasedSinceCasting = false;
        }
    }
}