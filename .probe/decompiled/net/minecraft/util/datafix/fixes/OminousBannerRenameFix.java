package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Optional;

public class OminousBannerRenameFix extends ItemStackTagFix {

    public OminousBannerRenameFix(Schema schema0) {
        super(schema0, "OminousBannerRenameFix", p_216698_ -> p_216698_.equals("minecraft:white_banner"));
    }

    @Override
    protected <T> Dynamic<T> fixItemStackTag(Dynamic<T> dynamicT0) {
        Optional<? extends Dynamic<?>> $$1 = dynamicT0.get("display").result();
        if ($$1.isPresent()) {
            Dynamic<?> $$2 = (Dynamic<?>) $$1.get();
            Optional<String> $$3 = $$2.get("Name").asString().result();
            if ($$3.isPresent()) {
                String $$4 = (String) $$3.get();
                $$4 = $$4.replace("\"translate\":\"block.minecraft.illager_banner\"", "\"translate\":\"block.minecraft.ominous_banner\"");
                $$2 = $$2.set("Name", $$2.createString($$4));
            }
            return dynamicT0.set("display", $$2);
        } else {
            return dynamicT0;
        }
    }
}