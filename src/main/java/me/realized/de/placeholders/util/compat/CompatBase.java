package me.realized.de.placeholders.util.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

class CompatBase {

    static final Method GET_HANDLE;
    static final Field PING;

    static {
        final Class<?> CB_PLAYER = ReflectionUtil.getCBClass("entity.CraftPlayer");
        GET_HANDLE = ReflectionUtil.getMethod(CB_PLAYER, "getHandle");

        final Class<?> NMS_PLAYER = ReflectionUtil.getNMSClass("EntityPlayer");
        PING = ReflectionUtil.getField(NMS_PLAYER, "ping");
    }
}
