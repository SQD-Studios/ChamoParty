package net.chamosmp.chamoparty.core.utils;

import net.chamosmp.sqdlib.internal.AdventureUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @apiNote Used the colorutil of sqdlib
 */
@ApiStatus.Obsolete
public class ColorUtils extends AdventureUtil {

    public static @NotNull Component parse(@NotNull String message) {
        return AdventureUtil.parse(message);
    }

    public static @NotNull String deParse(@NotNull Component message) {
        return AdventureUtil.deParse(message);
    }

    public static List<String> deParse(List<Component> message) {
        List<String> list = new ArrayList<>();
        for (Component i : message) {
            list.add(deParse(i));
        }
        return list;
    }
}
