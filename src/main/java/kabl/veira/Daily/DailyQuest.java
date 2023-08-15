package kabl.veira.Daily;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Random;

public abstract class DailyQuest {

    public int id;
    public String questTitle;
    public String questDescription;

    public boolean dailyNotification;
    public LocalDateTime dailyDate;
    public boolean dailyComplete;

    public Player player;

    public String reward;

    public static void givePlayerNewQuest(VeiraPlayer veiraPlayer) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Random random = new Random();
        veiraPlayer.setDaily(random.nextInt(Veira.session.getQuestBound()));
        Veira.log("tried to give new Quest");
    }

    public static DailyQuest getQuestById(int i) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return Veira.session.getQuest(i);
    }


    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.questTitle;
    }

    public String getDescription() {
        return this.questDescription;
    }

    public String getReward() {return this.reward;}

    public boolean getDailyNotification(){
        return this.dailyNotification;
    }

    public boolean getComplete(){
        return this.dailyComplete;
    }

    public LocalDateTime getDailyDate(){
        return this.dailyDate;
    }

    public abstract boolean checkComplete();

    public abstract boolean completeQuest();
    public abstract Component getRequirements();

    public DailyQuest setPlayer(Player p){
        this.player = p;
        return this;
    }

    public abstract void reroll();

    public abstract void giveOutReward(VeiraPlayer veiraPlayer) throws IOException;
}
