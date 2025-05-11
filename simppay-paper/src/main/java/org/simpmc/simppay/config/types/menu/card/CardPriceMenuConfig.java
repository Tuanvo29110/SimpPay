package org.simpmc.simppay.config.types.menu.card;

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
public class CardPriceMenuConfig {
    @Comment("Title có hỗ trợ PlaceholderAPI")
    public String title = "<gold>Chọn nhà mạng thẻ cào";
    public List<String> layout = Arrays.asList(
            "#########",
            "OOOOOOOOO",
            "#########"
    );
    @Comment({"Bản đồ các mục theo ký tự", "'O' là hiển thị thẻ"})
    public Map<Character, DisplayItem> displayItems = Map.of(
            '#', DisplayItem.builder()
                    .material(Material.GRAY_STAINED_GLASS_PANE)
                    .role(RoleType.NONE)
                    .amount(1)
                    .build()
    );

    public DisplayItem priceItem = DisplayItem.builder()
            .material(Material.PAPER)
            .amount(1)
            .name("<yellow><bold>{price_name}")
            .lores(List.of(
                    "<green>Bấm để chọn mệnh giá <3"
            ))
            .build();


}
