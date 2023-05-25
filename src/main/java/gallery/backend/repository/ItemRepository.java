package gallery.backend.repository;

import gallery.backend.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository  extends JpaRepository<Item, Integer> {

}
