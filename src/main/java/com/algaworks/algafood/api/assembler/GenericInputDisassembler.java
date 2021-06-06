package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericInputDisassembler<T, E> {

	@Autowired
    private ModelMapper modelMapper;    

    public E toDomainObject(T objectInput, Class<E> typeEntity) {
        return modelMapper.map(objectInput, typeEntity);
    }

    public void copyToDomainObject(T objectInput, Object destination) {    	
        modelMapper.map(objectInput, destination);
    }
}
