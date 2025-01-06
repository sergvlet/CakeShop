package com.chicu.cakeshop.repositories;


import com.chicu.cakeshop.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {

}
