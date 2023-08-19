package com.sm.user.token;

import java.util.ArrayList;
import java.util.Arrays;

import com.sm.user.document.Customer;
import com.sm.user.document.Store;
import com.sm.user.document.extention.CustomAuthority;
import com.sm.user.repository.CustomerRepository;
import com.sm.user.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	StoreRepository storeRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Store store = storeRepository.findByStoreIdOrPhone(username, username);
		String phone="";
		Customer customer=null;
		if(ObjectUtils.isEmpty(store)){
			  customer = customerRepository.findByPhone(username);
			phone=customer.getPhone();
		}else {
			phone=store.getPhone();
		}

		if (store!=null && phone.equals(username)) {
			return new User(phone, "$2a$10$.JjsHs.vrEm1tbpkq98VJedRj9vMCHaSmL8GHGiy0E9C3yACCbTjG",
					Arrays.asList(new CustomAuthority("userType","STORE"),new CustomAuthority("storeId",store.getStoreId()),new CustomAuthority("phone",store.getPhone()),new CustomAuthority("sessionYear",store.getRegistrationSessionYear()),new CustomAuthority("rooms",String.valueOf(store.getNoOfRooms()))));
		} else if(customer!=null && phone.equals(username)){
			return new User(phone, "$2a$10$.JjsHs.vrEm1tbpkq98VJedRj9vMCHaSmL8GHGiy0E9C3yACCbTjG",
					Arrays.asList(new CustomAuthority("userType",customer.getRoleType()),new CustomAuthority("storeId",customer.getStoreId()),new CustomAuthority("phone",customer.getPhone()),new CustomAuthority("sessionYear",customer.getRegisterSession()),new CustomAuthority("email",String.valueOf(customer.getEmail()))));
		}else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}