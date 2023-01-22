package de.tom.discordverifybot.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class VerifyQueueRepository {

    private HashMap<UUID, String> verify = new HashMap<>();

    private HashMap<UUID, String> verifySave = new HashMap<>();

    public void addVerifySave(Player player, String discordUser) {
        verifySave.put(player.getUniqueId(), discordUser);
    }

    public String getVerifySave(Player player) {
        return verifySave.get(player.getUniqueId());
    }

    public void addVerify(Player player, String code) {
        verify.put(player.getUniqueId(), code);
    }

    public void removeVerify(Player player) {
        verify.remove(player.getUniqueId());
    }

    public String getVerify(Player player) {
        return verify.get(player.getUniqueId());
    }

    public String getKeyByValue(String value) {
        for (UUID key : verify.keySet()) {
            if (verify.get(key).equals(value)) {
                return key.toString();
            }
        }
        return null;
    }

    public boolean isInVerifyQueue(Player player) {
        return verify.containsKey(player.getUniqueId());
    }

    public ArrayList<UUID> getVerifyList() {
        return new ArrayList<>(verify.keySet());
    }

}
