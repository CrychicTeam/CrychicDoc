package dev.latvian.mods.kubejs.block.entity;

public record BlockEntityAttachmentHolder(int index, BlockEntityAttachment.Factory factory) {

    public String toString() {
        return "attachment_" + this.index;
    }
}