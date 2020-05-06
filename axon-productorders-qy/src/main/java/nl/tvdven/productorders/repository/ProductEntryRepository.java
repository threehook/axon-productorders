package nl.tvdven.productorders.repository;

import nl.tvdven.productorders.entries.ProductEntry;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by Edison Xu on 2017/3/14.
 */
@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductEntryRepository extends MongoRepository<ProductEntry, String> {}
