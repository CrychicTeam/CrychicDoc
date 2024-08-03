package net.minecraft.nbt.visitors;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.TagType;

public record FieldTree(int f_202523_, Map<String, TagType<?>> f_202524_, Map<String, FieldTree> f_202525_) {

    private final int depth;

    private final Map<String, TagType<?>> selectedFields;

    private final Map<String, FieldTree> fieldsToRecurse;

    private FieldTree(int p_202527_) {
        this(p_202527_, new HashMap(), new HashMap());
    }

    public FieldTree(int f_202523_, Map<String, TagType<?>> f_202524_, Map<String, FieldTree> f_202525_) {
        this.depth = f_202523_;
        this.selectedFields = f_202524_;
        this.fieldsToRecurse = f_202525_;
    }

    public static FieldTree createRoot() {
        return new FieldTree(1);
    }

    public void addEntry(FieldSelector p_202539_) {
        if (this.depth <= p_202539_.path().size()) {
            ((FieldTree) this.fieldsToRecurse.computeIfAbsent((String) p_202539_.path().get(this.depth - 1), p_202534_ -> new FieldTree(this.depth + 1))).addEntry(p_202539_);
        } else {
            this.selectedFields.put(p_202539_.name(), p_202539_.type());
        }
    }

    public boolean isSelected(TagType<?> p_202536_, String p_202537_) {
        return p_202536_.equals(this.selectedFields().get(p_202537_));
    }
}