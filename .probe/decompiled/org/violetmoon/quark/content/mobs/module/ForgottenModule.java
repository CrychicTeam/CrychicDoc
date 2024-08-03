package org.violetmoon.quark.content.mobs.module;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.client.handler.ModelHandler;
import org.violetmoon.quark.content.mobs.client.render.entity.ForgottenRenderer;
import org.violetmoon.quark.content.mobs.entity.Forgotten;
import org.violetmoon.quark.content.mobs.item.ForgottenHatItem;
import org.violetmoon.zeta.advancement.modifier.MonsterHunterModifier;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.bus.ZResult;
import org.violetmoon.zeta.event.load.ZEntityAttributeCreation;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.living.ZMobSpawnEvent;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "mobs")
public class ForgottenModule extends ZetaModule {

    public static EntityType<Forgotten> forgottenType;

    @Hint
    public static Item forgotten_hat;

    @Config(description = "This is the probability of a Skeleton that spawns under the height threshold being replaced with a Forgotten.")
    public double forgottenSpawnRate = 0.05;

    @Config
    public int maxHeightForSpawn = 0;

    @LoadEvent
    public final void register(ZRegister event) {
        forgotten_hat = new ForgottenHatItem(this);
        forgottenType = EntityType.Builder.of(Forgotten::new, MobCategory.MONSTER).sized(0.7F, 2.4F).clientTrackingRange(8).setCustomClientFactory((spawnEntity, world) -> new Forgotten(forgottenType, world)).build("forgotten");
        Quark.ZETA.registry.register(forgottenType, "forgotten", Registries.ENTITY_TYPE);
        Quark.ZETA.entitySpawn.addEgg(forgottenType, 9868423, 3814192, this, BooleanSuppliers.TRUE);
        event.getAdvancementModifierRegistry().addModifier(new MonsterHunterModifier(this, ImmutableSet.of(forgottenType)));
    }

    @LoadEvent
    public final void entityAttrs(ZEntityAttributeCreation e) {
        e.put(forgottenType, Forgotten.registerAttributes().build());
    }

    @PlayEvent
    public void onSkeletonSpawn(ZMobSpawnEvent.CheckSpawn.Lowest event) {
        if (event.getSpawnType() != MobSpawnType.SPAWNER) {
            LivingEntity entity = event.getEntity();
            ZResult result = event.getResult();
            ServerLevelAccessor world = event.getLevel();
            if (entity.m_6095_() == EntityType.SKELETON && entity instanceof Mob mob && result != ZResult.DENY && entity.m_20186_() < (double) this.maxHeightForSpawn && world.m_213780_().nextDouble() < this.forgottenSpawnRate && (result == ZResult.ALLOW || mob.checkSpawnRules(world, event.getSpawnType()) && mob.checkSpawnObstruction(world))) {
                Forgotten forgotten = new Forgotten(forgottenType, entity.m_9236_());
                Vec3 epos = entity.m_20182_();
                forgotten.m_19890_(epos.x, epos.y, epos.z, entity.m_146908_(), entity.m_146909_());
                forgotten.prepareEquipment();
                BlockPos pos = BlockPos.containing(event.getX(), event.getY(), event.getZ());
                MobSpawnEvent.FinalizeSpawn newEvent = new MobSpawnEvent.FinalizeSpawn(forgotten, world, event.getX(), event.getY(), event.getZ(), world.m_6436_(pos), event.getSpawnType(), null, null, event.getSpawner());
                MinecraftForge.EVENT_BUS.post(newEvent);
                if (newEvent.getResult() != Result.DENY) {
                    world.m_7967_(forgotten);
                    event.setResult(ZResult.DENY);
                }
            }
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends ForgottenModule {

        @LoadEvent
        public final void clientSetup(ZClientSetup event) {
            event.enqueueWork(() -> {
                EntityRenderers.register(forgottenType, ForgottenRenderer::new);
                QuarkClient.ZETA_CLIENT.setHumanoidArmorModel(forgotten_hat, (living, stack, slot, original) -> ModelHandler.armorModel(ModelHandler.forgotten_hat, slot));
            });
        }
    }
}