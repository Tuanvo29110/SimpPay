package org.simpmc.simppay.config.types.menu.card.anvil;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.bukkit.Material;
import org.simpmc.simppay.config.annotations.Folder;
import org.simpmc.simppay.config.types.data.menu.DisplayItem;

import java.util.List;

@Configuration
@Folder("menus")
public class CardSerialMenuConfig {
    @Comment("Title có hỗ trợ PlaceholderAPI")
    public String title = "<gold>Nhập mã seri của thẻ cào";

    public DisplayItem item = DisplayItem.builder()
            .material(Material.PAPER)
            .amount(1)
            .name("Nhập seri của thẻ...")
            .lores(List.of(
                    "<green>Nhập mã serial của thẻ cào"
            ))
            .build();
}
