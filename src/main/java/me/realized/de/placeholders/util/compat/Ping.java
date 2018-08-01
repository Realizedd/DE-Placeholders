package me.realized.de.placeholders.util.compat;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;

public final class Ping extends CompatBase {

   public static int getPing(final Player player) {
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
