package pl.salata.f1betapp.datapopulating.batch;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;

@Service
@AllArgsConstructor
public class ItemWriterFactory<T> {

    private final EntityManagerFactory entityManagerFactory;

    public ItemWriter<T> getItemWriter() {
        JpaItemWriter<T> writer = new JpaItemWriter<>();
        writer.setUsePersist(false);
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
