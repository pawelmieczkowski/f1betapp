package pl.salata.f1betapp.exception;

import org.springframework.util.StringUtils;


public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> clazz, String searchParam) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), searchParam));
    }

    private static String generateMessage(String entity, String searchParam) {
        return StringUtils.capitalize(entity) +
                " was not found for parameter " +
                searchParam;
    }

}
