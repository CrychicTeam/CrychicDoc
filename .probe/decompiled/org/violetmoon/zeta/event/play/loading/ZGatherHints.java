package org.violetmoon.zeta.event.play.loading;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.config.ConfigFlagManager;
import org.violetmoon.zeta.config.ConfigObjectMapper;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.RegistryUtil;

public interface ZGatherHints extends IZetaPlayEvent {

    void accept(ItemLike var1, Component var2);

    RegistryAccess getRegistryAccess();

    default void hintItem(Zeta zeta, ItemLike itemLike, Object... extra) {
        Item item = itemLike.asItem();
        ResourceLocation res = BuiltInRegistries.ITEM.getKey(item);
        String ns = res.getNamespace();
        String path = res.getPath();
        if (ns.equals(zeta.modid)) {
            ns = "";
        } else {
            ns = ns + ".";
        }
        this.hintItem(zeta, itemLike, ns + path, extra);
    }

    default void hintItem(Zeta zeta, ItemLike itemLike, String key, Object... extra) {
        Item item = itemLike.asItem();
        String hint = zeta.modid + ".jei.hint." + key;
        this.accept(item, Component.translatable(hint, extra));
    }

    default void gatherHintsFromModule(ZetaModule module, ConfigFlagManager cfm) {
        if (module.enabled) {
            List<Field> fields = ConfigObjectMapper.walkModuleFields(module.getClass());
            Map<String, Field> fieldsByName = new HashMap();
            for (Field f : fields) {
                fieldsByName.put(f.getName(), f);
            }
            for (Field f : fields) {
                try {
                    Hint hint = (Hint) f.getAnnotation(Hint.class);
                    if (hint != null) {
                        f.setAccessible(true);
                        Object target = ConfigObjectMapper.getField(module, f);
                        if (target != null) {
                            String flag = hint.value();
                            if (flag.isEmpty() || cfm.getFlag(flag) != !hint.negate()) {
                                String key = hint.key();
                                List<Object> extraList = new ArrayList(hint.content().length);
                                for (String c : hint.content()) {
                                    if (!c.isEmpty()) {
                                        Field extraField = (Field) fieldsByName.get(c);
                                        if (extraField == null) {
                                            throw new RuntimeException("No field " + c + " referenced in @Hint " + f);
                                        }
                                        Object yes = ConfigObjectMapper.getField(module, extraField);
                                        extraList.add(yes);
                                    }
                                }
                                Object[] extra = extraList.toArray(new Object[0]);
                                if (target instanceof TagKey<?> tkey) {
                                    this.applyTag(module.zeta, tkey, key, extra);
                                } else if (target instanceof Iterable<?> iter) {
                                    this.applyIterable(module.zeta, iter, key, extra);
                                } else {
                                    this.applyObject(module.zeta, target, key, extra);
                                }
                            }
                        }
                    }
                } catch (Exception var18) {
                    throw new RuntimeException("Problem applying annotation hint " + f.getName() + " from module " + module.getClass().getName() + ": " + var18.getMessage(), var18);
                }
            }
        }
    }

    private void applyTag(Zeta zeta, TagKey<?> tkey, String key, Object... extra) {
        if (key.isEmpty()) {
            key = tkey.location().getPath();
        }
        try {
            List<?> tagItems = RegistryUtil.getTagValues(this.getRegistryAccess(), tkey);
            this.applyIterable(zeta, tagItems, key, extra);
        } catch (IllegalStateException var6) {
            throw new RuntimeException("TagKey " + tkey + " failed to load.", var6);
        }
    }

    private void applyIterable(Zeta zeta, Iterable<?> iter, String key, Object... extra) {
        if (key.isEmpty()) {
            throw new RuntimeException("Multi-item @Hints need a defined key.");
        } else {
            for (Object obj : iter) {
                this.applyObject(zeta, obj, key, extra);
            }
        }
    }

    private void applyObject(Zeta zeta, Object obj, String key, Object... extra) {
        if (obj instanceof ItemLike ilike) {
            this.applyItemLike(zeta, ilike, key, extra);
        } else {
            throw new RuntimeException("Not an ItemLike.");
        }
    }

    private void applyItemLike(Zeta zeta, ItemLike itemLike, String key, Object... extra) {
        if (key.isEmpty()) {
            this.hintItem(zeta, itemLike, extra);
        } else {
            this.hintItem(zeta, itemLike, key, extra);
        }
    }
}