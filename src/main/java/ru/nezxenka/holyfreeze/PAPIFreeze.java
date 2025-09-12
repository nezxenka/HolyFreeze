package ru.nezxenka.holyfreeze;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPIFreeze extends PlaceholderExpansion {

    private final HolyFreeze plugin;

    public PAPIFreeze(HolyFreeze plugin){
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "holyfreeze";
    }

    @Override
    public @NotNull String getAuthor() {
        return "nezxenka";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("status")) {
            if(plugin.statusFreezeHashMap.get(player.getUniqueId()) == null){
                return "";
            }
            else{
                switch (plugin.statusFreezeHashMap.get(player.getUniqueId())){
                    case NONE:
                        return "";
                    case CHECKING:
                        return ChatColor.translateAlternateColorCodes('&', "&x&E&5&3&1&F&B На проверке&f");
                    case AFK:
                        return ChatColor.translateAlternateColorCodes('&', "&x&E&5&3&1&F&B AFK&f");
                }
            }
        }

        return null;
    }
}