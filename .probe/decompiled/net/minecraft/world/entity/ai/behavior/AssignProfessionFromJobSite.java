package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

public class AssignProfessionFromJobSite {

    public static BehaviorControl<Villager> create() {
        return BehaviorBuilder.create(p_258312_ -> p_258312_.group(p_258312_.present(MemoryModuleType.POTENTIAL_JOB_SITE), p_258312_.registered(MemoryModuleType.JOB_SITE)).apply(p_258312_, (p_258304_, p_258305_) -> (p_258309_, p_258310_, p_258311_) -> {
            GlobalPos $$6 = p_258312_.get(p_258304_);
            if (!$$6.pos().m_203195_(p_258310_.m_20182_(), 2.0) && !p_258310_.assignProfessionWhenSpawned()) {
                return false;
            } else {
                p_258304_.erase();
                p_258305_.set($$6);
                p_258309_.broadcastEntityEvent(p_258310_, (byte) 14);
                if (p_258310_.getVillagerData().getProfession() != VillagerProfession.NONE) {
                    return true;
                } else {
                    MinecraftServer $$7 = p_258309_.getServer();
                    Optional.ofNullable($$7.getLevel($$6.dimension())).flatMap(p_22467_ -> p_22467_.getPoiManager().getType($$6.pos())).flatMap(p_258313_ -> BuiltInRegistries.VILLAGER_PROFESSION.m_123024_().filter(p_217125_ -> p_217125_.heldJobSite().test(p_258313_)).findFirst()).ifPresent(p_22464_ -> {
                        p_258310_.setVillagerData(p_258310_.getVillagerData().setProfession(p_22464_));
                        p_258310_.refreshBrain(p_258309_);
                    });
                    return true;
                }
            }
        }));
    }
}