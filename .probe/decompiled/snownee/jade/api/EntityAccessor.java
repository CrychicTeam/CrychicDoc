package snownee.jade.api;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

public interface EntityAccessor extends Accessor<EntityHitResult> {

    Entity getEntity();

    default Entity getRawEntity() {
        return this.getEntity();
    }

    @Override
    default Class<? extends Accessor<?>> getAccessorType() {
        return EntityAccessor.class;
    }

    @NonExtendable
    public interface Builder {

        EntityAccessor.Builder level(Level var1);

        EntityAccessor.Builder player(Player var1);

        EntityAccessor.Builder serverData(CompoundTag var1);

        EntityAccessor.Builder serverConnected(boolean var1);

        EntityAccessor.Builder showDetails(boolean var1);

        default EntityAccessor.Builder hit(EntityHitResult hit) {
            return this.hit(() -> hit);
        }

        EntityAccessor.Builder hit(Supplier<EntityHitResult> var1);

        default EntityAccessor.Builder entity(Entity entity) {
            return this.entity(() -> entity);
        }

        EntityAccessor.Builder entity(Supplier<Entity> var1);

        EntityAccessor.Builder from(EntityAccessor var1);

        EntityAccessor.Builder requireVerification();

        EntityAccessor build();
    }
}