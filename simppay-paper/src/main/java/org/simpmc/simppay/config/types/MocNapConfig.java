package org.simpmc.simppay.config.types;

import de.exlll.configlib.Configuration;
import net.kyori.adventure.bossbar.BossBar;
import org.simpmc.simppay.config.types.data.BossBarConfig;
import org.simpmc.simppay.config.types.data.MilestoneConfig;
import org.simpmc.simppay.data.milestone.MilestoneType;

import java.util.List;
import java.util.Map;

@Configuration
public class MocNapConfig {
    public Map<MilestoneType, List<MilestoneConfig>> mocnap = Map.of(
            MilestoneType.ALL, List.of(new MilestoneConfig(
                            MilestoneType.ALL,
                            100000,
                            new BossBarConfig(true, "MocNap 10k alltime",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 10k toàn thời gian")),
                    new MilestoneConfig(
                            MilestoneType.ALL,
                            200000,
                            new BossBarConfig(true, "MocNap 20k alltime",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 20k toàn thời gian"))
            ),
            MilestoneType.DAILY, List.of(new MilestoneConfig(

                            MilestoneType.DAILY,
                            100000,
                            new BossBarConfig(true, "MocNap 10k daily",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 10k daily")),
                    new MilestoneConfig(
                            MilestoneType.DAILY,
                            200000,
                            new BossBarConfig(true, "MocNap 20k daily",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 20k daily"))
            ),
            MilestoneType.WEEKLY, List.of(new MilestoneConfig(
                            MilestoneType.WEEKLY,
                            100000,
                            new BossBarConfig(true, "MocNap 10k weekly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 10k weekly")),
                    new MilestoneConfig(
                            MilestoneType.WEEKLY,
                            200000,
                            new BossBarConfig(true, "MocNap 20k weekly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 20k weekly"))
            ),
            MilestoneType.MONTHLY, List.of(new MilestoneConfig(
                            MilestoneType.MONTHLY,
                            100000,
                            new BossBarConfig(true, "MocNap 10k monthly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 10k monthly")),
                    new MilestoneConfig(
                            MilestoneType.MONTHLY,
                            200000,
                            new BossBarConfig(true, "MocNap 20k monthly",
                                    BossBar.Color.YELLOW,
                                    BossBar.Overlay.PROGRESS),
                            List.of("tell %player_name% Đã vượt mức pickleball 20k monthly"))
            )

    );

}
