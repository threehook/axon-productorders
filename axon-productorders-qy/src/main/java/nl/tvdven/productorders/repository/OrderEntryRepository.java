package nl.tvdven.productorders.repository;

import nl.tvdven.productorders.entries.OrderEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by Edison Xu on 2017/3/15.
 */
@RepositoryRestResource(collectionResourceRel = "orders", path = "orders")
public interface OrderEntryRepository extends MongoRepository<OrderEntry, String> {}
