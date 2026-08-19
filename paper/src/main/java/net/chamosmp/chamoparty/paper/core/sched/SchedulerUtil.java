package net.chamosmp.chamoparty.paper.core.sched;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Utility for transparent Folia/Paper scheduling.
 */
public final class SchedulerUtil {
    private SchedulerUtil() {
    }

    public static void runAsync(@NotNull Plugin plugin, @NotNull Runnable task) {
        Bukkit.getAsyncScheduler().runNow(plugin, _ -> task.run());
    }

    public static void runSync(@NotNull Plugin plugin, @NotNull Runnable task) {
        Bukkit.getGlobalRegionScheduler().run(plugin, _ -> task.run());
    }

    public static void runForEntity(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable fallback) {
        entity.getScheduler().run(plugin, _ -> task.run(), fallback);
    }

    public static void runAtLocation(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable task) {
        Bukkit.getRegionScheduler().run(plugin, location, _ -> task.run());
    }

    public static void runDelayed(@NotNull Plugin plugin, @NotNull Runnable task, long delayTicks) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, _ -> task.run(), delayTicks);
    }

    public static void runAtEntityLater(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable fallback, long delayTicks) {
        entity.getScheduler().runDelayed(plugin, _ -> task.run(), fallback, delayTicks);
    }
}