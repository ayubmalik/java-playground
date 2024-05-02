package boot.caching;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PharmacyRepositoryIntTest {

    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private PharmacyRepository repository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        System.out.println("postgres.getUsername() = " + postgres.getUsername());
        System.out.println("postgres.getPassword() = " + postgres.getPassword());
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void findByCode() {
        var pharmacy = repository.findByCode("FA002");
        assertThat(pharmacy).isNotNull();
        assertThat(pharmacy.name()).isEqualTo("ROWLANDS PHARMACY");
        System.out.println("pharmacy = " + pharmacy);
    }
}
