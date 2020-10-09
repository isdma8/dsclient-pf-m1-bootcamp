package com.isdma.dsclient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isdma.dsclient.dto.ClientDTO;
import com.isdma.dsclient.entities.Client;
import com.isdma.dsclient.repositories.ClientRepository;
import com.isdma.dsclient.services.exceptions.ResourceNotFindException;

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

		Optional<Client> obj = clientrepository.findById(id);

		return new ClientDTO(obj.orElseThrow(() -> new ResourceNotFindException("Entity Not Find!")));
	}

	@Transactional
	public ClientDTO insert(ClientDTO clientdto) {

		Client entity = new Client();

		entity.setBirthDate(clientdto.getBirthDate());
		entity.setChildren(clientdto.getChildren());
		entity.setCpf(clientdto.getCpf());
		entity.setIncome(clientdto.getIncome());
		entity.setName(clientdto.getName());

		entity = clientrepository.save(entity);

		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO clientdto) {

		try {
			Client entity = clientrepository.getOne(id);

			entity = clientDTOToClientEntity(clientdto, entity);

			return new ClientDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFindException("Entity with Id: " + id + " not Find!");
		}

	}

	private Client clientDTOToClientEntity(ClientDTO clientdto, Client entity) {

		entity.setBirthDate(clientdto.getBirthDate());
		entity.setChildren(clientdto.getChildren());
		entity.setCpf(clientdto.getCpf());
		entity.setIncome(clientdto.getIncome());
		entity.setName(clientdto.getName());

		entity = clientrepository.save(entity);

		return entity;
	}

	public void delete(Long id) {
		try {
			clientrepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFindException("Id Not Found: " + id);
		}
	}
}
