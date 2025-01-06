package com.chicu.cakeshop.repositories;

import com.chicu.cakeshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
