package com.mna.events;

import com.mna.ManaAndArtifice;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.entities.DamageHelper;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.tools.MATags;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mna.capabilities.particles.ParticleAuraProvider;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.effects.EffectInit;
import com.mna.effects.beneficial.EffectCamouflage;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mna.entities.boss.BossMonster;
import com.mna.entities.boss.PigDragon;
import com.mna.entities.constructs.animated.Construct;
import com.mna.factions.Factions;
import com.mna.gui.containers.providers.NamedGuideBook;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.items.armor.CouncilArmorItem;
import com.mna.items.artifice.charms.ItemContingencyCharm;
import com.mna.items.artifice.curio.ItemTrickeryBracelet;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.ritual.PractitionersPouchPatches;
import com.mna.items.sorcery.ItemStaff;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.EntityUtil;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.StructureUtils;
import com.mna.tools.SummonUtils;
import com.mna.tools.math.MathUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.NetworkHooks;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypePreset;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (!player.m_9236_().isClientSide() && event.getItemStack().getItem() == Items.LEATHER) {
            handleRightClick_Leather(player, event);
        }
    }

    private static void handleRightClick_Leather(Player player, PlayerInteractEvent.RightClickItem event) {
        ItemStack activestack = player.m_21120_(event.getHand());
        float f = player.m_146909_();
        float f1 = player.m_146908_();
        Vec3 vec3d = player.m_20299_(1.0F);
        float f2 = Mth.cos(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f4 = -Mth.cos(-f * (float) (Math.PI / 180.0));
        float f5 = Mth.sin(-f * (float) (Math.PI / 180.0));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue();
        Vec3 vec3d1 = vec3d.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
        BlockHitResult raytraceresult = player.m_9236_().m_45547_(new ClipContext(vec3d, vec3d1, ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, player));
        if (raytraceresult.getType() == HitResult.Type.BLOCK) {
            BlockState state = player.m_9236_().getBlockState(raytraceresult.getBlockPos());
            if (state == Blocks.WATER.defaultBlockState()) {
                ItemStack vellum = new ItemStack(ItemInit.VELLUM.get());
                if (!event.getEntity().addItem(vellum)) {
                    event.getEntity().drop(vellum, true);
                }
                activestack.shrink(1);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!GeneralConfigValues.SummonInteractions && event.getEntity() instanceof LivingEntity && SummonUtils.isSummon(event.getEntity())) {
            event.setCanceled(true);
            event.setResult(Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        if (!world.isClientSide && world.getBlockState(event.getPos()).m_60734_() == Blocks.LECTERN) {
            BlockEntity te = world.getBlockEntity(event.getPos());
            if (te instanceof LecternBlockEntity) {
                ItemStack bookStack = ((LecternBlockEntity) te).getBook();
                if (bookStack.getItem() == ItemInit.GUIDE_BOOK.get()) {
                    event.setCanceled(true);
                    if (event.getEntity().m_6144_()) {
                        ((LecternBlockEntity) te).setBook(ItemStack.EMPTY);
                        LecternBlock.resetBookState(event.getEntity(), world, event.getPos(), world.getBlockState(event.getPos()), false);
                        if (!event.getEntity().addItem(bookStack)) {
                            event.getEntity().m_19983_(bookStack);
                        }
                    } else if (!ItemInit.GUIDE_BOOK.get().checkMagicUnlock((ServerLevel) world, event.getEntity())) {
                        NetworkHooks.openScreen((ServerPlayer) event.getEntity(), new NamedGuideBook());
                    }
                    return;
                }
            }
        }
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(event.getItemStack());
        if (map.containsKey(EnchantmentInit.RETURNING.get())) {
            BlockState state = event.getLevel().getBlockState(event.getPos());
            if (state.m_60734_() != BlockInit.RUNIC_ANVIL.get()) {
                event.setCanceled(true);
            }
        }
    }

    private static void handleBossArenaBlockEvent(Level world, BlockEvent event, @Nullable Player player) {
        if (player == null || !player.isCreative()) {
            if (!(event.getState().m_60734_() instanceof ChalkRuneBlock)) {
                if (world instanceof ServerLevel && StructureUtils.isPointInAnyStructure((ServerLevel) world, event.getPos(), MATags.Structures.BOSS_ARENAS)) {
                    if (player != null) {
                        int bosses = world.getEntities(player, new AABB(event.getPos()).inflate(64.0), e -> e.isAlive() && e instanceof BossMonster).size();
                        if (bosses == 0 && player.getPersistentData().getInt("boss_arena_notification_cooldown") == 0) {
                            player.m_213846_(Component.translatable("helptip.mna.boss_break_denied").withStyle(ChatFormatting.AQUA));
                            player.getPersistentData().putInt("boss_arena_notification_cooldown", 1200);
                        }
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.Start event) {
        Level world = event.getLevel();
        for (int i = 0; i < 7; i++) {
            BlockPos pos = i == 0 ? BlockPos.containing(event.getExplosion().getPosition()) : getExplosionExtent(event.getExplosion(), Direction.values()[i - 1]);
            if (world instanceof ServerLevel && StructureUtils.isPointInAnyStructure((ServerLevel) world, pos, MATags.Structures.BOSS_ARENAS)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level world = (Level) event.getLevel();
        handleBossArenaBlockEvent(world, event, event.getPlayer());
    }

    @SubscribeEvent
    public static void onFluidPlace(BlockEvent.FluidPlaceBlockEvent event) {
        handleBossArenaBlockEvent((Level) event.getLevel(), event, null);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        handleBossArenaBlockEvent((Level) event.getLevel(), event, event.getEntity() instanceof Player ? (Player) event.getEntity() : null);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.getPersistentData().contains("mna:flung")) {
            long flingtime = player.getPersistentData().getLong("mna:fling_time");
            boolean flingEffect = player.m_9236_().getGameTime() - flingtime >= 3L;
            if (!player.m_20069_() && !player.m_20096_()) {
                if (player.f_19862_) {
                    if (!CuriosInterop.IsItemInCurioSlot(ItemInit.AIR_CAST_RING.get(), player, SlotTypePreset.RING) && flingEffect) {
                        player.hurt(player.m_269291_().flyIntoWall(), player.getPersistentData().getFloat("mna:flung") * 3.0F);
                        player.m_9236_().playSound(null, player.m_20182_().x, player.m_20182_().y, player.m_20182_().z, SFX.Entity.Spellbreaker.SHIELD_BASH, SoundSource.PLAYERS, 0.3F, 1.0F);
                    }
                    player.getPersistentData().remove("mna:flung");
                }
            } else if (flingEffect) {
                player.getPersistentData().remove("mna:flung");
            }
        }
        int breakCD = player.getPersistentData().getInt("boss_arena_notification_cooldown");
        if (breakCD > 0) {
            player.getPersistentData().putInt("boss_arena_notification_cooldown", --breakCD);
        }
        if (player.getPersistentData().contains("mna_remove_flight")) {
            int remaining = player.getPersistentData().getInt("mna_remove_flight") - 1;
            ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
            if (remaining == 0) {
                player.getPersistentData().remove("mna_remove_flight");
            } else {
                player.getPersistentData().putInt("mna_remove_flight", remaining);
            }
        }
        if (!player.m_20096_() && player.m_20184_().y < 0.0 && player.f_19789_ > 4.0F && player instanceof ServerPlayer && !player.m_21023_(MobEffects.SLOW_FALLING) && (double) getDistanceToGround(player) <= Math.abs(player.m_20184_().y * 10.0)) {
            ItemContingencyCharm.CheckAndConsumeCharmCharge((ServerPlayer) player, ItemContingencyCharm.ContingencyEvent.FALL);
        }
        Optional<SlotResult> trickery = CuriosApi.getCuriosHelper().findCurios(event.player, ItemInit.TRICKERY_BRACELET.get()).stream().findFirst();
        if (trickery.isPresent()) {
            boolean crouching = event.player.m_6144_();
            if (crouching) {
                ItemTrickeryBracelet.ApplyMoveSpeed(player);
            } else {
                ItemTrickeryBracelet.RemoveMoveSpeed(player);
            }
            player.m_6842_(crouching || EntityUtil.shouldBeInvisible(player));
        } else {
            ItemTrickeryBracelet.RemoveMoveSpeed(player);
        }
        if (!event.player.m_9236_().isClientSide() && event.player.m_20069_() && event.player.m_21023_(EffectInit.MIST_FORM.get()) && event.player.m_9236_().getGameTime() % 20L == 0L) {
            event.player.hurt(DamageHelper.forType(DamageHelper.DISPERSE, event.player.m_9236_().registryAccess()), 5.0F);
        }
        if (event.player.m_21023_(EffectInit.POSSESSION.get()) && event.player.getPersistentData().contains("posessed_entity_id")) {
            int id = event.player.getPersistentData().getInt("posessed_entity_id");
            Entity e = event.player.m_9236_().getEntity(id);
            if (e == null || !(e instanceof Mob) || !e.isAlive()) {
                event.player.getPersistentData().remove("posessed_entity_id");
                event.player.m_21195_(EffectInit.POSSESSION.get());
            }
        }
        event.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
            if (p.getAlliedFaction() == Factions.UNDEAD) {
                if (isPlayerInDaylight(event.player) && !event.player.m_20071_() && !event.player.m_21023_(EffectInit.SOAKED.get())) {
                    if (event.player.m_21033_(EquipmentSlot.HEAD)) {
                        if (event.player.m_9236_().getRandom().nextFloat() < 0.05F) {
                            ItemStack headslot = event.player.getItemBySlot(EquipmentSlot.HEAD);
                            headslot.hurtAndBreak(1, event.player, e -> {
                            });
                        }
                    } else {
                        event.player.m_20254_(8);
                    }
                } else if (event.player.m_20069_() && event.player.m_21124_(MobEffects.WATER_BREATHING) == null) {
                    event.player.m_7292_(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 1, false, false, true));
                }
                int frostWalkerLevelMH = player.m_21205_().getItem() instanceof ItemStaff ? player.m_21205_().getEnchantmentLevel(Enchantments.FROST_WALKER) : 0;
                int frostWalkerLevelOH = player.m_21206_().getItem() instanceof ItemStaff ? player.m_21206_().getEnchantmentLevel(Enchantments.FROST_WALKER) : 0;
                int frostWalkerLevel = Math.max(frostWalkerLevelMH, frostWalkerLevelOH);
                if (frostWalkerLevel > 0 && !event.player.m_9236_().isClientSide()) {
                    MobEffectInstance existing = player.m_21124_(EffectInit.AURA_OF_FROST.get());
                    if (existing == null || existing.getDuration() < 20) {
                        player.m_7292_(new MobEffectInstance(EffectInit.AURA_OF_FROST.get(), 201, frostWalkerLevel));
                    }
                }
            }
        });
        if (ItemInit.COUNCIL_ARMOR__CHEST.get().isSetEquipped(event.player)) {
            CouncilArmorItem.tickReflectCharges(event.player);
        }
        if (event.player.m_9236_().isClientSide()) {
            event.player.getCapability(ParticleAuraProvider.AURA).ifPresent(a -> {
                a.requestIfNeeded(player);
                Minecraft m = Minecraft.getInstance();
                if (!m.options.getCameraType().isFirstPerson() || a.showInFirstPerson()) {
                    a.spawn(event.player.m_9236_(), event.player.m_20182_(), Vec3.directionFromRotation(new Vec2(event.player.m_146909_(), event.player.f_20883_)));
                }
            });
            if (event.player.f_20938_ > 0 && event.player.m_21120_(InteractionHand.MAIN_HAND).getItem() == ItemInit.HELLFIRE_TRIDENT.get()) {
                Vec3 vel = event.player.m_20184_();
                for (int i = 0; i < 5; i++) {
                    event.player.m_9236_().addParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), event.player.m_20185_() + Math.random() * vel.x, event.player.m_20186_() + Math.random() * vel.y, event.player.m_20189_() + Math.random() * vel.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerPickupItem(EntityItemPickupEvent event) {
        ItemStack pickedUpItem = event.getItem().getItem();
        int originalQty = pickedUpItem.getCount();
        Inventory inv = event.getEntity().getInventory();
        boolean modifiedCollection = false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack invStack = inv.getItem(i);
            if (!invStack.isEmpty() && invStack.getItem() instanceof ItemPractitionersPouch) {
                ItemPractitionersPouch pouchItem = (ItemPractitionersPouch) invStack.getItem();
                if (pouchItem.getPatchLevel(invStack, PractitionersPouchPatches.COLLECTION) > 0 && pouchItem.hasRoomForItem(invStack, pickedUpItem)) {
                    pickedUpItem = pouchItem.insertItem(invStack, pickedUpItem);
                    modifiedCollection = originalQty != pickedUpItem.getCount();
                }
                if (pickedUpItem.isEmpty()) {
                    event.getEntity().m_7938_(event.getItem(), originalQty);
                    event.setCanceled(true);
                    event.getItem().m_142687_(Entity.RemovalReason.DISCARDED);
                    return;
                }
                if (pouchItem.getPatchLevel(invStack, PractitionersPouchPatches.VOID) > 0 && pouchItem.shouldVoidItem(invStack, pickedUpItem)) {
                    event.getEntity().m_7938_(event.getItem(), originalQty);
                    event.setCanceled(true);
                    event.getItem().m_142687_(Entity.RemovalReason.DISCARDED);
                    return;
                }
            }
        }
        if (modifiedCollection && !pickedUpItem.isEmpty()) {
            event.setCanceled(true);
            event.getItem().setItem(pickedUpItem);
            ForgeEventFactory.onItemPickup(event.getItem(), event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();
        if (living.getPersistentData().contains("mna_remove_ginvis")) {
            int remaining = living.getPersistentData().getInt("mna_remove_ginvis") - 1;
            EntityUtil.removeInvisibility(living);
            if (remaining == 0) {
                living.getPersistentData().remove("mna_remove_ginvis");
            } else {
                living.getPersistentData().putInt("mna_remove_ginvis", remaining);
            }
        }
        if (living.getPersistentData().contains("mna:flung")) {
            if (living.m_20069_()) {
                living.getPersistentData().remove("mna:flung");
            } else if (living.f_19862_) {
                if (!living.m_20096_()) {
                    long flingtime = living.getPersistentData().getLong("mna:fling_time");
                    if (living.m_9236_().getGameTime() - flingtime >= 3L) {
                        living.hurt(DamageHelper.createSourcedType(DamageTypes.FLY_INTO_WALL, living.m_9236_().registryAccess(), living), living.getPersistentData().getFloat("mna:flung") * 3.0F);
                        living.m_9236_().playSound(null, living.m_20182_().x, living.m_20182_().y, living.m_20182_().z, SFX.Entity.Spellbreaker.SHIELD_BASH, SoundSource.HOSTILE, 0.3F, 1.0F);
                    }
                }
                living.getPersistentData().remove("mna:flung");
            }
        }
        boolean isInWater = living.m_20069_();
        boolean isWet = living.m_20071_();
        if (living.getEffect(EffectInit.WATERY_GRAVE.get()) != null && isInWater) {
            living.m_5997_(0.0, -0.2, 0.0);
        }
        if (living.getEffect(EffectInit.GRAVITY_WELL.get()) != null && !living.m_20096_() && !isInWater && !living.m_20077_()) {
            living.m_5997_(0.0, -0.2, 0.0);
        }
        if (!living.hasEffect(EffectInit.SOAKED.get())) {
            if (isWet && (!(living instanceof Player player) || !player.isSpectator())) {
                living.addEffect(new MobEffectInstance(EffectInit.SOAKED.get(), 300, 0));
            }
        } else if (living.m_6060_()) {
            living.m_7311_(0);
        }
        if (living instanceof Mob monster) {
            if (monster.getTarget() != null) {
                LivingEntity target = monster.getTarget();
                MobEffectInstance effect = target.getEffect(EffectInit.TRUE_INVISIBILITY.get());
                boolean clearTarget = false;
                if (effect != null && monster.m_20280_(monster.getTarget()) > 3.0) {
                    clearTarget = true;
                }
                effect = target.getEffect(MobEffects.INVISIBILITY);
                if (effect != null && monster.m_20280_(monster.getTarget()) > 16.0) {
                    clearTarget = true;
                }
                effect = target.getEffect(EffectInit.CAMOUFLAGE.get());
                if (effect != null) {
                    float camoPercent = EffectCamouflage.getCamoflagePercent(monster.getTarget());
                    float disengageDistance = Mth.lerp(camoPercent, 8.0F, 50.0F);
                    if (monster.m_20280_(monster.getTarget()) > (double) disengageDistance) {
                        clearTarget = true;
                    }
                }
                if (clearTarget) {
                    monster.setTarget(null);
                }
            }
            if (SummonUtils.isSummon(monster) && SummonUtils.getSummoner(monster) == null) {
                monster.m_6074_();
            }
        }
        if (living.m_9236_().isClientSide() && living.isFallFlying()) {
            BlockPos pos = living.m_20183_();
            int count = 0;
            for (BlockState state = living.m_9236_().getBlockState(pos); state.m_60734_() != Blocks.WATER && count < 3; count++) {
                pos = pos.below();
                state = living.m_9236_().getBlockState(pos);
            }
            if (count < 3) {
                Vec3 c = Vec3.atCenterOf(pos).add(-0.1, 0.5, -0.1);
                for (int i = 0; i < 5; i++) {
                    Vec3 tempPos = c.add(Math.random() * 0.2, 0.0, Math.random() * 0.2);
                    living.m_9236_().addParticle(ParticleTypes.SPLASH, tempPos.x, tempPos.y, tempPos.z, 0.0, 0.25, 0.0);
                }
            }
        }
        float desiredScale = 1.0F;
        CompoundTag tag = living.getPersistentData();
        float actualScale = tag.getFloat("mna_entity_scale");
        float prevScale = tag.getFloat("mna_entity_scale_prev");
        if (actualScale <= 0.0F) {
            actualScale = 1.0F;
        }
        MobEffectInstance effectx = living.getEffect(EffectInit.REDUCE.get());
        if (effectx != null) {
            desiredScale -= 0.1F * (float) (effectx.getAmplifier() + 1);
        }
        effectx = living.getEffect(EffectInit.ENLARGE.get());
        if (effectx != null) {
            desiredScale += 0.1F * (float) (effectx.getAmplifier() + 1);
            if (living instanceof Player player && player.getGameProfile() != null && player.getGameProfile().getId() != null && player.getGameProfile().getId().toString() == "c10026c1-3b38-401a-8e4a-590cba8037b4") {
                desiredScale += 0.5F * (float) (effectx.getAmplifier() + 1);
            }
        }
        if (desiredScale < 0.2F) {
            desiredScale = 0.2F;
        } else if (desiredScale > 5.0F) {
            desiredScale = 5.0F;
        }
        if (desiredScale != actualScale) {
            float delta = desiredScale < actualScale ? -0.1F : 0.1F;
            if (Math.abs(desiredScale - actualScale) < 0.1F) {
                actualScale = desiredScale;
                delta = 0.0F;
            }
            tag.putFloat("mna_entity_scale_prev", actualScale);
            tag.putFloat("mna_entity_scale", actualScale + delta);
            living.m_6210_();
        } else if (actualScale != prevScale) {
            tag.putFloat("mna_entity_scale_prev", actualScale);
            tag.putFloat("mna_entity_scale", actualScale);
            living.m_6210_();
        }
    }

    @SubscribeEvent
    public static void onEntitySizeChange(EntityEvent.Size event) {
        if (event.getEntity().isAddedToWorld() && event.getEntity() instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) event.getEntity();
            float scale = living.getPersistentData().getFloat("mna_entity_scale");
            if (scale != 1.0F) {
                event.setNewSize(event.getNewSize().scale(scale));
                event.setNewEyeHeight(event.getNewEyeHeight() * scale);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity().getEffect(EffectInit.PILGRIM.get()) != null) {
            event.getEntity().m_5997_((double) ((float) (event.getEntity().m_20184_().x * 0.15)), 0.25, (double) ((float) (event.getEntity().m_20184_().z * 0.15)));
        }
        MobEffectInstance enlorj = event.getEntity().getEffect(EffectInit.ENLARGE.get());
        if (enlorj != null) {
            int amp = enlorj.getAmplifier() + 1;
            event.getEntity().m_5997_((double) ((float) (event.getEntity().m_20184_().x * 0.1 * (double) amp)), 0.05 * (double) amp, (double) ((float) (event.getEntity().m_20184_().z * 0.1 * (double) amp)));
        }
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!ItemInit.DEMON_ARMOR_BOOTS.get().handlePlayerJump(player) && GeneralConfigValues.FixJumpBoost) {
                MobEffectInstance jumpEffect = event.getEntity().getEffect(MobEffects.JUMP);
                if (jumpEffect != null) {
                    float multiplier = (float) event.getEntity().getAttributeValue(Attributes.MOVEMENT_SPEED) * (float) jumpEffect.getAmplifier();
                    event.getEntity().m_5997_((double) ((float) (event.getEntity().m_20184_().x * (double) multiplier)), 0.0, (double) ((float) (event.getEntity().m_20184_().z * (double) multiplier)));
                }
            }
            if (player.m_6047_() && !player.m_9236_().isClientSide) {
                int frostWalkerLevelMH = player.m_21205_().getItem() instanceof ItemStaff ? player.m_21205_().getEnchantmentLevel(Enchantments.FROST_WALKER) : 0;
                int frostWalkerLevelOH = player.m_21206_().getItem() instanceof ItemStaff ? player.m_21206_().getEnchantmentLevel(Enchantments.FROST_WALKER) : 0;
                int frostWalkerLevel = Math.max(frostWalkerLevelMH, frostWalkerLevelOH);
                if (frostWalkerLevel > 0) {
                    player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                        if (p.getAlliedFaction() == Factions.UNDEAD) {
                            player.m_7292_(new MobEffectInstance(EffectInit.MIST_FORM.get(), 80 * frostWalkerLevel, 1));
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity().m_21124_(EffectInit.CHRONO_ANCHOR.get()) != null) {
            event.getEntity().m_21195_(EffectInit.CHRONO_ANCHOR.get());
        }
        SummonUtils.getSummons(event.getEntity(), event.getEntity().m_20194_().getLevel(event.getFrom())).forEach(s -> s.m_6074_());
        event.getEntity().getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
            m.setSyncGrimoire();
            m.setSyncRote();
            m.getCantripData().setNeedsSync();
            m.getCastingResource().setNeedsSync();
            m.forceSync();
        });
        ServerMessageDispatcher.sendWellspringPowerNetworkSyncMessage(event.getTo(), (ServerPlayer) event.getEntity(), true);
    }

    @SubscribeEvent
    public static void onPlayerSleep(PlayerWakeUpEvent event) {
        BlockPos bp = event.getEntity().m_20183_();
        BlockState bedState = event.getEntity().m_9236_().getBlockState(bp);
        if (bedState.m_60734_() == BlockInit.COFFIN.get()) {
            Player player = event.getEntity();
            player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (p.getAlliedFaction() == Factions.UNDEAD) {
                    long lastRestTime = player.getPersistentData().getLong("last_coffin_boost");
                    long curTime = player.m_9236_().getGameTime();
                    float pct = MathUtils.clamp01((float) (curTime - lastRestTime) / (float) GeneralConfigValues.UndeadCoffinSoulsDelay);
                    event.getEntity().getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                        m.getCastingResource().restore((float) ((double) m.getCastingResource().getMaxAmount() * GeneralConfigValues.UndeadCoffinSoulsRestored * (double) pct));
                        player.getPersistentData().putLong("last_coffin_boost", curTime);
                    });
                }
            });
        }
    }

    @SubscribeEvent
    public static void onMobGriefingEvent(EntityMobGriefingEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();
            Optional<Player> player = creeper.m_9236_().m_6443_(Player.class, creeper.m_20191_().inflate(5.0), p -> ItemInit.ENDERGUARD_AMULET.get().isEquippedAndHasMana(p, 50.0F, false)).stream().findFirst();
            if (player.isPresent()) {
                CuriosApi.getCuriosHelper().findFirstCurio((LivingEntity) player.get(), ItemInit.ENDERGUARD_AMULET.get()).ifPresent(t -> {
                    if (ItemInit.ENDERGUARD_AMULET.get().consumeMana(t.stack(), 50.0F, (Player) player.get())) {
                        int count = 0;
                        while (count++ < 50) {
                            double x = creeper.m_20185_() + (creeper.m_217043_().nextDouble() - 0.5) * 32.0;
                            double y = Mth.clamp(creeper.m_20186_() + (double) (creeper.m_217043_().nextInt(16) - 8), 0.0, (double) (creeper.m_9236_().m_141928_() - 1));
                            double z = creeper.m_20189_() + (creeper.m_217043_().nextDouble() - 0.5) * 32.0;
                            Vec3 newPos = new Vec3(x, y, z);
                            if (newPos.distanceToSqr(((Player) player.get()).m_20182_()) >= 36.0 && creeper.m_20984_(x, y, z, true)) {
                                event.setResult(Result.DENY);
                                creeper.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                                break;
                            }
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Entity mount = event.getEntity().m_20202_();
        if (mount != null && mount instanceof Construct) {
            ((Construct) mount).soundHorn();
        }
    }

    @SubscribeEvent
    public static void onPlayerSize(EntityEvent.Size event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.m_21221_() != null) {
                MobEffectInstance mistForm = player.m_21124_(EffectInit.MIST_FORM.get());
                if (mistForm != null && mistForm.getDuration() > 5) {
                    event.setNewSize(EntityDimensions.fixed(0.5F, 0.5F));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof PathfinderMob && SummonUtils.isSummon(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityDropXP(LivingExperienceDropEvent event) {
        if (event.getEntity() instanceof PathfinderMob && SummonUtils.isSummon(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerPickupXP(PlayerXpEvent.PickupXp event) {
        Player player = event.getEntity();
        player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
            if (p.getAlliedFaction() == Factions.UNDEAD) {
                int enchantMagnitude = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.XP_TO_SOULS.get(), player);
                if (enchantMagnitude > 0) {
                    event.setCanceled(true);
                    ExperienceOrb orb = event.getOrb();
                    player.takeXpDelay = 2;
                    player.m_7938_(orb, 1);
                    int souls = orb.value * 5 * enchantMagnitude;
                    orb.value /= 2;
                    if (orb.value > 0) {
                        player.giveExperiencePoints(orb.value);
                    }
                    orb.m_142687_(Entity.RemovalReason.KILLED);
                    player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().restore((float) souls));
                }
            }
        });
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof AreaEffectCloud && ((AreaEffectCloud) event.getEntity()).effects.stream().anyMatch(e -> e.getEffect() instanceof INoCreeperLingering)) {
            event.setResult(Result.DENY);
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof LivingEntity) {
            event.getEntity().getPersistentData().putFloat("mna_entity_scale", 1.0F);
            event.getEntity().getPersistentData().putFloat("mna_entity_scale_prev", 1.0F);
        }
    }

    @SubscribeEvent
    public static void canEntityContinueSleeping(SleepingTimeCheckEvent event) {
        MobEffectInstance e = event.getEntity().m_21124_(EffectInit.COLD_DARK.get());
        if (e != null) {
            event.setResult(Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (!event.level.isClientSide()) {
            event.level.getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.getWellspringRegistry().serverTick(event.level.getServer()));
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!event.getProjectile().m_9236_().isClientSide() && event.getProjectile() instanceof AbstractArrow && event.getProjectile().getPersistentData().contains("return_on_next_arrow_shot")) {
            Entity owner = ((AbstractArrow) event.getProjectile()).m_19749_();
            if (owner != null && owner instanceof Player) {
                if (((AbstractArrow) event.getProjectile()).pickup == AbstractArrow.Pickup.CREATIVE_ONLY && !((Player) owner).isCreative()) {
                    return;
                }
                if (((AbstractArrow) event.getProjectile()).pickup == AbstractArrow.Pickup.DISALLOWED) {
                    return;
                }
                ItemStack returnStack = ItemStack.of((CompoundTag) event.getProjectile().getPersistentData().get("return_stack"));
                if (!returnStack.isEmpty() && !((Player) owner).isCreative()) {
                    boolean drop = true;
                    if (event.getProjectile().getPersistentData().getBoolean("cannon_shot")) {
                        returnStack = InventoryUtilities.mergeToPlayerInvPrioritizeOffhand((Player) owner, returnStack);
                        drop = !returnStack.isEmpty();
                    } else {
                        drop = !((Player) owner).addItem(returnStack);
                    }
                    if (drop) {
                        ((Player) owner).drop(returnStack, true);
                    }
                }
                event.getProjectile().m_146870_();
            }
        }
    }

    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {
        if (event.hasAmmo() && event.getEntity() instanceof Player) {
            ItemStack itemstack = event.getEntity().getProjectile(event.getBow());
            if (!itemstack.isEmpty() && itemstack.getEnchantmentLevel(EnchantmentInit.RETURNING.get()) > 0) {
                event.getEntity().getPersistentData().putBoolean("return_on_next_arrow_shot", true);
                ItemStack toReturn = itemstack.copy();
                toReturn.setCount(1);
                event.getEntity().getPersistentData().put("return_stack", toReturn.save(new CompoundTag()));
                return;
            }
        }
        event.getEntity().getPersistentData().remove("return_on_next_arrow_shot");
    }

    @SubscribeEvent
    public static void onEntityAddedToWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof AbstractArrow) {
            AbstractArrow arrow = (AbstractArrow) event.getEntity();
            if (arrow.m_19749_() != null && arrow.m_19749_().getPersistentData().contains("return_on_next_arrow_shot")) {
                arrow.getPersistentData().putBoolean("return_on_next_arrow_shot", true);
                if (arrow.m_19749_().getPersistentData().contains("return_stack")) {
                    arrow.getPersistentData().put("return_stack", arrow.m_19749_().getPersistentData().get("return_stack"));
                }
                arrow.m_19749_().getPersistentData().remove("return_on_next_arrow_shot");
                arrow.m_19749_().getPersistentData().remove("return_stack");
            }
        }
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof EnderDragon) {
            EnderDragon dragon = (EnderDragon) event.getEntity();
            EndDragonFight fight = dragon.getDragonFight();
            if (fight != null && fight.hasPreviouslyKilledDragon()) {
                AABB search = dragon.m_20191_().inflate(256.0);
                List<ItemEntity> items = (List<ItemEntity>) dragon.m_9236_().getEntities(dragon, search, e -> e instanceof ItemEntity).stream().map(e -> (ItemEntity) e).collect(Collectors.toList());
                boolean foundChop = items.stream().anyMatch(e -> e.getItem().getItem() == Items.PORKCHOP || e.getItem().getItem() == Items.COOKED_PORKCHOP);
                boolean foundCookie = items.stream().anyMatch(e -> e.getItem().getItem() == Items.COOKIE);
                if (foundChop && foundCookie) {
                    event.setResult(Result.DENY);
                    event.setCanceled(true);
                    PigDragon pd = new PigDragon(dragon.m_9236_());
                    pd.m_146884_(dragon.m_20182_());
                    dragon.m_9236_().m_7967_(pd);
                    dragon.m_142687_(Entity.RemovalReason.DISCARDED);
                    fight.setDragonKilled(dragon);
                } else {
                    dragon.m_9236_().m_45955_(new TargetingConditions(false).ignoreInvisibilityTesting().ignoreLineOfSight().range(256.0), null, search).stream().forEach(p -> p.m_213846_(Component.translatable("helptip.mna.pigdragon").withStyle(ChatFormatting.AQUA)));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingGearChange(LivingEquipmentChangeEvent event) {
        int newLoyalty = event.getFrom().getEnchantmentLevel(Enchantments.LOYALTY);
        SummonUtils.setBonusSummons(event.getEntity(), newLoyalty);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player player = event.getEntity();
            Player original = event.getOriginal();
            CompoundTag soulbound_inventory = original.getPersistentData().getCompound("mna_soulbound_inventory");
            CompoundTag soulbound_curios = original.getPersistentData().getCompound("mna_soulbound_curios");
            CompoundTag last_death_data = original.getPersistentData().getCompound("mna_last_death_data");
            for (int i = 0; i < original.getInventory().getContainerSize(); i++) {
                if (soulbound_inventory.contains("slot_" + i)) {
                    CompoundTag itemTag = soulbound_inventory.getCompound("slot_" + i);
                    ItemStack soulboundStack = ItemStack.of(itemTag);
                    player.getInventory().setItem(i, soulboundStack);
                }
            }
            CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(c -> c.getCurios().forEach((identifier, stackHandler) -> {
                if (soulbound_curios.contains(identifier)) {
                    CompoundTag tagForIdentifier = soulbound_curios.getCompound(identifier);
                    for (int ix = 0; ix < stackHandler.getSlots(); ix++) {
                        if (tagForIdentifier.contains("slot_" + ix)) {
                            CompoundTag itemTagx = tagForIdentifier.getCompound("slot_" + ix);
                            ItemStack soulboundStackx = ItemStack.of(itemTagx);
                            stackHandler.getStacks().setStackInSlot(ix, soulboundStackx);
                        }
                    }
                }
            }));
            player.getPersistentData().put("mna_last_death_data", last_death_data);
            player.getPersistentData().remove("mna_soulbound_inventory");
            player.getPersistentData().remove("mna_soulbound_curios");
        }
    }

    public static void onCalculatingVisibility(LivingEvent.LivingVisibilityEvent event) {
        if (event.getEntity().hasEffect(EffectInit.CAMOUFLAGE.get())) {
            float camoPercent = 1.0F - 0.6F * EffectCamouflage.getCamoflagePercent(event.getEntity());
            event.modifyVisibility((double) camoPercent);
        }
        if (event.getEntity().hasEffect(EffectInit.TRUE_INVISIBILITY.get())) {
            event.modifyVisibility(0.0);
        }
    }

    private static float getDistanceToGround(Player player) {
        float distance = 0.0F;
        for (BlockPos pos = player.m_20183_(); pos.m_123342_() > player.m_9236_().m_141937_() && !player.m_9236_().getBlockState(pos).m_60638_(player.m_9236_(), pos, player, Direction.UP); pos = pos.below()) {
            distance++;
        }
        return distance;
    }

    private static boolean isPlayerInDaylight(Player player) {
        if (player.m_9236_().isDay() && !player.m_9236_().isClientSide()) {
            float f = player.m_213856_();
            BlockPos blockpos = player.m_20202_() instanceof Boat ? BlockPos.containing(player.m_20185_(), (double) Math.round(player.m_20186_()), player.m_20189_()).above() : BlockPos.containing(player.m_20185_(), (double) Math.round(player.m_20186_()), player.m_20189_());
            if (f > 0.5F && player.m_9236_().random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && player.m_9236_().m_45527_(blockpos)) {
                return true;
            }
        }
        return false;
    }

    private static BlockPos getExplosionExtent(Explosion explosion, Direction dir) {
        BlockPos pos = BlockPos.containing(explosion.getPosition());
        Vec3i offset = dir.getNormal().multiply((int) Math.ceil((double) explosion.radius));
        return pos.offset(offset);
    }
}