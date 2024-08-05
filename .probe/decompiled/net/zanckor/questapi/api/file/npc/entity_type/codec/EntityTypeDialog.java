package net.zanckor.questapi.api.file.npc.entity_type.codec;

import java.util.List;
import net.zanckor.questapi.api.file.FileAbstract;

public class EntityTypeDialog extends FileAbstract {

    private String id;

    private List<String> entity_type;

    private List<String> dialog_list;

    public List<String> getEntity_type() {
        return this.entity_type;
    }

    public List<String> getDialog_list() {
        return this.dialog_list;
    }

    public String getId() {
        return this.id;
    }
}