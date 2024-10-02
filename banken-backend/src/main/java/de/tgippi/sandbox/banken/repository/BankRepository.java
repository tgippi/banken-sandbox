package de.tgippi.sandbox.banken.repository;

import de.tgippi.sandbox.banken.persistence.BankEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface BankRepository extends CrudRepository<BankEntity, UUID> {

    @Query(value = "SELECT b FROM Bank b WHERE b.blz = ?1")
    Collection<BankEntity> findByBlz(String blz);
}
