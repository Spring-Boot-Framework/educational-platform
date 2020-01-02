package com.user.management.api;

import com.user.management.api.dto.DocumentRequest;
import com.user.management.api.dto.DocumentResponse;
import com.user.management.api.mapper.DocumentApiMapper;
import com.user.management.domain.dto.DocumentDTO;
import com.user.management.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
class DocumentController {

    private final DocumentService documentService;
    private final DocumentApiMapper mapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    DocumentResponse create(@RequestBody DocumentRequest request) {
        final DocumentDTO convertedRequest = mapper.toDTO(request);
        final DocumentDTO createdDocument = documentService.create(convertedRequest);
        return mapper.toResponse(createdDocument);
    }

    @PutMapping(value = "/{id}")
    void update(@PathVariable Integer id, @RequestBody DocumentRequest request) {
        var dto = mapper.toDTO(request);
        // todo recheck solution
        dto.setId(id);
        documentService.update(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Integer id) {
        documentService.delete(id);
    }

    @GetMapping(value = "/{id}")
    DocumentResponse findById(@PathVariable Integer id) {
        var dto = documentService.find(id);
        return mapper.toResponse(dto);
    }

}
