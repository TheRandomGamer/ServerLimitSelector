package me.therandomgamer.serverlimitselector;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by robin on 08/05/17.
 */
public class BungeeChannelConnection implements PluginMessageListener {

    private static BungeeChannelConnection instance = null;
    private HashMap<String, Integer> serverPlayers;

    public boolean isNeedCheck() {
        return needCheck;
    }

    public void setNeedCheck(boolean needCheck) {
        this.needCheck = needCheck;
    }

    boolean needCheck;


    protected BungeeChannelConnection() {
    }

    public static BungeeChannelConnection getInstance() {
        if (instance == null) {
            instance = new BungeeChannelConnection();
        }
        return instance;
    }

    public void setServerPlayers(LinkedList<String> servers) {
        serverPlayers = new HashMap<>();

        while (!servers.isEmpty()) {

            serverPlayers.put(servers.getLast(), 0);

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerCount");
            out.writeUTF(servers.getLast());

            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player != null) {
                player.sendPluginMessage(Main.getMain(), "BungeeCord", out.toByteArray());
            }

            servers.removeLast();
        }
        setNeedCheck(false);
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int playercount = in.readInt();
            serverPlayers.remove(server);
            serverPlayers.put(server, playercount);
        }
    }

    public void connectPlayer(Player p, String server) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        if (p != null) {
            p.sendPluginMessage(Main.getMain(), "BungeeCord", out.toByteArray());
        }
    }

    public Integer getServerPlayers(String server){
        if(serverPlayers.get(server) == null){
            return 0;
        }

        return serverPlayers.get(server);
    }

}



