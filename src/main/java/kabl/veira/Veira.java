package kabl.veira;

import kabl.veira.Daily.DailyCommand;
import kabl.veira.Gamble.GambleCommand;
import kabl.veira.commands.*;
import kabl.veira.core.Session;
import kabl.veira.Gamble.GambleClickEvent;
import kabl.veira.listeners.PlayerChat;
import kabl.veira.listeners.PlayerJoin;
import kabl.veira.listeners.PlayerLeave;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public final class Veira extends JavaPlugin {

    public static Veira pluginInstance;
    public static Session session;
    public String veiraPath = System.getProperty("user.dir") + "\\plugins\\Veira";
    public static boolean debug = true;

    @Override
    public void onEnable() {
        // Plugin startup logic
        pluginInstance = this;
        try {
            session = new Session();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            getServer().shutdown();
        }

        if(!Files.exists(Paths.get(veiraPath))){
            try {
                Files.createDirectory(Paths.get(veiraPath));
            } catch (IOException e) {
                log("Konnte Directory nicht erstellen :(");
                log(Arrays.toString(e.getStackTrace()));
                getServer().shutdown();
            }
        }

        this.register();

        getServer().getAsyncScheduler().runAtFixedRate(this, session::cancelAllQuests, minutesTillMidnight(), 1440, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void log(String msg){
        Bukkit.getConsoleSender().sendMessage(msg);
    }

    private void register(){
        PluginManager PM = getServer().getPluginManager();

        PM.registerEvents(new PlayerJoin(), this);
        PM.registerEvents(new PlayerLeave(), this);
        PM.registerEvents(new PlayerChat(), this);
        PM.registerEvents(new GambleClickEvent(), this);

        getCommand("nword").setExecutor(new Nword());
        getCommand("playtime").setExecutor(new Playtime());
        getCommand("daily").setExecutor(new DailyCommand());
        getCommand("aktivierungsmodussteuerungsmodul").setExecutor(new Aktivierungsmodussteuerungsmodul());
        getCommand("info").setExecutor(new Info());

        getCommand("deposit").setExecutor(new Deposit());
        getCommand("withdraw").setExecutor(new Withdraw());

        getCommand("gamble").setExecutor(new GambleCommand());
    }

    private long minutesTillMidnight(){
        LocalDateTime now = LocalDateTime.now();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDateTime nextMidnight = now.with(midnight);
        if (now.toLocalTime().isAfter(midnight)) {
            nextMidnight = nextMidnight.plusDays(1);
        }
        Duration duration = Duration.between(now, nextMidnight);
        Veira.log("Es sind noch " + duration.toMinutes() + " bis Mitternacht");
        return duration.toMinutes();
    }
}
