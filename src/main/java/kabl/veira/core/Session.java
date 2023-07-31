package kabl.veira.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import kabl.veira.Daily.DailyQuest;
import kabl.veira.Daily.Quests.TestQuest;
import kabl.veira.Veira;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Session {
    private HashMap<UUID, VeiraPlayer> session;
    private List<DailyQuest> dailyQuests;

    public Session() throws IOException {
        session = new HashMap<UUID, VeiraPlayer>();
        Gson gson = new Gson();

        File[] playerFiles = new File(Veira.pluginInstance.veiraPath).listFiles();

        if (playerFiles != null && playerFiles.length != 0){
            for (File playerFile : playerFiles) {
                JsonObject result = gson.fromJson(Files.readString(playerFile.toPath()), JsonObject.class);
                Veira.log(playerFile.getName());
                session.put(UUID.fromString(playerFile.getName().substring(0, playerFile.getName().length()-5)), new VeiraPlayer(result));
            }
        }

        dailyQuests = new LinkedList<DailyQuest>();
        dailyQuests.add(null);
        dailyQuests.add(new TestQuest());
    }

    public void addPlayer(Player p, VeiraPlayer v){
        if(this.session.get(p.getUniqueId()) != null){
            this.session.replace(p.getUniqueId(), v);
        } else {
            this.session.put(p.getUniqueId(), v);
        }
        Veira.log("Spieler " + p.getName() + " wurde der Session hinzugefügt!");
    }

    public VeiraPlayer getVeiraPlayer(Player p){
        if(session.get(p.getUniqueId()) != null){
            return session.get(p.getUniqueId());
        } else {
            Veira.log("Unbekannter Spieler in Session gesucht: "+p.getName()+" | "+p.getUniqueId());
            p.sendMessage("Ein Fehler ist aufgetreten, bitte melde dies Kabl und verbinde dich neu zum Server :^)");
            return null;
        }
    }

    public List<VeiraPlayer> getTopList(Comparator<VeiraPlayer> comparator) {
        List<VeiraPlayer> playerList = new LinkedList<VeiraPlayer>();

        for(Map.Entry<UUID, VeiraPlayer> playerEntry: session.entrySet()){
            playerList.add(playerEntry.getValue());
        }

        playerList.sort(comparator);
        Collections.reverse(playerList);
        return playerList;
    }

    public DailyQuest quest(int i) {
        return this.dailyQuests.get(i);
    }

    public void cancelAllQuests(ScheduledTask scheduledTask) {
        Veira.pluginInstance.getServer().broadcast(Component.text("Minute"));
    }
}
