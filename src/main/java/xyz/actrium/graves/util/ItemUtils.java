package xyz.actrium.graves.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class ItemUtils {

    public static String convertItemsToString(ItemStack[] itemStacks) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(baos);
        bukkitObjectOutputStream.writeObject(itemStacks);
        bukkitObjectOutputStream.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static ItemStack[] convertStringToitems(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(string);
        BukkitObjectInputStream bois = new BukkitObjectInputStream(new ByteArrayInputStream(data));
        ItemStack[] itemStacks = (ItemStack[])bois.readObject();
        bois.close();
        return itemStacks;
    }
}