package pl.salata.f1betapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.repository.QualificationResultRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class QualificationResultService {

    private final QualificationResultRepository qualificationResultRepository;

    public List<QualificationResult> saveAll(List<QualificationResult> qualificationResults) {
        return qualificationResultRepository.saveAll(qualificationResults);
    }
}
