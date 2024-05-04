package com.quickmpay.repos;

import com.quickmpay.entities.KycDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycRepo extends JpaRepository<KycDetails,String> {
}
