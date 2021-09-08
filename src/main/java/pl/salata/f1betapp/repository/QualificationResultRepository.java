package pl.salata.f1betapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.salata.f1betapp.model.QualificationResult;

public interface QualificationResultRepository extends JpaRepository<QualificationResult, Long> {

}
