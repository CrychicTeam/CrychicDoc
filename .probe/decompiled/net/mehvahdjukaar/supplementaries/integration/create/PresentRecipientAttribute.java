package net.mehvahdjukaar.supplementaries.integration.create;

import com.simibubi.create.content.logistics.filter.ItemAttribute;
import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.supplementaries.common.items.PresentItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class PresentRecipientAttribute implements ItemAttribute {

    public static final PresentRecipientAttribute EMPTY = new PresentRecipientAttribute("@e");

    String recipient;

    public PresentRecipientAttribute(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public boolean appliesTo(ItemStack itemStack) {
        return this.readRecipient(itemStack).equals(this.recipient);
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack itemStack) {
        String name = this.readRecipient(itemStack);
        List<ItemAttribute> atts = new ArrayList();
        if (name.length() > 0) {
            atts.add(new PresentRecipientAttribute(name));
        }
        return atts;
    }

    @Override
    public String getTranslationKey() {
        return "present_recipient";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[] { this.recipient };
    }

    @Override
    public void writeNBT(CompoundTag compoundTag) {
        compoundTag.putString("recipient", this.recipient);
    }

    @Override
    public ItemAttribute readNBT(CompoundTag compoundTag) {
        return new PresentRecipientAttribute(compoundTag.getString("recipient"));
    }

    private String readRecipient(ItemStack itemStack) {
        if (itemStack.getItem() instanceof PresentItem) {
            CompoundTag t = itemStack.getTagElement("BlockEntityTag");
            if (t != null) {
                String name = t.getString("Recipient");
                if (name != "@e") {
                    return name;
                }
            }
        }
        return "";
    }
}