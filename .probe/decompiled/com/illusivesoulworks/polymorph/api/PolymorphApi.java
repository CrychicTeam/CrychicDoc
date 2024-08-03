package com.illusivesoulworks.polymorph.api;

import com.illusivesoulworks.polymorph.api.client.base.IPolymorphClient;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphCommon;

public final class PolymorphApi {

    public static final String MOD_ID = "polymorph";

    public static IPolymorphCommon common() {
        throw new IllegalStateException("Polymorph Common API missing!");
    }

    public static IPolymorphClient client() {
        throw new IllegalStateException("Polymorph Client API missing!");
    }
}