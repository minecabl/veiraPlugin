package kabl.veira.Daily;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;

public abstract class DailyQuest {

    public int id;
    public String questTitle;
    public String questDescription;

    public static void givePlayerNewQuest(VeiraPlayer veiraPlayer) {
        veiraPlayer.setDaily(1);
        Veira.log("tried to give new Quest");
    }

    public static DailyQuest getQuestById(int i) {
        return Veira.session.quest(i);
    }

    public void acceptQuest(VeiraPlayer p){
        p.setDaily(id);
    }

    public void cancelQuest(VeiraPlayer p){
        p.setDaily(0);
    }

    public void completeQuest(VeiraPlayer p){
        p.completedQuest();
    }

    public abstract int getId();

    public abstract String getTitle();

    public abstract String getDescription();
}
