package com.sm.user.repository;

import com.sm.user.document.Customer;
import com.sm.user.document.dto.DashboardCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

   Customer  findByPhone(String phone);
   List<Customer> findAllByStoreId(String storeId);
   List<Customer> findAllByStoreIdAndRoleType(String storeId, String roleType);
   Optional<Customer> findById(Long id);

   @Query(nativeQuery = true, value = "select * from customer c where  c.store_id=:storeId and ( c.first_name sounds like :searchParam or c.last_name  sounds like :searchParam  or c.phone like :searchParam)")
   List<Customer> findAllByStoreIdAndCustomerSearch(@Param("storeId") String storeId,@Param("searchParam")  String searchParam);


@Query(value = "select (select count(c.id) from customer c where c.register_session=:sessionYear and c.store_id= :storeId ) as customer_count, \n" +
        "                     ( select sum(cl.amount) from customer_loan cl where cl.session=:sessionYear and cl.store_id=:storeId) as loan_amount,\n" +
        "                       (select sum(po.quantity) from product_out po where po.session=:sessionYear  and po.store_id=:storeId) as total_product_out,\n" +
        "                       (select sum(pin.quantity) from product_in pin  where pin.session=:sessionYear and pin.store_id=:storeId) as total_product_in\n" +
        "                       from product_in pr  where pr.session=:sessionYear limit 1" ,nativeQuery = true)
Tuple getCountOfDashboardData(@Param("sessionYear") String sessionYear, @Param("storeId") String storeId);

@Query(value = "select sum(pi.quantity) from product_in pi where p.product_id=:prodcutId and pi.store_id=:storeId", nativeQuery = true)
Double totalAvailableQualityProductIn(@Param("prodcutId") String prodcutId, @Param("storeId") String storeId);


   @Query(value = "select sum(po.quantity) from product_out po   where po.product_id=:prodcutId and po.store_id=:storeId ", nativeQuery = true)
   Double totalAvailableQualityProductOut(String prodcutId, String storeId);

   @Query(value = "select sum(po.quantity) from product_out po  inner  join room_lot_details rld on po.lot_no = rld.generated_lot_name where rld.room_no= :roomNo and  rld.store_id=:storeId ", nativeQuery = true)
   Double totalAvailableQualityProductOutByRoom(String roomNo, String storeId);

   @Query(value = "select sum(pi.quantity) from product_in pi where pi.room_no= :roomNo and pi.store_id=:storeId ", nativeQuery = true)
   Double totalAvailableQualityProductInByRoom(String roomNo, String storeId);
}
