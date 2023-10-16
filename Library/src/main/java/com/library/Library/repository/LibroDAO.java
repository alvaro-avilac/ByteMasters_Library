package com.library.Library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.library.Library.entity.*;

@Repository
public interface LibroDAO extends CrudRepository<Libro, Long> {
}