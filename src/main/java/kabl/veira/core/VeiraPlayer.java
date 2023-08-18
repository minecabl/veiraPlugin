package kabl.veira.core;

import com.google.gson.JsonObject;
import kabl.veira.Daily.DailyQuest;
import kabl.veira.Gamble.SlotsGame;
import kabl.veira.Veira;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;

public class VeiraPlayer {

    private String name;
    private long playtime;
    private long lastupdate;
    private long diamonds;
    private long nwordcount;
    private long kablcoins;

    private LocalDateTime dailyDate;
    private long dailyAmount;

    private Player player;
    private SlotsGame slotsGame;
    private long lootBoxes;

    private long blocksMined;
    private long blocksPlaced;
    private long monstersKilled;
    private long animalsKilled;

    private boolean schizo;
    private boolean sendingMessage = false;

    private long loginStreak;
    private long lastLoginStreakRewardClaimed;
    private LocalDateTime lastLogin;

    public VeiraPlayer(Player p) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.player = p;
        //New Player
        this.playtime = 0;
        this.lastupdate = System.currentTimeMillis()/1000;
        this.nwordcount = 0;
        this.name = p.getName();
        this.diamonds = 0;
        this.kablcoins = 0;

        this.blocksMined = 0;
        this.blocksPlaced = 0;
        this.monstersKilled = 0;
        this.animalsKilled = 0;

        this.loginStreak = 1;
        this.lastLogin = LocalDateTime.now();
        this.lastLoginStreakRewardClaimed = 0;
        this.lootBoxes = 0;

        this.schizo = false;

        //Daily
        this.dailyDate = null;
        this.dailyAmount = 0;
        DailyQuest.givePlayerNewQuest(this);
    }

    public VeiraPlayer(Player p, JsonObject j) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.player = p;
        //Login Player
        this.name = p.getName();
        this.playtime = j.has("playtime") ? j.get("playtime").getAsLong() : 0;
        this.lastupdate = System.currentTimeMillis()/1000;
        this.nwordcount = j.has("nwordcount") ? j.get("nwordcount").getAsLong() : 0;
        this.diamonds = j.has("diamonds") ? j.get("diamonds").getAsLong() : 0;
        this.kablcoins = j.has("kablcoins") ? j.get("kablcoins").getAsLong() : 0;
        this.dailyAmount = j.has("dailyAmount") ? j.get("dailyAmount").getAsLong() : 0;
        this.dailyDate = j.has("dailyDate") ? LocalDateTime.parse(j.get("dailyDate").getAsString()) : null;
        this.lootBoxes = j.has("lootBoxes") ? j.get("lootBoxes").getAsLong() : 0;

        this.blocksMined = j.has("blocksMined") ? j.get("blocksMined").getAsLong() : 0;
        this.blocksPlaced = j.has("blocksPlaced") ? j.get("blocksPlaced").getAsLong() : 0;
        this.monstersKilled = j.has("monstersKilled") ? j.get("monstersKilled").getAsLong() : 0;
        this.animalsKilled = j.has("animalsKilled") ? j.get("animalsKilled").getAsLong() : 0;

        this.lastLoginStreakRewardClaimed = j.has("lastLoginStreakRewardClaimed") ? j.get("lastLoginStreakRewardClaimed").getAsLong() : 0;
        this.loginStreak = j.has("loginStreak") ? j.get("loginStreak").getAsLong() : 1;

        if (j.has("lastLogin"))  {
            this.lastLogin = LocalDateTime.parse(j.get("lastLogin").getAsString());
            if (!(lastLogin.getDayOfYear() == LocalDateTime.now().getDayOfYear())) {
                if ((lastLogin.getDayOfYear() + 1 == LocalDateTime.now().getDayOfYear())) {
                    this.loginStreak += 1;
                } else {
                    this.loginStreak = 1;
                    this.lastLoginStreakRewardClaimed = 0;
                }
            }
        }

        this.lastLogin = LocalDateTime.now();

        this.schizo = false;
    }

    public VeiraPlayer(JsonObject j){
        //Offline Player
        this.name = j.has("name") ? j.get("name").getAsString() : "unknown";
        this.playtime = j.has("playtime") ? j.get("playtime").getAsLong() : 0;
        this.lastupdate = System.currentTimeMillis()/1000;
        this.nwordcount = j.has("nwordcount") ? j.get("nwordcount").getAsLong() : 0;
        this.diamonds = j.has("diamonds") ? j.get("diamonds").getAsLong() : 0;
        this.kablcoins = j.has("kablcoins") ? j.get("kablcoins").getAsLong() : 0;
        this.dailyAmount = j.has("dailyAmount") ? j.get("dailyAmount").getAsLong() : 0;
        this.blocksMined = j.has("blocksMined") ? j.get("blocksMined").getAsLong() : 0;
        this.blocksPlaced = j.has("blocksPlaced") ? j.get("blocksPlaced").getAsLong() : 0;
        this.monstersKilled = j.has("monstersKilled") ? j.get("monstersKilled").getAsLong() : 0;
        this.animalsKilled = j.has("animalsKilled") ? j.get("animalsKilled").getAsLong() : 0;
        this.lastLogin = j.has("lastLogin") ? LocalDateTime.parse(j.get("lastLogin").getAsString()) : null;
        this.lastLoginStreakRewardClaimed = j.has("lastLoginStreakRewardClaimed") ? j.get("lastLoginStreakRewardClaimed").getAsLong() : 0;
        this.loginStreak = j.has("loginStreak") ? j.get("loginStreak").getAsLong() : 1;

        this.schizo = false;
    }

    public JsonObject getPlayerAsJson(){
        JsonObject result = new JsonObject();

        result.addProperty("name", this.name);
        result.addProperty("playtime", this.playtime + System.currentTimeMillis()/1000 - this.lastupdate);
        result.addProperty("nwordcount", this.nwordcount);
        result.addProperty("dailyID", this.getDaily() == null ? 0 : this.getDaily().getId());
        result.addProperty("dailyDate", String.valueOf(this.dailyDate));
        result.addProperty("diamonds", this.diamonds);
        result.addProperty("kablcoins", this.kablcoins);
        result.addProperty("dailyAmount", this.dailyAmount);
        result.addProperty("blocksMined", this.blocksMined);
        result.addProperty("blocksPlaced", this.blocksPlaced);
        result.addProperty("monstersKilled", this.monstersKilled);
        result.addProperty("animalsKilled", this.animalsKilled);
        result.addProperty("loginStreak", this.loginStreak);
        result.addProperty("lastLogin", String.valueOf(this.lastLogin));
        result.addProperty("lastLoginStreakRewardClaimed", this.lastLoginStreakRewardClaimed);
        result.addProperty("lootBoxes", this.lootBoxes);


        return result;
    }

    public boolean depositDiamond(long amount) throws IOException {
        if (Veira.debug) {
            this.player.sendMessage("Trying to deposit: " + amount);
        }

        if(amount <= 0){
            this.player.sendMessage("Surely :)");
            return false;
        }

        Inventory inv = this.player.getInventory();

        if(inv.contains(Material.DIAMOND)){
            int counter = 0;
            for(int i = 0; i < 36; i++){
                if(inv.getItem(i) == null){
                    continue;
                }
                if(inv.getItem(i).getType() == Material.DIAMOND){

                    if(counter + inv.getItem(i).getAmount() > amount){
                        inv.setItem(i, new ItemStack(Material.DIAMOND, (int) (inv.getItem(i).getAmount() - amount)));
                        counter = (int)amount;
                        break;
                    } else {
                        counter += inv.getItem(i).getAmount();
                        inv.setItem(i, null);
                    }
                }
            }
            this.diamonds += counter;
            this.savePlayer();
            player.sendMessage(Component.text("+" + counter+" Diamanten").color(NamedTextColor.GREEN));
            return true;
        }

        return false;
    }

    public boolean withdrawDiamond(long amount) throws IOException {
        if(amount <= 0){
            this.player.sendMessage("Surely :)");
            return false;
        }

        if(amount > this.diamonds){
            amount = this.diamonds;
        }

        long amountForMsg = amount;

        Inventory inv = this.player.getInventory();


        if (!this.checkForSlots((int) amount, Material.DIAMOND)){
            return false;
        }


        for(int i = 0; i < 36; i++){
            if(inv.getItem(i) == null){
                if (amount >= 64){
                    inv.setItem(i, new ItemStack(Material.DIAMOND, 64));
                    this.diamonds -= 64;
                    amount -= 64;
                } else {
                    inv.setItem(i, new ItemStack(Material.DIAMOND, (int) amount));
                    this.diamonds -= amount;
                    break;
                }
            } else if(inv.getItem(i).getType() == Material.DIAMOND){
                if (amount + inv.getItem(i).getAmount() >= 64){
                    inv.setItem(i, new ItemStack(Material.DIAMOND, 64));
                    this.diamonds -= (64 - inv.getItem(i).getAmount());
                    amount -= (64 - inv.getItem(i).getAmount());
                } else {
                    inv.setItem(i, new ItemStack(Material.DIAMOND, (int) (amount + inv.getItem(i).getAmount())));
                    this.diamonds -= amount;
                    break;
                }
            }
        }
        this.player.sendMessage(Component.text("-" + amountForMsg + " Diamanten").color(NamedTextColor.RED));
        this.savePlayer();
        return true;
    }

    public void saidNword(){
        this.nwordcount++;
    }

    public long getPlaytime() {
        this.playtime += System.currentTimeMillis()/1000 - this.lastupdate;
        this.lastupdate = System.currentTimeMillis()/1000;
        return this.playtime;
    }

    public long getNwordcount(){
        return this.nwordcount;
    }

    public String getName() {
        return this.name;
    }

    public DailyQuest getDaily() {
        return Veira.session.getActiveQuest(this.player.getUniqueId());
    }


    public void setDaily(int i) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Veira.session.addActiveQuest(this.player.getUniqueId(), Veira.session.getQuest(i).setPlayer(this.player));
        this.dailyDate = LocalDateTime.now();
    }

    public void completedQuest() throws IOException {
        this.getDaily().completeQuest();
        this.getDaily().giveOutReward(this);
        this.dailyAmount++;
        Veira.session.removeActiveQuest(this.player.getUniqueId());
        this.player.playSound(this.player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 10, 29);
        this.player.sendMessage("Du hast deine tägliche Herrausforderung abgeschlossen!");
    }

    public long getDiamonds() {
        return this.diamonds;
    }

    public LocalDateTime getDailyDate() {
        return this.dailyDate;
    }

    public void savePlayer() throws IOException {
        if (this.player != null) {
            Path playerPath = Paths.get(Veira.pluginInstance.veiraPath + "\\" + this.player.getUniqueId() + ".json");
            JsonObject v = this.getPlayerAsJson();
            Files.write(playerPath, Collections.singleton(v.toString()));
        }
    }

    public void setSlotsGame(SlotsGame s) {
        this.slotsGame = s;
    }

    public SlotsGame getSlotsGame(){
        return this.slotsGame;
    }

    public long getKablcoins(){
        return this.kablcoins;
    }

    public void addKablcoins(long amount){
        if (amount > 0){
            this.kablcoins += amount;
            this.player.sendMessage(Component.text("+" + amount +" Kablcoins").color(NamedTextColor.GREEN));
        }
    }

    public void removeKablcoins(long amount){
        if (amount > 0){
            this.kablcoins -= amount;
            this.player.sendMessage(Component.text("-" + amount +" Kablcoins").color(NamedTextColor.RED));
        }
    }

    public void setDiamonds(long l) {
        this.diamonds = l;
    }

    public long getDailyAmount() {
        return this.dailyAmount;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void addDiamonds(long amount) throws IOException {
        this.diamonds += amount;
        player.sendMessage(Component.text("+" + amount +" Diamanten").color(NamedTextColor.GREEN));
        this.savePlayer();
    }

    public void removeDiamonds(long amount) throws IOException {
        this.diamonds -= amount;
        this.player.sendMessage(Component.text("-" + amount + " Diamanten").color(NamedTextColor.RED));
        this.savePlayer();
    }

    public long getBlocksMined() {
        return this.blocksMined;
    }

    public long getBlocksPlaced() {
        return this.blocksPlaced;
    }

    public long getMonstersKilled() {
        return this.monstersKilled;
    }

    public long getAnimalsKilled() {
        return this.animalsKilled;
    }

    public void setBlocksMined(long amount) {
        this.blocksMined = amount;
    }

    public void setBlocksPlaced(long amount) {
        this.blocksPlaced = amount;
    }

    public void setMonstersKilled(long amount) {
        this.monstersKilled = amount;
    }

    public void setAnimalsKilled(long amount) {
        this.animalsKilled = amount;
    }

    public void incrementMonstersKilled() {
        this.monstersKilled++;
    }

    public void incrementAnimalsKilled() {
        this.animalsKilled++;
    }

    public void incrementBlocksMined() {
        this.blocksMined++;
    }

    public void incrementBlocksPlaced() {
        this.blocksPlaced++;
    }

    public void schizoToggle() {
        this.schizo = !this.schizo;
    }

    public boolean isSchizo() {
        return this.schizo;
    }

    public void sendingMessage() {
        this.sendingMessage = true;
    }
    public void noLongerSendingMessage() {
        this.sendingMessage = false;
    }

    public boolean isSendingMessage() {
        return this.sendingMessage;
    }

    public void resetDaily() {
        this.dailyDate = LocalDateTime.MIN;
    }

    public long getLoginStreak() {
        return this.loginStreak;
    }

    public int getLastLoginStreakRewardClaimed() {
        return (int)this.lastLoginStreakRewardClaimed;
    }

    public void giveLoginReward() {
        this.lastLoginStreakRewardClaimed = this.loginStreak;
        int amount;
        Inventory inv = this.player.getInventory();

        switch ((int)this.loginStreak) {
            case 1:
                this.loginRewardHelper(16, Material.COOKED_BEEF);
                break;
            case 2:
                this.loginRewardHelper(20, Material.IRON_ORE);
                this.loginRewardHelper(20, Material.COAL);
                break;
            case 3:
                this.loginRewardHelper(32, Material.EXPERIENCE_BOTTLE);
                this.loginRewardHelper(8, Material.TNT);
                break;
            case 4:
                this.loginRewardHelper(20, Material.EMERALD);
                break;
            case 5:
                ItemStack itemBook1 = new ItemStack(Material.ENCHANTED_BOOK);
                itemBook1.addEnchantment(Enchantment.DURABILITY, 3);

                for(int i = 0; i < 36; i++){
                    if(inv.getItem(i) == null){
                        inv.setItem(i, itemBook1);
                        return;
                    }
                }
                this.player.getWorld().dropItem(this.player.getLocation(), itemBook1);
                break;
            case 6:
                this.loginRewardHelper(64, Material.OAK_LOG);
                this.loginRewardHelper(64, Material.SPRUCE_LOG);
                this.loginRewardHelper(64, Material.BIRCH_LOG);
                this.loginRewardHelper(64, Material.DARK_OAK_LOG);
                break;
            case 7:
                this.loginRewardHelper(32, Material.OBSIDIAN);
                this.loginRewardHelper(1, Material.WITHER_SKELETON_SKULL);
                break;
            case 8:
                this.loginRewardHelper(2, Material.VILLAGER_SPAWN_EGG);
                break;
            case 9:
                this.loginRewardHelper(1, Material.PARROT_SPAWN_EGG);
                this.loginRewardHelper(1, Material.WOLF_SPAWN_EGG);
                this.loginRewardHelper(1, Material.CAT_SPAWN_EGG);
                this.loginRewardHelper(1, Material.AXOLOTL_SPAWN_EGG);
                break;
            case 10:
                this.loginRewardHelper(1, Material.END_PORTAL_FRAME);
                break;
            case 11:
                this.loginRewardHelper(1, Material.DIAMOND_AXE);
                this.loginRewardHelper(1, Material.ANVIL);
                this.loginRewardHelper(32, Material.EXPERIENCE_BOTTLE);
                ItemStack itemBook2 = new ItemStack(Material.ENCHANTED_BOOK);
                itemBook2.addEnchantment(Enchantment.DAMAGE_ALL, 5);

                for(int i = 0; i < 36; i++){
                    if(inv.getItem(i) == null){
                        inv.setItem(i, itemBook2);
                        return;
                    }
                }
                this.player.getWorld().dropItem(this.player.getLocation(), itemBook2);

                break;
            case 12:
                this.loginRewardHelper(3, Material.NETHERITE_INGOT);
                this.loginRewardHelper(1, Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
                break;
            case 13:
                ItemStack itemSword1 = new ItemStack(Material.WOODEN_SWORD);
                ItemMeta meta = itemSword1.getItemMeta();
                meta.displayName(Component.text("?XD"));
                itemSword1.setItemMeta(meta);
                itemSword1.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);

                for(int i = 0; i < 36; i++){
                    if(inv.getItem(i) == null){
                        inv.setItem(i, itemSword1);
                        return;
                    }
                }
                this.player.getWorld().dropItem(this.player.getLocation(), itemSword1);
                break;
            case 14:
                this.loginRewardHelper(1, Material.BEDROCK);
                break;
        }
    }

    private void loginRewardHelper(int amount, Material material) {
        if (checkForSlots(amount, material)) {
            fillSlots(amount, material);
        } else {
            ItemStack item = new ItemStack(material);
            item.setAmount(amount);
            this.player.getWorld().dropItem(this.player.getLocation(), item);
        }
    }

    private boolean checkForSlots(int amount, Material material) {
        int counter = 0;
        Inventory inv = this.player.getInventory();
        for(int i = 0; i < 36; i++){
            if(inv.getItem(i) == null){
                counter += 64;
            } else if(inv.getItem(i).getType() == material){
                counter += 64 - inv.getItem(i).getAmount();
            }

            if (counter > amount){
                //Sicher genug Platz
                return true;
            }
        }

        if (counter < amount){
            return false;
        }

        return true;
    }

    private void fillSlots(int amount, Material material) {
        Inventory inv = this.player.getInventory();

        for(int i = 0; i < 36; i++){
            if(inv.getItem(i) == null){
                if (amount >= 64){
                    inv.setItem(i, new ItemStack(material, 64));
                    amount -= 64;
                } else {
                    inv.setItem(i, new ItemStack(material, amount));
                    return;
                }
            } else if(inv.getItem(i).getType() == material){
                if (amount + inv.getItem(i).getAmount() >= 64){
                    inv.setItem(i, new ItemStack(material, 64));
                    amount -= (64 - inv.getItem(i).getAmount());
                } else {
                    inv.setItem(i, new ItemStack(material, amount + inv.getItem(i).getAmount()));
                    return;
                }
            }
        }
    }

    public void setLootBoxes(long a) {
        this.lootBoxes = a;
    }

    public long getLootBoxes() {
        return this.lootBoxes;
    }
}
