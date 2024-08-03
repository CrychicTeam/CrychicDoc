package com.simibubi.create.content.contraptions.minecart.capability;

import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.minecart.CouplingHandler;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.WorldAttached;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;

public class CapabilityMinecartController implements ICapabilitySerializable<CompoundTag> {

    public static WorldAttached<Map<UUID, MinecartController>> loadedMinecartsByUUID = new WorldAttached<>($ -> new HashMap());

    public static WorldAttached<Set<UUID>> loadedMinecartsWithCoupling = new WorldAttached<>($ -> new HashSet());

    static WorldAttached<List<AbstractMinecart>> queuedAdditions = new WorldAttached<>($ -> ObjectLists.synchronize(new ObjectArrayList()));

    static WorldAttached<List<UUID>> queuedUnloads = new WorldAttached<>($ -> ObjectLists.synchronize(new ObjectArrayList()));

    public static Capability<MinecartController> MINECART_CONTROLLER_CAPABILITY = CapabilityManager.get(new CapabilityToken<MinecartController>() {
    });

    private final LazyOptional<MinecartController> cap;

    private MinecartController handler;

    public static void tick(Level world) {
        List<UUID> toRemove = new ArrayList();
        Map<UUID, MinecartController> carts = loadedMinecartsByUUID.get(world);
        List<AbstractMinecart> queued = queuedAdditions.get(world);
        List<UUID> queuedRemovals = queuedUnloads.get(world);
        Set<UUID> cartsWithCoupling = loadedMinecartsWithCoupling.get(world);
        Set<UUID> keySet = carts.keySet();
        keySet.removeAll(queuedRemovals);
        cartsWithCoupling.removeAll(queuedRemovals);
        for (AbstractMinecart cart : queued) {
            UUID uniqueID = cart.m_20148_();
            if (world.isClientSide && carts.containsKey(uniqueID)) {
                MinecartController minecartController = (MinecartController) carts.get(uniqueID);
                if (minecartController != null) {
                    AbstractMinecart minecartEntity = minecartController.cart();
                    if (minecartEntity != null && minecartEntity.m_19879_() != cart.m_19879_()) {
                        continue;
                    }
                }
            }
            cartsWithCoupling.remove(uniqueID);
            LazyOptional<MinecartController> capability = cart.getCapability(MINECART_CONTROLLER_CAPABILITY);
            MinecartController controller = capability.orElse(null);
            capability.addListener(new CapabilityMinecartController.MinecartRemovalListener(world, cart));
            carts.put(uniqueID, controller);
            capability.ifPresent(mc -> {
                if (mc.isLeadingCoupling()) {
                    cartsWithCoupling.add(uniqueID);
                }
            });
            if (!world.isClientSide && controller != null) {
                controller.sendData();
            }
        }
        queuedRemovals.clear();
        queued.clear();
        for (Entry<UUID, MinecartController> entry : carts.entrySet()) {
            MinecartController controller = (MinecartController) entry.getValue();
            if (controller != null && controller.isPresent()) {
                controller.tick();
            } else {
                toRemove.add((UUID) entry.getKey());
            }
        }
        cartsWithCoupling.removeAll(toRemove);
        keySet.removeAll(toRemove);
    }

    public static void onChunkUnloaded(ChunkEvent.Unload event) {
        ChunkPos chunkPos = event.getChunk().getPos();
        Map<UUID, MinecartController> carts = loadedMinecartsByUUID.get(event.getLevel());
        for (MinecartController minecartController : carts.values()) {
            if (minecartController != null && minecartController.isPresent()) {
                AbstractMinecart cart = minecartController.cart();
                if (cart.m_146902_().equals(chunkPos)) {
                    queuedUnloads.get(event.getLevel()).add(cart.m_20148_());
                }
            }
        }
    }

    protected static void onCartRemoved(Level world, AbstractMinecart entity) {
        Map<UUID, MinecartController> carts = loadedMinecartsByUUID.get(world);
        List<UUID> unloads = queuedUnloads.get(world);
        UUID uniqueID = entity.m_20148_();
        if (carts.containsKey(uniqueID) && !unloads.contains(uniqueID)) {
            if (!world.isClientSide) {
                handleKilledMinecart(world, (MinecartController) carts.get(uniqueID), entity.m_20182_());
            }
        }
    }

    protected static void handleKilledMinecart(Level world, MinecartController controller, Vec3 removedPos) {
        if (controller != null) {
            for (boolean forward : Iterate.trueAndFalse) {
                MinecartController next = CouplingHandler.getNextInCouplingChain(world, controller, forward);
                if (next != null && next != MinecartController.EMPTY) {
                    next.removeConnection(!forward);
                    if (!controller.hasContraptionCoupling(forward)) {
                        AbstractMinecart cart = next.cart();
                        if (cart != null) {
                            Vec3 itemPos = cart.m_20182_().add(removedPos).scale(0.5);
                            ItemEntity itemEntity = new ItemEntity(world, itemPos.x, itemPos.y, itemPos.z, AllItems.MINECART_COUPLING.asStack());
                            itemEntity.setDefaultPickUpDelay();
                            world.m_7967_(itemEntity);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    public static MinecartController getIfPresent(Level world, UUID cartId) {
        Map<UUID, MinecartController> carts = loadedMinecartsByUUID.get(world);
        if (carts == null) {
            return null;
        } else {
            return !carts.containsKey(cartId) ? null : (MinecartController) carts.get(cartId);
        }
    }

    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof AbstractMinecart) {
            CapabilityMinecartController capability = new CapabilityMinecartController((AbstractMinecart) entity);
            ResourceLocation id = Create.asResource("minecart_controller");
            event.addCapability(id, capability);
            event.addListener(() -> {
                if (capability.cap.isPresent()) {
                    capability.cap.invalidate();
                }
            });
            queuedAdditions.get(entity.getCommandSenderWorld()).add((AbstractMinecart) entity);
        }
    }

    public static void startTracking(PlayerEvent.StartTracking event) {
        Entity entity = event.getTarget();
        if (entity instanceof AbstractMinecart) {
            entity.getCapability(MINECART_CONTROLLER_CAPABILITY).ifPresent(MinecartController::sendData);
        }
    }

    public CapabilityMinecartController(AbstractMinecart minecart) {
        this.handler = new MinecartController(minecart);
        this.cap = LazyOptional.of(() -> this.handler);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == MINECART_CONTROLLER_CAPABILITY ? this.cap.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return this.handler.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.handler.deserializeNBT(nbt);
    }

    public static class MinecartRemovalListener implements NonNullConsumer<LazyOptional<MinecartController>> {

        private Level world;

        private AbstractMinecart cart;

        public MinecartRemovalListener(Level world, AbstractMinecart cart) {
            this.world = world;
            this.cart = cart;
        }

        public boolean equals(Object obj) {
            return obj instanceof CapabilityMinecartController.MinecartRemovalListener;
        }

        public int hashCode() {
            return 100;
        }

        public void accept(LazyOptional<MinecartController> t) {
            CapabilityMinecartController.onCartRemoved(this.world, this.cart);
        }
    }
}