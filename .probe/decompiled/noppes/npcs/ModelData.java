package noppes.npcs;

import java.lang.reflect.Method;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.common.util.LogWriter;

public class ModelData extends ModelDataShared {

    public boolean simpleRender = false;

    public EntityCustomNpc npc;

    public ModelData(EntityCustomNpc npc) {
        this.npc = npc;
    }

    public LivingEntity getEntity(EntityNPCInterface npc) {
        if (!this.hasEntity()) {
            return null;
        } else {
            if (this.entity == null) {
                try {
                    this.entity = (LivingEntity) ForgeRegistries.ENTITY_TYPES.getValue(this.getEntityName()).create(npc.m_9236_());
                    CompoundTag comp = new CompoundTag();
                    this.entity.addAdditionalSaveData(comp);
                    if (PixelmonHelper.isPixelmon(this.entity) && !this.extra.contains("Name")) {
                        this.extra.putString("Name", "abra");
                    }
                    comp = comp.merge(this.extra);
                    try {
                        this.entity.readAdditionalSaveData(comp);
                        if (PixelmonHelper.isPixelmon(this.entity)) {
                            PixelmonHelper.initEntity(this.entity, this.extra.getString("Name"));
                        }
                    } catch (Exception var7) {
                        LogWriter.except(var7);
                    }
                    this.entity.m_20331_(true);
                    this.entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double) npc.m_21233_());
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        this.entity.setItemSlot(slot, npc.getItemBySlot(slot));
                    }
                } catch (Exception var8) {
                    LogWriter.except(var8);
                }
            }
            return this.entity;
        }
    }

    public ModelData copy() {
        ModelData data = new ModelData(this.npc);
        data.load(this.save());
        return data;
    }

    @Override
    public CompoundTag save() {
        CompoundTag compound = super.save();
        compound.putBoolean("SimpleRender", this.simpleRender);
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.simpleRender = compound.getBoolean("SimpleRender");
    }

    public void setExtra(LivingEntity entity, String key, String value) {
        key = key.toLowerCase();
        if (key.equals("breed") && entity.m_20078_().equals("tgvstyle.Dog")) {
            try {
                Method method = entity.getClass().getMethod("getBreedID");
                Enum breed = (Enum) method.invoke(entity);
                method = entity.getClass().getMethod("setBreedID", breed.getClass());
                method.invoke(entity, ((Enum[]) breed.getClass().getEnumConstants())[Integer.parseInt(value)]);
                CompoundTag comp = new CompoundTag();
                entity.readAdditionalSaveData(comp);
                this.extra.putString("EntityData21", comp.getString("EntityData21"));
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }
        if (key.equalsIgnoreCase("name") && PixelmonHelper.isPixelmon(entity)) {
            this.extra.putString("Name", value);
        }
        this.clearEntity();
    }

    @Override
    public LivingEntity getOwner() {
        return this.npc;
    }

    public static ModelData get(EntityCustomNpc npc) {
        return npc.modelData;
    }
}