package com.api.serviceCounter.deskQueue;

import com.api.serviceCounter.deskQueue.dtos.CreateDeskQueueRequest;
import com.api.serviceCounter.deskQueue.dtos.DeskQueueResponse;
import com.api.serviceCounter.deskQueue.dtos.UpdateDeskQueueRequest;
import com.api.serviceCounter.serviceDesk.ServiceDeskRepository;
import com.api.serviceCounter.shared.domain.BaseValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeskQueueService {

    private final DeskQueueRepository deskQueueRepository;
    private final ServiceDeskRepository serviceDeskRepository;
    private final List<BaseValidator<CreateDeskQueueRequest>> createValidators;

    public DeskQueueService(
            DeskQueueRepository deskQueueRepository,
            ServiceDeskRepository serviceDeskRepository,
            List<BaseValidator<CreateDeskQueueRequest>> createValidators
    ) {
        this.deskQueueRepository = deskQueueRepository;
        this.serviceDeskRepository = serviceDeskRepository;
        this.createValidators = createValidators;
    }

    public DeskQueueResponse createQueue(UUID deskId, CreateDeskQueueRequest data) {
        createValidators.forEach(v -> v.validate(data));

        var desk = serviceDeskRepository.findByIdAndIsActiveTrue(deskId)
                .orElseThrow(() -> new EntityNotFoundException("SERVICE_DESK_NOT_FOUND"));

        var queue = new DeskQueue();
        queue.setServiceDesk(desk);
        queue.setCode(data.code());
        queue.setName(data.name().trim());
        queue.setActive(true);

        try {
            var saved = deskQueueRepository.save(queue);
            return toResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }

    public List<DeskQueueResponse> listActiveQueues(UUID deskId) {
        serviceDeskRepository.findByIdAndIsActiveTrue(deskId)
                .orElseThrow(() -> new EntityNotFoundException("SERVICE_DESK_NOT_FOUND"));

        return deskQueueRepository.findAllByServiceDeskIdAndIsActiveTrue(deskId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DeskQueueResponse updateQueue(UUID queueId, UpdateDeskQueueRequest data) {
        var queue = deskQueueRepository.findById(queueId)
                .orElseThrow(() -> new EntityNotFoundException("QUEUE_NOT_FOUND"));

        if (data.name() != null && !data.name().isBlank()) {
            var n = data.name().trim();
            if (n.length() < 3 || n.length() > 120) {
                throw new IllegalArgumentException("QUEUE_NAME_INVALID_LENGTH");
            }
            queue.setName(n);
        }

        if (data.isActive() != null) {
            queue.setActive(data.isActive());
        }

        var saved = deskQueueRepository.save(queue);
        return toResponse(saved);
    }

    public DeskQueue getQueueForDesk(UUID deskId, UUID queueId) {
        serviceDeskRepository.findByIdAndIsActiveTrue(deskId)
                .orElseThrow(() -> new EntityNotFoundException("SERVICE_DESK_NOT_FOUND"));

        var queue = deskQueueRepository.findById(queueId)
                .orElseThrow(() -> new EntityNotFoundException("QUEUE_NOT_FOUND"));

        if (!queue.getServiceDesk().getId().equals(deskId)) {
            throw new IllegalArgumentException("QUEUE_DOES_NOT_BELONG_TO_DESK");
        }

        return queue;
    }

    private DeskQueueResponse toResponse(DeskQueue q) {
        return new DeskQueueResponse(
                q.getId(),
                q.getServiceDesk().getId(),
                q.getCode(),
                q.getName(),
                q.isActive(),
                q.getCreatedAt(),
                q.getUpdatedAt()
        );
    }
}