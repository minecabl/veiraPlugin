package kabl.veira.Gamble;

import kabl.veira.Veira;
import kabl.veira.core.InventoryProvider;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

public class SlotsGame {
    private long coinsPerRow;
    private int rows;
    private Inventory inventory;
    private Player player;
    private boolean spinning;

    private ItemStack[][] spinners;

    //For UI
    private ItemStack deactivated = new ItemStack(Material.COAL_BLOCK);
    private ItemStack activated = new ItemStack(Material.DIAMOND_BLOCK);
    private ItemStack changer = new ItemStack(Material.DIAMOND_ORE);
    private ItemStack einsatz = new ItemStack(Material.DIAMOND);
    private ItemStack start = new ItemStack(Material.LIME_SHULKER_BOX);

    //Slot Items
    private ItemStack apple = new ItemStack(Material.APPLE);
    private ItemStack iron = new ItemStack(Material.IRON_INGOT);
    private ItemStack gold = new ItemStack(Material.GOLD_INGOT);
    private ItemStack blaze = new ItemStack(Material.BLAZE_POWDER);
    private ItemStack diamond = new ItemStack(Material.DIAMOND);
    private ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG);


    SlotsGame(Player p){
        coinsPerRow = 10;
        rows = 1;
        player = p;
        inventory = InventoryProvider.getGambleSlotsGUI(p);
        spinners = new ItemStack[3][5];

        //For UI

    }

    public void clickRows(){
        if (this.rows == 3){
            this.rows = 1;
        } else {
            this.rows++;
        }
        this.updateInventory();
    }

    public void clickPrice(){
        switch ((int) this.coinsPerRow) {
            case 10: this.coinsPerRow = 20; break;
            case 20: this.coinsPerRow = 50; break;
            case 50: this.coinsPerRow = 100; break;
            case 100: this.coinsPerRow = 250; break;
            case 250: this.coinsPerRow = 500; break;
            case 500: this.coinsPerRow = 1000; break;
            case 1000: this.coinsPerRow = 10; break;
        }
        this.updateInventory();
    }

    private void updateInventory() {
        ItemMeta deMe = deactivated.getItemMeta();
        deMe.displayName(Component.text("Inaktiv").color(NamedTextColor.RED));
        deactivated.setItemMeta(deMe);

        ItemMeta acMe = activated.getItemMeta();
        acMe.displayName(Component.text("Aktiv").color(NamedTextColor.GREEN));
        activated.setItemMeta(acMe);

        ItemMeta changerMeta = changer.getItemMeta();
        changerMeta.displayName(Component.text("Anzahl der Reihen ändern").color(NamedTextColor.GREEN));

        ItemMeta einsatzMeta = einsatz.getItemMeta();
        einsatzMeta.displayName(Component.text("Einsatz anpassen").color(NamedTextColor.GREEN));

        ItemMeta startMeta = start.getItemMeta();
        startMeta.displayName(Component.text("Spielen").color(NamedTextColor.GREEN));


        //Line Setters ----------------------------
        inventory.setItem(10, rows > 1 ? activated : deactivated);
        inventory.setItem(19, activated);
        inventory.setItem(28, rows > 2 ? activated : deactivated);

        inventory.setItem(16, rows > 1 ? activated : deactivated);
        inventory.setItem(25, activated);
        inventory.setItem(34, rows > 2 ? activated : deactivated);

        ArrayList<Component> changerLore = new ArrayList<>();
        changerLore.add(Component.text("Aktuelle Reihen: " + rows).color(NamedTextColor.WHITE));
        changerMeta.lore(changerLore);

        changer.setItemMeta(changerMeta);

        inventory.setItem(46, changer);
        ArrayList<Component> einsatzLore = new ArrayList<>();
        einsatzLore.add(Component.text("Aktuell: " + this.coinsPerRow +" Kablcoins").color(NamedTextColor.WHITE));
        einsatzMeta.lore(einsatzLore);

        einsatz.setItemMeta(einsatzMeta);

        inventory.setItem(52, einsatz);

        //Start button ------------------------------
        ArrayList<Component> startLore = new ArrayList<>();
        startLore.add(Component.text("Kosten: " + this.getPrice() +" Kablcoins").color(NamedTextColor.WHITE));
        startMeta.lore(startLore);

        start.addUnsafeEnchantment(Enchantment.LUCK, 1);
        start.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        start.setItemMeta(startMeta);

        inventory.setItem(49, start);

        this.player.openInventory(this.inventory);
    }

    public long getPrice(){
        return this.rows * this.coinsPerRow;
    }

    public void spin() {
        VeiraPlayer vp = Veira.session.getVeiraPlayer(player);
        if (vp.getKablcoins() < getPrice()){
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 15, 15);
            player.sendMessage("Du hast nicht genug Kablcoins :(");
            return;
        }

        vp.removeKablcoins(getPrice());

        for (int i = 6; i <= 34; i+=2){
            int finalI = i;
            setSpinning(true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Veira.pluginInstance, new Runnable() {
                @Override
                public void run() {
                    if (finalI == 34) {
                        setSpinning(false);
                        checkForWins();
                    } else {

                        spinners[2] = spinners[1].clone();
                        spinners[1] = spinners[0].clone();

                        spinners[0][0] = getSpinnerEntry();
                        spinners[0][1] = getSpinnerEntry();
                        spinners[0][2] = getSpinnerEntry();
                        spinners[0][3] = getSpinnerEntry();
                        spinners[0][4] = getSpinnerEntry();

                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 29);

                        displaySpinners();
                    }
                }
            }, (long) Math.exp(1 + ((float)i / 10.0)));
        }
    }

    private void setSpinning(boolean b) {
        this.spinning = b;
    }

    private void checkForWins() {
        long wonCoins = 0;

        wonCoins += coinsPerRow * getRowMultiplier(1);

        if (rows > 1) {
            wonCoins += coinsPerRow * getRowMultiplier(0);

            if (rows == 3) {
                wonCoins += coinsPerRow * getRowMultiplier(2);
            }
        }

        if (wonCoins != 0){
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 29);
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 20, 29);
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 20, 20);
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 20, 40);
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 20, 10);
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_SAND_BREAK, 20, 10);
        }

        Veira.session.getVeiraPlayer(player).addKablcoins(wonCoins);
        player.sendMessage("+"+wonCoins+" Kablcoins");

        ItemStack info = new ItemStack(Material.OAK_SIGN);

        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.displayName(Component.text("Infos:"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Kablcoins insgesamt: " + Veira.session.getVeiraPlayer(player).getKablcoins() + "").color(NamedTextColor.GOLD));
        lore.add(Component.text("Letzer Gewinn: " + wonCoins + "!").color(NamedTextColor.GOLD));
        infoMeta.lore(lore);
        info.setItemMeta(infoMeta);
        inventory.setItem(40, info);

        this.player.openInventory(this.inventory);
    }

    private double getRowMultiplier(int index){
        Material testFor = spinners[index][0].getType();
        double multiplier = 0;

        if (testFor == Material.APPLE) {
            multiplier = 0.5;

            for (int i = 0; i <= 4; i++){
                if (spinners[index][i].getType() == Material.APPLE){
                    multiplier += 0.5;
                } else {
                    return multiplier;
                }
            }
        } else {
            for (int i = 0; i <= 4; i++) {
                if (spinners[index][i].getType() != testFor) {
                    return multiplier;
                }
                if (i >= 2) {
                    switch (spinners[index][i].getType()) {
                        case IRON_INGOT: multiplier += 5; break;
                        case GOLD_INGOT: multiplier += 10; break;
                        case BLAZE_POWDER: multiplier += 25; break;
                        case DIAMOND: multiplier += 100; break;
                        case DRAGON_EGG: multiplier += 1000; break;
                    }
                }
            }
        }

        return multiplier;
    }

    private ItemStack getSpinnerEntry(){

        Random random = new Random();
        int i = random.nextInt(1001);

        if (i == 0){
            return this.dragonEgg;
        }

        if (i >= 1 && i <= 10){
            return this.diamond;
        }

        if (i >= 11 && i <= 50){
            return this.blaze;
        }

        if (i >= 51 && i <= 200){
            return this.gold;
        }

        if (i >= 201 && i <= 500){
            return this.iron;
        }

        return apple;
    }

    private void displaySpinners(){
        int row = 0;
        int column = 0;
        for (int i = 11; i < 30; i+=9) {
            for (int j = 0; j < 5; j++) {
                this.inventory.setItem(i + j, spinners[row][column]);
                column++;
            }
            row++;
            column = 0;
        }
        player.openInventory(this.inventory);
    }

    public boolean isSpinning() {
        return this.spinning;
    }
}
