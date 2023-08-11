package kabl.veira.core;

import com.google.gson.JsonObject;
import kabl.veira.Daily.DailyQuest;
import kabl.veira.Gamble.SlotsGame;
import kabl.veira.Veira;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
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
import java.util.Objects;

public class VeiraPlayer {

    private String name;
    private long playtime;
    private long lastupdate;
    private long diamonds;
    private long nwordcount;
    private long kablcoins;

    private LocalDateTime dailyDate;


    private Player player;
    private SlotsGame slotsGame;

    public VeiraPlayer(Player p) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.player = p;
        //New Player
        this.playtime = 0;
        this.lastupdate = System.currentTimeMillis()/1000;
        this.nwordcount = 0;
        this.name = p.getName();
        this.diamonds = 0;
        this.kablcoins = 0;

        //Daily
        this.dailyDate = null;
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

        //Daily
        if(j.has("dailyDate")){
            if(Objects.equals(j.get("dailyDate").getAsString(), "null") || Objects.equals(j.get("dailyDate").getAsString(), "")){
                DailyQuest.givePlayerNewQuest(this);
            } else {
                LocalDateTime fromFile = LocalDateTime.parse(j.get("dailyDate").getAsString());
                if (fromFile.getDayOfYear() < LocalDateTime.now().getDayOfYear()){
                    //New Daily
                    DailyQuest.givePlayerNewQuest(this);
                }
            }
        } else {
            DailyQuest.givePlayerNewQuest(this);
        }
    }

    public VeiraPlayer(JsonObject j){
        //Offline Player
        this.name = j.has("name") ? j.get("name").getAsString() : "unknown";
        this.playtime = j.has("playtime") ? j.get("playtime").getAsLong() : 0;
        this.lastupdate = System.currentTimeMillis()/1000;
        this.nwordcount = j.has("nwordcount") ? j.get("nwordcount").getAsLong() : 0;
        this.diamonds = j.has("diamonds") ? j.get("diamonds").getAsLong() : 0;
        this.kablcoins = j.has("kablcoins") ? j.get("kablcoins").getAsLong() : 0;
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

        return result;
    }

    public boolean depositDiamond(long amount) throws IOException {
        this.player.sendMessage("Trying to deposit: " + amount);
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
            player.sendMessage(counter+" Diamanten eingezahlt");
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

    public void completedQuest() {
        Veira.session.removeActiveQuest(this.player.getUniqueId());
        this.player.playSound(this.player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 10, 29);
        this.getDaily().completeQuest();
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
}
