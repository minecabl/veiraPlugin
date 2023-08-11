package kabl.veira.core;

import kabl.veira.Veira;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;

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

        //Buy item
        ItemStack buyer = new ItemStack(Material.PAPER);

        ItemMeta buyerMeta = buyer.getItemMeta();
        buyerMeta.displayName(Component.text("Klicke hier um Kablcoins zu kaufen!"));

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
}
