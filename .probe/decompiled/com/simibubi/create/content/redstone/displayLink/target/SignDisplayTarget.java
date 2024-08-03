package com.simibubi.create.content.redstone.displayLink.target;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

public class SignDisplayTarget extends DisplayTarget {

    @Override
    public void acceptText(int line, List<MutableComponent> text, DisplayLinkContext context) {
        if (context.getTargetBlockEntity() instanceof SignBlockEntity sign) {
            boolean changed = false;
            SignText signText = new SignText();
            for (int i = 0; i < text.size() && i + line < 4; i++) {
                if (i == 0) {
                    reserve(i + line, sign, context);
                }
                if (i > 0 && this.isReserved(i + line, sign, context)) {
                    break;
                }
                signText.setMessage(i + line, (Component) text.get(i));
                changed = true;
            }
            if (changed) {
                for (boolean side : Iterate.trueAndFalse) {
                    sign.setText(signText, side);
                }
            }
            context.level().sendBlockUpdated(context.getTargetPos(), sign.m_58900_(), sign.m_58900_(), 2);
        }
    }

    @Override
    public DisplayTargetStats provideStats(DisplayLinkContext context) {
        return new DisplayTargetStats(4, 15, this);
    }
}