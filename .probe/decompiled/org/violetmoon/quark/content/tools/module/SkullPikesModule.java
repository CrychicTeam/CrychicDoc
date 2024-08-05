package org.violetmoon.quark.content.tools.module;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tools.ai.RunAwayFromPikesGoal;
import org.violetmoon.quark.content.tools.client.render.entity.SkullPikeRenderer;
import org.violetmoon.quark.content.tools.entity.SkullPike;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.ZBlock;
import org.violetmoon.zeta.event.play.entity.ZEntityJoinLevel;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.MiscUtil;

@ZetaLoadModule(category = "tools")
public class SkullPikesModule extends ZetaModule {

    public static EntityType<SkullPike> skullPikeType;

    @Hint(key = "skull_pikes")
    public static TagKey<Block> pikeTrophiesTag;

    @Config
    public static double pikeRange = 5.0;

    @LoadEvent
    public final void register(ZRegister event) {
        skullPikeType = EntityType.Builder.of(SkullPike::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(3).updateInterval(Integer.MAX_VALUE).setShouldReceiveVelocityUpdates(false).setCustomClientFactory((spawnEntity, world) -> new SkullPike(skullPikeType, world)).build("skull_pike");
        Quark.ZETA.registry.register(skullPikeType, "skull_pike", Registries.ENTITY_TYPE);
    }

    @LoadEvent
    public final void setup(ZCommonSetup event) {
        pikeTrophiesTag = BlockTags.create(new ResourceLocation("quark", "pike_trophies"));
    }

    @LoadEvent
    public final void clientSetup(ZClientSetup event) {
        EntityRenderers.register(skullPikeType, SkullPikeRenderer::new);
    }

    @PlayEvent
    public void onPlaceBlock(ZBlock.EntityPlace event) {
        BlockState state = event.getPlacedBlock();
        if (state.m_204336_(pikeTrophiesTag) && event.getLevel() instanceof Level world) {
            BlockPos pos = event.getPos();
            BlockPos down = pos.below();
            BlockState downState = world.getBlockState(down);
            if (downState.m_204336_(BlockTags.FENCES)) {
                SkullPike pike = new SkullPike(skullPikeType, world);
                pike.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
                world.m_7967_(pike);
            }
        }
    }

    @PlayEvent
    public void onMonsterAppear(ZEntityJoinLevel event) {
        Entity e = event.getEntity();
        if (e instanceof Monster monster && !(e instanceof PatrollingMonster) && !(e instanceof Warden) && e.canChangeDimensions() && e.isAlive()) {
            boolean alreadySetUp = monster.f_21345_.getAvailableGoals().stream().anyMatch(goal -> goal.getGoal() instanceof RunAwayFromPikesGoal);
            if (!alreadySetUp) {
                MiscUtil.addGoalJustAfterLatestWithPriority(monster.f_21345_, 3, new RunAwayFromPikesGoal(monster, (float) pikeRange, 1.0, 1.2));
            }
        }
    }
}