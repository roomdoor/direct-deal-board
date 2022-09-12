package roomdoor.directdealboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomdoor.directdealboard.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {

}
