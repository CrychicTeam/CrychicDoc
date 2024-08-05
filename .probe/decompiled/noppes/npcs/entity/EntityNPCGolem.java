package noppes.npcs.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomEntities;
import noppes.npcs.ModelData;

public class EntityNPCGolem extends EntityNPCInterface {

    public EntityNPCGolem(EntityType<? extends EntityNPCInterface> type, Level world) {
        super(type, world);
        this.display.setSkinTexture("customnpcs:textures/entity/golem/irongolem.png");
        this.baseSize = new EntityDimensions(1.4F, 2.5F, false);
    }

    @Override
    public EntityDimensions getDimensions(Pose pos) {
        this.currentAnimation = this.f_19804_.get(Animation);
        if (this.currentAnimation == 2) {
            return new EntityDimensions(0.5F, 0.5F, false);
        } else {
            return this.currentAnimation == 1 ? new EntityDimensions(1.4F, 2.0F, false) : new EntityDimensions(1.4F, 2.5F, false);
        }
    }

    @Override
    public void tick() {
        this.m_146870_();
        this.m_21557_(true);
        if (!this.m_9236_().isClientSide) {
            CompoundTag compound = new CompoundTag();
            this.m_7380_(compound);
            EntityCustomNpc npc = new EntityCustomNpc(CustomEntities.entityCustomNpc, this.m_9236_());
            npc.readAdditionalSaveData(compound);
            ModelData data = npc.modelData;
            data.setEntity(ForgeRegistries.ENTITY_TYPES.getKey(CustomEntities.entityNPCGolem));
            this.m_9236_().m_7967_(npc);
        }
        super.tick();
    }
}