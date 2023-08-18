package kabl.veira.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class Csgo implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Random random = new Random();

        ArrayList<String> csgoVideoLinks = new ArrayList<>();
        csgoVideoLinks.add("https://youtu.be/arIhE9CsiJM");
        csgoVideoLinks.add("https://youtu.be/XDqvLRbd0jc");
        csgoVideoLinks.add("https://youtu.be/UbsV1yXyBjY");

        sender.sendMessage(csgoVideoLinks.get(random.nextInt(csgoVideoLinks.size())));

        return true;
    }
}
