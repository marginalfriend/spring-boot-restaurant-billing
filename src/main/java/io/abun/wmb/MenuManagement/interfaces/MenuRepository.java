package io.abun.wmb.MenuManagement.interfaces;

import io.abun.wmb.MenuManagement.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer>, JpaSpecificationExecutor<MenuEntity> {
}