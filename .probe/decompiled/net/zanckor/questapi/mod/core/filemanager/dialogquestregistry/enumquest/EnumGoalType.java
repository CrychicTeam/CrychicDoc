package net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest;

import net.zanckor.questapi.api.enuminterface.enumquest.IEnumQuestGoal;
import net.zanckor.questapi.api.enuminterface.enumquest.IEnumTargetType;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractGoal;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractTargetType;
import net.zanckor.questapi.example.common.handler.questgoal.CollectGoal;
import net.zanckor.questapi.example.common.handler.questgoal.CollectNBTGoal;
import net.zanckor.questapi.example.common.handler.questgoal.InteractEntityGoal;
import net.zanckor.questapi.example.common.handler.questgoal.InteractSpecificEntityGoal;
import net.zanckor.questapi.example.common.handler.questgoal.KillGoal;
import net.zanckor.questapi.example.common.handler.questgoal.MoveToGoal;
import net.zanckor.questapi.example.common.handler.questgoal.XpGoal;
import net.zanckor.questapi.example.common.handler.targettype.EntityTargetType;
import net.zanckor.questapi.example.common.handler.targettype.ItemTargetType;
import net.zanckor.questapi.example.common.handler.targettype.MoveToTargetType;
import net.zanckor.questapi.example.common.handler.targettype.XPTargetType;

public enum EnumGoalType implements IEnumQuestGoal {

    INTERACT_ENTITY(new InteractEntityGoal()),
    INTERACT_SPECIFIC_ENTITY(new InteractSpecificEntityGoal()),
    KILL(new KillGoal()),
    MOVE_TO(new MoveToGoal()),
    COLLECT(new CollectGoal()),
    COLLECT_WITH_NBT(new CollectNBTGoal()),
    XP(new XpGoal());

    final AbstractGoal goal;

    private EnumGoalType(AbstractGoal abstractGoal) {
        this.goal = abstractGoal;
        this.registerEnumGoal(this.getClass());
    }

    @Override
    public AbstractGoal getQuest() {
        return this.goal;
    }

    public static enum EnumTargetType implements IEnumTargetType {

        TARGET_TYPE_INTERACT_ENTITY(new EntityTargetType()),
        TARGET_TYPE_INTERACT_SPECIFIC_ENTITY(new EntityTargetType()),
        TARGET_TYPE_KILL(new EntityTargetType()),
        TARGET_TYPE_MOVE_TO(new MoveToTargetType()),
        TARGET_TYPE_COLLECT(new ItemTargetType()),
        TARGET_TYPE_COLLECT_WITH_NBT(new ItemTargetType()),
        TARGET_TYPE_XP(new XPTargetType());

        final AbstractTargetType targetType;

        private EnumTargetType(AbstractTargetType targetType) {
            this.targetType = targetType;
            this.registerTargetType(this.getClass());
        }

        @Override
        public AbstractTargetType getTargetType() {
            return this.targetType;
        }
    }
}