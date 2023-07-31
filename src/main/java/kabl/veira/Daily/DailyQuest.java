package kabl.veira.Daily;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;

public class DailyQuest {
    public static void givePlayerNewQuest(VeiraPlayer veiraPlayer) {
        veiraPlayer.setDaily(1);
        Veira.log("tried to give new Quest");

    }
}
