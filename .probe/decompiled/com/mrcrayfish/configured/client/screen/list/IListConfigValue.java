package com.mrcrayfish.configured.client.screen.list;

import com.mrcrayfish.configured.api.IConfigValue;
import java.util.List;
import javax.annotation.Nullable;

public interface IListConfigValue<T> extends IConfigValue<List<T>> {

    @Nullable
    IListType<T> getListType();
}