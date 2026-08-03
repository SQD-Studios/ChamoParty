package net.chamosmp.chamoparty.paper.core.utils.storage;

import net.chamosmp.chamoparty.core.enums.Folder;
import net.chamosmp.chamoparty.core.utils.storage.DiscUtils;
import net.chamosmp.chamoparty.paper.core.Plugin;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import net.chamosmp.chamoparty.paper.save.Config;

import java.io.File;
import java.lang.reflect.Type;

import static net.chamosmp.chamoparty.paper.core.logger.Logger.log;

public class Persist extends Utils {

    private final Plugin p;

    public Persist(Plugin p) {
        this.p = p;
    }

    // ------------------------------------------------------------ //
    // GET NAME - What should we call this type of object?
    // ------------------------------------------------------------ //

    public static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String getName(Object o) {
        return getName(o.getClass());
    }

    public static String getName(Type type) {
        return getName(type.getClass());
    }

    // ------------------------------------------------------------ //
    // GET FILE - In which file would we like to store this object?
    // ------------------------------------------------------------ //

    public File getFile(String name) {
        return new File(p.getDataFolder(), name + ".json");
    }

    public File getFile(Class<?> clazz) {
        return getFile(getName(clazz));
    }

    public File getFile(Object obj) {
        return getFile(getName(obj));
    }

    // NICE WRAPPERS

    public <T> void loadOrSaveDefault(T def, Class<T> clazz) {
        loadOrSaveDefault(def, clazz, getFile(clazz));
    }

    public <T> void loadOrSaveDefault(T def, Class<T> clazz, String name) {
        loadOrSaveDefault(def, clazz, getFile(name));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz, Folder folder, String name) {
        return loadOrSaveDefault(def, clazz, getFile(folder.toFolder() + File.separator + name));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz, File file) {
        if (!file.exists()) {
            if (Config.enableLogMessage)
                log("Creating default: " + file, LogType.SUCCESS);
            this.save(def, file);
            return def;
        }

        T loaded = this.load(clazz, file);

        if (loaded == null) {
            log("Using default as I failed to load: " + file, LogType.WARNING);

            /*
             * Create new config backup
             */

            File backup = new File(file.getPath() + "_bad");
            if (backup.exists())
                backup.delete();
            log("Backing up copy of bad file to: " + backup, LogType.WARNING);

            file.renameTo(backup);

            return def;
        } else {

            if (Config.enableLogMessage)
                log(file.getAbsolutePath() + " loaded successfully !", LogType.SUCCESS);

        }

        return loaded;
    }

    // SAVE

    public boolean save(Object instance) {
        return save(instance, getFile(instance));
    }

    public boolean save(Object instance, String name) {
        return save(instance, getFile(name));
    }

    public boolean save(Object instance, Folder folder, String name) {
        return save(instance, getFile(folder.toFolder() + File.separator + name));
    }

    public boolean save(Object instance, File file) {

        try {

            boolean b = DiscUtils.writeCatch(file, p.getGson().toJson(instance));
            if (Config.enableLogMessage)
                log(file.getAbsolutePath() + " successfully saved !", LogType.SUCCESS);
            return b;

        } catch (Exception e) {

            if (Config.enableLogMessage)
                log("cannot save file " + file.getAbsolutePath(), LogType.ERROR);
            e.printStackTrace();

            return false;
        }
    }

    // LOAD BY CLASS

    public <T> T load(Class<T> clazz) {
        return load(clazz, getFile(clazz));
    }

    public <T> T load(Class<T> clazz, String name) {
        return load(clazz, getFile(name));
    }

    public <T> T load(Class<T> clazz, File file) {
        String content = DiscUtils.readCatch(file);
        if (content == null) {
            return null;
        }

        try {
            return p.getGson().fromJson(content, clazz);
        } catch (Exception ex) { // output the error message rather than full
            // stack trace; error parsing the file, most
            // likely
            if (Config.enableDebug)
                ex.printStackTrace();
            log(ex.getMessage(), LogType.ERROR);
        }

        return null;
    }

    // LOAD BY TYPE
    public <T> T load(Type typeOfT, String name) {
        return load(typeOfT, getFile(name));
    }

    public <T> T load(Type typeOfT, File file) {
        String content = DiscUtils.readCatch(file);
        if (content == null) {
            return null;
        }

        try {
            return p.getGson().fromJson(content, typeOfT);
        } catch (Exception ex) { // output the error message rather than full
            // stack trace; error parsing the file, most
            // likely
            log(ex.getMessage(), LogType.ERROR);
        }

        return null;
    }

}
