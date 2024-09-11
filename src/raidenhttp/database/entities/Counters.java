package raidenhttp.database.entities;

// Imports
import raidenhttp.config.ConfigManager;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity(value = "counters", useDiscriminator = false)
public class Counters {
    @Id private String id;
    private int count;

    public Counters() {}

    public Counters(String id) {
        this.id = id;
        this.count = ConfigManager.httpConfig.dbInfo.startPlayerCounterPosition;
    }

    public int getNextId() {
        int id = ++count;
        return id;
    }
}
