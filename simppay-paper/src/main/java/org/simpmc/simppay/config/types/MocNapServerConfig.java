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
                            10000,
                            new BossBarConfig(true, "MocNapServer 10k all",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 10k toàn thời gian")),
                    new MilestoneConfig(
                            MilestoneType.ALL,
                            20000,
                            new BossBarConfig(true, "MocNapServer 20k all",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 20k toàn thời gian"))
            ),
            MilestoneType.DAILY, List.of(new MilestoneConfig(

                            MilestoneType.DAILY,
                            10000,
                            new BossBarConfig(true, "MocNapServer10k daily",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 10k daily")),
                    new MilestoneConfig(
                            MilestoneType.DAILY,
                            20000,
                            new BossBarConfig(true, "MocNapServer20k daily",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 20k daily"))
            ),
            MilestoneType.WEEKLY, List.of(new MilestoneConfig(
                            MilestoneType.WEEKLY,
                            10000,
                            new BossBarConfig(true, "MocNapServer10k weekly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 10k weekly")),
                    new MilestoneConfig(
                            MilestoneType.WEEKLY,
                            20000,
                            new BossBarConfig(true, "MocNapServer20k weekly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 20k weekly"))
            ),
            MilestoneType.MONTHLY, List.of(new MilestoneConfig(
                            MilestoneType.MONTHLY,
                            10000,
                            new BossBarConfig(true, "MocNapServer10k monthly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 10k monthly")),
                    new MilestoneConfig(
                            MilestoneType.MONTHLY,
                            20000,
                            new BossBarConfig(true, "MocNapServer20k monthly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 20k monthly"))
            )

    );

}
