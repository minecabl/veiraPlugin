package kabl.veira.Daily.Quests;

import kabl.veira.Daily.DailyQuest;
import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Random;

public class TextBasedQuest extends DailyQuest {

    public TextBasedQuest() {
        this.id = 2;
        Random random = new Random();

        int roller = random.nextInt(10);

        switch (roller) {
            case 1:
                this.questTitle = "Meisterbauer - 1";
                this.questDescription = "Errichte ein Monument für einen anderen Spieler!";
                break;
            case 2:
                this.questTitle = "Meisterbauer - 2";
                this.questDescription = "Baue eine Struktur deiner Wahl am Spawn";
                break;
            case 3:
                this.questTitle = "Komm 1v1 - 1";
                this.questDescription = "Mach einen Faustkampf gegen einen Spieler deiner Wahl";
                break;
            case 4:
                this.questTitle = "Komm 1v1 - 2";
                this.questDescription = "Mache ein 1v1 in CSGO gegen eine Person deiner Wahl";
                break;
            case 5:
                this.questTitle = "Komm 1v1 - 3";
                this.questDescription = "Mache ein 1v1 in LoL gegen eine Person deiner Wahl";
                break;
            case 6:
                this.questTitle = "Netter Typ";
                this.questDescription = "Mache einem Mitspieler eine Freude";
                break;
            case 7:
                this.questTitle = "Falle - 1";
                this.questDescription = "Baue eine Falle in die Basis eines Mitspielers";
                break;
            case 8:
                this.questTitle = "Falle - 2";
                this.questDescription = "Baue eine Falle am Spawn";
                break;
            case 9:
                this.questTitle = "Epischer Troll";
                this.questDescription = "Trolle einen Mitspieler (Sei kreativ, aber nicht zu destruktiv)";
                break;
            case 10:
                this.questTitle = "Skinwalker";
                this.questDescription = "Ändere deinen Skin für einen Tag zu dem eines Mitspielers";
                break;

            default:
                this.questTitle = "Something went wrong xd";
                this.questDescription = "@Kabl @Kabl @Kabl @Kabl";
        }

        this.reward = "5 Diamanten";
        this.dailyNotification = false;
        this.dailyDate = LocalDateTime.now();
        this.dailyComplete = false;
    }

    @Override
    public boolean checkComplete() {
        return true;
    }

    @Override
    public boolean completeQuest() {
        return true;
    }

    @Override
    public String getRequirements() {
        return "Nach eigenem ermessen, mit /daily complete beenden";
    }

    @Override
    public void reroll() {

    }

    @Override
    public void giveOutReward(VeiraPlayer veiraPlayer) throws IOException {
        veiraPlayer.addDiamonds(5);
    }
}
