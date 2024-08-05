package io.github.apace100.origins.data;

import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.origins.origin.Impact;
import io.github.apace100.origins.origin.OriginUpgrade;
import java.util.List;

public final class OriginsDataTypes {

    public static final SerializableDataType<Impact> IMPACT = SerializableDataType.enumValue(Impact.class);

    @Deprecated
    public static final SerializableDataType<OriginUpgrade> UPGRADE = new SerializableDataType(OriginUpgrade.class, OriginUpgrade.CODEC);

    @Deprecated
    public static final SerializableDataType<List<OriginUpgrade>> UPGRADES = SerializableDataType.list(UPGRADE);
}