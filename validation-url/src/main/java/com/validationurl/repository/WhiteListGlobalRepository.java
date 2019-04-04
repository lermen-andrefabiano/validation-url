package com.validationurl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.validationurl.model.WhiteListGlobal;

@Repository
public interface WhiteListGlobalRepository extends JpaRepository<WhiteListGlobal, Long> {

}