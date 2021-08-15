package badgamesinc.hypnotic.utils;

import java.util.ArrayList;
import java.util.List;

import badgamesinc.hypnotic.event.EventManager;
import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventJoinGame;
import badgamesinc.hypnotic.event.events.EventReceivePacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class TPSUtils {

	public static TPSUtils INSTANCE = new TPSUtils();
	MinecraftClient mc = MinecraftClient.getInstance();
    private List<Long> reports = new ArrayList<>();
    private long timeLastTimeUpdate = -1;
    private long timeGameJoined;
    
    public TPSUtils() {
    	EventManager.INSTANCE.register(this);
    }

    public double getTPS(int averageOfSeconds) {
        if (reports.size() < 2) {
            return 20.0; // we can't compare yet
        }

        long currentTimeMS = reports.get(reports.size() - 1);
        long previousTimeMS = reports.get(reports.size() - averageOfSeconds);

        // on average, how long did it take for 20 ticks to execute? (ideal value: 1 second)
        double longTickTime = Math.max((currentTimeMS - previousTimeMS) / (1000.0 * (averageOfSeconds - 1)), 1.0);
        return 20 / longTickTime;
    }

    public double getAverageTPS() {
       return getTPS(reports.size());
    }

    @EventTarget
    private void run(EventReceivePacket event) {
        if (mc.world == null || mc.player.age < 20) {
            reports.clear();
        }
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            reports.add(System.currentTimeMillis());
        }
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
        	timeLastTimeUpdate = System.currentTimeMillis();
        }
    }
    
    @EventTarget
    public void joinGame(EventJoinGame event) {
    	timeLastTimeUpdate = -1;
        timeGameJoined = System.currentTimeMillis();
    }
    
    public float getTimeSinceLastTick() {
        if (System.currentTimeMillis() - timeGameJoined < 4000) return 0;
        return (System.currentTimeMillis() - timeLastTimeUpdate) / 1000f;
    }
}
