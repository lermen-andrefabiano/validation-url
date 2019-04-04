package com.validationurl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.validationurl.model.ClientHistoryResponse;

@Repository
public interface ClientHistoryResponseRepository extends JpaRepository<ClientHistoryResponse, Long> {

}