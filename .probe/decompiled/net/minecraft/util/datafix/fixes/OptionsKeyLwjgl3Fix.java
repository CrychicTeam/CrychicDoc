package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OptionsKeyLwjgl3Fix extends DataFix {

    public static final String KEY_UNKNOWN = "key.unknown";

    private static final Int2ObjectMap<String> MAP = (Int2ObjectMap<String>) DataFixUtils.make(new Int2ObjectOpenHashMap(), p_16640_ -> {
        p_16640_.put(0, "key.unknown");
        p_16640_.put(11, "key.0");
        p_16640_.put(2, "key.1");
        p_16640_.put(3, "key.2");
        p_16640_.put(4, "key.3");
        p_16640_.put(5, "key.4");
        p_16640_.put(6, "key.5");
        p_16640_.put(7, "key.6");
        p_16640_.put(8, "key.7");
        p_16640_.put(9, "key.8");
        p_16640_.put(10, "key.9");
        p_16640_.put(30, "key.a");
        p_16640_.put(40, "key.apostrophe");
        p_16640_.put(48, "key.b");
        p_16640_.put(43, "key.backslash");
        p_16640_.put(14, "key.backspace");
        p_16640_.put(46, "key.c");
        p_16640_.put(58, "key.caps.lock");
        p_16640_.put(51, "key.comma");
        p_16640_.put(32, "key.d");
        p_16640_.put(211, "key.delete");
        p_16640_.put(208, "key.down");
        p_16640_.put(18, "key.e");
        p_16640_.put(207, "key.end");
        p_16640_.put(28, "key.enter");
        p_16640_.put(13, "key.equal");
        p_16640_.put(1, "key.escape");
        p_16640_.put(33, "key.f");
        p_16640_.put(59, "key.f1");
        p_16640_.put(68, "key.f10");
        p_16640_.put(87, "key.f11");
        p_16640_.put(88, "key.f12");
        p_16640_.put(100, "key.f13");
        p_16640_.put(101, "key.f14");
        p_16640_.put(102, "key.f15");
        p_16640_.put(103, "key.f16");
        p_16640_.put(104, "key.f17");
        p_16640_.put(105, "key.f18");
        p_16640_.put(113, "key.f19");
        p_16640_.put(60, "key.f2");
        p_16640_.put(61, "key.f3");
        p_16640_.put(62, "key.f4");
        p_16640_.put(63, "key.f5");
        p_16640_.put(64, "key.f6");
        p_16640_.put(65, "key.f7");
        p_16640_.put(66, "key.f8");
        p_16640_.put(67, "key.f9");
        p_16640_.put(34, "key.g");
        p_16640_.put(41, "key.grave.accent");
        p_16640_.put(35, "key.h");
        p_16640_.put(199, "key.home");
        p_16640_.put(23, "key.i");
        p_16640_.put(210, "key.insert");
        p_16640_.put(36, "key.j");
        p_16640_.put(37, "key.k");
        p_16640_.put(82, "key.keypad.0");
        p_16640_.put(79, "key.keypad.1");
        p_16640_.put(80, "key.keypad.2");
        p_16640_.put(81, "key.keypad.3");
        p_16640_.put(75, "key.keypad.4");
        p_16640_.put(76, "key.keypad.5");
        p_16640_.put(77, "key.keypad.6");
        p_16640_.put(71, "key.keypad.7");
        p_16640_.put(72, "key.keypad.8");
        p_16640_.put(73, "key.keypad.9");
        p_16640_.put(78, "key.keypad.add");
        p_16640_.put(83, "key.keypad.decimal");
        p_16640_.put(181, "key.keypad.divide");
        p_16640_.put(156, "key.keypad.enter");
        p_16640_.put(141, "key.keypad.equal");
        p_16640_.put(55, "key.keypad.multiply");
        p_16640_.put(74, "key.keypad.subtract");
        p_16640_.put(38, "key.l");
        p_16640_.put(203, "key.left");
        p_16640_.put(56, "key.left.alt");
        p_16640_.put(26, "key.left.bracket");
        p_16640_.put(29, "key.left.control");
        p_16640_.put(42, "key.left.shift");
        p_16640_.put(219, "key.left.win");
        p_16640_.put(50, "key.m");
        p_16640_.put(12, "key.minus");
        p_16640_.put(49, "key.n");
        p_16640_.put(69, "key.num.lock");
        p_16640_.put(24, "key.o");
        p_16640_.put(25, "key.p");
        p_16640_.put(209, "key.page.down");
        p_16640_.put(201, "key.page.up");
        p_16640_.put(197, "key.pause");
        p_16640_.put(52, "key.period");
        p_16640_.put(183, "key.print.screen");
        p_16640_.put(16, "key.q");
        p_16640_.put(19, "key.r");
        p_16640_.put(205, "key.right");
        p_16640_.put(184, "key.right.alt");
        p_16640_.put(27, "key.right.bracket");
        p_16640_.put(157, "key.right.control");
        p_16640_.put(54, "key.right.shift");
        p_16640_.put(220, "key.right.win");
        p_16640_.put(31, "key.s");
        p_16640_.put(70, "key.scroll.lock");
        p_16640_.put(39, "key.semicolon");
        p_16640_.put(53, "key.slash");
        p_16640_.put(57, "key.space");
        p_16640_.put(20, "key.t");
        p_16640_.put(15, "key.tab");
        p_16640_.put(22, "key.u");
        p_16640_.put(200, "key.up");
        p_16640_.put(47, "key.v");
        p_16640_.put(17, "key.w");
        p_16640_.put(45, "key.x");
        p_16640_.put(21, "key.y");
        p_16640_.put(44, "key.z");
    });

    public OptionsKeyLwjgl3Fix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsKeyLwjgl3Fix", this.getInputSchema().getType(References.OPTIONS), p_16633_ -> p_16633_.update(DSL.remainderFinder(), p_145575_ -> (Dynamic) p_145575_.getMapValues().map(p_145578_ -> p_145575_.createMap((Map) p_145578_.entrySet().stream().map(p_145580_ -> {
            if (((Dynamic) p_145580_.getKey()).asString("").startsWith("key_")) {
                int $$1 = Integer.parseInt(((Dynamic) p_145580_.getValue()).asString(""));
                if ($$1 < 0) {
                    int $$2 = $$1 + 100;
                    String $$3;
                    if ($$2 == 0) {
                        $$3 = "key.mouse.left";
                    } else if ($$2 == 1) {
                        $$3 = "key.mouse.right";
                    } else if ($$2 == 2) {
                        $$3 = "key.mouse.middle";
                    } else {
                        $$3 = "key.mouse." + ($$2 + 1);
                    }
                    return Pair.of((Dynamic) p_145580_.getKey(), ((Dynamic) p_145580_.getValue()).createString($$3));
                } else {
                    String $$7 = (String) MAP.getOrDefault($$1, "key.unknown");
                    return Pair.of((Dynamic) p_145580_.getKey(), ((Dynamic) p_145580_.getValue()).createString($$7));
                }
            } else {
                return Pair.of((Dynamic) p_145580_.getKey(), (Dynamic) p_145580_.getValue());
            }
        }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)))).result().orElse(p_145575_)));
    }
}