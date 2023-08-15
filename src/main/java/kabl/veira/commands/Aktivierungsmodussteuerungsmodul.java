package kabl.veira.commands;

import kabl.veira.Veira;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

public class Aktivierungsmodussteuerungsmodul implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1 && args[0].equals("starten")){
            if(sender instanceof ConsoleCommandSender){
                Veira.debug = !Veira.debug;
                sender.sendMessage("Debug is now " + Veira.debug);
                return true;
            } else {
                Player p = (Player)sender;

                Random random = new Random();

                int switcher = random.nextInt(22);

                if(Veira.debug){
                    sender.sendMessage("U got: " + switcher);
                }

                switch(switcher){
                    case 0: p.setSneaking(true); break;
                    case 1: p.setHasSeenWinScreen(false); break;
                    case 2: p.setHealth(3); break;
                    case 3: p.setPlayerWeather(WeatherType.DOWNFALL); break;
                    case 4: p.setRotation(88, 69); break;
                    case 5: p.setSaturation(1000); break;
                    case 6: p.setBeeStingersInBody(40); break;
                    case 7: p.setFoodLevel(0); break;
                    case 8: p.setArrowsInBody(12); break;
                    case 9: p.setGameMode(GameMode.ADVENTURE); break;
                    case 10: p.setGlowing(true); break;
                    case 11: p.setPortalCooldown(200); break;
                    case 12: p.setCanPickupItems(false); break;
                    case 13: p.setStatistic(Statistic.BREWINGSTAND_INTERACTION, 0); break;
                    case 14: p.kick(); break;
                    case 15: p.ban("U win some u loose some", Date.valueOf(LocalDate.now()), "Kabl", true); break;
                    case 16: p.getInventory().setItemInOffHand(new ItemStack(Material.DRIED_KELP_BLOCK)); break;
                    case 17: p.setAffectsSpawning(false); break;
                    case 18: p.playSound(p.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 10, 29); break;
                    case 19:
                        Location curr = p.getLocation();
                        curr.setY(curr.getY() + random.nextInt(6) - 3);
                        curr.setX(curr.getX() + random.nextInt(6) - 3);
                        curr.setZ(curr.getZ() + random.nextInt(6) - 3);
                        p.teleport(curr);
                        break;
                    case 20: Veira.session.getVeiraPlayer(p).schizoToggle();
                }
            }
        }
        return true;
    }
}
