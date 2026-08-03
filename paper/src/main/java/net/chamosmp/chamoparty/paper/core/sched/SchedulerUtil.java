package net.chamosmp.chamoparty.paper.core.sched;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Utility for transparent Folia/Paper scheduling.
 */
public final class SchedulerUtil {
    private static final Executor VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    private SchedulerUtil() {
    }

    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void runAsync(@NotNull Plugin plugin, @NotNull Runnable task) {
        if (isFolia()) {
            Bukkit.getAsyncScheduler().runNow(plugin, _ -> task.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }

    public static void runSync(@NotNull Plugin plugin, @NotNull Runnable task) {
        if (isFolia()) {
            Bukkit.getGlobalRegionScheduler().run(plugin, _ -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    public static void runForEntity(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable fallback) {
        if (isFolia()) {
            entity.getScheduler().run(plugin, _ -> task.run(), fallback);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    public static void runAtLocation(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable task) {
        if (isFolia()) {
            Bukkit.getRegionScheduler().run(plugin, location, _ -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    public static void runDelayed(@NotNull Plugin plugin, @NotNull Runnable task, long delayTicks) {
        if (isFolia()) {
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, _ -> task.run(), delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    public static void runAtEntityLater(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable fallback, long delayTicks) {
        if (isFolia()) {
            entity.getScheduler().runDelayed(plugin, _ -> task.run(), fallback, delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }
}