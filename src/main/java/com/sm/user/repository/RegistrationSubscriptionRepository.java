package com.sm.user.repository;

import com.sm.user.document.RegistrationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationSubscriptionRepository extends JpaRepository<RegistrationSubscription, Long> {

   RegistrationSubscription findByStoreKey(String storeKey);
}
