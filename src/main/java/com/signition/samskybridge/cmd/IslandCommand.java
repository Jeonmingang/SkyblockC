
package com.signition.samskybridge.cmd;

import com.signition.samskybridge.Main;
import com.signition.samskybridge.data.IslandData;
import com.signition.samskybridge.level.LevelService;
import com.signition.samskybridge.rank.RankingService;
import com.signition.samskybridge.upgrade.UpgradeService;
import com.signition.samskybridge.util.ConfigUtil;
import com.signition.samskybridge.util.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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

    private void sendHelp(Player p){
        p.sendMessage(Text.color("&7/섬 레벨 &f: 섬 레벨 확인"));
        p.sendMessage(Text.color("&7/섬 업그레이드 &f: 업그레이드 GUI 열기"));
        p.sendMessage(Text.color("&7/섬 랭킹 &f: 섬 랭킹 보기"));
        if (p.hasPermission("samsky.admin")){
            p.sendMessage(Text.color("&8---- 관리자 ----"));
            p.sendMessage(Text.color("&7/섬 설정 리로드 &f: 설정/블럭경험치 리로드"));
            p.sendMessage(Text.color("&7/섬 설정 보기 &f: 주요 설정값 확인"));
            p.sendMessage(Text.color("&7/섬 설정 블럭경험치 설정 <블럭> <xp> &f: blocks.yml 수정"));
            p.sendMessage(Text.color("&7/섬 설정 바리어 시간 <초> &f: 방벽 표시 시간 변경"));
            p.sendMessage(Text.color("&7/섬 설정 레벨 증가율 <퍼센트> &f: 필요 경험치 증가율(%)"));
            p.sendMessage(Text.color("&7/섬 설정 비용 size base|multiplier <값> &f: 섬 크기 비용/증가율"));
            p.sendMessage(Text.color("&7/섬 설정 비용 team base|multiplier <값> &f: 팀원 비용/증가율"));
            p.sendMessage(Text.color("&7/섬 설정 랭킹접두어 <형식> &f: [ 섬 랭킹 <rank>위 ] 포맷"));
            p.sendMessage(Text.color("&7/섬 설정 저장 &f: config.yml 저장"));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(Text.color(plugin.getConfig().getString("messages.not-player","플레이어만 사용가능")));
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0){
            sendHelp(p);
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
            case "설정": {
                if (!p.hasPermission("samsky.admin")){
                    p.sendMessage(Text.color("&c권한이 없습니다."));
                    return true;
                }
                if (args.length == 1){
                    sendHelp(p);
                    return true;
                }
                // /섬 설정 리로드
                if ("리로드".equals(args[1])){
                    plugin.reloadConfig();
                    level.reloadBlocks();
                    p.sendMessage(Text.color("&a설정이 리로드되었습니다."));
                    return true;
                }
                // /섬 설정 보기
                if ("보기".equals(args[1])){
                    p.sendMessage(Text.color("&7increase-percent: &f"+plugin.getConfig().getDouble("leveling.increase-percent",1.5)));
                    p.sendMessage(Text.color("&7barrier.show-seconds: &f"+plugin.getConfig().getInt("barrier.show-seconds",6)));
                    p.sendMessage(Text.color("&7economy.size: base &f"+plugin.getConfig().getDouble("economy.costs.size.base",10000.0)+" &7multiplier &f"+plugin.getConfig().getDouble("economy.costs.size.multiplier",1.25)));
                    p.sendMessage(Text.color("&7economy.team: base &f"+plugin.getConfig().getDouble("economy.costs.team.base",5000.0)+" &7multiplier &f"+plugin.getConfig().getDouble("economy.costs.team.multiplier",1.5)));
                    p.sendMessage(Text.color("&7scoreboard.prefix-format: &f"+plugin.getConfig().getString("scoreboard.prefix-format","&7[ &a섬 랭킹 &f<rank>위 &7] ")));
                    return true;
                }
                // /섬 설정 바리어 시간 <초>
                if ("바리어".equals(args[1]) && args.length>=4 && "시간".equals(args[2])){
                    try{
                        int v = Integer.parseInt(args[3]);
                        plugin.getConfig().set("barrier.show-seconds", v);
                        p.sendMessage(Text.color("&abarrier.show-seconds = "+v));
                    }catch (Exception ex){ p.sendMessage(Text.color("&c숫자를 입력하세요.")); }
                    return true;
                }
                // /섬 설정 레벨 증가율 <퍼센트>
                if ("레벨".equals(args[1]) && args.length>=4 && "증가율".equals(args[2])){
                    try{
                        double v = Double.parseDouble(args[3]);
                        plugin.getConfig().set("leveling.increase-percent", v);
                        p.sendMessage(Text.color("&aincrease-percent = "+v));
                    }catch (Exception ex){ p.sendMessage(Text.color("&c숫자를 입력하세요.")); }
                    return true;
                }
                // /섬 설정 비용 size|team base|multiplier <값>
                if ("비용".equals(args[1]) && args.length>=5){
                    String cat = args[2]; String key = args[3];
                    try{
                        double v = Double.parseDouble(args[4]);
                        if ("size".equals(cat) && ("base".equals(key) || "multiplier".equals(key))){
                            plugin.getConfig().set("economy.costs.size."+key, v);
                            p.sendMessage(Text.color("&aeconomy.costs.size."+key+" = "+v));
                        } else if ("team".equals(cat) && ("base".equals(key) || "multiplier".equals(key))){
                            plugin.getConfig().set("economy.costs.team."+key, v);
                            p.sendMessage(Text.color("&aeconomy.costs.team."+key+" = "+v));
                        } else {
                            p.sendMessage(Text.color("&c사용법: /섬 설정 비용 size|team base|multiplier <값>"));
                        }
                    }catch (Exception ex){ p.sendMessage(Text.color("&c숫자를 입력하세요.")); }
                    return true;
                }
                // /섬 설정 랭킹접두어 <형식>
                if ("랭킹접두어".equals(args[1]) && args.length>=3){
                    StringBuilder sb = new StringBuilder();
                    for (int i=2;i<args.length;i++){
                        if (i>2) sb.append(" ");
                        sb.append(args[i]);
                    }
                    plugin.getConfig().set("scoreboard.prefix-format", sb.toString());
                    p.sendMessage(Text.color("&aprefix-format = "+sb));
                    return true;
                }
                // /섬 설정 블럭경험치 설정 <블럭> <xp>
                if ("블럭경험치".equals(args[1]) && args.length>=4 && "설정".equals(args[2])){
                    FileConfiguration blocks = ConfigUtil.loadBlocks(plugin);
                    try{
                        String mat = args[3].toUpperCase();
                        int xp = Integer.parseInt(args[4]);
                        blocks.set(mat, xp);
                        if (ConfigUtil.saveBlocks(plugin, blocks)){
                            level.reloadBlocks();
                            p.sendMessage(Text.color("&ablocks.yml: "+mat+" = "+xp));
                        } else {
                            p.sendMessage(Text.color("&cblocks.yml 저장 실패"));
                        }
                    }catch (Exception ex){
                        p.sendMessage(Text.color("&c사용법: /섬 설정 블럭경험치 설정 <블럭> <xp>"));
                    }
                    return true;
                }
                // /섬 설정 저장
                if ("저장".equals(args[1])){
                    plugin.saveConfig();
                    p.sendMessage(Text.color("&aconfig.yml 저장 완료"));
                    return true;
                }

                sendHelp(p);
                return true;
            }
            default:
                sendHelp(p);
                return true;
        }
    }
}
