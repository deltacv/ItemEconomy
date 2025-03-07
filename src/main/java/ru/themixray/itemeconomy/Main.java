package ru.themixray.itemeconomy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Config.loadConfig(new UnrealConfig(this, getDataFolder(), "config.yml"));
        Bukkit.getServicesManager().register(Economy.class, new VaultLayer(), this, ServicePriority.High);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static List<ItemStack> getInventory(Player player) {
        ArrayList<ItemStack> items = new ArrayList<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                items.add(item);
            }
        }

        for(ItemStack item : player.getEnderChest().getContents()) {
            if (item != null) {
                items.add(item);
            }
        }

        return items.stream()
                .map(o -> o == null ? new ItemStack(Material.AIR) : o)
                .toList();
    }

    public static boolean removeItems(Player player, Material type, int amount) {
        // also remove from enderchest
        int removed = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == type) {
                if (item.getAmount() > amount - removed) {
                    item.setAmount(item.getAmount() - (amount - removed));
                    removed = amount;
                    break;
                } else {
                    removed += item.getAmount();
                    item.setAmount(0);
                }
            }
        }

        for (ItemStack item : player.getEnderChest().getContents()) {
            if (item != null && item.getType() == type) {
                if (item.getAmount() > amount - removed) {
                    item.setAmount(item.getAmount() - (amount - removed));
                    removed = amount;
                    break;
                } else {
                    removed += item.getAmount();
                    item.setAmount(0);
                }
            }
        }

        return true;
    }

    public static void addItems(Player player, Material type, int amount) {
        HashMap<Integer, ItemStack> nope=player.getInventory().addItem(new ItemStack(type,amount));
        for(ItemStack v:nope.values())player.getWorld().dropItemNaturally(player.getLocation(),v);
    }
}