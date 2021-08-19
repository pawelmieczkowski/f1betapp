package pl.salata.f1betapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pl.salata.f1betapp.model.Circuit;

public interface CircuitRepository extends JpaRepository<Circuit, Long> {

}
