package com.dz.io.multiplication.repository;

import com.dz.io.multiplication.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

    Optional<Multiplication> findByFactorAAndAndFactorB(int factorA, int factorB);
}
