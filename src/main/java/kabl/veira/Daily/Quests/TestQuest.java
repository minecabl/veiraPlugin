package kabl.veira.Daily.Quests;

import kabl.veira.Daily.DailyQuest;
import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

public class TestQuest extends DailyQuest {
    public TestQuest(){
        this.id = 0;
        this.questTitle = "Test-Quest";
        this.questDescription = "Die ist eine Test-Quest, wenn du diese erhalten hast... Opfer";
        this.reward = "Keiner, xd";

        this.dailyNotification = false;
        this.dailyDate = LocalDateTime.now();
        this.dailyComplete = false;
    }

    @Override
    public boolean checkComplete() {
        if (this.player == null) {
            Veira.log("Tried to complete Quest of null-Player");
            return false;
        }

        return this.player.getInventory().contains(Material.COMMAND_BLOCK);
    }

    @Override
    public boolean completeQuest() {
        this.dailyComplete = true;
        return false;
    }

    @Override
    public String getRequirements() {
        return "Schreibe \"test\" in den Chat";
    }

    @Override
    public void reroll() {
        player.sendMessage("xd");
    }

    @Override
    public void giveOutReward(VeiraPlayer veiraPlayer) {

    }
}
