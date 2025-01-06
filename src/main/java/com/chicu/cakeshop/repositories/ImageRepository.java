package com.chicu.cakeshop.repositories;


import com.chicu.cakeshop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image getImageByName(String name);
}
