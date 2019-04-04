package com.validationurl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.validationurl.model.WhiteList;

@Repository
public interface WhiteListRepository extends JpaRepository<WhiteList, Long> {

	List<WhiteList> findByClient(String client);

}