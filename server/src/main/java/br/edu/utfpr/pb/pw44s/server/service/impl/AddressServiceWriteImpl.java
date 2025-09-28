package br.edu.utfpr.pb.pw44s.server.service.impl;

import br.edu.utfpr.pb.pw44s.server.model.Address;
import br.edu.utfpr.pb.pw44s.server.repository.AddressRepository;
import br.edu.utfpr.pb.pw44s.server.service.AuthService;
import br.edu.utfpr.pb.pw44s.server.service.IAddressServiceWrite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
public class AddressServiceWriteImpl extends CrudServiceWriteImpl<Address, Long> implements IAddressServiceWrite {
    private final AddressRepository addressRepository;
    private final AuthService authService;

    public AddressServiceWriteImpl(AddressRepository addressRepository, AuthService authService) {
        this.addressRepository = addressRepository;
        this.authService = authService;
    }


    @Override
    protected JpaRepository<Address, Long> getRepository() {
        return addressRepository;
    }

    @Override
    public Address save(Address entity) {
        entity.setUser(authService.getAuthenticatedUser());
        return super.save(entity);
    }

}
