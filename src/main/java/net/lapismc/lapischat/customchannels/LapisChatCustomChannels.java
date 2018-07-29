package net.lapismc.lapischat.customchannels;

import net.lapismc.lapischat.api.ChannelAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class LapisChatCustomChannels extends JavaPlugin {

    private static LapisChatCustomChannels instance;
    ChannelAPI channelAPI;

    public static LapisChatCustomChannels getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        channelAPI = new ChannelAPI(this);
        ChannelLoader channelLoader = new ChannelLoader(this);
        channelLoader.loadChannels();
        getLogger().info(getName() + " v" + getDescription().getVersion() + " has been enabled");
    }
}
