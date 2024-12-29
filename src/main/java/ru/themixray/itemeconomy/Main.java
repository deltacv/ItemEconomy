package ru.themixray.itemeconomy;

import com.google.common.io.ByteArrayDataInput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Config.loadConfig(new UnrealConfig(this, getDataFolder(), "config.yml"));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static List<ItemStack> getInventory(Player player) {
        return Arrays.stream(player.getInventory().getContents()).map(o -> o == null ? new ItemStack(Material.AIR) : o).toList();
    }

    public static boolean removeItems(Player player, Material type, int amount) {
        if(player.getInventory().all(type).values().stream().mapToInt(ItemStack::getAmount).sum()<amount)return false;
        player.getInventory().removeItem(new ItemStack(type,amount));
        return true;
    }

    public static void addItems(Player player, Material type, int amount) {
        HashMap<Integer, ItemStack> nope=player.getInventory().addItem(new ItemStack(type,amount));
        for(ItemStack v:nope.values())player.getWorld().dropItemNaturally(player.getLocation(),v);
    }
}