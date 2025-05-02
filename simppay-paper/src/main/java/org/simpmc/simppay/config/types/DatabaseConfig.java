package org.simpmc.simppay.config.types;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Data;
import org.simpmc.simppay.api.DatabaseSettings;

@Data
@Configuration
public class DatabaseConfig implements DatabaseSettings {
    @Comment("The type of database to use. Supported types: MYSQL, H2")
    public String type = "H2";
    @Comment({"Below is for SQL Connection Only", "The host of the MySQL database"})
    public String host = "localhost";
    @Comment("The port of the MySQL database")
    public int port = 3306;
    @Comment("The name of the MySQL database")
    public String database = "chuyenxu";
    @Comment("The username of the MySQL database")
    public String username = "root";
    @Comment("The password of the MySQL database")
    public String password = "password";
}
