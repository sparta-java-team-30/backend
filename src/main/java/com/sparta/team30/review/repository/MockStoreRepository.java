package com.sparta.team30.review.repository;


import com.sparta.team30.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MockStoreRepository extends JpaRepository<Store, UUID> {

    Optional<Store> findByStoreIdAndIsDeletedFalse(UUID store);
}
