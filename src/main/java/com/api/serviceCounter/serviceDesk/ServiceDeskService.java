package com.api.serviceCounter.serviceDesk;

import com.api.serviceCounter.serviceDesk.dtos.CreateDeskRequest;
import com.api.serviceCounter.serviceDesk.dtos.DeskResponse;
import com.api.serviceCounter.serviceDesk.dtos.UpdateDeskRequest;
import com.api.serviceCounter.shared.domain.BaseValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceDeskService {

    private final ServiceDeskRepository serviceDeskRepository;
    private final List<BaseValidator<CreateDeskRequest>> createValidators;

    public ServiceDeskService(
            ServiceDeskRepository serviceDeskRepository,
            List<BaseValidator<CreateDeskRequest>> createValidators
    ) {
        this.serviceDeskRepository = serviceDeskRepository;
        this.createValidators = createValidators;
    }

    public DeskResponse createDesk(CreateDeskRequest data) {
        createValidators.forEach(v -> v.validate(data));

        var desk = new ServiceDesk();
        desk.setName(data.name().trim());
        desk.setTimezone(
                (data.timezone() == null || data.timezone().isBlank())
                        ? "America/Manaus"
                        : data.timezone().trim()
        );
        desk.setActive(true);

        var saved = serviceDeskRepository.save(desk);
        return toResponse(saved);
    }

    public Page<DeskResponse> listDesks(Pageable pageable) {
        return serviceDeskRepository.findAllByIsActiveTrue(pageable)
                .map(this::toResponse);
    }

    public DeskResponse getDesk(UUID id) {
        var desk = serviceDeskRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("SERVICE_DESK_NOT_FOUND"));

        return toResponse(desk);
    }

    public DeskResponse updateDesk(UUID id, UpdateDeskRequest data) {
        var desk = serviceDeskRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("SERVICE_DESK_NOT_FOUND"));

        if (data.name() != null && !data.name().isBlank()) {
            var n = data.name().trim();
            if (n.length() < 3 || n.length() > 120) {
                throw new IllegalArgumentException("DESK_NAME_INVALID_LENGTH");
            }
            desk.setName(n);
        }

        if (data.timezone() != null && !data.timezone().isBlank()) {
            // valida timezone (mesma regra do create)
            try {
                java.time.ZoneId.of(data.timezone().trim());
            } catch (Exception e) {
                throw new IllegalArgumentException("DESK_TIMEZONE_INVALID");
            }
            desk.setTimezone(data.timezone().trim());
        }

        var saved = serviceDeskRepository.save(desk);
        return toResponse(saved);
    }

    public void softDeleteDesk(UUID id) {
        var desk = serviceDeskRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("SERVICE_DESK_NOT_FOUND"));

        desk.setActive(false);
        serviceDeskRepository.save(desk);
    }

    private DeskResponse toResponse(ServiceDesk d) {
        return new DeskResponse(
                d.getId(),
                d.getName(),
                d.getTimezone(),
                d.isActive(),
                d.getCreatedAt(),
                d.getUpdatedAt()
        );
    }
}