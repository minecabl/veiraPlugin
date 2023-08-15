package kabl.veira.Daily.Quests;

import kabl.veira.Daily.DailyQuest;
import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class KillPlayerQuest extends DailyQuest {
    String target;

    public KillPlayerQuest() {
        this.id = 1;
        this.questTitle = "Auftragsmord";

        Random random = new Random();
        Object[] values = Veira.session.getFullMap().values().toArray();
        this.target = ((VeiraPlayer)values[random.nextInt((values.length))]).getName();

        this.questDescription = "Töte Spieler " + target;
        this.reward = "10 Diamanten";
        this.dailyNotification = false;
        this.dailyDate = LocalDateTime.now();
        this.dailyComplete = false;
    }

    @Override
    public boolean checkComplete() {
        return this.dailyComplete;
    }

    @Override
    public boolean completeQuest() {
        this.dailyComplete = true;
        return true;
    }

    @Override
    public Component getRequirements() {
        return Component.text("Töte " + this.target);
    }

    @Override
    public void reroll() {
        if (!this.dailyComplete) {
            Random random = new Random();
            Object[] values = Veira.session.getFullMap().values().toArray();
            this.target = ((VeiraPlayer)values[random.nextInt((values.length))]).getName();

            this.questDescription = "Töte Spieler " + target;
        }
    }

    @Override
    public void giveOutReward(VeiraPlayer veiraPlayer) throws IOException {
        veiraPlayer.addDiamonds(10);
    }
}
