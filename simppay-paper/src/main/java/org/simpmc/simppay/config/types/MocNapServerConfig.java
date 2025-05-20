package org.simpmc.simppay.config.types;

import de.exlll.configlib.Configuration;
import net.kyori.adventure.bossbar.BossBar;
import org.simpmc.simppay.config.types.data.BossBarConfig;
import org.simpmc.simppay.config.types.data.MilestoneConfig;
import org.simpmc.simppay.data.milestone.MilestoneType;

import java.util.List;
import java.util.Map;

@Configuration
public class MocNapServerConfig {
    public Map<MilestoneType, List<MilestoneConfig>> mocnap = Map.of(
            MilestoneType.ALL, List.of(new MilestoneConfig(
                            MilestoneType.ALL,
                            100000,
                            new BossBarConfig(true, "MocNapServer 10k all",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức server pickleball 100k toàn thời gian")),
                    new MilestoneConfig(
                            MilestoneType.ALL,
                            200000,
                            new BossBarConfig(true, "MocNapServer 20k all",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức server pickleball 200k toàn thời gian"))
            ),
            MilestoneType.DAILY, List.of(new MilestoneConfig(

                            MilestoneType.DAILY,
                            100000,
                            new BossBarConfig(true, "MocNapServer 100k daily",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 100k daily")),
                    new MilestoneConfig(
                            MilestoneType.DAILY,
                            200000,
                            new BossBarConfig(true, "MocNapServer 200k daily",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức server pickleball 200k daily"))
            ),
            MilestoneType.WEEKLY, List.of(new MilestoneConfig(
                            MilestoneType.WEEKLY,
                            100000,
                            new BossBarConfig(true, "MocNapServer 100k weekly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức server pickleball 100k weekly")),
                    new MilestoneConfig(
                            MilestoneType.WEEKLY,
                            200000,
                            new BossBarConfig(true, "MocNapServer200k weekly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức server pickleball 200k weekly"))
            ),
            MilestoneType.MONTHLY, List.of(new MilestoneConfig(
                            MilestoneType.MONTHLY,
                            100000,
                            new BossBarConfig(true, "MocNapServer100k monthly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức server pickleball 100k monthly")),
                    new MilestoneConfig(
                            MilestoneType.MONTHLY,
                            200000,
                            new BossBarConfig(true, "MocNapServer200k monthly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức server pickleball 200k monthly"))
            )

    );

}
