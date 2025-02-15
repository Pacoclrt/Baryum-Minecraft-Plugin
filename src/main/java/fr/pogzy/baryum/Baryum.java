package fr.pogzy.baryum;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;

import java.util.HashMap;
import java.util.UUID;

public class Baryum extends JavaPlugin {

    private final HashMap<UUID, Location> lastLocations = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Baryum plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Baryum plugin disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("nether")) {
                if (player.hasPermission("baryum.nether")) {
                    storeLastLocation(player);
                    teleportToDimension(player, World.Environment.NETHER);
                    return true;
                } else {
                    player.sendMessage("Tu n'as pas la permission d'exécuter cette commande.");
                    return true;
                }
            } else if (command.getName().equalsIgnoreCase("end")) {
                if (player.hasPermission("baryum.end")) {
                    storeLastLocation(player);
                    teleportToDimension(player, World.Environment.THE_END);
                    return true;
                } else {
                    player.sendMessage("Tu n'as pas la permission d'exécuter cette commande.");
                    return true;
                }
            } else if (command.getName().equalsIgnoreCase("overworld")) {
                returnLastLocation(player);
                return true;
            }
        }
        return false;
    }

    private void storeLastLocation(Player player) {
        lastLocations.put(player.getUniqueId(), player.getLocation());
    }

    private void returnLastLocation(Player player) {
        Location lastLocation = lastLocations.get(player.getUniqueId());
        if (lastLocation != null) {
            player.teleport(lastLocation);
            player.sendMessage("Tu as été téléporté à ton dernier emplacement dans l'Overworld !");
        } else {
            player.sendMessage("Aucun emplacement précédent trouvé dans l'Overworld.");
        }
    }

    private void teleportToDimension(Player player, World.Environment environment) {
        World world = getServer().getWorlds().stream()
                .filter(w -> w.getEnvironment() == environment)
                .findFirst()
                .orElse(null);

        if (world != null) {
            player.teleport(world.getSpawnLocation());
            player.sendMessage("Tu as été téléporté dans le " + environment.name().toLowerCase() + " !");
        } else {
            player.sendMessage("La dimension " + environment.name().toLowerCase() + " n'est pas disponible.");
        }
    }
}
