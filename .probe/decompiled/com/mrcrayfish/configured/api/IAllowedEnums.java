package com.mrcrayfish.configured.api;

import java.util.Set;

public interface IAllowedEnums<T> {

    Set<T> getAllowedValues();
}