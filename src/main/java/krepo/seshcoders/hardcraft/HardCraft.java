package krepo.seshcoders.hardcraft;

import krepo.seshcoders.hardcraft.armor.CorruptedSet;
import krepo.seshcoders.hardcraft.mobs.HuntMode;
import krepo.seshcoders.hardcraft.mobs.MobsStats;
import krepo.seshcoders.hardcraft.players.PlayersStats;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HardCraft extends JavaPlugin implements HuntMode.OnHuntEnabledListener {

    private MobsStats mobsStats;

    @Override
    public void onEnable() {
        mobsStats = new MobsStats(this);
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(mobsStats, this);
        getServer().getPluginManager().registerEvents(new CorruptedSet(), this);
        getServer().getPluginManager().registerEvents(new PlayersStats(), this);

        // TODO: 16.11.2019 usunac bloki siana i arbuzow z wiosek
        // TODO: 16.11.2019 skonfigurowac plugin od Unbreaking III, Fortune, Mending i Sharpness V
        // TODO: 16.11.2019 moby chase distance wiekszy jak horda inaczej mniejszy bo troche za duzy
        // TODO: 17.11.2019 dodac zeby zombiaki widizaly przez sciany
        /**
         * Lista zmian:
         *
         * wszystkie moby wiekszy damage i predkosc ruchu, creeper wiekszy wybuch
         * (troszke) mniej zwierzat na swiecie,(duzo) wiecej mobow
         * zombie niszczą ściany, mogą wchodzić do domków
         * hunt mode(HORDA), czyli 10% szans na to, że w nocy stanie sie podkrecenie punktu pierwszego i drugiego o 20%
         * poziom jedzenia szybciej ucieka
         * mniejszy food level z jedzenia (jedzenie mniej daje)
         * wiekszy czas rośnięcia roślin
         * mniejszy drop ziemniakow i rotten flesha
         * itemy przy craftowaniu sa niszczone w 25%
         * wszystkie itemy maja trwałość zmniejszoną o 50%
         * usunięte ulepszenia: Unbreaking III, Fortune, Mending, Sharpness V
         * wiekszy damage od trucizn, upadku, ognia
         * wzmocnione bossy, tj. smok, wither
         * jezeli level wiekszy od 10 i zycie wieksze niz 5 serduszek to podczas hordy zespawnuje sie od 0 do 8 zombie obok ciebie XDD
         *
         * */
        HuntMode huntMode = new HuntMode(this, this);
        huntMode.runTaskTimer(this, 0, 10);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void huntModeStatusChanged(boolean huntModeOn) {
        mobsStats.setHuntModeOn(huntModeOn);
    }
}
