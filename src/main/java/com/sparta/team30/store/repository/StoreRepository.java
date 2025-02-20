package com.sparta.team30.store.repository;

import com.sparta.team30.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID>, StoreRepositoryCustom { }
