package net.zanckor.questapi.api.file.npc.entity_type_tag.codec;

import java.util.List;
import net.zanckor.questapi.api.file.FileAbstract;
import net.zanckor.questapi.api.file.npc.entity_type_tag.gateenum.LogicGate;

public class EntityTypeTagDialog extends FileAbstract {

    private String id;

    private List<String> entity_type;

    private List<EntityTypeTagDialog.EntityTypeTagDialogCondition> conditions;

    public List<String> getEntity_type() {
        return this.entity_type;
    }

    public List<EntityTypeTagDialog.EntityTypeTagDialogCondition> getConditions() {
        return this.conditions;
    }

    public class EntityTypeTagDialogCondition {

        private List<EntityTypeTagDialog.EntityTypeTagDialogCondition.EntityTypeTagDialogNBT> nbt;

        private String logic_gate;

        private List<String> dialog_list;

        public LogicGate getLogic_gate() {
            return LogicGate.valueOf(this.logic_gate);
        }

        public List<EntityTypeTagDialog.EntityTypeTagDialogCondition.EntityTypeTagDialogNBT> getNbt() {
            return this.nbt;
        }

        public List<String> getEntity_type() {
            return EntityTypeTagDialog.this.entity_type;
        }

        public List<String> getDialog_list() {
            return this.dialog_list;
        }

        public class EntityTypeTagDialogNBT {

            private String tag;

            private String value;

            public String getTag() {
                return this.tag;
            }

            public String getValue() {
                return this.value;
            }
        }
    }
}