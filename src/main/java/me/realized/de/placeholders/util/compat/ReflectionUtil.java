package me.realized.de.placeholders.util.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import me.realized.de.placeholders.util.NumberUtil;
import org.bukkit.Bukkit;

public final class ReflectionUtil {

    private static final String PACKAGE_VERSION;
    private static final int MAJOR_VERSION;

    static {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        PACKAGE_VERSION = packageName.substring(packageName.lastIndexOf('.') + 1);
        MAJOR_VERSION = NumberUtil.parseInt(PACKAGE_VERSION.split("_")[1]).orElse(0);
    }

    public static int getMajorVersion() {
        return MAJOR_VERSION;
    }

    public static Class<?> getNMSClass(final String name) {
        try {
            return Class.forName("net.minecraft.server." + PACKAGE_VERSION + "." + name);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static Class<?> getCBClass(final String path) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + PACKAGE_VERSION + "." + path);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static Method getMethod(final Class<?> clazz, final String name, final Class<?>... parameters) {
        try {
            return clazz.getMethod(name, parameters);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    public static Field getField(final Class<?> clazz, final String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException ex) {
            return null;
        }
    }

    private ReflectionUtil() {}
}
