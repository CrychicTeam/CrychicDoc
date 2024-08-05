package com.illusivesoulworks.polymorph.platform.services;

import com.illusivesoulworks.polymorph.common.integration.AbstractCompatibilityModule;
import java.util.Map;
import java.util.function.Supplier;

public interface IIntegrationPlatform {

    Map<String, Supplier<Supplier<AbstractCompatibilityModule>>> createCompatibilityModules();
}