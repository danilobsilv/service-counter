package com.api.serviceCounter.shared.seed;

import com.api.serviceCounter.deskQueue.DeskQueue;
import com.api.serviceCounter.deskQueue.DeskQueueRepository;
import com.api.serviceCounter.deskQueue.QueueCode;
import com.api.serviceCounter.serviceDesk.ServiceDesk;
import com.api.serviceCounter.serviceDesk.ServiceDeskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Profile("dev")
public class DevSeedRunner implements CommandLineRunner {

    private final ServiceDeskRepository deskRepo;
    private final DeskQueueRepository queueRepo;

    @Value("${seed.dev.enabled:true}")
    private boolean enabled;

    @Value("${seed.dev.desk-name:Balcao Principal}")
    private String deskName;

    @Value("${seed.dev.desk-timezone:America/Sao_Paulo}")
    private String deskTimezone;

    public DevSeedRunner(ServiceDeskRepository deskRepo, DeskQueueRepository queueRepo) {
        this.deskRepo = deskRepo;
        this.queueRepo = queueRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled) return;

        ServiceDesk desk = deskRepo.findByName(deskName)
                .orElseGet(() -> {
                    ServiceDesk d = new ServiceDesk();
                    d.setName(deskName);
                    d.setTimezone(deskTimezone);
                    d.setActive(true);
                    return deskRepo.save(d);
                });

        List<QueueCode> codes = List.of(QueueCode.NORMAL, QueueCode.PRIORITY, QueueCode.RETURN);

        for (QueueCode code : codes) {
            queueRepo.findByServiceDeskIdAndCode(desk.getId(), code)
                    .orElseGet(() -> {
                        DeskQueue queue = new DeskQueue();
                        queue.setServiceDesk(desk);
                        queue.setCode(code);
                        queue.setName(code.name());
                        queue.setActive(true);
                        return queueRepo.save(queue);
                    });
        }
    }
}