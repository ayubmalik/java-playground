package boot.caching;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyRepository extends ListCrudRepository<Pharmacy, Long> {
    Pharmacy findByCode(String code);
}
