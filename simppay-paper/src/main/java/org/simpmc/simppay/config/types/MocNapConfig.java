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
    public Map<Integer, MilestoneConfig> mocnap = Map.of(
            10000, new MilestoneConfig(
                    MilestoneType.ALL,
                    10000,
                    new BossBarConfig(true, "MocNap",
                            BossBar.Color.YELLOW,
                            BossBar.Overlay.PROGRESS),
                    List.of("/tell %player_name% Đã vượt mức pickleball 10k")
            ),
            20000, new MilestoneConfig(
                    MilestoneType.DAILY,
                    20000,
                    new BossBarConfig(true, "MocNap",
                            BossBar.Color.YELLOW,
                            BossBar.Overlay.PROGRESS),
                    List.of("/tell %player_name% Đã vượt mức pickleball 20k")
            ),
            30000, new MilestoneConfig(
                    MilestoneType.MONTHLY,
                    30000,
                    new BossBarConfig(true, "MocNap",
                            BossBar.Color.YELLOW,
                            BossBar.Overlay.PROGRESS),
                    List.of("/tell %player_name% Đã vượt mức pickleball 30k")
            ),
            40000, new MilestoneConfig(
                    MilestoneType.YEARLY,
                    40000,
                    new BossBarConfig(true, "MocNap",
                            BossBar.Color.YELLOW,
                            BossBar.Overlay.PROGRESS),
                    List.of("/tell %player_name% Đã vượt mức pickleball 40k")
            )
    );

}
