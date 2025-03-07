package ru.themixray.itemeconomy;

import org.bukkit.Material;

public class Config {
    public static Material NUGGET;
    public static Material INGOT;
    public static Material BLOCK;

    public static String FORMAT;
    public static String PLURAL;
    public static String SINGULAR;

    public static void loadConfig(UnrealConfig conf) {
        NUGGET = Material.getMaterial((String) conf.get("nugget"));
        INGOT = Material.getMaterial((String) conf.get("ingot"));
        BLOCK = Material.getMaterial((String) conf.get("block"));

        FORMAT = (String) conf.get("format");
        PLURAL = (String) conf.get("plural");
        SINGULAR = (String) conf.get("singular");
    }
}
