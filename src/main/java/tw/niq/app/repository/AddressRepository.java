package tw.niq.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import tw.niq.app.entity.AddressEntity;
import tw.niq.app.entity.UserEntity;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

	List<AddressEntity> findAllByUserDetails(UserEntity userDetails);

	AddressEntity findByAddressId(String addressId);

}
