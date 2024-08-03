package noppes.npcs.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomEntities;
import noppes.npcs.ModelData;

public class EntityNpcSlime extends EntityNPCInterface {

    public EntityNpcSlime(EntityType<? extends EntityNPCInterface> type, Level world) {
        super(type, world);
        this.scaleX = 2.0F;
        this.scaleY = 2.0F;
        this.scaleZ = 2.0F;
        this.display.setSkinTexture("customnpcs:textures/entity/slime/slime.png");
        this.baseSize = new EntityDimensions(0.8F, 0.8F, false);
    }

    @Override
    public EntityDimensions getDimensions(Pose pos) {
        return new EntityDimensions(0.8F, 0.8F, false);
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
            data.setEntity(ForgeRegistries.ENTITY_TYPES.getKey(CustomEntities.entityNpcSlime));
            this.m_9236_().m_7967_(npc);
        }
        super.tick();
    }
}