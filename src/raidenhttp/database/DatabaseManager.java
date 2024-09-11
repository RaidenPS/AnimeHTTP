package raidenhttp.database;

// Imports
import raidenhttp.Main;
import raidenhttp.database.entities.Counters;
import com.mongodb.client.*;
import dev.morphia.*;
import dev.morphia.annotations.Entity;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.query.experimental.filters.Filters;
import lombok.Getter;

public final class DatabaseManager {
    @Getter private static MongoClient mongoClient;
    @Getter private static String dbName;
    @Getter private static String dburl;
    private static Datastore httpDatastore;
    public static Datastore getHTTPDataStore() {
        return httpDatastore;
    }

    /**
     * Initializes a mongodb database.
     * @param db_url Database url (mongodb://localhost:27017) for example.
     * @param db_name Database name.
     */
    public static void initialize(String db_url, String db_name) {
        dbName = db_name;
        dburl = db_url;
        mongoClient = MongoClients.create(db_url);

        // Set mapper options.
        MapperOptions mapperOptions = MapperOptions.builder().storeEmpties(true).storeNulls(false).build();
        httpDatastore = Morphia.createDatastore(mongoClient, dbName, mapperOptions);

        // Map classes.
        var entities =
                Main.reflector.getTypesAnnotatedWith(Entity.class).stream()
                        .filter(
                                cls -> {
                                    Entity e = cls.getAnnotation(Entity.class);
                                    return e != null && !e.value().equals(Mapper.IGNORED_FIELDNAME);
                                })
                        .toArray(Class<?>[]::new);

        httpDatastore.getMapper().map(entities);
        httpDatastore.ensureIndexes();
    }


    /**
     * Fetches the next id from the database by class.
     * @param c Class
     * @return The id.
     */
    public static synchronized int getNextId(Class<?> c) {
        Counters counter = getHTTPDataStore().find(Counters.class).filter(Filters.eq("_id", c.getSimpleName())).first();
        if (counter == null) {
            counter = new Counters(c.getSimpleName());
        }
        try {
            return counter.getNextId();
        } finally {
            DatabaseHelper.saveAccountAsync(counter);
        }
    }
}
