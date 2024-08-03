package dev.xkmc.modulargolems.content.entity.humanoid;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

public enum HumaniodGolemPartType implements IGolemPart<HumaniodGolemPartType> {

    BODY, ARMS, LEGS;

    @Override
    public MutableComponent getDesc(MutableComponent desc) {
        return Component.translatable("golem_part.humanoid_golem." + this.name().toLowerCase(Locale.ROOT), desc).withStyle(ChatFormatting.GREEN);
    }

    @Override
    public GolemPart<?, HumaniodGolemPartType> toItem() {
        return switch(this) {
            case BODY ->
                (GolemPart) GolemItems.HUMANOID_BODY.get();
            case ARMS ->
                (GolemPart) GolemItems.HUMANOID_ARMS.get();
            case LEGS ->
                (GolemPart) GolemItems.HUMANOID_LEGS.get();
        };
    }

    public void setupItemRender(PoseStack stack, ItemDisplayContext transform, @Nullable HumaniodGolemPartType part) {
        HumanoidGolemRenderer.transform(stack, transform, part);
    }
}