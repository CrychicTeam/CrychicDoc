package snownee.jade.impl;

import com.google.common.base.Suppliers;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import snownee.jade.Jade;
import snownee.jade.api.AccessorImpl;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.util.CommonProxy;
import snownee.jade.util.WailaExceptionHandler;

public class EntityAccessorImpl extends AccessorImpl<EntityHitResult> implements EntityAccessor {

    private final Supplier<Entity> entity;

    public EntityAccessorImpl(EntityAccessorImpl.Builder builder) {
        super(builder.level, builder.player, builder.serverData, builder.hit, builder.connected, builder.showDetails);
        this.entity = builder.entity;
    }

    public static void handleRequest(FriendlyByteBuf buf, ServerPlayer player, Consumer<Runnable> executor, Consumer<CompoundTag> responseSender) {
        EntityAccessor accessor;
        try {
            accessor = fromNetwork(buf, player);
        } catch (Exception var6) {
            WailaExceptionHandler.handleErr(var6, null, null, null);
            return;
        }
        executor.accept((Runnable) () -> {
            Entity entity = accessor.getEntity();
            if (entity != null && !(player.m_20280_(entity) > (double) Jade.MAX_DISTANCE_SQR)) {
                List<IServerDataProvider<EntityAccessor>> providers = WailaCommonRegistration.INSTANCE.getEntityNBTProviders(entity);
                if (!providers.isEmpty()) {
                    CompoundTag tag = accessor.getServerData();
                    for (IServerDataProvider<EntityAccessor> provider : providers) {
                        try {
                            provider.appendServerData(tag, accessor);
                        } catch (Exception var9) {
                            WailaExceptionHandler.handleErr(var9, provider, null, null);
                        }
                    }
                    tag.putInt("WailaEntityID", entity.getId());
                    responseSender.accept(tag);
                }
            }
        });
    }

    public static EntityAccessor fromNetwork(FriendlyByteBuf buf, ServerPlayer player) {
        EntityAccessorImpl.Builder builder = new EntityAccessorImpl.Builder();
        builder.level(player.m_9236_());
        builder.player(player);
        builder.showDetails(buf.readBoolean());
        int id = buf.readVarInt();
        int partIndex = buf.readVarInt();
        float hitX = buf.readFloat();
        float hitY = buf.readFloat();
        float hitZ = buf.readFloat();
        Supplier<Entity> entity = Suppliers.memoize(() -> CommonProxy.getPartEntity(builder.level.getEntity(id), partIndex));
        builder.entity(entity);
        builder.hit(Suppliers.memoize(() -> new EntityHitResult((Entity) entity.get(), new Vec3((double) hitX, (double) hitY, (double) hitZ))));
        return builder.build();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeBoolean(this.showDetails());
        Entity entity = this.getEntity();
        buf.writeVarInt(entity.getId());
        if (CommonProxy.isMultipartEntity(entity)) {
            buf.writeVarInt(CommonProxy.getPartEntityIndex(entity));
        } else {
            buf.writeVarInt(-1);
        }
        Vec3 hitVec = this.getHitResult().m_82450_();
        buf.writeFloat((float) hitVec.x);
        buf.writeFloat((float) hitVec.y);
        buf.writeFloat((float) hitVec.z);
    }

    @Override
    public Entity getEntity() {
        return CommonProxy.wrapPartEntityParent(this.getRawEntity());
    }

    @Override
    public Entity getRawEntity() {
        return (Entity) this.entity.get();
    }

    @Override
    public ItemStack getPickedResult() {
        return CommonProxy.getEntityPickedResult((Entity) this.entity.get(), this.getPlayer(), this.getHitResult());
    }

    @Override
    public Object getTarget() {
        return this.getEntity();
    }

    @Override
    public boolean verifyData(CompoundTag data) {
        if (!this.verify) {
            return true;
        } else {
            return !data.contains("WailaEntityID") ? false : data.getInt("WailaEntityID") == this.getEntity().getId();
        }
    }

    public static class Builder implements EntityAccessor.Builder {

        public boolean showDetails;

        private Level level;

        private Player player;

        private CompoundTag serverData;

        private boolean connected;

        private Supplier<EntityHitResult> hit;

        private Supplier<Entity> entity;

        private boolean verify;

        public EntityAccessorImpl.Builder level(Level level) {
            this.level = level;
            return this;
        }

        public EntityAccessorImpl.Builder player(Player player) {
            this.player = player;
            return this;
        }

        public EntityAccessorImpl.Builder serverData(CompoundTag serverData) {
            this.serverData = serverData;
            return this;
        }

        public EntityAccessorImpl.Builder serverConnected(boolean connected) {
            this.connected = connected;
            return this;
        }

        public EntityAccessorImpl.Builder showDetails(boolean showDetails) {
            this.showDetails = showDetails;
            return this;
        }

        public EntityAccessorImpl.Builder hit(Supplier<EntityHitResult> hit) {
            this.hit = hit;
            return this;
        }

        public EntityAccessorImpl.Builder entity(Supplier<Entity> entity) {
            this.entity = entity;
            return this;
        }

        public EntityAccessorImpl.Builder from(EntityAccessor accessor) {
            this.level = accessor.getLevel();
            this.player = accessor.getPlayer();
            this.serverData = accessor.getServerData();
            this.connected = accessor.isServerConnected();
            this.showDetails = accessor.showDetails();
            this.hit = accessor::getHitResult;
            this.entity = accessor::getRawEntity;
            return this;
        }

        @Override
        public EntityAccessor.Builder requireVerification() {
            this.verify = true;
            return this;
        }

        @Override
        public EntityAccessor build() {
            EntityAccessorImpl accessor = new EntityAccessorImpl(this);
            if (this.verify) {
                accessor.requireVerification();
            }
            return accessor;
        }
    }
}