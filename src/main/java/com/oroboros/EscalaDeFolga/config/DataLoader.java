package com.oroboros.EscalaDeFolga.config;


import com.oroboros.EscalaDeFolga.model.CargoEnum;
import com.oroboros.EscalaDeFolga.model.Colaborador;
import com.oroboros.EscalaDeFolga.model.TurnoEnum;
import com.oroboros.EscalaDeFolga.repository.ColaboradorRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {


    private final ColaboradorRepository colaboradorRepository;

    @Override
    public void run(String... args) {

        if (colaboradorRepository.count() == 0) {

            for (int i = 1; i <= 40; i++) {
                Colaborador c = new Colaborador();
                c.setNome("Colaborador " + i);
                c.setTurno(TurnoEnum.TARDE);

                // 80% técnicos, 20% enfermeiros
                if (i % 5 == 0) {
                    c.setCargo(CargoEnum.ENFERMEIRO);
                } else {
                    c.setCargo(CargoEnum.TECNICO);
                }

                colaboradorRepository.save(c);
            }

            System.out.println("✅ 40 colaboradores cadastrados automaticamente!");
        } else {
            System.out.println("ℹ️ Colaboradores já existem no banco, não será recarregado.");
        }
    }
}
