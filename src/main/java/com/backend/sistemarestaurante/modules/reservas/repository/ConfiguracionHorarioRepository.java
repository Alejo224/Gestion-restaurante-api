package com.backend.sistemarestaurante.modules.reservas.repository;

import com.backend.sistemarestaurante.modules.reservas.ConfiguracionHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfiguracionHorarioRepository extends JpaRepository<ConfiguracionHorario, Long> {
    List<ConfiguracionHorario> findByActivoTrueOrderByHoraAsc();
    boolean existsByHora(String hora);
}
