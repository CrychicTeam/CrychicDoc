package net.mehvahdjukaar.supplementaries.integration.forge;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ItemLike;

public class CreateCompatImpl {

    public static boolean isContraption(MovementContext context, Entity passenger) {
        if (passenger instanceof AbstractContraptionEntity ace && ace.getContraption() == context.contraption) {
            return true;
        }
        return false;
    }

    public static void setupClient() {
        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_TARGETS).add((ItemLike) ModRegistry.NOTICE_BOARD.get());
        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_TARGETS).add((ItemLike) ModRegistry.SIGN_POST_ITEMS.get(WoodTypeRegistry.OAK_TYPE));
        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_TARGETS).add((ItemLike) ModRegistry.SPEAKER_BLOCK.get());
        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_TARGETS).add((ItemLike) ModRegistry.BLACKBOARD.get());
        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_SOURCES).add((ItemLike) ModRegistry.NOTICE_BOARD.get());
        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_SOURCES).add((ItemLike) ModRegistry.GLOBE_ITEM.get());
        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_SOURCES).add((ItemLike) ModRegistry.PEDESTAL.get());
        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_SOURCES).add((ItemLike) ModRegistry.JAR.get());
    }
}