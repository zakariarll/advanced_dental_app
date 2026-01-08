package ma.dentalTech.service.common;

import java.util.List;

public interface CrudService<T, ID> {
    List<T> getAll();
    T getById(ID id);
    void create(T entity);
    void update(T entity);
    void deleteById(ID id);
}