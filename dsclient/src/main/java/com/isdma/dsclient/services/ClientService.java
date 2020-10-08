package com.isdma.dsclient.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isdma.dsclient.dto.ClientDTO;
import com.isdma.dsclient.entities.Client;
import com.isdma.dsclient.repositories.ClientRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository clientrepository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pagerequest) {
		
		Page<Client> list = clientrepository.findAll(pagerequest);
		
		return list.map(page -> new ClientDTO(page));
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		
		Optional<Client> entity = clientrepository.findById(id);
		
		return new ClientDTO(entity.get());
	}

}
