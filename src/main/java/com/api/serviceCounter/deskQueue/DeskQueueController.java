package com.api.serviceCounter.deskQueue;

import com.api.serviceCounter.deskQueue.dtos.CreateDeskQueueRequest;
import com.api.serviceCounter.deskQueue.dtos.DeskQueueResponse;
import com.api.serviceCounter.deskQueue.dtos.UpdateDeskQueueRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/queues")
public class DeskQueueController {

    private final DeskQueueService deskQueueService;

    public DeskQueueController(DeskQueueService deskQueueService) {
        this.deskQueueService = deskQueueService;
    }

    @PostMapping("/desks/{deskId}")
    public ResponseEntity<DeskQueueResponse> createQueue(@PathVariable UUID deskId, @RequestBody CreateDeskQueueRequest request, UriComponentsBuilder uriBuilder) {
        DeskQueueResponse response = deskQueueService.createQueue(deskId, request);

        URI location = uriBuilder.path("/queues/{queueId}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/desks/{deskId}")
    public ResponseEntity<List<DeskQueueResponse>> listQueues(@PathVariable UUID deskId) {
        return ResponseEntity.ok(deskQueueService.listActiveQueues(deskId));
    }

    @PatchMapping("/{queueId}")
    public ResponseEntity<DeskQueueResponse> updateQueue(@PathVariable UUID queueId, @RequestBody UpdateDeskQueueRequest request) {
        return ResponseEntity.ok(deskQueueService.updateQueue(queueId, request));
    }
}