package dev.latvian.mods.kubejs.util;

import dev.latvian.mods.kubejs.bindings.TextWrapper;
import dev.latvian.mods.kubejs.client.NotificationToast;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.util.color.SimpleColor;
import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NotificationBuilder {

    public static final Component[] NO_TEXT = new Component[0];

    public static final Duration DEFAULT_DURATION = Duration.ofSeconds(5L);

    public static final Color DEFAULT_BORDER_COLOR = new SimpleColor(4663636);

    public static final Color DEFAULT_BACKGROUND_COLOR = new SimpleColor(2364213);

    private static final int FLAG_ICON = 1;

    private static final int FLAG_TEXT_SHADOW = 2;

    private static final int FLAG_DURATION = 4;

    public Duration duration;

    public Component text;

    public transient int iconType;

    public transient String icon;

    public int iconSize;

    public Color outlineColor;

    public Color borderColor;

    public Color backgroundColor;

    public boolean textShadow;

    public static NotificationBuilder of(Context cx, Object object) {
        if (object instanceof NotificationBuilder) {
            return (NotificationBuilder) object;
        } else if (object instanceof Map<?, ?> map) {
            return null;
        } else if (object instanceof BaseFunction func) {
            Consumer consumer = (Consumer) NativeJavaObject.createInterfaceAdapter(cx, Consumer.class, func);
            NotificationBuilder b = new NotificationBuilder();
            consumer.accept(b);
            return b;
        } else {
            NotificationBuilder b = new NotificationBuilder();
            b.text = TextWrapper.of(object);
            return b;
        }
    }

    public static NotificationBuilder make(Consumer<NotificationBuilder> consumer) {
        NotificationBuilder b = new NotificationBuilder();
        consumer.accept(b);
        return b;
    }

    public NotificationBuilder() {
        this.duration = DEFAULT_DURATION;
        this.text = Component.empty();
        this.iconType = 0;
        this.icon = "";
        this.iconSize = 16;
        this.outlineColor = SimpleColor.BLACK;
        this.borderColor = DEFAULT_BORDER_COLOR;
        this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
        this.textShadow = true;
    }

    public NotificationBuilder(FriendlyByteBuf buf) {
        int flags = buf.readVarInt();
        this.text = buf.readComponent();
        this.duration = (flags & 4) != 0 ? Duration.ofMillis(buf.readVarLong()) : DEFAULT_DURATION;
        if ((flags & 1) != 0) {
            this.iconType = buf.readVarInt();
            this.icon = buf.readUtf();
            this.iconSize = buf.readByte();
        } else {
            this.iconType = 0;
            this.icon = "";
            this.iconSize = 16;
        }
        this.outlineColor = UtilsJS.readColor(buf);
        this.borderColor = UtilsJS.readColor(buf);
        this.backgroundColor = UtilsJS.readColor(buf);
        this.textShadow = (flags & 2) != 0;
    }

    public void write(FriendlyByteBuf buf) {
        int flags = 0;
        if (this.iconType != 0) {
            flags |= 1;
        }
        if (this.textShadow) {
            flags |= 2;
        }
        if (this.duration != DEFAULT_DURATION) {
            flags |= 4;
        }
        buf.writeVarInt(flags);
        buf.writeComponent(this.text);
        if (this.duration != DEFAULT_DURATION) {
            buf.writeVarLong(this.duration.toMillis());
        }
        if (this.iconType != 0) {
            buf.writeVarInt(this.iconType);
            buf.writeUtf(this.icon);
            buf.writeByte(this.iconSize);
        }
        UtilsJS.writeColor(buf, this.outlineColor);
        UtilsJS.writeColor(buf, this.borderColor);
        UtilsJS.writeColor(buf, this.backgroundColor);
    }

    public void setIcon(String icon) {
        this.icon = icon;
        this.iconType = 1;
    }

    public void setItemIcon(ItemStack stack) {
        this.icon = stack.kjs$getId();
        if (stack.getCount() > 1) {
            this.icon = stack.getCount() + "x " + this.icon;
        }
        if (stack.getTag() != null) {
            this.icon = this.icon + " " + stack.getTag();
        }
        this.iconType = 2;
    }

    public void setAtlasIcon(String icon) {
        this.icon = icon;
        this.iconType = 3;
    }

    @OnlyIn(Dist.CLIENT)
    public void show() {
        Minecraft mc = Minecraft.getInstance();
        mc.getToasts().addToast(new NotificationToast(mc, this));
    }
}