package net.minecraft.client;

import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.util.FormattedCharSequence;

public record GuiMessage(int f_90786_, Component f_240363_, @Nullable MessageSignature f_240905_, @Nullable GuiMessageTag f_240352_) {

    private final int addedTime;

    private final Component content;

    @Nullable
    private final MessageSignature signature;

    @Nullable
    private final GuiMessageTag tag;

    public GuiMessage(int f_90786_, Component f_240363_, @Nullable MessageSignature f_240905_, @Nullable GuiMessageTag f_240352_) {
        this.addedTime = f_90786_;
        this.content = f_240363_;
        this.signature = f_240905_;
        this.tag = f_240352_;
    }

    public static record Line(int f_240350_, FormattedCharSequence f_240339_, @Nullable GuiMessageTag f_240351_, boolean f_240367_) {

        private final int addedTime;

        private final FormattedCharSequence content;

        @Nullable
        private final GuiMessageTag tag;

        private final boolean endOfEntry;

        public Line(int f_240350_, FormattedCharSequence f_240339_, @Nullable GuiMessageTag f_240351_, boolean f_240367_) {
            this.addedTime = f_240350_;
            this.content = f_240339_;
            this.tag = f_240351_;
            this.endOfEntry = f_240367_;
        }
    }
}