package net.lapismc.lapischat.customchannels;

import net.lapismc.lapischat.customchannels.channel.CustomChannel;
import net.lapismc.lapischat.framework.Channel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;

class ChannelLoader {

    private LapisChatCustomChannels plugin;

    ChannelLoader(LapisChatCustomChannels plugin) {
        this.plugin = plugin;
    }

    void loadChannels() {
        Channel global = plugin.channelAPI.getChannel("Global");
        plugin.channelAPI.removeChannel(global);
        for (String channel : plugin.getConfig().getStringList("Disable")) {
            plugin.channelAPI.removeChannel(plugin.channelAPI.getChannel(channel));
        }
        FileConfiguration config = plugin.getConfig();
        for (String key : config.getConfigurationSection("Channels").getKeys(false)) {
            String name = config.getString("Channels." + key + ".Name");
            String shortName = config.getString("Channels." + key + ".ShortName");
            String prefix = config.getString("Channels." + key + ".Prefix");
            String permission = config.getString("Channels." + key + ".Permission");
            String password = config.getString("Channels." + key + ".Password");
            String format = config.getString("Channels." + key + ".Format");
            CustomChannel channel = new CustomChannel(name, shortName, prefix,
                    new Permission(permission), format, password);
            plugin.channelAPI.addChannel(channel);
        }
    }


}
