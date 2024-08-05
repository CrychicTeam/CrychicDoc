package noppes.npcs.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomEntities;
import noppes.npcs.ModelData;

public class EntityNpcCrystal extends EntityNPCInterface {

    public EntityNpcCrystal(EntityType<? extends EntityNPCInterface> type, Level world) {
        super(type, world);
        this.scaleX = 0.7F;
        this.scaleY = 0.7F;
        this.scaleZ = 0.7F;
        this.display.setSkinTexture("customnpcs:textures/entity/crystal/endercrystal.png");
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
            data.setEntity(ForgeRegistries.ENTITY_TYPES.getKey(CustomEntities.entityNpcCrystal));
            this.m_9236_().m_7967_(npc);
        }
        super.tick();
    }
}