package krepo.seshcoders.hardcraft.utils;

import java.util.concurrent.ThreadLocalRandom;

public class Randomize {

    public static boolean checkChance(int percentage){
        double randNum = Math.random() * 100;
        return !(randNum < 100 - percentage);
    }

    public static int randomNum(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
