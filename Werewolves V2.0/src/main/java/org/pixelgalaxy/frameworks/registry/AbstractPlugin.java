package org.pixelgalaxy.frameworks.registry;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.pixelgalaxy.frameworks.registry.enums.LogType;
import org.pixelgalaxy.frameworks.registry.interfaces.Loggable;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractPlugin extends JavaPlugin implements Loggable {

    private static final LinkedHashMap<Class<? extends AbstractModule>, AbstractModule> MODULES = new LinkedHashMap<>();

    @Getter
    private static AbstractPlugin instance;

    protected abstract List<Class<? extends AbstractModule>> getModules();

    protected abstract String getPrefix();

    public AbstractPlugin() {
        super();
    }

    protected AbstractPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @SuppressWarnings("unchecked")
    public static <M extends AbstractModule> M getModule(Class<M> aClass) {
        return (M) MODULES.get(aClass);
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        instance = this;
        registerModules();
        //Language.loadLanguages();
    }

    private void registerModules() {
        getModules().stream()
                .filter(Objects::nonNull)
                .forEach(module -> {

                    try {

                        registerModule(module);
                        log(LogType.INFO, "+ Module " + module.getSimpleName() + " enabled");

                    } catch (IllegalAccessException illegalAccessEx) {
                        log(LogType.ERROR, "The module " + module.getSimpleName() + " has been illegally accessed!");
                        illegalAccessEx.printStackTrace();

                    } catch (InstantiationException instantiateEx) {
                        log(LogType.ERROR, "The module " + module.getSimpleName() + " couldn't be instantiated!");
                        instantiateEx.printStackTrace();

                    }

                });

        // Register listeners and commands for each module that is loaded
        PluginManager pluginManager = Bukkit.getPluginManager();
        Reflections reflections = new Reflections("org.pixelgalaxy.modules");
        Set<Class<? extends AbstractListener>> classes = reflections.getSubTypesOf(AbstractListener.class);
        Set<Class<? extends AbstractCommand>> commandClasses = reflections.getSubTypesOf(AbstractCommand.class);
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandClasses.forEach(commandClass -> {
            try {
                Constructor constructor = commandClass.getConstructor();
                commandManager.registerCommand((BaseCommand) constructor.newInstance());
                log(LogType.INFO, "Registered abstract command -> " + commandClass.getSimpleName());
            } catch (NoSuchMethodException noSuchMethodEx) {
                log(LogType.ERROR, "Constructor not found for " + commandClass.getSimpleName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        classes.forEach(abstractListener -> {
            try {
                Constructor constructor = abstractListener.getConstructor();
                pluginManager.registerEvents((AbstractListener) constructor.newInstance(), this);
                log(LogType.INFO, "Registered abstract listener -> " + abstractListener.getSimpleName());
            } catch (NoSuchMethodException noSuchMethodEx) {
                log(LogType.ERROR, "Constructor not found for " + abstractListener.getSimpleName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void registerModule(Class<? extends AbstractModule> aClass) throws IllegalAccessException, InstantiationException {
        AbstractModule abstractModule = aClass.newInstance();
        MODULES.put(aClass, abstractModule);
        abstractModule.onLoad();
    }

    public void log(LogType logType, String message) {
        Bukkit.getConsoleSender().sendMessage(getPrefix() + logType.getColor() + message);
    }

}
