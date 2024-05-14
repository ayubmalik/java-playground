package boot.caching;

import org.springframework.web.bind.annotation.*;

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

    @PostMapping(consumes = "application/json")
    void addNew(@RequestBody Pharmacy pharmacy) {
        pharmacies.save(pharmacy);
    }
}
