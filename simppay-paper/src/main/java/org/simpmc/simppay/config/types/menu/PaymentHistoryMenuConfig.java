package org.simpmc.simppay.config.types.menu;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.bukkit.Material;
import org.simpmc.simppay.config.annotations.Folder;
import org.simpmc.simppay.config.types.data.menu.DisplayItem;
import org.simpmc.simppay.config.types.data.menu.RoleType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@Folder("menus")
public class PaymentHistoryMenuConfig {
    @Comment("Title có hỗ trợ PlaceholderAPI")
    public String title = "<gold>Lịch sử nạp của <white><papi:player_name> <gray>(<white>{page} / <white>{maxPage}<gray><gold>";

    public List<String> layout = Arrays.asList(
            "#########",
            "#OOOOOOO#",
            "#OOOOOOO#",
            "#OOOOOOO#",
            "#OOOOOOO#",
            "###L#R###"
    );

    @Comment({"Map ký tự -> item hiển thị", "'O' là vị trí hiển thị giao dịch"})
    public Map<Character, DisplayItem> displayItems = Map.of(
            '#', DisplayItem.builder()
                    .material(Material.GRAY_STAINED_GLASS_PANE)
                    .role(RoleType.NONE)
                    .amount(1)
                    .build(),

            'L', DisplayItem.builder()
                    .amount(1)
                    .material(Material.ARROW)
                    .role(RoleType.PREV_PAGE)
                    .name("<green>‹ Quay lại")
                    .lores(List.of(" "))
                    .build(),

            'R', DisplayItem.builder()
                    .material(Material.ARROW)
                    .amount(1)
                    .role(RoleType.NEXT_PAGE)
                    .name("<green>Tiếp theo ›")
                    .lores(List.of(" "))
                    .build()
    );

    public DisplayItem cardItem = DisplayItem.builder()
            .material(Material.PAPER)
            .amount(1)
            .name("<green>{card_type} - {amount}")
            .lores(List.of(
                    "<yellow>Thời gian: <aqua>{time}",
                    "<yellow>Số serial: <aqua>{serial}",
                    "<yellow>Mã PIN: <aqua>{pin}",
                    "<yellow>Cổng nạp: <aqua>{api}",
                    "<yellow>ID giao dịch: <aqua>{transaction_id}"
            ))
            .build();

    public DisplayItem bankItem = DisplayItem.builder()
            .material(Material.PAPER)
            .amount(1)
            .name("<green>{amount}")
            .lores(List.of(
                    "<yellow>Thời gian: <aqua>{time}",
                    "<yellow>Cổng nạp: <aqua>{api}",
                    "<yellow>ID giao dịch: <aqua>{transaction_id}"
            ))
            .build();
}
