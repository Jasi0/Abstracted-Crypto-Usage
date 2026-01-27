package db.migration;

/**
 * Utility holder for constants used in migration.
 * Mirrors the presence of a fixed salt similar to the Kotlin original.
 */
public final class MigrationUtil {
    public static final String FIXED_SALT = "FIXED_SALT_FOR_MIGRATION";

    private MigrationUtil() {
        // no instances
    }
}