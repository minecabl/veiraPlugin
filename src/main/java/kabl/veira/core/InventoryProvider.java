package kabl.veira.core;

import kabl.veira.Veira;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class InventoryProvider {

    public static Inventory getGambleChooseGUI(Player p){

        Inventory gameChooser = Bukkit.createInventory(p, 9, Component.text("Wähle ein Spiel").color(NamedTextColor.LIGHT_PURPLE));

        //Slots Item-----------------
        ItemStack slots = new ItemStack(Material.CHISELED_SANDSTONE);

        ItemMeta slotsMeta = slots.getItemMeta();
        slotsMeta.displayName(Component.text("Slots").color(NamedTextColor.GOLD));

        ArrayList<Component> slotsLore = new ArrayList<>();
        slotsLore.add(Component.text("Spiele das Hit-Spiel ").color(NamedTextColor.WHITE));
        slotsLore.add(Component.text("Eye of Kabl").color(NamedTextColor.YELLOW));
        slotsMeta.lore(slotsLore);


        slots.setItemMeta(slotsMeta);
        gameChooser.setItem(0, slots);



        //Loot-Chest
        ArrayList<Component> lootBoxLore = new ArrayList<>();
        lootBoxLore.add(Component.text("Du hast aktuell " + Veira.session.getVeiraPlayer(p).getLootBoxes() + "Loot-Boxen").color(NamedTextColor.WHITE));
        ItemStack lootBox = createItemStackOf(Material.CHEST, "Öffne eine Loot-Box", NamedTextColor.GREEN, lootBoxLore);

        if (Veira.session.getVeiraPlayer(p).getLootBoxes() != 0) {
            lootBox.addUnsafeEnchantment(Enchantment.LUCK, 1);
            lootBox.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        gameChooser.setItem(1, lootBox);



        //Buy item
        ItemStack buyer = new ItemStack(Material.PAPER);

        ItemMeta buyerMeta = buyer.getItemMeta();
        buyerMeta.displayName(Component.text("Klicke hier um den Shop"));

        ArrayList<Component> buyerLore = new ArrayList<>();
        buyerLore.add(Component.text("Menge: 100 Kabl-Coins"));
        buyerLore.add(Component.text("Preis: 1 Diamant"));
        buyerMeta.lore(buyerLore);

        buyer.setItemMeta(buyerMeta);
        gameChooser.setItem(7, buyer);

        //Exit Item------------------
        ItemStack exit = new ItemStack(Material.BARRIER);

        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.displayName(Component.text("Verlassen").color(NamedTextColor.RED));

        exit.setItemMeta(exitMeta);
        gameChooser.setItem(8, exit);


        return gameChooser;
    }

    public static Inventory getGambleSlotsGUI(Player p){
        Inventory slots = Bukkit.createInventory(p, 54, Component.text("Eye of Kabl").color(NamedTextColor.YELLOW));

        //Header Item-----------------
        ItemStack header = new ItemStack(Material.GOLD_BLOCK);

        ItemMeta headerMeta = header.getItemMeta();
        headerMeta.displayName(Component.text("Eye of Kabl").color(NamedTextColor.GOLD));

        ArrayList<Component> headerLore = new ArrayList<>();
        headerLore.add(Component.text("Klicke auf spielen und gewinne groß!").color(NamedTextColor.WHITE));
        headerMeta.lore(headerLore);

        header.addUnsafeEnchantment(Enchantment.LUCK, 1);
        header.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        header.setItemMeta(headerMeta);
        slots.setItem(4, header);

        //Exit Item------------------
        ItemStack exit = new ItemStack(Material.BARRIER);

        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.displayName(Component.text("Zurück").color(NamedTextColor.RED));

        exit.setItemMeta(exitMeta);
        slots.setItem(53, exit);

        //Border block --------------------------
        ItemStack border = new ItemStack(Material.SANDSTONE_WALL);

        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.displayName(Component.text("$$$").color(NamedTextColor.GOLD));

        border.setItemMeta(borderMeta);

        for (int i = 0; i < 53; i += 9) {
            slots.setItem(i, border);
            if (i + 8 != 53) {
                slots.setItem(i + 8, border);
            }
        }

        //Line Setters ----------------------------
        ItemStack deactivated = new ItemStack(Material.COAL_BLOCK);
        ItemStack activated = new ItemStack(Material.DIAMOND_BLOCK);

        ItemMeta deMe = deactivated.getItemMeta();
        deMe.displayName(Component.text("Inaktiv").color(NamedTextColor.RED));
        deactivated.setItemMeta(deMe);

        ItemMeta acMe = activated.getItemMeta();
        acMe.displayName(Component.text("Aktiv").color(NamedTextColor.GREEN));
        activated.setItemMeta(acMe);

        activated.addUnsafeEnchantment(Enchantment.LUCK, 1);
        activated.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        slots.setItem(10, deactivated);
        slots.setItem(19, activated);
        slots.setItem(28, deactivated);

        slots.setItem(16, deactivated);
        slots.setItem(25, activated);
        slots.setItem(34, deactivated);


        //Border bottom top ---------------
        ItemStack borderH = new ItemStack(Material.SMOOTH_SANDSTONE);

        ItemMeta borderHMeta = border.getItemMeta();
        borderHMeta.displayName(Component.text("$$$").color(NamedTextColor.GOLD));
        borderH.setItemMeta(borderHMeta);

        for (int i = 1; i <= 7; i++) {
            if (i != 4) {
                slots.setItem(i, borderH);
            }
        }

        for (int i = 1; i <= 7; i++) {
            slots.setItem(i + 36, borderH);
        }

        //Row changer ----------------------------
        ItemStack changer = new ItemStack(Material.DIAMOND_ORE);

        ItemMeta changerMeta = changer.getItemMeta();
        changerMeta.displayName(Component.text("Anzahl der Reihen ändern").color(NamedTextColor.GREEN));

        ArrayList<Component> changerLore = new ArrayList<>();
        changerLore.add(Component.text("Aktuelle Reihen: 1").color(NamedTextColor.WHITE));
        changerMeta.lore(changerLore);

        changer.setItemMeta(changerMeta);

        slots.setItem(46, changer);

        //Start button ------------------------------
        ItemStack start = new ItemStack(Material.LIME_SHULKER_BOX);

        ItemMeta startMeta = start.getItemMeta();
        startMeta.displayName(Component.text("Spielen").color(NamedTextColor.GREEN));

        ArrayList<Component> startLore = new ArrayList<>();
        startLore.add(Component.text("Kosten: 10 Kablcoins").color(NamedTextColor.WHITE));
        startMeta.lore(startLore);

        start.addUnsafeEnchantment(Enchantment.LUCK, 1);
        start.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        start.setItemMeta(startMeta);

        slots.setItem(49, start);

        //Einsatz ------------------------------
        ItemStack einsatz = new ItemStack(Material.DIAMOND);

        ItemMeta einsatzMeta = einsatz.getItemMeta();
        einsatzMeta.displayName(Component.text("Einsatz anpassen").color(NamedTextColor.GREEN));

        ArrayList<Component> einsatzLore = new ArrayList<>();
        einsatzLore.add(Component.text("Aktuell: 10 Kablcoins").color(NamedTextColor.WHITE));
        einsatzMeta.lore(einsatzLore);

        einsatz.setItemMeta(einsatzMeta);

        slots.setItem(52, einsatz);

        //Info
        ItemStack info = new ItemStack(Material.OAK_SIGN);

        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.displayName(Component.text("Infos:"));


        ArrayList<Component> infolore = new ArrayList<>();
        infolore.add(Component.text("Kablcoins insgesamt: " + Veira.session.getVeiraPlayer(p).getKablcoins() + "").color(NamedTextColor.GOLD));
        infolore.add(Component.text("Letzer Gewinn:").color(NamedTextColor.GOLD));

        infoMeta.lore(infolore);

        info.setItemMeta(infoMeta);

        slots.setItem(40, info);

        return slots;
    }

    public static Inventory getShopGui(VeiraPlayer p) {
        Inventory shop = Bukkit.createInventory(p.getPlayer(), 54, Component.text("Shop").color(NamedTextColor.YELLOW));

        //Fill With yellow

        ItemStack background = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta backgroundMeta = background.getItemMeta();
        backgroundMeta.displayName(Component.text(""));
        background.setItemMeta(backgroundMeta);

        for (int i = 0; i < 54; i++) {
            shop.setItem(i, background);
        }

        //Buy 1
        ItemStack buyer = new ItemStack(Material.EMERALD_ORE);

        ItemMeta buyerMeta = buyer.getItemMeta();
        buyerMeta.displayName(Component.text("100 Kablcoins kaufen").color(NamedTextColor.GREEN));

        ArrayList<Component> buyerLore = new ArrayList<>();
        buyerLore.add(Component.text("Menge: 100 Kabl-Coins"));
        buyerLore.add(Component.text("Preis: 1 Diamant"));
        buyerMeta.lore(buyerLore);

        buyer.setItemMeta(buyerMeta);

        shop.setItem(11, buyer);

        //Buy 10
        ItemStack buyer2 = new ItemStack(Material.EMERALD);
        ItemMeta buyerMeta2 = buyer.getItemMeta();
        buyerMeta2.displayName(Component.text("1000 Kablcoins kaufen").color(NamedTextColor.GREEN));

        ArrayList<Component> buyer2Lore = new ArrayList<>();
        buyer2Lore.add(Component.text("Menge: 1000 Kabl-Coins"));
        buyer2Lore.add(Component.text("Preis: 10 Diamanten"));
        buyerMeta2.lore(buyer2Lore);

        buyer2.setItemMeta(buyerMeta2);

        shop.setItem(13, buyer2);

        //Buy 100
        ItemStack buyer3 = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta buyerMeta3 = buyer.getItemMeta();
        buyerMeta3.displayName(Component.text("10000 Kablcoins kaufen").color(NamedTextColor.GREEN));

        ArrayList<Component> buyer3Lore = new ArrayList<>();
        buyer3Lore.add(Component.text("Menge: 10000 Kabl-Coins"));
        buyer3Lore.add(Component.text("Preis: 100 Diamanten"));
        buyerMeta3.lore(buyer3Lore);

        buyer3.setItemMeta(buyerMeta3);


        shop.setItem(15, buyer3);

        //Exit Item------------------
        ItemStack exit = new ItemStack(Material.BARRIER);

        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.displayName(Component.text("Zurück").color(NamedTextColor.RED));

        exit.setItemMeta(exitMeta);
        shop.setItem(53, exit);

        //Header Item-----------------
        ItemStack header = new ItemStack(Material.GOLD_BLOCK);

        ItemMeta headerMeta = header.getItemMeta();
        headerMeta.displayName(Component.text("Shop").color(NamedTextColor.GOLD));

        ArrayList<Component> headerLore = new ArrayList<>();
        headerLore.add(Component.text("Kaufe und kaufe mit Kablcoins!").color(NamedTextColor.WHITE));
        headerLore.add(Component.text("Deine Kablcoins: " + p.getKablcoins()).color(NamedTextColor.WHITE));
        headerLore.add(Component.text("Deine Diamanten: " + p.getDiamonds()).color(NamedTextColor.WHITE));
        headerMeta.lore(headerLore);

        header.addUnsafeEnchantment(Enchantment.LUCK, 1);
        header.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        header.setItemMeta(headerMeta);
        shop.setItem(4, header);

        // Map
        ItemStack map = new ItemStack(Material.MAP);

        ItemMeta mapMeta = map.getItemMeta();
        mapMeta.displayName(Component.text("Map-Art"));

        ArrayList<Component> mapLore = new ArrayList<>();
        mapLore.add(Component.text("Preis: 1337 Kabl-Coins"));
        mapLore.add(Component.text("Erhalte eine Karte mit einem Bild deiner Wahl"));
        mapLore.add(Component.text("Schreibe nach dem Kauf einen direkten Link zu deinem Bild in den Chat"));

        mapMeta.lore(mapLore);
        map.setItemMeta(mapMeta);

        shop.setItem(28, map);

        // diamond_block
        ItemStack diamond_block = new ItemStack(Material.DIAMOND_BLOCK);

        ItemMeta diamond_blockMeta = diamond_block.getItemMeta();
        diamond_blockMeta.displayName(Component.text("Diamant-Block"));

        ArrayList<Component> diamond_blockLore = new ArrayList<>();
        diamond_blockLore.add(Component.text("Preis: 1000 Kabl-Coins"));

        diamond_blockMeta.lore(diamond_blockLore);
        diamond_block.setItemMeta(diamond_blockMeta);

        shop.setItem(29, diamond_block);

        return shop;
    }

    public static Inventory getRewardGUI(VeiraPlayer p) {

        Inventory reward = Bukkit.createInventory(p.getPlayer(), 54, Component.text("Täglicher Einlogbonus").color(NamedTextColor.YELLOW));

        //Fill With blue
        ItemStack background = createItemStackOf(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "");

        for (int i = 0; i < 54; i++) {
            reward.setItem(i, background);
        }

        int claimed = p.getLastLoginStreakRewardClaimed();
        long available = p.getLoginStreak();

        //Already claimed rewards
        ItemStack[] claimedItem = new ItemStack[claimed];
        for (int i = 0; i < claimed; i++) {
            claimedItem[i] = createItemStackOf(Material.BARRIER, "Tag " + i+1);
        }

        //Not finished yet
        ItemStack unfinished = createItemStackOf(Material.STONE, "Comming soon");


        for (int i = 9; i <= 36; i+=9) {
            for (int j = 1; j < 8; j++) {
                reward.setItem(i + j, unfinished);
            }
        }






        //Day 1
        ArrayList<Component> day1lore = new ArrayList<>();
        day1lore.add(Component.text("16x Fleisch").color(NamedTextColor.WHITE));
        ItemStack day1 = createItemStackOf(Material.COOKED_BEEF, "Tag 1", NamedTextColor.GOLD, day1lore);
        reward.setItem(10, day1);

        //Day 2
        ArrayList<Component> day2lore = new ArrayList<>();
        day2lore.add(Component.text("20x Eisenerz").color(NamedTextColor.WHITE));
        day2lore.add(Component.text("20x Kohle").color(NamedTextColor.WHITE));
        ItemStack day2 = createItemStackOf(Material.IRON_ORE, "Tag 2", NamedTextColor.GOLD, day2lore);
        reward.setItem(11, day2);

        //Day 3
        ArrayList<Component> day3lore = new ArrayList<>();
        day3lore.add(Component.text("32x EXP-Flasche").color(NamedTextColor.WHITE));
        day3lore.add(Component.text("8x TNT").color(NamedTextColor.WHITE));
        ItemStack day3 = createItemStackOf(Material.EXPERIENCE_BOTTLE, "Tag 3", NamedTextColor.GOLD, day3lore);
        reward.setItem(12, day3);

        //Day 4
        ArrayList<Component> day4lore = new ArrayList<>();
        day4lore.add(Component.text("20x Smaragd").color(NamedTextColor.WHITE));
        ItemStack day4 = createItemStackOf(Material.EMERALD, "Tag 4", NamedTextColor.GOLD, day4lore);
        reward.setItem(13, day4);

        //Day 5
        ArrayList<Component> day5lore = new ArrayList<>();
        day5lore.add(Component.text("1x Haltbarkeit 3 Buch").color(NamedTextColor.WHITE));
        ItemStack day5 = createItemStackOf(Material.ENCHANTED_BOOK, "Tag 5", NamedTextColor.GOLD, day5lore);
        reward.setItem(14, day5);

        //Day 6
        ArrayList<Component> day6lore = new ArrayList<>();
        day6lore.add(Component.text("64x Eichenholz").color(NamedTextColor.WHITE));
        day6lore.add(Component.text("64x Fichtenholz").color(NamedTextColor.WHITE));
        day6lore.add(Component.text("64x Birkenholz").color(NamedTextColor.WHITE));
        day6lore.add(Component.text("64x Dunkles Eichenholz").color(NamedTextColor.WHITE));
        ItemStack day6 = createItemStackOf(Material.OAK_LOG, "Tag 6", NamedTextColor.GOLD, day6lore);
        reward.setItem(15, day6);

        //Day 7
        ArrayList<Component> day7lore = new ArrayList<>();
        day7lore.add(Component.text("32x Obsidian").color(NamedTextColor.WHITE));
        day7lore.add(Component.text("1x Wither Schädel").color(NamedTextColor.WHITE));
        ItemStack day7 = createItemStackOf(Material.OBSIDIAN, "Tag 7", NamedTextColor.GOLD, day7lore);
        reward.setItem(16, day7);

        // Week 2
        
        //Day 8
        ArrayList<Component> day8lore = new ArrayList<>();
        day8lore.add(Component.text("2x Dorfbewohner").color(NamedTextColor.WHITE));
        ItemStack day8 = createItemStackOf(Material.VILLAGER_SPAWN_EGG, "Tag 8", NamedTextColor.GOLD, day8lore);
        reward.setItem(19, day8);

        //Day 9
        ArrayList<Component> day9lore = new ArrayList<>();
        day9lore.add(Component.text("1x Papagei").color(NamedTextColor.WHITE));
        day9lore.add(Component.text("1x Wolf").color(NamedTextColor.WHITE));
        day9lore.add(Component.text("1x Katze").color(NamedTextColor.WHITE));
        day9lore.add(Component.text("1x Axalotl").color(NamedTextColor.WHITE));
        ItemStack day9 = createItemStackOf(Material.PARROT_SPAWN_EGG, "Tag 9", NamedTextColor.GOLD, day9lore);
        reward.setItem(20, day9);

        //Day 10
        ArrayList<Component> day10lore = new ArrayList<>();
        day10lore.add(Component.text("1x Endportal").color(NamedTextColor.WHITE));
        ItemStack day10 = createItemStackOf(Material.END_PORTAL_FRAME, "Tag 10", NamedTextColor.GOLD, day10lore);
        reward.setItem(21, day10);

        //Day 11
        ArrayList<Component> day11lore = new ArrayList<>();
        day11lore.add(Component.text("1x Diamantaxt").color(NamedTextColor.WHITE));
        day11lore.add(Component.text("1x Schärfe 5").color(NamedTextColor.WHITE));
        day11lore.add(Component.text("1x Amboss").color(NamedTextColor.WHITE));
        day11lore.add(Component.text("32x EXP-Flasche").color(NamedTextColor.WHITE));
        ItemStack day11 = createItemStackOf(Material.DIAMOND_AXE, "Tag 11", NamedTextColor.GOLD, day11lore);
        reward.setItem(22, day11);

        //Day 12
        ArrayList<Component> day12lore = new ArrayList<>();
        day12lore.add(Component.text("3x Netherite-Barren").color(NamedTextColor.WHITE));
        day12lore.add(Component.text("1x Netherite-Upgrade").color(NamedTextColor.WHITE));
        ItemStack day12 = createItemStackOf(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, "Tag 12", NamedTextColor.GOLD, day12lore);
        reward.setItem(23, day12);

        //Day 13
        ArrayList<Component> day13lore = new ArrayList<>();
        day13lore.add(Component.text("1x Spezial-Holzschwert").color(NamedTextColor.WHITE));
        ItemStack day13 = createItemStackOf(Material.WOODEN_SWORD, "Tag 13", NamedTextColor.GOLD, day13lore);
        reward.setItem(24, day13);

        //Day 14
        ArrayList<Component> day14lore = new ArrayList<>();
        day14lore.add(Component.text("1x Bedrock").color(NamedTextColor.WHITE));
        ItemStack day14 = createItemStackOf(Material.BEDROCK, "Tag 14", NamedTextColor.GOLD, day14lore);
        reward.setItem(25, day14);


        int counter = 0;
        for (int i = 9; i <= 36; i+=9) {
            for (int j = 1; j < 8; j++) {
                if (counter < claimed) {
                    reward.setItem(i + j, claimedItem[counter]);
                    counter++;
                }
            }
        }



        return reward;
    }

    public static Inventory getLootBoxGUI(VeiraPlayer p) {
        Inventory lootBox = Bukkit.createInventory(p.getPlayer(), 27, Component.text("Loot-Boxen").color(NamedTextColor.GREEN));

        //Fill With blue
        ItemStack background = createItemStackOf(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "");

        for (int i = 0; i < 27; i++) {
            lootBox.setItem(i, background);
        }

        return lootBox;
    }

     private static ItemStack createItemStackOf(Material material, String title) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(title));
        item.setItemMeta(meta);
        return item;
    }

    static private ItemStack createItemStackOf(Material material, String title, NamedTextColor color) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(title).color(color));
        item.setItemMeta(meta);
        return item;
    }

    static private ItemStack createItemStackOf(Material material, String title, NamedTextColor color, ArrayList<Component> lore) {
        ItemStack item = createItemStackOf(material, title, color);

        ItemMeta meta = item.getItemMeta();
        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
