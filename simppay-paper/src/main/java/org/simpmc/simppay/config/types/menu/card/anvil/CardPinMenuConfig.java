package org.simpmc.simppay.config.types.menu.card.anvil;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.bukkit.Material;
import org.simpmc.simppay.config.annotations.Folder;
import org.simpmc.simppay.config.types.data.menu.DisplayItem;

import java.util.List;

@Configuration
@Folder("menus")
public class CardPinMenuConfig {
    @Comment("Title có hỗ trợ PlaceholderAPI")
    public String title = "<gold>Nhập mã pin của thẻ cào";

    public DisplayItem item = DisplayItem.builder()
            .material(Material.PAPER)
            .amount(1)
            .name("Nhập pin của thẻ...")
            .lores(List.of(
                    "<green>Nhập mã pin của thẻ cào"
            ))
            .build();
}
