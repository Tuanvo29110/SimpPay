package org.simpmc.simppay.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "player_data")
public class PlayerData {
    @DatabaseField(id = true, columnName = "player_uuid", foreign = true, foreignAutoRefresh = true, canBeNull = false, unique = true)
    public SPPlayer player;
    @DatabaseField(columnName = "key", canBeNull = false)
    public String key;
    @DatabaseField(columnName = "value", canBeNull = false)
    public String value;
}
