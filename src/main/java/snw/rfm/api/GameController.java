package snw.rfm.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface GameController {

    // --- Time Controlling ---

    // use isTimeReversed before calling this method!
    void timeReverse();

    boolean isTimeReversed();

    int getRemainingTime();

    // addCoin default true
    void removeRemainingTime(int secsToRemove) throws IllegalArgumentException;

    void removeRemainingTime(int secsToRemove, boolean addCoin) throws IllegalArgumentException;

    void pause() throws IllegalStateException;

    void resume() throws IllegalStateException;

    boolean isPaused();

    // --- Others ---

    boolean respawn(Player player);

    void forceOut(Player player) throws IllegalStateException;


    /* ***************** MONEY CONTROLLING ***************** */

    // --- Money + Time ---

    // IllegalArgumentException will be thrown if cps == 0
    void setCoinPerSecond(int cps) throws IllegalArgumentException;

    int getCoinPerSecond();


    // --- Data ---


    // return 0 if no record
    double getMoney(OfflinePlayer player);

    void setMoney(OfflinePlayer player, double amount);

    void addMoney(OfflinePlayer player, double coin);

    void clearCoin();
}
