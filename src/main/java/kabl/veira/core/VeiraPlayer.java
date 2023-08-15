package kabl.veira.core;

import com.google.gson.JsonObject;
import kabl.veira.Daily.DailyQuest;
import kabl.veira.Gamble.SlotsGame;
import kabl.veira.Veira;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    private long blocksMined;
    private long blocksPlaced;
    private long monstersKilled;
    private long animalsKilled;

    private boolean schizo;

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

        this.blocksMined = j.has("blocksMined") ? j.get("blocksMined").getAsLong() : 0;
        this.blocksPlaced = j.has("blocksPlaced") ? j.get("blocksPlaced").getAsLong() : 0;
        this.monstersKilled = j.has("monstersKilled") ? j.get("monstersKilled").getAsLong() : 0;
        this.animalsKilled = j.has("animalsKilled") ? j.get("animalsKilled").getAsLong() : 0;

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

        int counter = 0;
        for(int i = 0; i < 36; i++){
            if(inv.getItem(i) == null){
                counter += 64;
            } else if(inv.getItem(i).getType() == Material.DIAMOND){
                counter += 64 - inv.getItem(i).getAmount();
            }

            if (counter > amount){
                //Sicher genug Platz
                break;
            }
        }

        if (counter < amount){
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
        Path playerPath = Paths.get(Veira.pluginInstance.veiraPath + "\\" + this.player.getUniqueId() + ".json");
        JsonObject v = this.getPlayerAsJson();
        Files.write(playerPath, Collections.singleton(v.toString()));
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
        }
    }

    public void removeKablcoins(long amount){
        if (amount > 0){
            this.kablcoins -= amount;
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
}
