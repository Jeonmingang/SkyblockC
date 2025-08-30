package com.signition.samskybridge.cmd;

import com.signition.samskybridge.Main;
import com.signition.samskybridge.data.IslandData;
import com.signition.samskybridge.level.LevelService;
import com.signition.samskybridge.rank.RankingService;
import com.signition.samskybridge.upgrade.UpgradeService;
import com.signition.samskybridge.util.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IslandCommand implements CommandExecutor {
    private final Main plugin;
    private final com.signition.samskybridge.data.DataStore store;
    private final LevelService level;
    private final UpgradeService upgrade;
    private final RankingService ranking;

    public IslandCommand(Main plugin, com.signition.samskybridge.data.DataStore store, LevelService level, UpgradeService upgrade, RankingService ranking) {
        this.plugin = plugin;
        this.store = store;
        this.level = level;
        this.upgrade = upgrade;
        this.ranking = ranking;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(Text.color(plugin.getConfig().getString("messages.not-player","플레이어만 사용가능")));
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0){
            for (String line : plugin.getConfig().getStringList("messages.cmd-help")){
                p.sendMessage(Text.color(line));
            }
            return true;
        }
        switch (args[0]){
            case "레벨": {
                IslandData is = level.getIslandOf(p);
                long need = level.requiredXp(is.getLevel());
                double percent = Math.min(100.0, (is.getXp() * 100.0) / need);
                String msg = plugin.getConfig().getString("messages.level.status","섬 레벨")
                        .replace("<level>", String.valueOf(is.getLevel()))
                        .replace("<xp>", String.valueOf(is.getXp()))
                        .replace("<need>", String.valueOf(need))
                        .replace("<percent>", String.format("%.1f", percent));
                p.sendMessage(Text.color(msg));
                return true;
            }
            case "업그레이드": {
                upgrade.openGui(p);
                return true;
            }
            case "랭킹": {
                ranking.sendTop(p, 10);
                return true;
            }
            default:
                for (String line : plugin.getConfig().getStringList("messages.cmd-help")){
                    p.sendMessage(Text.color(line));
                }
                return true;
        }
    }
}