package krepo.seshcoders.hardcraft.mobs;

import krepo.seshcoders.hardcraft.utils.Randomize;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HuntMode extends BukkitRunnable {

    public static final int SPAWN_LIMIT = 60;

    public interface OnHuntEnabledListener{
        void huntModeStatusChanged(boolean huntModeOn);
    }

    private OnHuntEnabledListener huntEnabledListener;
    private JavaPlugin hardCraft;
    private World world;

    public HuntMode(JavaPlugin hardCraft, OnHuntEnabledListener huntEnabledListener) {
        this.hardCraft = hardCraft;
        this.huntEnabledListener = huntEnabledListener;
        this.world = hardCraft.getServer().getWorld("World");
    }

    @Override
    public void run() {
        long time = world.getTime();

        //if day begin disable huntMode
        if(time>=0 && time <= 10){
            huntEnabledListener.huntModeStatusChanged(false);
            world.setMonsterSpawnLimit(SPAWN_LIMIT);
        }
        //if day, then speeeed it up
        else if (time >= 0 && time <= 12000) {
            world.setTime(time + 3);
        }
        //if night begin, then check if should run huntMode
        else if (time>13800 && time <= 13810){
            if (Randomize.checkChance(10)){
                //if true, run HUNT MODE!!
                for(int i=0; i<3; i++){
                    hardCraft.getServer().broadcastMessage(ChatColor.RED +"HORDA WŁĄCZONA POTWORY SĄ SILNIEJSZE!!");
                }

                for (Player player : world.getPlayers()) {
                    if (player.getLevel()>=10 && player.getHealth()>=15 && player.getLocation().getY()>50){

                        world.spawnEntity(player.getLocation().add(0,2,5), EntityType.ZOMBIE);
                        world.spawnEntity(player.getLocation().add(0,2,-5), EntityType.ZOMBIE);

                        world.spawnEntity(player.getLocation().add(5,2,0), EntityType.ZOMBIE);
                        world.spawnEntity(player.getLocation().add(-5,2,0), EntityType.ZOMBIE);

                        world.spawnEntity(player.getLocation().add(5,2,5), EntityType.ZOMBIE);
                        world.spawnEntity(player.getLocation().add(-5,2,5), EntityType.ZOMBIE);

                        world.spawnEntity(player.getLocation().add(-5,2,5), EntityType.ZOMBIE);
                        world.spawnEntity(player.getLocation().add(+5,2,-5), EntityType.ZOMBIE);
                        hardCraft.getServer().getPlayer(player.getUniqueId()).sendMessage(ChatColor.GOLD +"MOB ZESPAWNOWAL SIE OBOK CIEBIE");

                    }
                }
                world.setMonsterSpawnLimit(SPAWN_LIMIT);
                huntEnabledListener.huntModeStatusChanged(true);
                world.setMonsterSpawnLimit((int)(world.getMonsterSpawnLimit()*1.2));
            }
        }
    }

}
