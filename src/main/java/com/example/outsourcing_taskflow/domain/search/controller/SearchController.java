package com.example.outsourcing_taskflow.domain.search.controller;

import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.domain.search.model.response.GetSearchResponse;
import com.example.outsourcing_taskflow.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
// - Properties
    private final SearchService searchService;

// - Methods
    // - Integrated Search
    @GetMapping("/api/search")
    public ResponseEntity<ApiResponse<GetSearchResponse>> search(
            @RequestParam("query") String query) {
        // - Check Exception(400)
        if (query == null || query.isBlank()) {
            throw new CustomException(ErrorMessage.MUST_SEARCH_TERM);
        }
        // - Get Result
        GetSearchResponse result = searchService.search(query);
        // - Return Result
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("검색 성공", result));
    }
}
