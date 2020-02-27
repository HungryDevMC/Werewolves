package org.pixelgalaxy;

import org.pixelgalaxy.frameworks.registry.AbstractModule;
import org.pixelgalaxy.frameworks.registry.AbstractPlugin;

import java.util.List;

public class WerewolvesPlugin extends AbstractPlugin {

    @Override
    protected List<Class<? extends AbstractModule>> getModules() {
        return null;
    }

    @Override
    protected String getPrefix() {
        return "§8§l[§4§lWereWolves§8§l] §a";
    }
}
