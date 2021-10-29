package de.tom.discordverifybot.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.tom.discordverifybot.DiscordVerifyBot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class HikariCP {

    public HikariConfig config;
    public static HikariDataSource ds;

    public HikariCP(DiscordVerifyBot plugin) {


        config = new HikariConfig();
        try {
            config.setJdbcUrl("jdbc:mysql://localhost:3306/DiscordVerfy");
            config.setUsername("root");
            config.setPassword("oaBSh8px");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            ds = new HikariDataSource(config);
            System.out.println("MySQL Connected");
            System.out.println(ds.getConnection());
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§4The connection was disconnected, please check if the login data is correct in MySQL.yml");
            e.printStackTrace();
        }
    }

    public HikariCP close() {
        if (!ds.isClosed()) {
            ds.close();
        } else {
            System.out.println("The connection was already closed");
        }
        return null;
    }

    public void setVerifiedUser(Player player, UUID mcUUID, String mcName, String discordID, String discordName) {
        try {
                ds.getConnection().createStatement().executeUpdate("INSERT INTO VerifiedUsers ( UUID_Minecraft, Name_Minecraft, Discord_ID, Discord_Name) VALUES ('"+mcUUID+"', '"+mcName+"', '"+discordID+"', '"+discordName+"')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerInList(String dcId) {
        try {
            ResultSet rs = getResult("SELECT * FROM VerifiedUsers WHERE Discord_ID = '"+dcId+"'");
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet getResult(String qry) {
            try {
                return ds.getConnection().createStatement().executeQuery(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }

}
