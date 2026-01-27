package db.migration;

import java.sql.ResultSet;
import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import etl.EtlEncryptionService;

 // Java-based migration demonstrating deterministic encryption of IDs
 // Reads CORE_ENCRYPTION_KEY from environment with fallback "test-encryption-key"
 // Iterates all IDs from etl_tenants and updates each row with an encrypted ID
public class V19_1__Encrypt_existing_etl_tenants extends BaseJavaMigration {

    private String getEnvironmentEncryptionKey() {
        String key = System.getenv("CORE_ENCRYPTION_KEY");
        return key != null ? key : "test-encryption-key"; 
    }

    @Override
    public void migrate(Context context) throws Exception {
        String key = getEnvironmentEncryptionKey();
        EtlEncryptionService service = new EtlEncryptionService(key);

        try (Statement selectStatement = context.getConnection().createStatement();
             ResultSet rs = selectStatement.executeQuery("select id from etl_tenants")) {

            while (rs.next()) {
                String id = rs.getString(1);
                String encryptedValue = service.encrypt(id, MigrationUtil.FIXED_SALT);

                try (Statement updateStatement = context.getConnection().createStatement()) {
                    updateStatement.execute("update etl_tenants set id = '" + encryptedValue + "' where id = '" + id + "'");
                }
            }
        }
    }
}