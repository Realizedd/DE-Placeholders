package me.realized.de.placeholders.util.compat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;

public final class Ping {

    static Method GET_HANDLE;
    static Field PING;

    static {
        final Class<?> CB_PLAYER = ReflectionUtil.getCBClass("entity.CraftPlayer");

        if (CB_PLAYER != null) {
            GET_HANDLE = ReflectionUtil.getMethod(CB_PLAYER, "getHandle");
        }

        final Class<?> NMS_PLAYER = ReflectionUtil.getNMSClass("EntityPlayer");

        if (NMS_PLAYER != null) {
            PING = ReflectionUtil.getField(NMS_PLAYER, "ping");
        }
    }

    public static int getPing(final Player player) {
        if (ReflectionUtil.getMajorVersion() >= 17) {
            return player.getPing();
        }

        final Object handle;
        try {
            handle = GET_HANDLE.invoke(player);
            return (int) PING.get(handle);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    private Ping() {}
}
