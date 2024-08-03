package team.lodestar.lodestone.systems.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class LodestoneBoatEntity extends Boat {

    private final RegistryObject<Item> boatItem;

    @Deprecated
    public LodestoneBoatEntity(EntityType<? extends LodestoneBoatEntity> type, Level level, RegistryObject<Item> boatItem, RegistryObject<Item> plankItem) {
        this(type, level, boatItem);
    }

    public LodestoneBoatEntity(EntityType<? extends LodestoneBoatEntity> type, Level level, RegistryObject<Item> boatItem) {
        super(type, level);
        this.boatItem = boatItem;
    }

    @Override
    protected void checkFallDamage(double dY, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        this.f_38281_ = this.m_20184_().y;
        if (!this.m_20159_()) {
            if (onGround) {
                if (this.f_19789_ > 3.0F) {
                    if (this.f_38279_ != Boat.Status.ON_LAND) {
                        this.f_19789_ = 0.0F;
                        return;
                    }
                    this.m_142535_(this.f_19789_, 1.0F, this.m_9236_().damageSources().fall());
                    if (!this.m_9236_().isClientSide && !this.m_213877_()) {
                        this.m_6074_();
                        if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (int i = 0; i < 3; i++) {
                                this.m_19998_(this.m_28554_().getPlanks());
                            }
                            for (int j = 0; j < 2; j++) {
                                this.m_19998_(Items.STICK);
                            }
                        }
                    }
                }
                this.f_19789_ = 0.0F;
            } else if (!this.m_9236_().getFluidState(this.m_20183_().below()).is(FluidTags.WATER) && dY < 0.0) {
                this.f_19789_ = (float) ((double) this.f_19789_ - dY);
            }
        }
    }

    @NotNull
    @Override
    public Item getDropItem() {
        return this.boatItem.get();
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}