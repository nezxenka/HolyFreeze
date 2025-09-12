package ru.nezxenka.holyfreeze;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import static java.lang.Math.abs;

public class Freeze {
    public Player checker;
    public Player suspect;
    public final HolyFreeze plugin;
    public Location suspectLocation;
    public BukkitTask messageTask;
    public BukkitTask titleTask;
    public BukkitTask teleportTask;
    public Location checkLocation;
    public Integer locIndex = null;
    public String checkerName;
    public String suspectName;
    public boolean teleport;
    public boolean enableBlockJoin;

    public Freeze(HolyFreeze plugin, Player checker, Player suspect, boolean teleport){
        this.plugin = plugin;
        this.checker = checker;
        this.suspect = suspect;
        checkerName = checker.getName();
        suspectName = suspect.getName();
        this.teleport = teleport;
        this.enableBlockJoin = plugin.getConfig().getBoolean("deny-join.enable", false);
        start();
    }

    public void start(){
        if(enableBlockJoin && plugin.getDatabaseManager() != null){
            plugin.getDatabaseManager().addPlayer(suspectName);
        }
        suspectLocation = suspect.getLocation();

        if(teleport) {
            World spawn = Bukkit.getWorld(plugin.getConfig().getString("location.world", "spawn"));
            checkLocation = new Location(spawn,
                    plugin.getConfig().getDouble("location.x"),
                    plugin.getConfig().getDouble("location.y"),
                    plugin.getConfig().getDouble("location.z"),
                    (float) plugin.getConfig().getDouble("location.yaw"),
                    (float) plugin.getConfig().getDouble("location.pitch"));
            suspect.teleport(checkLocation);
        }

        messageTask = new BukkitRunnable(){
            @Override
            public void run(){
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9▶ &fВы подозреваетесь в &x&E&D&2&4&2&9использовании читов, &fпоэтому были заморожены!"));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 Чтобы разморозить себя и не получить бан, &fу Вас есть &67 мин., &fчтобы отправить свой логин &x&E&D&2&4&2&9&nAnyDesk&f в чат &7(Кликабельно: &x&E&D&2&4&2&9www.anydesk.com/ru&7) &fи пройти проверку."));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9 В случае отказа/выхода/игнора &f- &x&E&D&2&4&2&9блокировка аккаунта"));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
            }
        }.runTaskTimer(plugin, 0, 30 * 20);
        titleTask = new BukkitRunnable(){
            @Override
            public void run(){
                suspect.sendTitle(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9ПРОВЕРКА НА ЧИТЫ"), ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9Следите за чатом и не выходите из игры! &fВсе инструкции &6отправлены &fвам в &eчат&f!"));
            }
        }.runTaskTimer(plugin, 0, 3 * 20);
    }

    public void updatePlayer(){
        suspect = Bukkit.getPlayer(suspectName);
        if(messageTask != null) {
            messageTask.cancel();
        }
        if(titleTask != null) {
            titleTask.cancel();
        }

        messageTask = new BukkitRunnable(){
            @Override
            public void run(){
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9▶ &fВы подозреваетесь в &x&E&D&2&4&2&9использовании читов, &fпоэтому были заморожены!"));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 Чтобы разморозить себя и не получить бан, &fу Вас есть &67 мин., &fчтобы отправить свой логин &x&E&D&2&4&2&9&nAnyDesk&f в чат &7(Кликабельно: &x&E&D&2&4&2&9www.anydesk.com/ru&7) &fи пройти проверку."));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9 В случае отказа/выхода/игнора &f- &x&E&D&2&4&2&9блокировка аккаунта"));
                suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
            }
        }.runTaskTimer(plugin, 0, 30 * 20);
        titleTask = new BukkitRunnable(){
            @Override
            public void run(){
                suspect.sendTitle(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9ПРОВЕРКА НА ЧИТЫ"), ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9Следите за чатом и не выходите из игры! &fВсе инструкции &6отправлены &fвам в &eчат&f!"));
            }
        }.runTaskTimer(plugin, 0, 3 * 20);
    }

    public void stop(){
        if(enableBlockJoin && plugin.getDatabaseManager() != null){
            plugin.getDatabaseManager().removePlayer(suspectName);
        }
        if(messageTask != null) {
            messageTask.cancel();
        }
        if(titleTask != null) {
            titleTask.cancel();
        }
        if(teleport) {
            teleportTask = new BukkitRunnable() {
                int seconds = 11;

                @Override
                public void run() {
                    seconds--;
                    if (suspect == null) {
                        this.cancel();
                        return;
                    }
                    if (seconds <= 0) {
                        suspect.teleport(suspectLocation);
                        suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a▶ &fТелепортация..."));
                        this.cancel();
                        return;
                    }
                    if (suspect.getLocation().getWorld() != checkLocation.getWorld() ||
                            abs(suspect.getLocation().getX() - checkLocation.getX()) > 2.5 ||
                            abs(suspect.getLocation().getY() - checkLocation.getY()) > 2.5 ||
                            abs(suspect.getLocation().getZ() - checkLocation.getZ()) > 2.5) {
                        suspect.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9▶ &fВы покинули область, телепортация &x&E&D&2&4&2&9отменена&f!"));
                        this.cancel();
                        return;
                    }
                    suspect.sendTitle(ChatColor.translateAlternateColorCodes('&', "Вы &aразморожены&f!"), ChatColor.translateAlternateColorCodes('&', "&fТелепортация обратно через: &b" + seconds + "&f. Покиньте область проверки для &x&E&D&2&4&2&9отмены&f."));
                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

    public void stopKick(Boolean msg){
        if(Bukkit.getPlayer(checkerName) != null) {
            checker = Bukkit.getPlayer(checkerName);
            if (msg) {
                checker.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9▶ &fПроверяемый покинул &x&E&D&2&4&2&9игру&f. Его нужно &x&E&D&2&4&2&9заблокировать&f."));
            }
        }
        if(messageTask != null) {
            messageTask.cancel();
        }
        if(titleTask != null) {
            titleTask.cancel();
        }
    }
}