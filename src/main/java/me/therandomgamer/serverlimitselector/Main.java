package me.therandomgamer.serverlimitselector;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Created by robin on 08/05/17.
 */
public class Main extends JavaPlugin implements Listener {

    private static Main main;
    private LinkedList<String> servers;

    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().info("ServerLimitSelector by TheRandomGamer has been enabled");
        main = this;

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", BungeeChannelConnection.getInstance());

        BungeeChannelConnection.getInstance().setNeedCheck(true);
        servers = this.getConfig().getConfigurationSection("servers").getKeys(false).stream().collect(Collectors.toCollection(() -> new LinkedList<>()));
    }

    public static Main getMain(){
        return main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        BungeeChannelConnection bcc = BungeeChannelConnection.getInstance();
        if(bcc.isNeedCheck()){
            bcc.setServerPlayers(servers);

        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        BungeeChannelConnection.getInstance().setServerPlayers(servers);
    }



}
