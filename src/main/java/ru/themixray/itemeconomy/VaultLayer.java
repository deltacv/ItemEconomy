package ru.themixray.itemeconomy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class VaultLayer implements Economy {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "ItemEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return Config.FORMAT.replace("{}", String.valueOf(amount));
    }

    @Override
    public String currencyNamePlural() {
        return Config.PLURAL;
    }

    @Override
    public String currencyNameSingular() {
        return Config.SINGULAR;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return Bukkit.getPlayer(playerName) != null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Player not found");
        }

        int totalNuggetsToRemove = (int) (amount * 9); // Convert to nuggets

        Inventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();

        int removedNuggets = 0;

        for (ItemStack item : contents) {
            if (item == null || removedNuggets >= totalNuggetsToRemove) continue;

            if (item.getType().equals(Config.NUGGET)) {
                int remove = Math.min(item.getAmount(), totalNuggetsToRemove - removedNuggets);
                item.setAmount(item.getAmount() - remove);
                removedNuggets += remove;
            } else if (item.getType().equals(Config.INGOT)) {
                int remove = Math.min(item.getAmount(), (totalNuggetsToRemove - removedNuggets) / 9);
                item.setAmount(item.getAmount() - remove);
                removedNuggets += remove * 9;
            } else if (item.getType().equals(Config.BLOCK)) {
                int remove = Math.min(item.getAmount(), (totalNuggetsToRemove - removedNuggets) / 81);
                item.setAmount(item.getAmount() - remove);
                removedNuggets += remove * 81;
            }
        }

        if (removedNuggets < totalNuggetsToRemove) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }

        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Player not found");
        }

        Inventory inventory = player.getInventory();

        int totalNuggets = (int) (amount * 9);

        int blocks = totalNuggets / 81;
        int ingots = (totalNuggets % 81) / 9;
        int nuggets = totalNuggets % 9;

        // Create the items to be added
        Main.addItems(player, Config.BLOCK, blocks);
        Main.addItems(player, Config.INGOT, ingots);
        Main.addItems(player, Config.NUGGET, nuggets);

        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public double getBalance(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return 0;
        }

        int totalNuggets = 0;

        for (ItemStack item : Main.getInventory(player)) {
            if (item == null) continue;

            if (item.getType().equals(Config.BLOCK)) {
                totalNuggets += item.getAmount() * 81;
            } else if (item.getType().equals(Config.INGOT)) {
                totalNuggets += item.getAmount() * 9;
            } else if (item.getType().equals(Config.NUGGET)) {
                totalNuggets += item.getAmount();
            }
        }

        return totalNuggets / 9.0; // Keep decimal precision
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return hasAccount(player.getName());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player.getName());
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(player.getName());
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player.getName());
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(player.getName(), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
