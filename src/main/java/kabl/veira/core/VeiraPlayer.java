package kabl.veira.core;

import com.google.gson.JsonObject;
import kabl.veira.Daily.DailyQuest;
import kabl.veira.Veira;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class VeiraPlayer {

    private String name;
    private long playtime;
    private long lastupdate;
    private long kills;
    private long deaths;
    private float kd;
    private long diamonds;
    private long nwordcount;

    private DailyQuest daily;
    private boolean dailyNotification;
    private LocalDateTime dailyDate;
    private boolean dailyComplete;

    private Player player;

    public VeiraPlayer(Player p){
        this.player = p;
        //New Player
        this.playtime = 0;
        this.lastupdate = System.currentTimeMillis()/1000;
        this.nwordcount = 0;
        this.name = p.getName();

        //Daily
        this.daily = null;
        this.dailyNotification = false;
        DailyQuest.givePlayerNewQuest(this);
    }

    public VeiraPlayer(Player p, JsonObject j){
        this.player = p;
        //Login Player
        this.name = p.getName();
        this.playtime = j.has("playtime") ? j.get("playtime").getAsLong() : 0;
        this.lastupdate = System.currentTimeMillis()/1000;
        this.nwordcount = j.has("nwordcount") ? j.get("nwordcount").getAsLong() : 0;

        //Daily
        if(j.has("dailyDate")){
            LocalDateTime fromFile = LocalDateTime.parse(j.get("dailyDate").getAsString());
            if (fromFile.getDayOfYear() < LocalDateTime.now().getDayOfYear()){
                //New Daily
                this.daily = null;
                this.dailyNotification = false;
                DailyQuest.givePlayerNewQuest(this);
            } else {
                Veira.log("Test: " + j.get("dailyID").getAsInt());
                this.daily = DailyQuest.getQuestById(j.has("dailyID") ? j.get("dailyID").getAsInt() : 0);
                this.dailyDate = fromFile;
                this.dailyNotification = true;
                this.dailyComplete = j.has("dailyComplete") && j.get("dailyComplete").getAsBoolean();
            }
        } else {
            this.daily = null;
            this.dailyNotification = false;
            DailyQuest.givePlayerNewQuest(this);
        }
    }

    public VeiraPlayer(JsonObject j){
        //Offline Player
        this.name = j.has("name") ? j.get("name").getAsString() : "unknown";
        this.playtime = j.has("playtime") ? j.get("playtime").getAsLong() : 0;
        this.lastupdate = System.currentTimeMillis()/1000;
        this.nwordcount = j.has("nwordcount") ? j.get("nwordcount").getAsLong() : 0;
    }

    public JsonObject getPlayerAsJson(){
        JsonObject result = new JsonObject();

        result.addProperty("name", this.name);
        result.addProperty("playtime", this.playtime + System.currentTimeMillis()/1000 - this.lastupdate);
        result.addProperty("nwordcount", this.nwordcount);
        result.addProperty("dailyID", this.daily.getId());
        result.addProperty("dailyNotification", this.dailyNotification);
        result.addProperty("dailyDate", String.valueOf(this.dailyDate));

        return result;
    }

    public boolean depositDiamond(Player p, long amount){
        if(amount <= 0){
            p.sendMessage("Surely :)");
            return false;
        }

        if(p.getInventory().contains(Material.DIAMOND)){
            //TODO
        }

        return false;
    }

    public boolean withdrawDiamond(Player p, long amount){
        if(amount <= 0){
            p.sendMessage("Surely :)");
            return false;
        }



        return false;
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
        return this.daily;
    }

    public boolean getDailyNotification(){
        return dailyNotification;
    }

    public void gotDailyNotification(){
        this.dailyNotification = true;
    }

    public void setDaily(int i) {
        this.daily = Veira.session.quest(i);
        this.dailyDate = LocalDateTime.now();
        this.dailyComplete = false;
    }

    public boolean getDailyStatus(){
        return dailyComplete;
    }

    public void completedQuest() {
        this.dailyComplete = true;
        this.player.playSound(this.player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 10, 29);

    }
}
