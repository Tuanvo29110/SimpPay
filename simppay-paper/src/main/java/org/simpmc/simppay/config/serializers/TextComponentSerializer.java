package org.simpmc.simppay.config.serializers;

import de.exlll.configlib.Serializer;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TextComponentSerializer implements Serializer<Component, String> {
    @Override
    public String serialize(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    @Override
    public Component deserialize(String s) {
        return MiniMessage.miniMessage().deserialize(s);
    }
}
