package net.lapismc.lapischat.customchannels.channel;

import net.lapismc.lapischat.LapisChat;
import net.lapismc.lapischat.customchannels.LapisChatCustomChannels;
import net.lapismc.lapischat.framework.Channel;
import net.lapismc.lapischat.framework.ChatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

public class CustomChannel extends Channel implements Listener {

    private String password;
    private List<ChatPlayer> waitingForPassword = new ArrayList<>();

    public CustomChannel(String name, String shortName, String prefix, Permission perm, String format, String password) {
        super(name, shortName, prefix, perm, format);
        this.password = password;
        Bukkit.getPluginManager().registerEvents(this, LapisChatCustomChannels.getInstance());
    }

    @Override
    public void addPlayer(ChatPlayer p) {
        if (password.equals("")) {
            super.addPlayer(p);
        } else {
            sendMessageToPlayer(p, "Messages.PasswordRequired");
            waitingForPassword.add(p);
            p.removeChannel(this);
            p.setMainChannel(null);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerEnterPassword(AsyncPlayerChatEvent e) {
        if (waitingForPassword.contains(getChatPlayer(e.getPlayer()))) {
            e.setCancelled(true);
            waitingForPassword.remove(getChatPlayer(e.getPlayer()));
            if (e.getMessage().equals(password)) {
                sendMessageToPlayer(getChatPlayer(e.getPlayer()), "Messages.CorrectPassword");
                super.addPlayer(getChatPlayer(e.getPlayer()));
                getChatPlayer(e.getPlayer()).setMainChannel(this);
                getChatPlayer(e.getPlayer()).sendMessage(LapisChat.getInstance()
                        .config.getMessage("Channel.Set").replace("%CHANNEL%", getName()));
            } else {
                sendMessageToPlayer(getChatPlayer(e.getPlayer()), "Messages.IncorrectPassword");
            }
        }
    }

    private ChatPlayer getChatPlayer(Player p) {
        return LapisChat.getInstance().getPlayer(p.getUniqueId());
    }

    private void sendMessageToPlayer(ChatPlayer sender, String key) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LapisChatCustomChannels.getInstance().getConfig().getString(key)));
    }

    @Override
    protected String format(ChatPlayer from, String msg, String format) {
        return applyDefaultFormat(from, msg, format);
    }
}
