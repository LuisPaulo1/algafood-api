package com.algaworks.algafood.api.v1.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericModelAssembler<M, E> {
	
	@Autowired
    private ModelMapper modelMapper;   

    public M toModel(E domainObject, Class<M> typeModel) {
        return modelMapper.map(domainObject, typeModel);
    }

    public List<M> toCollectionModel(Collection<E> objects, Class<M> typeModel) {
        return objects.stream()
                .map(object -> toModel(object, typeModel))
                .collect(Collectors.toList());
    }
}
