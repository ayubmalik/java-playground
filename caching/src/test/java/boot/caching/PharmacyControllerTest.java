package boot.caching;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PharmacyController.class)
class PharmacyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PharmacyRepository repository;

    @Test
    void addNewPharmacy() throws Exception {
        var json = """
                {
                  "code": "ABC",
                  "name": "name",
                  "addressLine1": "line1",
                  "addressLine2": "line2",
                  "town": "town",
                  "postcode": "postcode"
                }""";

        mvc.perform(post("/pharmacies")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());

        var expected = new Pharmacy("ABC", "name", "line1", "line2", "town", "postcode");

        verify(repository).save(expected);
    }
}