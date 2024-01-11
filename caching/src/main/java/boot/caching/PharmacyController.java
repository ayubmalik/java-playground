package boot.caching;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pharmacies")
public class PharmacyController {

    private final PharmacyRepository pharmacies;

    public PharmacyController(PharmacyRepository pharmacyRepository) {
        this.pharmacies = pharmacyRepository;
    }

    @GetMapping
    List<Pharmacy> getAllPharmacies() {
        return pharmacies.findAll();
    }

    @GetMapping("/{code}")
    Pharmacy getByCode(@PathVariable String code) {
        return pharmacies.findByCode(code);
    }
}
