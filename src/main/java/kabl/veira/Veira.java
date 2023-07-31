package kabl.veira;

import kabl.veira.commands.Nword;
import kabl.veira.commands.Playtime;
import kabl.veira.core.Session;
import kabl.veira.listeners.PlayerChat;
import kabl.veira.listeners.PlayerJoin;
import kabl.veira.listeners.PlayerLeave;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.N;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
        } catch (IOException e) {
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

        getCommand("nword").setExecutor(new Nword());
        getCommand("playtime").setExecutor(new Playtime());
    }
}
