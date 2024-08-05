package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntityHydraHead;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.github.alexthe666.iceandfire.entity.ai.AiDebug;
import com.github.alexthe666.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import com.github.alexthe666.iceandfire.entity.ai.VillagerAIFearUntamed;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemChain;
import com.github.alexthe666.iceandfire.item.ItemCockatriceScepter;
import com.github.alexthe666.iceandfire.item.ItemDeathwormGauntlet;
import com.github.alexthe666.iceandfire.item.ItemDragonsteelArmor;
import com.github.alexthe666.iceandfire.item.ItemGhostSword;
import com.github.alexthe666.iceandfire.item.ItemScaleArmor;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import com.github.alexthe666.iceandfire.message.MessagePlayerHitMultipart;
import com.github.alexthe666.iceandfire.message.MessageSwingArm;
import com.github.alexthe666.iceandfire.message.MessageSyncPath;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.AbstractPathJob;
import com.github.alexthe666.iceandfire.world.gen.WorldGenFireDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenIceDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenLightningDragonCave;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

@EventBusSubscriber(modid = "iceandfire")
public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");

    private static final Predicate<LivingEntity> VILLAGER_FEAR = entity -> entity instanceof IVillagerFear;

    private final Random rand = new Random();

    private static final String[] VILLAGE_TYPES = new String[] { "plains", "desert", "snowy", "savanna", "taiga" };

    public static String BOLT_DONT_DESTROY_LOOT = "iceandfire.bolt_skip_loot";

    private static void signalChickenAlarm(LivingEntity chicken, LivingEntity attacker) {
        float d0 = (float) IafConfig.cockatriceChickenSearchLength;
        List<EntityCockatrice> list = chicken.m_9236_().m_45976_(EntityCockatrice.class, new AABB(chicken.m_20185_(), chicken.m_20186_(), chicken.m_20189_(), chicken.m_20185_() + 1.0, chicken.m_20186_() + 1.0, chicken.m_20189_() + 1.0).inflate((double) d0, 10.0, (double) d0));
        if (!list.isEmpty()) {
            for (EntityCockatrice cockatrice : list) {
                if (!(attacker instanceof EntityCockatrice) && !DragonUtils.hasSameOwner(cockatrice, attacker)) {
                    if (attacker instanceof Player) {
                        Player player = (Player) attacker;
                        if (!player.isCreative() && !cockatrice.m_21830_(player)) {
                            cockatrice.m_6710_(player);
                        }
                    } else {
                        cockatrice.m_6710_(attacker);
                    }
                }
            }
        }
    }

    private static void signalAmphithereAlarm(LivingEntity villager, LivingEntity attacker) {
        float d0 = IafConfig.amphithereVillagerSearchLength;
        List<EntityAmphithere> list = villager.m_9236_().m_45976_(EntityAmphithere.class, new AABB(villager.m_20185_() - 1.0, villager.m_20186_() - 1.0, villager.m_20189_() - 1.0, villager.m_20185_() + 1.0, villager.m_20186_() + 1.0, villager.m_20189_() + 1.0).inflate((double) d0, (double) d0, (double) d0));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof EntityAmphithere) {
                    EntityAmphithere amphithere = (EntityAmphithere) entity;
                    if (!(attacker instanceof EntityAmphithere) && !DragonUtils.hasSameOwner(amphithere, attacker)) {
                        if (attacker instanceof Player) {
                            Player player = (Player) attacker;
                            if (!player.isCreative() && !amphithere.m_21830_(player)) {
                                amphithere.m_6710_(player);
                            }
                        } else {
                            amphithere.m_6710_(attacker);
                        }
                    }
                }
            }
        }
    }

    private static boolean isInEntityTag(ResourceLocation loc, EntityType<?> type) {
        return type.is(((ITagManager) Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.tags())).createTagKey(loc));
    }

    public static boolean isLivestock(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.FEAR_DRAGONS, entity.getType());
    }

    public static boolean isVillager(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.VILLAGERS, entity.getType());
    }

    public static boolean isSheep(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.SHEEP, entity.getType());
    }

    public static boolean isChicken(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.CHICKENS, entity.getType());
    }

    public static boolean isCockatriceTarget(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.COCKATRICE_TARGETS, entity.getType());
    }

    public static boolean doesScareCockatrice(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.SCARES_COCKATRICES, entity.getType());
    }

    public static boolean isBlindMob(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.BLINDED, entity.getType());
    }

    public static boolean isRidingOrBeingRiddenBy(Entity first, Entity entityIn) {
        if (first != null && entityIn != null) {
            for (Entity entity : first.getPassengers()) {
                if (entity.equals(entityIn) || isRidingOrBeingRiddenBy(entity, entityIn)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @SubscribeEvent
    public void onArrowCollide(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult result) {
            Entity shotEntity = result.getEntity();
            if (shotEntity instanceof EntityGhost) {
                event.setCanceled(true);
            } else if (event.getEntity() instanceof AbstractArrow arrow && arrow.m_19749_() != null) {
                Entity shootingEntity = arrow.m_19749_();
                if (shootingEntity instanceof LivingEntity && isRidingOrBeingRiddenBy(shootingEntity, shotEntity) && shotEntity instanceof TamableAnimal tamable && tamable.isTame() && shotEntity.isAlliedTo(shootingEntity)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void addNewVillageBuilding(ServerAboutToStartEvent event) {
        if (IafConfig.villagerHouseWeight > 0) {
            Registry<StructureTemplatePool> templatePoolRegistry = (Registry<StructureTemplatePool>) event.getServer().registryAccess().m_6632_(Registries.TEMPLATE_POOL).orElseThrow();
            Registry<StructureProcessorList> processorListRegistry = (Registry<StructureProcessorList>) event.getServer().registryAccess().m_6632_(Registries.PROCESSOR_LIST).orElseThrow();
            for (String type : VILLAGE_TYPES) {
                IafVillagerRegistry.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("village/" + type + "/houses"), "iceandfire:village/" + type + "_scriber_1", IafConfig.villagerHouseWeight);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttackMob(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityMutlipartPart && event.getEntity() instanceof Player) {
            event.setCanceled(true);
            Entity parent = ((EntityMutlipartPart) event.getTarget()).getParent();
            try {
                if (parent != null) {
                    event.getEntity().attack(parent);
                }
            } catch (Exception var4) {
                IceAndFire.LOGGER.warn("Exception thrown while interacting with entity.", var4);
            }
            int extraData = 0;
            if (event.getTarget() instanceof EntityHydraHead && parent instanceof EntityHydra) {
                extraData = ((EntityHydraHead) event.getTarget()).headIndex;
                ((EntityHydra) parent).triggerHeadFlags(extraData);
            }
            if (event.getTarget().level().isClientSide && parent != null) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessagePlayerHitMultipart(parent.getId(), extraData));
            }
        }
    }

    @SubscribeEvent
    public void onEntityFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player) {
            EntityDataProvider.getCapability(event.getEntity()).ifPresent(data -> {
                if (data.miscData.hasDismounted) {
                    event.setDamageMultiplier(0.0F);
                    data.miscData.setDismounted(false);
                }
            });
        }
    }

    @SubscribeEvent
    public void onEntityDamage(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
            float multi = 1.0F;
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1F;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemTrollArmor) {
                multi -= 0.3F;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemTrollArmor) {
                multi -= 0.2F;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1F;
            }
            event.setAmount(event.getAmount() * multi);
        }
        if (event.getSource().is(IafDamageRegistry.DRAGON_FIRE_TYPE) || event.getSource().is(IafDamageRegistry.DRAGON_ICE_TYPE) || event.getSource().is(IafDamageRegistry.DRAGON_LIGHTNING_TYPE)) {
            float multix = 1.0F;
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemScaleArmor || event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemDragonsteelArmor) {
                multix -= 0.1F;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemScaleArmor || event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemDragonsteelArmor) {
                multix -= 0.3F;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemScaleArmor || event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemDragonsteelArmor) {
                multix -= 0.2F;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemScaleArmor || event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemDragonsteelArmor) {
                multix -= 0.1F;
            }
            event.setAmount(event.getAmount() * multix);
        }
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof WitherSkeleton) {
            event.getDrops().add(new ItemEntity(event.getEntity().m_9236_(), event.getEntity().m_20185_(), event.getEntity().m_20186_(), event.getEntity().m_20189_(), new ItemStack(IafItemRegistry.WITHERBONE.get(), event.getEntity().getRandom().nextInt(2))));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void makeItemDropsFireImmune(LivingDropsEvent event) {
        boolean makeFireImmune;
        label20: {
            makeFireImmune = false;
            if (event.getSource().getDirectEntity() instanceof LightningBolt bolt && bolt.m_19880_().contains(BOLT_DONT_DESTROY_LOOT)) {
                makeFireImmune = true;
                break label20;
            }
            if (event.getSource().getEntity() instanceof Player player && player.m_21120_(player.m_7655_()).is(IafItemTags.MAKE_ITEM_DROPS_FIREIMMUNE)) {
                makeFireImmune = true;
            }
        }
        if (makeFireImmune) {
            Set<ItemEntity> fireImmuneDrops = (Set<ItemEntity>) event.getDrops().stream().map(itemEntity -> new ItemEntity(itemEntity.m_9236_(), itemEntity.m_20185_(), itemEntity.m_20186_(), itemEntity.m_20189_(), itemEntity.getItem()) {

                @Override
                public boolean fireImmune() {
                    return true;
                }
            }).collect(Collectors.toSet());
            event.getDrops().clear();
            event.getDrops().addAll(fireImmuneDrops);
        }
    }

    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event) {
        if (event.getSource() != null && event.getSource().getEntity() != null) {
            Entity attacker = event.getSource().getEntity();
            if (attacker instanceof LivingEntity) {
                EntityDataProvider.getCapability(attacker).ifPresent(data -> {
                    if (data.miscData.loveTicks > 0) {
                        event.setCanceled(true);
                    }
                });
                if (isChicken(event.getEntity())) {
                    signalChickenAlarm(event.getEntity(), (LivingEntity) attacker);
                } else if (DragonUtils.isVillager(event.getEntity())) {
                    signalAmphithereAlarm(event.getEntity(), (LivingEntity) attacker);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingSetTarget(LivingChangeTargetEvent event) {
        LivingEntity target = event.getOriginalTarget();
        if (target != null) {
            LivingEntity attacker = event.getEntity();
            if (isChicken(target)) {
                signalChickenAlarm(target, attacker);
            } else if (DragonUtils.isVillager(target)) {
                signalAmphithereAlarm(target, attacker);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.getTarget() != null && isSheep(event.getTarget())) {
            float dist = (float) IafConfig.cyclopesSheepSearchLength;
            List<Entity> list = event.getTarget().level().m_45933_(event.getEntity(), event.getEntity().m_20191_().expandTowards((double) dist, (double) dist, (double) dist));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity instanceof EntityCyclops) {
                        EntityCyclops cyclops = (EntityCyclops) entity;
                        if (!cyclops.isBlinded() && !event.getEntity().isCreative()) {
                            cyclops.m_6710_(event.getEntity());
                        }
                    }
                }
            }
        }
        if (event.getTarget() instanceof EntityStoneStatue statue) {
            statue.m_21153_(statue.m_21233_());
            if (event.getEntity() != null) {
                ItemStack stack = event.getEntity().m_21205_();
                event.getTarget().playSound(SoundEvents.STONE_BREAK, 2.0F, 0.5F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                if (stack.getItem().isCorrectToolForDrops(Blocks.STONE.defaultBlockState()) || stack.getItem().getDescriptionId().contains("pickaxe")) {
                    event.setCanceled(true);
                    statue.setCrackAmount(statue.getCrackAmount() + 1);
                    if (statue.getCrackAmount() > 9) {
                        CompoundTag writtenTag = new CompoundTag();
                        event.getTarget().saveWithoutId(writtenTag);
                        event.getTarget().playSound(SoundEvents.STONE_BREAK, 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                        event.getTarget().remove(Entity.RemovalReason.KILLED);
                        if (stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
                            ItemStack statuette = new ItemStack(IafItemRegistry.STONE_STATUE.get());
                            CompoundTag tag = statuette.getOrCreateTag();
                            tag.putBoolean("IAFStoneStatuePlayerEntity", statue.getTrappedEntityTypeString().equalsIgnoreCase("minecraft:player"));
                            tag.putString("IAFStoneStatueEntityID", statue.getTrappedEntityTypeString());
                            tag.put("IAFStoneStatueNBT", writtenTag);
                            statue.addAdditionalSaveData(tag);
                            if (!statue.m_9236_().isClientSide()) {
                                statue.m_5552_(statuette, 1.0F);
                            }
                        } else if (!statue.m_9236_().isClientSide) {
                            statue.m_20000_(Blocks.COBBLESTONE.asItem(), 2 + event.getEntity().m_217043_().nextInt(4));
                        }
                        statue.m_142687_(Entity.RemovalReason.KILLED);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDie(LivingDeathEvent event) {
        EntityDataProvider.getCapability(event.getEntity()).ifPresent(data -> {
            if (!event.getEntity().m_9236_().isClientSide()) {
                if (!data.chainData.getChainedTo().isEmpty()) {
                    ItemEntity entityitem = new ItemEntity(event.getEntity().m_9236_(), event.getEntity().m_20185_(), event.getEntity().m_20186_() + 1.0, event.getEntity().m_20189_(), new ItemStack(IafItemRegistry.CHAIN.get(), data.chainData.getChainedTo().size()));
                    entityitem.setDefaultPickUpDelay();
                    event.getEntity().m_9236_().m_7967_(entityitem);
                    data.chainData.clearChains();
                }
            }
        });
        if (event.getEntity().m_20148_().equals(ALEX_UUID)) {
            event.getEntity().m_5552_(new ItemStack(IafItemRegistry.WEEZER_BLUE_ALBUM.get()), 1.0F);
        }
        if (event.getEntity() instanceof Player && IafConfig.ghostsFromPlayerDeaths) {
            Entity attacker = event.getEntity().getLastHurtByMob();
            if (attacker instanceof Player && event.getEntity().getRandom().nextInt(3) == 0) {
                CombatTracker combat = event.getEntity().getCombatTracker();
                CombatEntry entry = combat.getMostSignificantFall();
                boolean flag = entry != null && (entry.source().is(DamageTypes.FALL) || entry.source().is(DamageTypes.DROWN) || entry.source().is(DamageTypes.LAVA));
                if (event.getEntity().hasEffect(MobEffects.POISON)) {
                    flag = true;
                }
                if (flag) {
                    Level world = event.getEntity().m_9236_();
                    EntityGhost ghost = IafEntityRegistry.GHOST.get().create(world);
                    ghost.m_20359_(event.getEntity());
                    if (!world.isClientSide) {
                        ghost.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(event.getEntity().m_20183_()), MobSpawnType.SPAWNER, null, null);
                        world.m_7967_(ghost);
                    }
                    ghost.setDaytimeMode(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityStopUsingItem(LivingEntityUseItemEvent.Tick event) {
        if (event.getItem().getItem() instanceof ItemDeathwormGauntlet || event.getItem().getItem() instanceof ItemCockatriceScepter) {
            event.setDuration(20);
        }
    }

    @SubscribeEvent
    public void onEntityUseItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() != null && event.getEntity().m_146909_() > 87.0F && event.getEntity().m_20202_() != null && event.getEntity().m_20202_() instanceof EntityDragonBase) {
            ((EntityDragonBase) event.getEntity().m_20202_()).mobInteract(event.getEntity(), event.getHand());
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingTickEvent event) {
        if (AiDebug.isEnabled() && event.getEntity() instanceof Mob && AiDebug.contains((Mob) event.getEntity())) {
            AiDebug.logData();
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof LivingEntity target) {
            EntityDataProvider.getCapability(target).ifPresent(data -> {
                if (data.chainData.isChainedTo(event.getEntity())) {
                    data.chainData.removeChain(event.getEntity());
                    if (!event.getLevel().isClientSide()) {
                        event.getTarget().spawnAtLocation(IafItemRegistry.CHAIN.get(), 1);
                    }
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            });
        }
        if (!event.getLevel().isClientSide() && event.getTarget() instanceof Mob && event.getItemStack().getItem() == Items.STICK) {
            if (AiDebug.isEnabled()) {
                AiDebug.addEntity((Mob) event.getTarget());
            }
            if (Pathfinding.isDebug()) {
                if (((UUID) AbstractPathJob.trackingMap.getOrDefault(event.getEntity(), UUID.randomUUID())).equals(event.getTarget().getUUID())) {
                    AbstractPathJob.trackingMap.remove(event.getEntity());
                    IceAndFire.sendMSGToPlayer(new MessageSyncPath(new HashSet(), new HashSet(), new HashSet()), (ServerPlayer) event.getEntity());
                } else {
                    AbstractPathJob.trackingMap.put(event.getEntity(), event.getTarget().getUUID());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        onLeftClick(event.getEntity(), event.getItemStack());
        if (event.getLevel().isClientSide) {
            IceAndFire.sendMSGToServer(new MessageSwingArm());
        }
    }

    public static void onLeftClick(Player playerEntity, ItemStack stack) {
        if (stack.getItem() == IafItemRegistry.GHOST_SWORD.get()) {
            ItemGhostSword.spawnGhostSwordEntity(stack, playerEntity);
        }
    }

    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() != null && event.getLevel().getBlockState(event.getPos()).m_60734_() instanceof AbstractChestBlock && !event.getEntity().isCreative()) {
            float dist = (float) IafConfig.dragonGoldSearchLength;
            List<Entity> list = event.getLevel().m_45933_(event.getEntity(), event.getEntity().m_20191_().inflate((double) dist, (double) dist, (double) dist));
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity instanceof EntityDragonBase) {
                        EntityDragonBase dragon = (EntityDragonBase) entity;
                        if (!dragon.m_21824_() && !dragon.isModelDead() && !dragon.m_21830_(event.getEntity())) {
                            dragon.setInSittingPose(false);
                            dragon.setOrderedToSit(false);
                            dragon.setTarget(event.getEntity());
                        }
                    }
                }
            }
        }
        if (event.getLevel().getBlockState(event.getPos()).m_60734_() instanceof WallBlock) {
            ItemChain.attachToFence(event.getEntity(), event.getLevel(), event.getPos());
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && (event.getState().m_60734_() instanceof AbstractChestBlock || event.getState().m_60734_() == IafBlockRegistry.GOLD_PILE.get() || event.getState().m_60734_() == IafBlockRegistry.SILVER_PILE.get() || event.getState().m_60734_() == IafBlockRegistry.COPPER_PILE.get())) {
            float dist = (float) IafConfig.dragonGoldSearchLength;
            List<Entity> list = event.getLevel().m_45933_(event.getPlayer(), event.getPlayer().m_20191_().inflate((double) dist, (double) dist, (double) dist));
            if (list.isEmpty()) {
                return;
            }
            for (Entity entity : list) {
                if (entity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) entity;
                    if (!dragon.m_21824_() && !dragon.isModelDead() && !dragon.m_21830_(event.getPlayer()) && !event.getPlayer().isCreative()) {
                        dragon.setInSittingPose(false);
                        dragon.setOrderedToSit(false);
                        dragon.setTarget(event.getPlayer());
                    }
                }
            }
        }
    }

    public static void onChestGenerated(LootTableLoadEvent event) {
        ResourceLocation eventName = event.getName();
        boolean condition1 = eventName.equals(BuiltInLootTables.SIMPLE_DUNGEON) || eventName.equals(BuiltInLootTables.ABANDONED_MINESHAFT) || eventName.equals(BuiltInLootTables.DESERT_PYRAMID) || eventName.equals(BuiltInLootTables.JUNGLE_TEMPLE) || eventName.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR) || eventName.equals(BuiltInLootTables.STRONGHOLD_CROSSING);
        if (condition1 || eventName.equals(BuiltInLootTables.VILLAGE_CARTOGRAPHER)) {
            LootPoolEntryContainer.Builder item = LootItem.lootTableItem(IafItemRegistry.MANUSCRIPT.get()).setQuality(20).setWeight(5);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_manuscript").add(item).when(LootItemRandomChanceCondition.randomChance(0.35F)).setRolls(UniformGenerator.between(1.0F, 4.0F)).setBonusRolls(UniformGenerator.between(0.0F, 3.0F));
            event.getTable().addPool(builder.build());
        }
        if (!condition1 && !eventName.equals(BuiltInLootTables.IGLOO_CHEST) && !eventName.equals(BuiltInLootTables.WOODLAND_MANSION) && !eventName.equals(BuiltInLootTables.VILLAGE_TOOLSMITH) && !eventName.equals(BuiltInLootTables.VILLAGE_ARMORER)) {
            if (event.getName().equals(WorldGenFireDragonCave.FIRE_DRAGON_CHEST) || event.getName().equals(WorldGenFireDragonCave.FIRE_DRAGON_CHEST_MALE) || event.getName().equals(WorldGenIceDragonCave.ICE_DRAGON_CHEST) || event.getName().equals(WorldGenIceDragonCave.ICE_DRAGON_CHEST_MALE) || event.getName().equals(WorldGenLightningDragonCave.LIGHTNING_DRAGON_CHEST) || event.getName().equals(WorldGenLightningDragonCave.LIGHTNING_DRAGON_CHEST_MALE)) {
                LootPoolEntryContainer.Builder item = LootItem.lootTableItem(IafItemRegistry.WEEZER_BLUE_ALBUM.get()).setQuality(100).setWeight(1);
                LootPool.Builder builder = new LootPool.Builder().name("iaf_weezer").add(item).when(LootItemRandomChanceCondition.randomChance(0.01F)).setRolls(UniformGenerator.between(1.0F, 1.0F));
                event.getTable().addPool(builder.build());
            }
        } else {
            LootPoolEntryContainer.Builder item = LootItem.lootTableItem(IafItemRegistry.SILVER_INGOT.get()).setQuality(15).setWeight(12);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_silver_ingot").add(item).when(LootItemRandomChanceCondition.randomChance(0.5F)).setRolls(UniformGenerator.between(1.0F, 3.0F)).setBonusRolls(UniformGenerator.between(0.0F, 3.0F));
            event.getTable().addPool(builder.build());
        }
    }

    @SubscribeEvent
    public void onPlayerLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() != null && !event.getEntity().m_20197_().isEmpty()) {
            for (Entity entity : event.getEntity().m_20197_()) {
                entity.stopRiding();
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        try {
            if (isSheep(mob) && mob instanceof Animal animal) {
                animal.f_21345_.addGoal(8, new EntitySheepAIFollowCyclops(animal, 1.2));
            }
            if (isVillager(mob) && IafConfig.villagersFearDragons) {
                mob.goalSelector.addGoal(1, new VillagerAIFearUntamed((PathfinderMob) mob, LivingEntity.class, 8.0F, 0.8, 0.8, VILLAGER_FEAR));
            }
            if (isLivestock(mob) && IafConfig.animalsFearDragons) {
                mob.goalSelector.addGoal(1, new VillagerAIFearUntamed((PathfinderMob) mob, LivingEntity.class, 30.0F, 1.0, 0.5, entity -> entity instanceof IAnimalFear && ((IAnimalFear) entity).shouldAnimalsFear(mob)));
            }
        } catch (Exception var4) {
            IceAndFire.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
        }
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == IafVillagerRegistry.SCRIBE.get()) {
            IafVillagerRegistry.addScribeTrades(event.getTrades());
        }
    }

    @SubscribeEvent
    public void onLightningHit(EntityStruckByLightningEvent event) {
        if ((event.getEntity() instanceof ItemEntity || event.getEntity() instanceof ExperienceOrb) && event.getLightning().m_19880_().contains(BOLT_DONT_DESTROY_LOOT)) {
            event.setCanceled(true);
        } else if (event.getLightning().m_19880_().contains(event.getEntity().getStringUUID())) {
            event.setCanceled(true);
        }
    }
}