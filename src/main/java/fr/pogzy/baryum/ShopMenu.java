package fr.pogzy.baryum;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Arrays;

public class ShopMenu implements Listener {

    private final Baryum plugin;
    private static Economy econ = null;

    public ShopMenu(Baryum plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        setupEconomy();
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public void openCategoryMenu(Player player, String category) {
        Inventory categoryMenu = Bukkit.createInventory(null, 27, "Shop - " + category);

        if (category.equals("Nourritures")) {
            addItemToMenu(categoryMenu, Material.BREAD, "Pain", 1.0, -1);
            addItemToMenu(categoryMenu, Material.WHEAT, "Blé", 0.5, -1);
            addItemToMenu(categoryMenu, Material.COOKED_BEEF, "Steak cuit", 1.0, -1);
            addItemToMenu(categoryMenu, Material.BEEF, "Steak cru", 0.5, -1);
            addItemToMenu(categoryMenu, Material.GOLDEN_CARROT, "Carotte dorée", 3.0, -1);
        } else if (category.equals("Minerais")) {
            addItemToMenu(categoryMenu, Material.RAW_IRON, "Fer brut", -1, 0.5);
            addItemToMenu(categoryMenu, Material.IRON_INGOT, "Lingot de fer", 2.0, -1);
            addItemToMenu(categoryMenu, Material.RAW_GOLD, "Or brut", -1, 1.0);
            addItemToMenu(categoryMenu, Material.GOLD_INGOT, "Lingot d'or", 2.0, -1);
            addItemToMenu(categoryMenu, Material.DIAMOND, "Diamant", 25.0, 5.0);
            addItemToMenu(categoryMenu, Material.NETHERITE_INGOT, "Lingot de netherite", 150.0, 75.0);
        } else if (category.equals("Blocs")) {
            addItemToMenu(categoryMenu, Material.DIRT, "Bloc de terre", 0.1, 0.001);
            addItemToMenu(categoryMenu, Material.GRASS_BLOCK, "Bloc d'herbe", 0.5, -1);
            addItemToMenu(categoryMenu, Material.COBBLESTONE, "Cobblestone", 0.1, 0.001);
            addItemToMenu(categoryMenu, Material.STONE, "Stone", 0.5, -1);
            addItemToMenu(categoryMenu, Material.SAND, "Sable", 0.1, 0.001);
            addItemToMenu(categoryMenu, Material.PACKED_ICE, "Glace compacté", 1.0, -1);
            addItemToMenu(categoryMenu, Material.MOSS_BLOCK, "Bloc de mousse", 1.0, -1);
            addItemToMenu(categoryMenu, Material.DIORITE, "Bloc de diorite", 0.1, 0.001);
            addItemToMenu(categoryMenu, Material.ANDESITE, "Bloc d'andésite", 0.1, 0.001);
            addItemToMenu(categoryMenu, Material.TUFF, "Bloc de tuff", 0.1, 0.001);
            addItemToMenu(categoryMenu, Material.DEEPSLATE, "Deepslate", 0.1, 0.001);
        } else if (category.equals("Mobs")) {
            addItemToMenu(categoryMenu, Material.BONE, "Os", 1.0, -1);
            addItemToMenu(categoryMenu, Material.ROTTEN_FLESH, "Rotten Flesh", -1, 0.001);
            addItemToMenu(categoryMenu, Material.GUNPOWDER, "Gunpowder", 5.0, 0.5);
            addItemToMenu(categoryMenu, Material.ENDER_PEARL, "Ender Pearl", 10.0, -1, 16);
        }

        addReturnButton(categoryMenu);
        player.openInventory(categoryMenu);
    }

    private void addItemToMenu(Inventory menu, Material material, String name, double buyPrice, double sellPrice) {
        addItemToMenu(menu, material, name, buyPrice, sellPrice, 64);
    }

    private void addItemToMenu(Inventory menu, Material material, String name, double buyPrice, double sellPrice, int stackSize) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(
                buyPrice > 0 ? ChatColor.WHITE + "Clique gauche - Acheter: " + ChatColor.RED + buyPrice + "$ " + ChatColor.GREEN + "(Shift pour x" + stackSize + ": " + (buyPrice * stackSize) + "$)" : ChatColor.WHITE + "Indisponible à l'achat",
                sellPrice > 0 ? ChatColor.WHITE + "Clique droit - Vendre: " + ChatColor.RED + sellPrice + "$ " + ChatColor.GREEN + "(Shift pour x" + stackSize + ": " + (sellPrice * stackSize) + "$)" : ChatColor.WHITE + "Indisponible à la vente"
        ));
        item.setItemMeta(meta);
        menu.addItem(item);
    }

    private void addReturnButton(Inventory menu) {
        ItemStack returnItem = new ItemStack(Material.ARROW);
        ItemMeta meta = returnItem.getItemMeta();
        meta.setDisplayName("Retour");
        returnItem.setItemMeta(meta);
        menu.setItem(18, returnItem);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Baryum Shop")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            if (clickedItem.getType() == Material.WHEAT) {
                openCategoryMenu(player, "Nourritures");
            } else if (clickedItem.getType() == Material.DIAMOND) {
                openCategoryMenu(player, "Minerais");
            } else if (clickedItem.getType() == Material.GRASS_BLOCK) {
                openCategoryMenu(player, "Blocs");
            } else if (clickedItem.getType() == Material.BONE) {
                openCategoryMenu(player, "Mobs");
            }
        } else if (event.getView().getTitle().startsWith("Shop - ")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.hasItemMeta()) {
                String itemName = clickedItem.getItemMeta().getDisplayName();
                if (itemName.equals("Retour")) {
                    openShopMenu(player);
                } else {
                    if (event.isLeftClick()) {
                        buyItem(player, itemName, event.isShiftClick() ? 64 : 1);
                    } else if (event.isRightClick()) {
                        sellItem(player, itemName, event.isShiftClick() ? 64 : 1);
                    }
                }
            }
        }
    }

    private void buyItem(Player player, String itemName, int quantity) {
        double price = getItemBuyPrice(itemName) * quantity;
        if (price > 0 && econ.getBalance(player) >= price) {
            econ.withdrawPlayer(player, price);
            ItemStack itemStack = new ItemStack(getMaterialByName(itemName), quantity);
            player.getInventory().addItem(itemStack);
            player.sendMessage("Vous avez acheté " + quantity + " " + itemName + " pour " + price + "$.");
        } else {
            player.sendMessage("Vous n'avez pas assez d'argent pour acheter cet item ou cet item n'est pas disponible à l'achat.");
        }
    }

    private void sellItem(Player player, String itemName, int quantity) {
        double price = getItemSellPrice(itemName) * quantity;
        Material material = getMaterialByName(itemName);
        if (price > 0 && player.getInventory().containsAtLeast(new ItemStack(material), quantity)) {
            player.getInventory().removeItem(new ItemStack(material, quantity));
            econ.depositPlayer(player, price);
            player.sendMessage("Vous avez vendu " + quantity + " " + itemName + " pour " + price + "$.");
        } else {
            player.sendMessage("Vous n'avez pas cet item à vendre ou cet item n'est pas disponible à la vente.");
        }
    }

    private double getItemBuyPrice(String itemName) {
        switch (itemName) {
            case "Pain": return 1.0;
            case "Blé": return 0.5;
            case "Steak cuit": return 1.0;
            case "Steak cru": return 0.5;
            case "Carotte dorée": return 3.0;
            case "Lingot de fer": return 2.0;
            case "Diamant": return 25.0;
            case "Lingot de netherite": return 150.0;
            case "Bloc de terre": return 0.1;
            case "Bloc d'herbe": return 0.5;
            case "Cobblestone": return 0.1;
            case "Stone": return 0.5;
            case "Sable": return 0.1;
            case "Glace compacté": return 1.0;
            case "Bloc de mousse": return 1.0;
            case "Bloc de diorite": return 0.1;
            case "Bloc d'andésite": return 0.1;
            case "Bloc de tuff": return 0.1;
            case "Deepslate": return 0.1;
            case "Os": return 1.0;
            case "Rotten Flesh": return -1;
            case "Gunpowder": return 5.0;
            case "Ender Pearl": return 10.0;
            default: return -1;
        }
    }

    private double getItemSellPrice(String itemName) {
        switch (itemName) {
            case "Pain": return -1;
            case "Fer brut": return 0.5;
            case "Or brut": return 1.0;
            case "Diamant": return 5.0;
            case "Lingot de netherite": return 75.0;
            case "Bloc de terre": return 0.001;
            case "Cobblestone": return 0.001;
            case "Sable": return 0.001;
            case "Bloc de diorite": return 0.001;
            case "Bloc d'andésite": return 0.001;
            case "Bloc de tuff": return 0.001;
            case "Deepslate": return 0.001;
            case "Rotten Flesh": return 0.001;
            case "Gunpowder": return 0.5;
            case "Ender Pearl": return -1;
            default: return -1;
        }
    }

    private Material getMaterialByName(String itemName) {
        switch (itemName) {
            case "Pain": return Material.BREAD;
            case "Blé": return Material.WHEAT;
            case "Steak cuit": return Material.COOKED_BEEF;
            case "Steak cru": return Material.BEEF;
            case "Carotte dorée": return Material.GOLDEN_CARROT;
            case "Fer brut": return Material.RAW_IRON;
            case "Lingot de fer": return Material.IRON_INGOT;
            case "Or brut": return Material.RAW_GOLD;
            case "Lingot d'or": return Material.GOLD_INGOT;
            case "Diamant": return Material.DIAMOND;
            case "Lingot de netherite": return Material.NETHERITE_INGOT;
            case "Bloc de terre": return Material.DIRT;
            case "Bloc d'herbe": return Material.GRASS_BLOCK;
            case "Cobblestone": return Material.COBBLESTONE;
            case "Stone": return Material.STONE;
            case "Sable": return Material.SAND;
            case "Glace compacté": return Material.PACKED_ICE;
            case "Bloc de mousse": return Material.MOSS_BLOCK;
            case "Bloc de diorite": return Material.DIORITE;
            case "Bloc d'andésite": return Material.ANDESITE;
            case "Bloc de tuff": return Material.TUFF;
            case "Deepslate": return Material.DEEPSLATE;
            case "Os": return Material.BONE;
            case "Rotten Flesh": return Material.ROTTEN_FLESH;
            case "Gunpowder": return Material.GUNPOWDER;
            case "Ender Pearl": return Material.ENDER_PEARL;
            default: return Material.AIR;
        }
    }

    private void openShopMenu(Player player) {
        Inventory shopMenu = Bukkit.createInventory(null, 9, "Baryum Shop");

        ItemStack foodItem = new ItemStack(Material.WHEAT);
        ItemMeta foodMeta = foodItem.getItemMeta();
        foodMeta.setDisplayName("Nourritures");
        foodItem.setItemMeta(foodMeta);

        ItemStack oresItem = new ItemStack(Material.DIAMOND);
        ItemMeta oresMeta = oresItem.getItemMeta();
        oresMeta.setDisplayName("Minerais");
        oresItem.setItemMeta(oresMeta);

        ItemStack blocksItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta blocksMeta = blocksItem.getItemMeta();
        blocksMeta.setDisplayName("Blocs");
        blocksItem.setItemMeta(blocksMeta);

        ItemStack mobsItem = new ItemStack(Material.BONE);
        ItemMeta mobsMeta = mobsItem.getItemMeta();
        mobsMeta.setDisplayName("Mobs");
        mobsItem.setItemMeta(mobsMeta);

        shopMenu.setItem(1, foodItem);
        shopMenu.setItem(3, oresItem);
        shopMenu.setItem(5, blocksItem);
        shopMenu.setItem(7, mobsItem);

        player.openInventory(shopMenu);
    }
}