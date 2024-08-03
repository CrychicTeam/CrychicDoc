package com.rekindled.embers.augment;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.api.EmbersAPI;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.event.ScaleEvent;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.network.message.MessageScalesData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class ShiftingScalesAugment extends AugmentBase {

    public static final ResourceLocation TEXTURE_HUD = new ResourceLocation("embers", "textures/gui/icons.png");

    public static final ResourceLocation SCALES = new ResourceLocation("embers", "scales");

    public static final int COOLDOWN = 33;

    public static final double MOVE_PER_SECOND_THRESHOLD = 0.5;

    public static HashSet<String> unaffectedDamageTypes = new HashSet();

    public static HashMap<UUID, Integer> cooldownTicksServer = new HashMap();

    public static HashMap<UUID, Vec3> lastPositionServer = new HashMap();

    public static ArrayList<ShiftingScalesAugment.ShardParticle> shards = new ArrayList();

    public static int scales = 0;

    public static int scalesLast = 0;

    public ShiftingScalesAugment(ResourceLocation id) {
        super(id, 0.0);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, ShiftingScalesAugment::attachCapability);
    }

    public static void setLastPosition(UUID uuid, Vec3 pos) {
        lastPositionServer.put(uuid, pos);
    }

    public static double getMoveDistance(UUID uuid, Vec3 pos) {
        Vec3 lastPos = (Vec3) lastPositionServer.getOrDefault(uuid, pos);
        return lastPos.distanceTo(pos);
    }

    private static void resetEntity(UUID uuid) {
        lastPositionServer.remove(uuid);
        cooldownTicksServer.remove(uuid);
    }

    public static void setCooldown(UUID uuid, int ticks) {
        cooldownTicksServer.put(uuid, ticks);
    }

    public static void setMaxCooldown(UUID uuid, int ticks) {
        cooldownTicksServer.put(uuid, Math.max(ticks, (Integer) cooldownTicksServer.getOrDefault(uuid, 0)));
    }

    public static boolean hasCooldown(UUID uuid) {
        return (Integer) cooldownTicksServer.getOrDefault(uuid, 0) > 0;
    }

    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity && !event.getCapabilities().containsKey(SCALES)) {
            event.addCapability(SCALES, new ShiftingScalesAugment.ScalesCapabilityProvider(new ShiftingScalesAugment.DefaultScalesCapability()));
        }
    }

    public static void sendScalesData(ServerPlayer player) {
        double scales = EmbersAPI.getScales(player);
        if (scales > 0.0) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MessageScalesData(scales));
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (UUID uuid : cooldownTicksServer.keySet()) {
                int ticks = (Integer) cooldownTicksServer.get(uuid) - 1;
                cooldownTicksServer.put(uuid, ticks);
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.m_9236_().isClientSide()) {
            UUID uuid = entity.m_20148_();
            int scaleLevel = AugmentUtil.getArmorAugmentLevel(entity, this) * 2;
            if (scaleLevel > 0) {
                if (getMoveDistance(uuid, entity.m_20182_()) * 20.0 > 0.5) {
                    setMaxCooldown(uuid, 33);
                }
                double scales = EmbersAPI.getScales(entity);
                if (!hasCooldown(uuid)) {
                    scales++;
                    setCooldown(uuid, 33);
                }
                scales = Math.min(Math.min(scales, (double) (scaleLevel * 3)), (double) entity.getMaxHealth() * 1.5);
                EmbersAPI.setScales(entity, scales);
                setLastPosition(uuid, entity.m_20182_());
            } else {
                EmbersAPI.setScales(entity, 0.0);
                resetEntity(uuid);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onHit(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (!unaffectedDamageTypes.contains(source.type().msgId())) {
            int scaleLevel = AugmentUtil.getArmorAugmentLevel(entity, this) * 2;
            if (scaleLevel > 0) {
                if (!entity.m_9236_().isClientSide()) {
                    setMaxCooldown(entity.m_20148_(), 99);
                }
                ScaleEvent scaleEvent = new ScaleEvent(entity, event.getAmount(), source, ConfigManager.getScaleDamageRate(source.type().msgId()), ConfigManager.getScaleDamagePass(source.type().msgId()));
                MinecraftForge.EVENT_BUS.post(scaleEvent);
                double totalDamage = (double) event.getAmount();
                double extraDamage = totalDamage * scaleEvent.getScalePassRate();
                totalDamage -= extraDamage;
                double multiplier = scaleEvent.getScaleDamageRate();
                double damage = totalDamage * multiplier;
                double scales = EmbersAPI.getScales(entity);
                double absorbed = Math.min(scales, damage);
                scales -= absorbed;
                damage -= absorbed;
                if ((int) scales < (int) scales) {
                    entity.m_9236_().playSound(null, entity, EmbersSounds.SHIFTING_SCALES_BREAK.get(), entity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE, 10.0F, 1.0F);
                }
                EmbersAPI.setScales(entity, scales);
                event.setAmount((float) ((damage == 0.0 ? 0.0 : damage / multiplier) + extraDamage));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderHeartsOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = gui.getMinecraft();
        if (!mc.player.m_7500_() && !mc.player.m_5833_()) {
            int x = getBarX(width);
            int y = getBarY(height);
            int segs = scales / 3;
            int last = scales % 3;
            if (last > 0) {
                segs++;
            }
            int u = 18;
            int v = 0;
            for (int i = 0; i < segs; i++) {
                if (i == segs - 1) {
                    u = (last + 2) % 3 * 9;
                }
                graphics.blit(TEXTURE_HUD, x + 8 * (i % 10), y - 10 * (i / 10), u, v, 9, 9);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static int getBarY(int height) {
        return height - 39;
    }

    @OnlyIn(Dist.CLIENT)
    private static int getBarX(int width) {
        return width / 2 - 11 - 80;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (ShiftingScalesAugment.ShardParticle particle : shards) {
                particle.update();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderIngameOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = gui.getMinecraft();
        if (!mc.player.m_7500_() && !mc.player.m_5833_()) {
            Iterator<ShiftingScalesAugment.ShardParticle> iterator = shards.iterator();
            while (iterator.hasNext()) {
                ShiftingScalesAugment.ShardParticle particle = (ShiftingScalesAugment.ShardParticle) iterator.next();
                if (particle.getY() > (double) height) {
                    iterator.remove();
                }
                int u = particle.getFrame() % 8 > 4 ? 5 : 0;
                int v = 9;
                graphics.blit(TEXTURE_HUD, (int) particle.getX() - 2, (int) particle.getY() - 2, u, v, 5, 5);
            }
            Random random = new Random();
            if (scales < scalesLast) {
                int x = getBarX(width);
                int y = getBarY(height);
                int segsLast = scalesLast / 3;
                int lastLast = scalesLast % 3;
                if (lastLast > 0) {
                    segsLast++;
                }
                int segs = scales / 3;
                int last = scales % 3;
                if (last > 0) {
                    segs++;
                }
                for (int i = 0; i < Math.max(segs, segsLast); i++) {
                    int currentScale = i * 3 + last;
                    if (currentScale >= scales) {
                        int xHeart = x + 8 * (i % 10) + 4;
                        int yHeart = y - 10 * (i / 10) + 4;
                        int pieces = 2;
                        if (lastLast == 1 && i == Math.max(segs, segsLast) - 1) {
                            pieces = 1;
                        }
                        for (int e = 0; e < pieces; e++) {
                            shards.add(new ShiftingScalesAugment.ShardParticle((double) xHeart, (double) yHeart, random.nextInt(8), (random.nextDouble() - 0.5) * 10.0, (random.nextDouble() - 0.5) * 10.0, 0.5));
                        }
                    }
                }
            }
            scalesLast = scales;
        }
    }

    public static class DefaultScalesCapability implements ShiftingScalesAugment.IScalesCapability {

        public double scales = -1.0;

        @Override
        public double getScales() {
            return this.scales;
        }

        @Override
        public void setScales(double scales) {
            this.scales = scales;
        }

        @Override
        public void writeToNBT(CompoundTag tag) {
            tag.putDouble("scales", this.scales);
        }

        @Override
        public void readFromNBT(CompoundTag tag) {
            this.scales = tag.getDouble("scales");
        }
    }

    public interface IScalesCapability {

        double getScales();

        void setScales(double var1);

        void writeToNBT(CompoundTag var1);

        void readFromNBT(CompoundTag var1);
    }

    public static class ScalesCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        private ShiftingScalesAugment.IScalesCapability capability;

        public LazyOptional<ShiftingScalesAugment.IScalesCapability> holder = LazyOptional.of(() -> this.capability);

        public static final Capability<ShiftingScalesAugment.IScalesCapability> scalesCapability = CapabilityManager.get(new CapabilityToken<ShiftingScalesAugment.IScalesCapability>() {
        });

        public ScalesCapabilityProvider() {
            this.capability = new ShiftingScalesAugment.DefaultScalesCapability();
            this.holder = LazyOptional.of(() -> this.capability);
        }

        public ScalesCapabilityProvider(ShiftingScalesAugment.IScalesCapability capability) {
            this.capability = capability;
            this.holder = LazyOptional.of(() -> this.capability);
        }

        @Nullable
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
            return scalesCapability != null && capability == scalesCapability ? scalesCapability.orEmpty(capability, this.holder) : LazyOptional.empty();
        }

        public CompoundTag serializeNBT() {
            CompoundTag compound = new CompoundTag();
            this.capability.writeToNBT(compound);
            return compound;
        }

        public void deserializeNBT(CompoundTag compound) {
            this.capability.readFromNBT(compound);
        }
    }

    public static class ShardParticle {

        double x;

        double y;

        int frame;

        double xSpeed;

        double ySpeed;

        double gravity;

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public int getFrame() {
            return this.frame;
        }

        public ShardParticle(double x, double y, int frame, double xSpeed, double ySpeed, double gravity) {
            this.x = x;
            this.y = y;
            this.frame = frame;
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
            this.gravity = gravity;
        }

        public void update() {
            this.x = this.x + this.xSpeed;
            this.y = this.y + this.ySpeed;
            if (this.ySpeed < 12.0) {
                this.ySpeed = this.ySpeed + this.gravity;
            }
            this.frame++;
        }
    }
}