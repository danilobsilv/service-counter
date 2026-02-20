package com.api.serviceCounter.serviceDesk;

import com.api.serviceCounter.serviceDesk.dtos.CreateDeskRequest;
import com.api.serviceCounter.serviceDesk.dtos.DeskResponse;
import com.api.serviceCounter.serviceDesk.dtos.UpdateDeskRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/desks")
public class ServiceDeskController {

    private final ServiceDeskService serviceDeskService;

    public ServiceDeskController(ServiceDeskService serviceDeskService) {
        this.serviceDeskService = serviceDeskService;
    }

    @PostMapping("/")
    public ResponseEntity<DeskResponse> createDesk(
            @RequestBody CreateDeskRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        DeskResponse response = serviceDeskService.createDesk(request);

        URI location = uriBuilder.path("/desks/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<Page<DeskResponse>> listDesks(Pageable pageable) {
        return ResponseEntity.ok(serviceDeskService.listDesks(pageable));
    }

    @GetMapping("/desk-id/{deskId}")
    public ResponseEntity<DeskResponse> getDesk(@PathVariable UUID deskId) {
        return ResponseEntity.ok(serviceDeskService.getDesk(deskId));
    }

    @PatchMapping("/desk-id/{deskId}")
    public ResponseEntity<DeskResponse> updateDesk(@PathVariable UUID deskId,@RequestBody UpdateDeskRequest request) {
        return ResponseEntity.ok(serviceDeskService.updateDesk(deskId, request));
    }

    @DeleteMapping("/desk-id/{deskId}")
    public ResponseEntity<Void> deleteDesk(@PathVariable UUID deskId) {
        serviceDeskService.softDeleteDesk(deskId);
        return ResponseEntity.noContent().build();
    }
}