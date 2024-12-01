package resource.artifact.repositories;

import resource.artifact.domains.Entity;
import resource.artifact.utils.page.Page;
import resource.artifact.utils.page.Pageable;

public interface PagingRepository<Id,E extends Entity<Id>> extends Repository<Id,E>{
    Page<E> findAllOnPage(Pageable pageable);
}
