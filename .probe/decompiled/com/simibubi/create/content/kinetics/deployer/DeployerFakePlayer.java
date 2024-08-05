package com.simibubi.create.content.kinetics.deployer;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CKinetics;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber
public class DeployerFakePlayer extends FakePlayer {

    private static final Connection NETWORK_MANAGER = new Connection(PacketFlow.CLIENTBOUND);

    public static final UUID fallbackID = UUID.fromString("9e2faded-cafe-4ec2-c314-dad129ae971d");

    Pair<BlockPos, Float> blockBreakingProgress;

    ItemStack spawnedItemEffects;

    public boolean placedTracks;

    public boolean onMinecartContraption;

    private UUID owner;

    public DeployerFakePlayer(ServerLevel world, @Nullable UUID owner) {
        super(world, new DeployerFakePlayer.DeployerGameProfile(fallbackID, "Deployer", owner));
        this.f_8906_ = new DeployerFakePlayer.FakePlayNetHandler(world.getServer(), this);
        this.owner = owner;
    }

    @Override
    public OptionalInt openMenu(MenuProvider menuProvider) {
        return OptionalInt.empty();
    }

    @Override
    public Component getDisplayName() {
        return Lang.translateDirect("block.deployer.damage_source_name");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getEyeHeight(Pose poseIn) {
        return 0.0F;
    }

    @Override
    public Vec3 position() {
        return new Vec3(this.m_20185_(), this.m_20186_(), this.m_20189_());
    }

    @Override
    public float getCurrentItemAttackStrengthDelay() {
        return 0.015625F;
    }

    @Override
    public boolean canEat(boolean ignoreHunger) {
        return false;
    }

    @Override
    public ItemStack eat(Level world, ItemStack stack) {
        stack.shrink(1);
        return stack;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance pEffectInstance) {
        return false;
    }

    @Override
    public UUID getUUID() {
        return this.owner == null ? super.m_20148_() : this.owner;
    }

    @SubscribeEvent
    public static void deployerHasEyesOnHisFeet(EntityEvent.Size event) {
        if (event.getEntity() instanceof DeployerFakePlayer) {
            event.setNewEyeHeight(0.0F);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void deployerCollectsDropsFromKilledEntities(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        Entity trueSource = source.getEntity();
        if (trueSource != null && trueSource instanceof DeployerFakePlayer fakePlayer) {
            event.getDrops().forEach(stack -> fakePlayer.m_150109_().placeItemBackInInventory(stack.getItem()));
            event.setCanceled(true);
        }
    }

    @Override
    protected boolean doesEmitEquipEvent(EquipmentSlot equipmentSlot0) {
        return false;
    }

    @Override
    public void remove(Entity.RemovalReason entityRemovalReason0) {
        if (this.blockBreakingProgress != null && !this.m_9236_().isClientSide) {
            this.m_9236_().destroyBlockProgress(this.m_19879_(), (BlockPos) this.blockBreakingProgress.getKey(), -1);
        }
        super.m_142687_(entityRemovalReason0);
    }

    @SubscribeEvent
    public static void deployerKillsDoNotSpawnXP(LivingExperienceDropEvent event) {
        if (event.getAttackingPlayer() instanceof DeployerFakePlayer) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void entitiesDontRetaliate(LivingChangeTargetEvent event) {
        if (event.getNewTarget() instanceof DeployerFakePlayer) {
            if (event.getEntity() instanceof Mob mob) {
                CKinetics.DeployerAggroSetting setting = (CKinetics.DeployerAggroSetting) AllConfigs.server().kinetics.ignoreDeployerAttacks.get();
                switch(setting) {
                    case ALL:
                        mob.setTarget(null);
                        break;
                    case CREEPERS:
                        if (mob instanceof Creeper) {
                            mob.setTarget(null);
                        }
                    case NONE:
                }
            }
        }
    }

    private static class DeployerGameProfile extends GameProfile {

        private UUID owner;

        public DeployerGameProfile(UUID id, String name, UUID owner) {
            super(id, name);
            this.owner = owner;
        }

        public UUID getId() {
            return this.owner == null ? super.getId() : this.owner;
        }

        public String getName() {
            if (this.owner == null) {
                return super.getName();
            } else {
                String lastKnownUsername = UsernameCache.getLastKnownUsername(this.owner);
                return lastKnownUsername == null ? super.getName() : lastKnownUsername;
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else {
                return !(o instanceof GameProfile otherProfile) ? false : Objects.equals(this.getId(), otherProfile.getId()) && Objects.equals(this.getName(), otherProfile.getName());
            }
        }

        public int hashCode() {
            UUID id = this.getId();
            String name = this.getName();
            int result = id == null ? 0 : id.hashCode();
            return 31 * result + (name == null ? 0 : name.hashCode());
        }
    }

    private static class FakePlayNetHandler extends ServerGamePacketListenerImpl {

        public FakePlayNetHandler(MinecraftServer server, ServerPlayer playerIn) {
            super(server, DeployerFakePlayer.NETWORK_MANAGER, playerIn);
        }

        @Override
        public void send(Packet<?> packetIn) {
        }

        @Override
        public void send(Packet<?> packet0, @Nullable PacketSendListener packetSendListener1) {
        }
    }
}