package com.oroboros.EscalaDeFolga.config;

import com.oroboros.EscalaDeFolga.app.dto.colaborador.AuditoriaInfoDTO;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.CargoEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.AcaoAuditoriaEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.AuditoriaColaboradorRepository;
import com.oroboros.EscalaDeFolga.infrastructure.repository.ColaboradorRepository;
import com.oroboros.EscalaDeFolga.domain.service.AuditoriaColaboradorService;
import com.oroboros.EscalaDeFolga.util.JsonUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;

//@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {


    private final ColaboradorRepository colaboradorRepository;

    private final AuditoriaColaboradorRepository auditoriaRepository;

    private final AuditoriaColaboradorService auditoriaService;

    @Override
    public void run(String... args) {

        if (colaboradorRepository.count() == 0) {

            // 🔹 Definindo auditor genérico do sistema
            AuditoriaInfoDTO auditor = new AuditoriaInfoDTO(
                    0L,
                    "Sistema",
                    "127.0.0.1",
                    "DataLoader"
            );


            for (int i = 1; i <= 40; i++) {
                Colaborador c = new Colaborador();
                c.setNome("Colaborador " + i);
                c.setTurno(TurnoEnum.TARDE);
                c.setAtivo(true);

                // 80% técnicos, 20% enfermeiros
                if (i % 5 == 0) {
                    c.setCargo(CargoEnum.ENFERMEIRO);
                } else {
                    c.setCargo(CargoEnum.TECNICO);
                }

                colaboradorRepository.save(c);



                auditoriaService.criarAuditoria(
                        AcaoAuditoriaEnum.CRIACAO,
                        c,
                        auditor,
                        null,
                        JsonUtil.toJson(c)
                );


            }

            System.out.println("✅ 40 colaboradores cadastrados automaticamente!");
        } else {
            System.out.println("ℹ️ Colaboradores já existem no banco, não será recarregado.");
        }
    }
}
