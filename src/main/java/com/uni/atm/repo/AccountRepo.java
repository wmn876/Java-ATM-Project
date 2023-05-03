package com.uni.atm.repo;

import com.uni.atm.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, Long> {

}
