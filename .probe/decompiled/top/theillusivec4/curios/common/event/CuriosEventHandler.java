package top.theillusivec4.curios.common.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.EnderManAngerEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.CuriosConfig;
import top.theillusivec4.curios.common.capability.CurioInventoryCapability;
import top.theillusivec4.curios.common.capability.ItemizedCurioCapability;
import top.theillusivec4.curios.common.data.CuriosEntityManager;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.SPacketSetIcons;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncCurios;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncData;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncModifiers;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncStack;
import top.theillusivec4.curios.common.util.EquipCurioTrigger;
import top.theillusivec4.curios.mixin.CuriosImplMixinHooks;

public class CuriosEventHandler {

    public static boolean dirtyTags = false;

    private static void handleDrops(String identifier, LivingEntity livingEntity, List<Tuple<Predicate<ItemStack>, ICurio.DropRule>> dropRules, NonNullList<Boolean> renders, IDynamicStackHandler stacks, boolean cosmetic, Collection<ItemEntity> drops, boolean keepInventory, LivingDropsEvent evt) {
        for (int i = 0; i < stacks.getSlots(); i++) {
            ItemStack stack = stacks.getStackInSlot(i);
            SlotContext slotContext = new SlotContext(identifier, livingEntity, i, cosmetic, renders.size() > i && renders.get(i));
            if (!stack.isEmpty()) {
                ICurio.DropRule dropRuleOverride = null;
                for (Tuple<Predicate<ItemStack>, ICurio.DropRule> override : dropRules) {
                    if (override.getA().test(stack)) {
                        dropRuleOverride = override.getB();
                    }
                }
                ICurio.DropRule dropRule = dropRuleOverride != null ? dropRuleOverride : (ICurio.DropRule) CuriosApi.getCurio(stack).map(curio -> curio.getDropRule(slotContext, evt.getSource(), evt.getLootingLevel(), evt.isRecentlyHit())).orElse(ICurio.DropRule.DEFAULT);
                if (dropRule == ICurio.DropRule.DEFAULT) {
                    dropRule = (ICurio.DropRule) CuriosApi.getSlot(identifier, livingEntity.m_9236_()).map(ISlotType::getDropRule).orElse(ICurio.DropRule.DEFAULT);
                }
                if ((dropRule != ICurio.DropRule.DEFAULT || !keepInventory) && dropRule != ICurio.DropRule.ALWAYS_KEEP) {
                    if (!EnchantmentHelper.hasVanishingCurse(stack) && dropRule != ICurio.DropRule.DESTROY) {
                        drops.add(getDroppedItem(stack, livingEntity));
                    }
                    stacks.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }
    }

    private static ItemEntity getDroppedItem(ItemStack droppedItem, LivingEntity livingEntity) {
        double d0 = livingEntity.m_20186_() - 0.3F + (double) livingEntity.m_20192_();
        ItemEntity entityitem = new ItemEntity(livingEntity.m_9236_(), livingEntity.m_20185_(), d0, livingEntity.m_20189_(), droppedItem);
        entityitem.setPickUpDelay(40);
        float f = livingEntity.m_9236_().random.nextFloat() * 0.5F;
        float f1 = livingEntity.m_9236_().random.nextFloat() * (float) (Math.PI * 2);
        entityitem.m_20334_((double) (-Mth.sin(f1) * f), 0.2F, (double) (Mth.cos(f1) * f));
        return entityitem;
    }

    private static boolean handleMending(Player player, IDynamicStackHandler stacks, PlayerXpEvent.PickupXp evt) {
        for (int i = 0; i < stacks.getSlots(); i++) {
            ItemStack stack = stacks.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getEnchantmentLevel(Enchantments.MENDING) > 0 && stack.isDamaged()) {
                evt.setCanceled(true);
                ExperienceOrb orb = evt.getOrb();
                player.takeXpDelay = 2;
                player.m_7938_(orb, 1);
                int toRepair = Math.min(orb.value * 2, stack.getDamageValue());
                orb.value -= toRepair / 2;
                stack.setDamageValue(stack.getDamageValue() - toRepair);
                if (orb.value > 0) {
                    player.giveExperiencePoints(orb.value);
                }
                orb.m_142687_(Entity.RemovalReason.KILLED);
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
        Player playerEntity = evt.getEntity();
        if (playerEntity instanceof ServerPlayer) {
            Collection<ISlotType> slotTypes = CuriosApi.getPlayerSlots(playerEntity).values();
            Map<String, ResourceLocation> icons = new HashMap();
            slotTypes.forEach(type -> icons.put(type.getIdentifier(), type.getIcon()));
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) playerEntity), new SPacketSetIcons(icons));
        }
    }

    @SubscribeEvent
    public void onDatapackSync(OnDatapackSyncEvent evt) {
        if (evt.getPlayer() == null) {
            PlayerList playerList = evt.getPlayerList();
            for (ServerPlayer player : playerList.getPlayers()) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SPacketSyncData(CuriosSlotManager.getSyncPacket(), CuriosEntityManager.getSyncPacket()));
                CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                    handler.readTag(handler.writeTag());
                    NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SPacketSyncCurios(player.m_19879_(), handler.getCurios()));
                    if (player.f_36096_ instanceof ICuriosMenu curiosContainer) {
                        curiosContainer.resetSlots();
                    }
                });
                Collection<ISlotType> slotTypes = CuriosApi.getPlayerSlots(player).values();
                Map<String, ResourceLocation> icons = new HashMap();
                slotTypes.forEach(type -> icons.put(type.getIdentifier(), type.getIcon()));
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SPacketSetIcons(icons));
            }
        } else {
            ServerPlayer mp = evt.getPlayer();
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> mp), new SPacketSyncData(CuriosSlotManager.getSyncPacket(), CuriosEntityManager.getSyncPacket()));
            CuriosApi.getCuriosInventory(mp).ifPresent(handler -> NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> mp), new SPacketSyncCurios(mp.m_19879_(), handler.getCurios())));
            Collection<ISlotType> slotTypes = CuriosApi.getPlayerSlots(mp).values();
            Map<String, ResourceLocation> icons = new HashMap();
            slotTypes.forEach(type -> icons.put(type.getIdentifier(), type.getIcon()));
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> mp), new SPacketSetIcons(icons));
        }
    }

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinLevelEvent evt) {
        Entity entity = evt.getEntity();
        if (entity instanceof ServerPlayer serverPlayerEntity) {
            CuriosApi.getCuriosInventory(serverPlayerEntity).ifPresent(handler -> {
                ServerPlayer mp = (ServerPlayer) entity;
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> mp), new SPacketSyncCurios(mp.m_19879_(), handler.getCurios()));
            });
        }
    }

    @SubscribeEvent
    public void attachEntitiesCapabilities(AttachCapabilitiesEvent<Entity> evt) {
        if (evt.getObject() instanceof LivingEntity livingEntity) {
            evt.addCapability(CuriosCapability.ID_INVENTORY, CurioInventoryCapability.createProvider(livingEntity));
        }
    }

    @SubscribeEvent
    public void attachStackCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
        ItemStack stack = evt.getObject();
        Item item = stack.getItem();
        ICurioItem curioItem = (ICurioItem) CuriosImplMixinHooks.getCurioFromRegistry(item).orElse(null);
        if (curioItem == null && item instanceof ICurioItem itemCurio) {
            curioItem = itemCurio;
        }
        if (curioItem != null && curioItem.hasCurioCapability(stack)) {
            ItemizedCurioCapability itemizedCapability = new ItemizedCurioCapability(curioItem, stack);
            evt.addCapability(CuriosCapability.ID_ITEM, CuriosApi.createCurioProvider(itemizedCapability));
        }
    }

    @SubscribeEvent
    public void playerStartTracking(PlayerEvent.StartTracking evt) {
        Entity target = evt.getTarget();
        Player player = evt.getEntity();
        if (player instanceof ServerPlayer && target instanceof LivingEntity livingBase) {
            CuriosApi.getCuriosInventory(livingBase).ifPresent(handler -> NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new SPacketSyncCurios(target.getId(), handler.getCurios())));
        }
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone evt) {
        Player player = evt.getEntity();
        Player oldPlayer = evt.getOriginal();
        oldPlayer.revive();
        LazyOptional<ICuriosItemHandler> oldHandler = CuriosApi.getCuriosInventory(oldPlayer);
        LazyOptional<ICuriosItemHandler> newHandler = CuriosApi.getCuriosInventory(player);
        oldHandler.ifPresent(oldCurios -> newHandler.ifPresent(newCurios -> newCurios.readTag(oldCurios.writeTag())));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerDrops(LivingDropsEvent evt) {
        LivingEntity livingEntity = evt.getEntity();
        if (!livingEntity.m_5833_()) {
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                Collection<ItemEntity> drops = evt.getDrops();
                Collection<ItemEntity> curioDrops = new ArrayList();
                Map<String, ICurioStacksHandler> curios = handler.getCurios();
                DropRulesEvent dropRulesEvent = new DropRulesEvent(livingEntity, handler, evt.getSource(), evt.getLootingLevel(), evt.isRecentlyHit());
                MinecraftForge.EVENT_BUS.post(dropRulesEvent);
                List<Tuple<Predicate<ItemStack>, ICurio.DropRule>> dropRules = dropRulesEvent.getOverrides();
                boolean keepInventory = false;
                if (livingEntity instanceof Player) {
                    keepInventory = livingEntity.m_9236_().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
                    if (CuriosConfig.SERVER.keepCurios.get() != CuriosConfig.KeepCurios.DEFAULT) {
                        keepInventory = CuriosConfig.SERVER.keepCurios.get() == CuriosConfig.KeepCurios.ON;
                    }
                }
                boolean finalKeepInventory = keepInventory;
                curios.forEach((id, stacksHandler) -> {
                    handleDrops(id, livingEntity, dropRules, stacksHandler.getRenders(), stacksHandler.getStacks(), false, curioDrops, finalKeepInventory, evt);
                    handleDrops(id, livingEntity, dropRules, stacksHandler.getRenders(), stacksHandler.getCosmeticStacks(), true, curioDrops, finalKeepInventory, evt);
                });
                if (!MinecraftForge.EVENT_BUS.post(new CurioDropsEvent(livingEntity, handler, evt.getSource(), curioDrops, evt.getLootingLevel(), evt.isRecentlyHit()))) {
                    drops.addAll(curioDrops);
                }
            });
        }
    }

    @SubscribeEvent
    public void playerXPPickUp(PlayerXpEvent.PickupXp evt) {
        Player player = evt.getEntity();
        if (!player.m_9236_().isClientSide) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                Map<String, ICurioStacksHandler> curios = handler.getCurios();
                for (ICurioStacksHandler stacksHandler : curios.values()) {
                    if (handleMending(player, stacksHandler.getStacks(), evt) || handleMending(player, stacksHandler.getCosmeticStacks(), evt)) {
                        return;
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void curioRightClick(PlayerInteractEvent.RightClickItem evt) {
        Player player = evt.getEntity();
        ItemStack stack = evt.getItemStack();
        CuriosApi.getCurio(stack).ifPresent(curio -> CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            Tuple<IDynamicStackHandler, SlotContext> firstSlot = null;
            for (Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                IDynamicStackHandler stackHandler = ((ICurioStacksHandler) entry.getValue()).getStacks();
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    String id = (String) entry.getKey();
                    NonNullList<Boolean> renderStates = ((ICurioStacksHandler) entry.getValue()).getRenders();
                    SlotContext slotContext = new SlotContext(id, player, i, false, renderStates.size() > i && renderStates.get(i));
                    if (stackHandler.isItemValid(i, stack) && curio.canEquipFromUse(slotContext)) {
                        ItemStack present = stackHandler.getStackInSlot(i);
                        if (present.isEmpty()) {
                            stackHandler.setStackInSlot(i, stack.copy());
                            curio.onEquipFromUse(slotContext);
                            if (!player.isCreative()) {
                                int count = stack.getCount();
                                stack.shrink(count);
                            }
                            evt.setCancellationResult(InteractionResult.sidedSuccess(player.m_9236_().isClientSide()));
                            evt.setCanceled(true);
                            return;
                        }
                        if (firstSlot == null) {
                            CurioUnequipEvent unequipEvent = new CurioUnequipEvent(present, slotContext);
                            MinecraftForge.EVENT_BUS.post(unequipEvent);
                            Result result = unequipEvent.getResult();
                            if (result != Result.DENY && stackHandler.extractItem(i, stack.getMaxStackSize(), true).getCount() == stack.getCount()) {
                                firstSlot = new Tuple<>(stackHandler, slotContext);
                            }
                        }
                    }
                }
            }
            if (firstSlot != null) {
                IDynamicStackHandler stackHandler = firstSlot.getA();
                SlotContext slotContext = firstSlot.getB();
                int ix = slotContext.index();
                ItemStack presentx = stackHandler.getStackInSlot(ix);
                stackHandler.setStackInSlot(ix, stack.copy());
                curio.onEquipFromUse(slotContext);
                player.m_21008_(evt.getHand(), presentx.copy());
                evt.setCancellationResult(InteractionResult.sidedSuccess(player.m_9236_().isClientSide()));
                evt.setCanceled(true);
            }
        }));
    }

    @SubscribeEvent
    public void worldTick(TickEvent.LevelTickEvent evt) {
        if (evt.level instanceof ServerLevel && dirtyTags) {
            PlayerList list = ((ServerLevel) evt.level).getServer().getPlayerList();
            for (ServerPlayer player : list.getPlayers()) {
                CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                    for (Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                        ICurioStacksHandler stacksHandler = (ICurioStacksHandler) entry.getValue();
                        String id = (String) entry.getKey();
                        IDynamicStackHandler stacks = stacksHandler.getStacks();
                        IDynamicStackHandler cosmeticStacks = stacksHandler.getCosmeticStacks();
                        replaceInvalidStacks(player, id, stacks, false, stacksHandler.getRenders());
                        replaceInvalidStacks(player, id, cosmeticStacks, true, stacksHandler.getRenders());
                    }
                });
            }
            dirtyTags = false;
        }
    }

    private static void replaceInvalidStacks(ServerPlayer player, String id, IDynamicStackHandler stacks, boolean cosmetic, NonNullList<Boolean> renders) {
        for (int i = 0; i < stacks.getSlots(); i++) {
            ItemStack stack = stacks.getStackInSlot(i);
            if (!stack.isEmpty() && !stacks.isItemValid(i, stack)) {
                stacks.setStackInSlot(i, ItemStack.EMPTY);
                ItemHandlerHelper.giveItemToPlayer(player, stack);
            }
        }
    }

    @SubscribeEvent
    public void looting(LootingLevelEvent evt) {
        DamageSource source = evt.getDamageSource();
        if (source != null && source.getEntity() instanceof LivingEntity living) {
            evt.setLootingLevel(evt.getLootingLevel() + (Integer) CuriosApi.getCuriosInventory(living).map(handler -> handler.getLootingLevel(source, evt.getEntity(), evt.getLootingLevel())).orElse(0));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBreakBlock(BlockEvent.BreakEvent evt) {
        Player player = evt.getPlayer();
        AtomicInteger fortuneLevel = new AtomicInteger();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            for (Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                IDynamicStackHandler stacks = ((ICurioStacksHandler) entry.getValue()).getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    NonNullList<Boolean> renderStates = ((ICurioStacksHandler) entry.getValue()).getRenders();
                    SlotContext slotContext = new SlotContext((String) entry.getKey(), player, i, false, renderStates.size() > i && renderStates.get(i));
                    fortuneLevel.addAndGet((Integer) CuriosApi.getCurio(stacks.getStackInSlot(i)).map(curio -> curio.getFortuneLevel(slotContext, null)).orElse(0));
                }
            }
        });
        ItemStack stack = player.m_21205_();
        int bonusLevel = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
        int silklevel = stack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
        LevelAccessor level = evt.getLevel();
        evt.setExpToDrop(evt.getState().getExpDrop(level, level.getRandom(), evt.getPos(), bonusLevel + fortuneLevel.get(), silklevel));
    }

    @SubscribeEvent
    public void enderManAnger(EnderManAngerEvent evt) {
        Player player = evt.getPlayer();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            for (Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                IDynamicStackHandler stacks = ((ICurioStacksHandler) entry.getValue()).getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    int index = i;
                    NonNullList<Boolean> renderStates = ((ICurioStacksHandler) entry.getValue()).getRenders();
                    boolean hasMask = (Boolean) CuriosApi.getCurio(stacks.getStackInSlot(i)).map(curio -> curio.isEnderMask(new SlotContext((String) entry.getKey(), player, index, false, renderStates.size() > index && renderStates.get(index)), evt.getEntity())).orElse(false);
                    if (hasMask) {
                        evt.setCanceled(true);
                        return;
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public void tick(LivingEvent.LivingTickEvent evt) {
        LivingEntity livingEntity = evt.getEntity();
        if (livingEntity instanceof Player player && player.containerMenu instanceof CuriosContainerV2 curiosContainer) {
            curiosContainer.checkQuickMove();
        }
        CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
            handler.clearCachedSlotModifiers();
            handler.handleInvalidStacks();
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            for (Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                ICurioStacksHandler stacksHandler = (ICurioStacksHandler) entry.getValue();
                String identifier = (String) entry.getKey();
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                IDynamicStackHandler cosmeticStackHandler = stacksHandler.getCosmeticStacks();
                for (int i = 0; i < stacksHandler.getSlots(); i++) {
                    NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                    SlotContext slotContext = new SlotContext(identifier, livingEntity, i, false, renderStates.size() > i && renderStates.get(i));
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    LazyOptional<ICurio> currentCurio = CuriosApi.getCurio(stack);
                    int index = i;
                    if (!stack.isEmpty()) {
                        stack.inventoryTick(livingEntity.m_9236_(), livingEntity, -1, false);
                        currentCurio.ifPresent(curio -> curio.curioTick(slotContext));
                        if (livingEntity.m_9236_().isClientSide) {
                            currentCurio.ifPresent(curio -> curio.curioAnimate(identifier, index, livingEntity));
                        }
                    }
                    if (!livingEntity.m_9236_().isClientSide) {
                        ItemStack prevStack = stackHandler.getPreviousStackInSlot(i);
                        if (!ItemStack.matches(stack, prevStack)) {
                            LazyOptional<ICurio> prevCurio = CuriosApi.getCurio(prevStack);
                            syncCurios(livingEntity, stack, currentCurio, prevCurio, identifier, index, false, renderStates.size() > index && renderStates.get(index), SPacketSyncStack.HandlerType.EQUIPMENT);
                            MinecraftForge.EVENT_BUS.post(new CurioChangeEvent(livingEntity, identifier, i, prevStack, stack));
                            UUID uuid = CuriosApi.getSlotUuid(slotContext);
                            if (!prevStack.isEmpty()) {
                                Multimap<Attribute, AttributeModifier> map = CuriosApi.getAttributeModifiers(slotContext, uuid, prevStack);
                                Multimap<String, AttributeModifier> slots = HashMultimap.create();
                                Set<SlotAttribute> toRemove = new HashSet();
                                for (Attribute attribute : map.keySet()) {
                                    if (attribute instanceof SlotAttribute wrapper) {
                                        slots.putAll(wrapper.getIdentifier(), map.get(attribute));
                                        toRemove.add(wrapper);
                                    }
                                }
                                for (Attribute attributex : toRemove) {
                                    map.removeAll(attributex);
                                }
                                livingEntity.getAttributes().removeAttributeModifiers(map);
                                handler.removeSlotModifiers(slots);
                                prevCurio.ifPresent(curio -> curio.onUnequip(slotContext, stack));
                            }
                            if (!stack.isEmpty()) {
                                Multimap<Attribute, AttributeModifier> map = CuriosApi.getAttributeModifiers(slotContext, uuid, stack);
                                Multimap<String, AttributeModifier> slots = HashMultimap.create();
                                Set<SlotAttribute> toRemove = new HashSet();
                                for (Attribute attributex : map.keySet()) {
                                    if (attributex instanceof SlotAttribute wrapper) {
                                        slots.putAll(wrapper.getIdentifier(), map.get(attributex));
                                        toRemove.add(wrapper);
                                    }
                                }
                                for (Attribute attributexx : toRemove) {
                                    map.removeAll(attributexx);
                                }
                                livingEntity.getAttributes().addTransientAttributeModifiers(map);
                                handler.addTransientSlotModifiers(slots);
                                currentCurio.ifPresent(curio -> curio.onEquip(slotContext, prevStack));
                                if (livingEntity instanceof ServerPlayer) {
                                    EquipCurioTrigger.INSTANCE.trigger(slotContext, (ServerPlayer) livingEntity, stack, (ServerLevel) livingEntity.m_9236_(), livingEntity.m_20185_(), livingEntity.m_20186_(), livingEntity.m_20189_());
                                }
                            }
                            stackHandler.setPreviousStackInSlot(i, stack.copy());
                        }
                        ItemStack cosmeticStack = cosmeticStackHandler.getStackInSlot(i);
                        ItemStack prevCosmeticStack = cosmeticStackHandler.getPreviousStackInSlot(i);
                        if (!ItemStack.matches(cosmeticStack, prevCosmeticStack)) {
                            syncCurios(livingEntity, cosmeticStack, CuriosApi.getCurio(cosmeticStack), CuriosApi.getCurio(prevCosmeticStack), identifier, index, true, true, SPacketSyncStack.HandlerType.COSMETIC);
                            cosmeticStackHandler.setPreviousStackInSlot(index, cosmeticStack.copy());
                        }
                        Set<ICurioStacksHandler> updates = handler.getUpdatingInventories();
                        if (!updates.isEmpty()) {
                            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new SPacketSyncModifiers(livingEntity.m_19879_(), updates));
                            updates.clear();
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public void livingEquipmentChange(LivingEquipmentChangeEvent evt) {
        CuriosApi.getCuriosInventory(evt.getEntity()).ifPresent(inv -> {
            ItemStack from = evt.getFrom();
            ItemStack to = evt.getTo();
            EquipmentSlot slot = evt.getSlot();
            if (!from.isEmpty()) {
                Multimap<Attribute, AttributeModifier> map = from.getAttributeModifiers(slot);
                Multimap<String, AttributeModifier> slots = HashMultimap.create();
                for (Attribute attribute : map.keySet()) {
                    if (attribute instanceof SlotAttribute wrapper) {
                        slots.putAll(wrapper.getIdentifier(), map.get(attribute));
                    }
                }
                inv.removeSlotModifiers(slots);
            }
            if (!to.isEmpty()) {
                Multimap<Attribute, AttributeModifier> map = to.getAttributeModifiers(slot);
                Multimap<String, AttributeModifier> slots = HashMultimap.create();
                for (Attribute attributex : map.keySet()) {
                    if (attributex instanceof SlotAttribute wrapper) {
                        slots.putAll(wrapper.getIdentifier(), map.get(attributex));
                    }
                }
                inv.addTransientSlotModifiers(slots);
            }
        });
    }

    private static void syncCurios(LivingEntity livingEntity, ItemStack stack, LazyOptional<ICurio> currentCurio, LazyOptional<ICurio> prevCurio, String identifier, int index, boolean cosmetic, boolean visible, SPacketSyncStack.HandlerType type) {
        SlotContext slotContext = new SlotContext(identifier, livingEntity, index, cosmetic, visible);
        boolean syncable = (Boolean) currentCurio.map(curio -> curio.canSync(slotContext)).orElse(false) || (Boolean) prevCurio.map(curio -> curio.canSync(slotContext)).orElse(false);
        CompoundTag syncTag = syncable ? (CompoundTag) currentCurio.map(curio -> {
            CompoundTag tag = curio.writeSyncData(slotContext);
            return tag != null ? tag : new CompoundTag();
        }).orElse(new CompoundTag()) : new CompoundTag();
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new SPacketSyncStack(livingEntity.m_19879_(), identifier, index, stack, type, syncTag));
    }
}