package dev.latvian.mods.kubejs.level;

import dev.latvian.mods.kubejs.core.FireworkRocketEntityKJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class FireworksJS {

    public int flight = 2;

    public int lifetime = -1;

    public final List<FireworksJS.Explosion> explosions = new ArrayList();

    public static FireworksJS of(Object o) {
        Map<?, ?> properties = MapJS.of(o);
        FireworksJS fireworks = new FireworksJS();
        if (properties == null) {
            return fireworks;
        } else {
            if (properties.get("flight") instanceof Number flight) {
                fireworks.flight = flight.intValue();
            }
            if (properties.get("lifetime") instanceof Number lifetime) {
                fireworks.lifetime = lifetime.intValue();
            }
            if (properties.containsKey("explosions")) {
                for (Object o1 : ListJS.orSelf(properties.get("explosions"))) {
                    Map<?, ?> m = MapJS.of(o1);
                    if (m != null) {
                        FireworksJS.Explosion e = new FireworksJS.Explosion();
                        if (m.get("shape") instanceof String shape) {
                            e.shape = FireworksJS.Shape.get(shape);
                        }
                        if (m.get("flicker") instanceof Boolean flicker) {
                            e.flicker = flicker;
                        }
                        if (m.get("trail") instanceof Boolean trail) {
                            e.trail = trail;
                        }
                        if (m.containsKey("colors")) {
                            for (Object o2 : ListJS.orSelf(m.get("colors"))) {
                                e.colors.add(ColorWrapper.of(o2).getFireworkColorJS());
                            }
                        }
                        if (m.containsKey("fadeColors")) {
                            for (Object o2 : ListJS.orSelf(m.get("fadeColors"))) {
                                e.fadeColors.add(ColorWrapper.of(o2).getFireworkColorJS());
                            }
                        }
                        if (e.colors.isEmpty()) {
                            e.colors.add(ColorWrapper.YELLOW_DYE.getFireworkColorJS());
                        }
                        fireworks.explosions.add(e);
                    }
                }
            }
            if (fireworks.explosions.isEmpty()) {
                FireworksJS.Explosion ex = new FireworksJS.Explosion();
                ex.colors.add(ColorWrapper.YELLOW_DYE.getFireworkColorJS());
                fireworks.explosions.add(ex);
            }
            return fireworks;
        }
    }

    public FireworkRocketEntity createFireworkRocket(Level w, double x, double y, double z) {
        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("Flight", this.flight);
        ListTag list = new ListTag();
        for (FireworksJS.Explosion e : this.explosions) {
            CompoundTag nbt1 = new CompoundTag();
            nbt1.putInt("Type", e.shape.type);
            nbt1.putBoolean("Flicker", e.flicker);
            nbt1.putBoolean("Trail", e.trail);
            nbt1.putIntArray("Colors", e.colors.toIntArray());
            nbt1.putIntArray("FadeColors", e.fadeColors.toIntArray());
            list.add(nbt1);
        }
        nbt.put("Explosions", list);
        stack.addTagElement("Fireworks", nbt);
        FireworkRocketEntity rocket = new FireworkRocketEntity(w, x, y, z, stack);
        if (this.lifetime != -1) {
            ((FireworkRocketEntityKJS) rocket).setLifetimeKJS(this.lifetime);
        }
        rocket.m_6842_(true);
        return rocket;
    }

    public static class Explosion {

        public FireworksJS.Shape shape = FireworksJS.Shape.SMALL_BALL;

        public boolean flicker = false;

        public boolean trail = false;

        public final IntOpenHashSet colors = new IntOpenHashSet();

        public final IntOpenHashSet fadeColors = new IntOpenHashSet();
    }

    public static enum Shape {

        SMALL_BALL("small_ball", 0), LARGE_BALL("large_ball", 1), STAR("star", 2), CREEPER("creeper", 3), BURST("burst", 4);

        public static final FireworksJS.Shape[] VALUES = values();

        private final String name;

        public final int type;

        private Shape(String n, int t) {
            this.name = n;
            this.type = t;
        }

        public static FireworksJS.Shape get(String name) {
            for (FireworksJS.Shape s : VALUES) {
                if (s.name.equals(name)) {
                    return s;
                }
            }
            return SMALL_BALL;
        }
    }
}