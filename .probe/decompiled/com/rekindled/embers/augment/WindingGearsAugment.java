package com.rekindled.embers.augment;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.EmbersAPI;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WindingGearsAugment extends AugmentBase {

    public static final ResourceLocation TEXTURE_HUD = new ResourceLocation("embers:textures/gui/icons.png");

    public static final int BAR_U = 0;

    public static final int BAR_V = 32;

    public static final int BAR_WIDTH = 180;

    public static final int BAR_HEIGHT = 8;

    public static final String TAG_CHARGE = "windingGearsCharge";

    public static final String TAG_CHARGE_TIME = "windingGearsLastTime";

    public static final double MAX_CHARGE = 500.0;

    public static final int CHARGE_DECAY_DELAY = 20;

    public static final double CHARGE_DECAY = 0.25;

    static int ticks;

    static double angle;

    static double angleLast;

    static int spool;

    static int spoolLast;

    static ThreadLocal<Map<Entity, Double>> bounceLocal = ThreadLocal.withInitial(WeakHashMap::new);

    public WindingGearsAugment(ResourceLocation id) {
        super(id, 0.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @OnlyIn(Dist.CLIENT)
    private static int getBarY(int height) {
        return height - 31;
    }

    @OnlyIn(Dist.CLIENT)
    private static int getBarX(int width) {
        return width / 2 - 11 - 81;
    }

    public static ItemStack getHeldClockworkTool(LivingEntity entity) {
        ItemStack mainStack = entity.getMainHandItem();
        ItemStack offStack = entity.getOffhandItem();
        boolean isClockworkMain = isClockworkTool(mainStack);
        boolean isClockworkOff = isClockworkTool(offStack);
        if (isClockworkMain == isClockworkOff) {
            return ItemStack.EMPTY;
        } else {
            return isClockworkMain ? mainStack : offStack;
        }
    }

    public static boolean isClockworkTool(ItemStack stack) {
        return AugmentUtil.hasHeat(stack) && AugmentUtil.hasAugment(stack, RegistryManager.WINDING_GEARS_AUGMENT);
    }

    public static double getChargeDecay(Level world, ItemStack stack) {
        return 0.25;
    }

    public static double getCharge(Level world, ItemStack stack) {
        if (stack.hasTag()) {
            long dTime = getTimeSinceLastCharge(world, stack);
            return Math.max(0.0, stack.getTag().getDouble("windingGearsCharge") - (double) Math.max(0L, dTime - 20L) * getChargeDecay(world, stack));
        } else {
            return 0.0;
        }
    }

    private static long getTimeSinceLastCharge(Level world, ItemStack stack) {
        if (stack.hasTag()) {
            long lastTime = stack.getTag().getLong("windingGearsLastTime");
            long currentTime = world.getGameTime();
            return lastTime > currentTime ? 0L : currentTime - lastTime;
        } else {
            return Long.MAX_VALUE;
        }
    }

    public static double getMaxCharge(Level world, ItemStack stack) {
        int level = getClockworkLevel(stack);
        return Math.min(200.0 * (double) level, 500.0);
    }

    private static int getClockworkLevel(ItemStack stack) {
        return AugmentUtil.getAugmentLevel(stack, RegistryManager.WINDING_GEARS_AUGMENT);
    }

    public static void setCharge(Level world, ItemStack stack, double charge) {
        if (!world.isClientSide()) {
            CompoundTag tagCompound = stack.getTag();
            if (tagCompound != null) {
                tagCompound.putDouble("windingGearsCharge", charge);
                tagCompound.putLong("windingGearsLastTime", world.getGameTime());
            }
        }
    }

    public static void depleteCharge(Level world, ItemStack stack, double charge) {
        setCharge(world, stack, Math.max(0.0, getCharge(world, stack) - charge));
    }

    public static void addCharge(Level world, ItemStack stack, double charge) {
        if (!world.isClientSide()) {
            setCharge(world, stack, Math.min(getMaxCharge(world, stack), getCharge(world, stack) + charge));
            CompoundTag tagCompound = stack.getTag();
            if (tagCompound != null) {
                tagCompound.putLong("windingGearsLastTime", world.getGameTime());
            }
        }
    }

    public static float getSpeedBonus(Level world, ItemStack stack) {
        double charge = getCharge(world, stack);
        return (float) Mth.clampedLerp(-0.2, 20.0, (charge - 50.0) / 300.0);
    }

    public static float getDamageBonus(Level world, ItemStack stack) {
        double charge = getCharge(world, stack);
        return (float) Mth.clampedLerp(1.0, 6.0, (charge - 50.0) / 300.0);
    }

    public static double getRotationSpeed(Level world, ItemStack stack) {
        long dTime = getTimeSinceLastCharge(world, stack);
        double charge = getCharge(world, stack);
        double standardSpeed = Mth.clampedLerp(0.0, 400.0, charge / 500.0);
        return dTime > 20L && charge > 0.0 ? Mth.clampedLerp(0.0, -10.0, (double) (dTime - 20L) / 10.0) : Mth.clampedLerp(standardSpeed, 0.0, (double) (dTime - 10L) / 10.0);
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack stack = getHeldClockworkTool(entity);
        if (!stack.isEmpty() && isClockworkTool(entity.getItemBySlot(EquipmentSlot.FEET))) {
            double charge = getCharge(entity.m_9236_(), stack);
            double cost = Math.max(16.0, charge * 0.16);
            if (charge > 0.0) {
                double x = 0.0;
                double z = 0.0;
                if (entity.m_20142_() && charge > Math.max(40.0, cost * 1.5)) {
                    x = entity.m_20184_().x;
                    z = entity.m_20184_().z;
                    cost = Math.max(40.0, cost * 1.5);
                }
                entity.m_20256_(entity.m_20184_().add(new Vec3(x, Mth.clampedLerp(0.0, 0.35, charge / 500.0), z)));
                if (charge >= cost) {
                    entity.m_5496_(EmbersSounds.WINDING_GEARS_SPRING.get(), 1.0F, 1.0F);
                }
            }
            if (!entity.m_9236_().isClientSide()) {
                depleteCharge(entity.m_9236_(), stack, cost);
            }
        }
    }

    @SubscribeEvent
    public void onTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Map<Entity, Double> bounce = (Map<Entity, Double>) bounceLocal.get();
        if (bounce.containsKey(entity)) {
            entity.m_20256_(entity.m_20184_().add(new Vec3(0.0, (Double) bounce.get(entity), 0.0)));
            bounce.remove(entity);
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack stack = getHeldClockworkTool(entity);
        if (!stack.isEmpty() && isClockworkTool(entity.getItemBySlot(EquipmentSlot.FEET))) {
            double spoolCost = (double) (Math.max(0.0F, event.getDistance() - 1.0F) * 5.0F);
            if (getCharge(entity.m_9236_(), stack) >= spoolCost) {
                event.setDamageMultiplier(0.0F);
                if (entity.m_20184_().y < -0.5) {
                    if (!entity.m_9236_().isClientSide()) {
                        depleteCharge(entity.m_9236_(), stack, spoolCost);
                    }
                    ((Map) bounceLocal.get()).put(entity, -entity.m_20184_().y);
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttack(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof LivingEntity player) {
            ItemStack mainStack = player.getMainHandItem();
            if (isClockworkTool(mainStack)) {
                double charge = getCharge(player.m_9236_(), mainStack);
                double cost = 5.0;
                if (charge >= getMaxCharge(player.m_9236_(), mainStack)) {
                    cost = charge;
                }
                if (!player.m_9236_().isClientSide()) {
                    depleteCharge(player.m_9236_(), mainStack, cost);
                }
            }
        }
    }

    @SubscribeEvent
    public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        ItemStack mainStack = player.m_21205_();
        float speed = event.getNewSpeed();
        if (isClockworkTool(mainStack)) {
            double charge = getCharge(player.m_9236_(), mainStack);
            if (charge > 0.0) {
                event.setNewSpeed(Math.max(Math.min(speed, 0.1F), speed + getSpeedBonus(player.m_9236_(), mainStack)));
            }
        }
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (!event.isCanceled() && player != null) {
            ItemStack mainStack = player.m_21205_();
            if (isClockworkTool(mainStack)) {
                double charge = getCharge(player.m_9236_(), mainStack);
                if (charge > 0.0 && !player.m_9236_().isClientSide()) {
                    depleteCharge(player.m_9236_(), mainStack, 40.0);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (isClockworkTool(stack)) {
            int level = getClockworkLevel(stack);
            double maxCharge = getMaxCharge(player.m_9236_(), stack);
            if (level > 0) {
                double resonance = EmbersAPI.getEmberResonance(stack);
                double charge = getCharge(player.m_9236_(), stack);
                double addAmount = Math.max((0.025 + 0.01 * (double) level) * (maxCharge - charge), 5.0 * resonance);
                addCharge(player.m_9236_(), stack, addAmount);
                player.m_6674_(event.getHand());
                event.setCancellationResult(InteractionResult.PASS);
                event.setCanceled(true);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ticks++;
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player != null) {
                ItemStack stack = getHeldClockworkTool(player);
                if (!stack.isEmpty()) {
                    spoolLast = spool;
                    spool = (int) (720.0 * getCharge(player.m_9236_(), stack) / 500.0);
                    angleLast = angle;
                    angle = angle + getRotationSpeed(player.m_9236_(), stack);
                    if (mc.options.keyAttack.isDown() && mc.hitResult instanceof EntityHitResult entityHit && this.canAutoAttack(player, stack, entityHit)) {
                        mc.gameMode.attack(player, entityHit.getEntity());
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private boolean canAutoAttack(LocalPlayer player, ItemStack stack, EntityHitResult objectMouseOver) {
        return player.m_36403_(0.0F) >= 1.0F && getCharge(player.m_9236_(), stack) > 0.0;
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isInvulnerable(Entity entity) {
        return entity.isInvulnerable() || entity instanceof LivingEntity && entity.invulnerableTime > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderSpringUnderlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        int fill = (int) ((float) spoolLast * (1.0F - partialTicks) + (float) spool * partialTicks);
        fill += 16;
        Minecraft mc = gui.getMinecraft();
        Player player = mc.player;
        if (player != null) {
            ItemStack stack = getHeldClockworkTool(player);
            if (!stack.isEmpty()) {
                int x = getBarX(width);
                int y = getBarY(height);
                int segs = fill / 32;
                int last = fill % 32;
                int u = 0;
                int v = 40;
                int evenWidth = segs * 8;
                int oddWidth = segs * 8 - 4;
                int evenFillBack = Mth.clamp(last - 16, 0, 8);
                int oddFillBack = Mth.clamp(last - 0, 0, 8);
                graphics.blit(TEXTURE_HUD, x, y, u, v, evenWidth, 8);
                graphics.blit(TEXTURE_HUD, x + evenWidth, y + 8 - evenFillBack, u + evenWidth, v + 8 - evenFillBack, 8, evenFillBack);
                v += 16;
                graphics.blit(TEXTURE_HUD, x, y, u, v, oddWidth, 8);
                graphics.blit(TEXTURE_HUD, x + oddWidth, y + 8 - oddFillBack, u + oddWidth, v + 8 - oddFillBack, 8, oddFillBack);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderSpringOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        int fill = (int) ((float) spoolLast * (1.0F - partialTicks) + (float) spool * partialTicks);
        double currentAngle = angleLast * (double) (1.0F - partialTicks) + angle * (double) partialTicks;
        int gearFrame = (int) (currentAngle * 4.0 / 360.0);
        int uGear = gearFrame % 4 * 10;
        int vGear = 16;
        fill += 16;
        Minecraft mc = gui.getMinecraft();
        Player player = mc.player;
        if (player != null) {
            ItemStack stack = getHeldClockworkTool(player);
            if (!stack.isEmpty()) {
                int x = getBarX(width);
                int y = getBarY(height);
                int segs = fill / 32;
                int last = fill % 32;
                int u = 0;
                int v = 32;
                int evenWidth = segs * 8;
                int oddWidth = segs * 8 - 4;
                int evenFillFront = Mth.clamp(last - 24, 0, 8);
                int oddFillFront = Mth.clamp(last - 8, 0, 8);
                graphics.blit(TEXTURE_HUD, x - 9, y - 1, uGear, vGear, 10, 10);
                graphics.blit(TEXTURE_HUD, x, y, u, v, evenWidth, 8);
                graphics.blit(TEXTURE_HUD, x + evenWidth, y, u + evenWidth, v, 8, evenFillFront);
                v += 16;
                graphics.blit(TEXTURE_HUD, x, y, u, v, oddWidth, 8);
                graphics.blit(TEXTURE_HUD, x + oddWidth, y, u + oddWidth, v, 8, oddFillFront);
            }
        }
    }
}