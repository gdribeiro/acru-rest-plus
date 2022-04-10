package com.acrud.acrudplus.web;


import com.acrud.acrudplus.domain.TourRating;
import com.acrud.acrudplus.service.TourRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
@Tag(name = "Tour Rating", description = "The Rating for a Tour API")
public class TourRatingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TourRatingController.class);
    private TourRatingService tourRatingService;

    @Autowired
    public TourRatingController(TourRatingService tourRatingService) {
        this.tourRatingService = tourRatingService;
    }

    protected TourRatingController() {

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a Tour Rating")
    public void createTourRating(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated RatingDto ratingDto) {
        LOGGER.info("POST /tours/{}/ratings", tourId);
        tourRatingService.createNew(tourId, ratingDto.getCustomerId(), ratingDto.getScore(), ratingDto.getComment());
    }

    @PostMapping("/{score}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Give Many Tours Same Score")
    public void createManyTourRatings(@PathVariable(value = "tourId") int tourId,
                                      @PathVariable(value = "score") int score,
                                      @RequestParam("customers") Integer customers[]) {
        LOGGER.info("POST /tours/{}/ratings/{}", tourId, score);
        tourRatingService.rateMany(tourId, score, customers);
    }

    @GetMapping
    @Operation(summary = "Lookup All Ratings for a Tour")
    public Page<RatingDto> getAllRatingsForTour(@PathVariable(value = "tourId") int tourId, Pageable pageable,
                                                          PagedResourcesAssembler pagedAssembler) {
        LOGGER.info("GET /tours/{}/ratings", tourId);
        Page<TourRating> tourRatingPage = tourRatingService.lookupRatings(tourId, pageable);
        List<RatingDto> ratingDtoList = tourRatingPage.getContent()
                .stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<RatingDto>(ratingDtoList, pageable, tourRatingPage.getTotalPages());
    }

    @GetMapping("/average")
    @Operation(summary = "Get the Average Score for a Tour")
    public AbstractMap.SimpleEntry<String, Double> getAverage(@PathVariable(value = "tourId") int tourId) {
        LOGGER.info("GET /tours/{}/ratings/average", tourId);
        return new AbstractMap.SimpleEntry<String, Double>("average", tourRatingService.getAverageScore(tourId));
    }

    @PutMapping
    @Operation(summary = "Modify All Tour Rating Attributes")
    public RatingDto updateWithPut(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated RatingDto ratingDto) {
        LOGGER.info("PUT /tours/{}/ratings", tourId);
        return toDto(tourRatingService.update(tourId, ratingDto.getCustomerId(),
                 ratingDto.getScore(), ratingDto.getComment()));
    }

    @PatchMapping
    @Operation(summary = "Modify Some Tour Rating Attributes")
    public RatingDto updateWithPatch(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated RatingDto ratingDto) {
        LOGGER.info("PATCH /tours/{}/ratings", tourId);
        return toDto(tourRatingService.updateSome(tourId, ratingDto.getCustomerId(),
                 ratingDto.getScore(), ratingDto.getComment()));
    }

    @DeleteMapping("/{customerId}")
    public void delete(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        LOGGER.info("DELETE /tours/{}/ratings/{}", tourId, customerId);
        tourRatingService.delete(tourId, customerId);
    }

    private RatingDto toDto(TourRating tourRating) {
        return new RatingDto(tourRating.getScore(), tourRating.getComment(), tourRating.getCustomerId());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return404(NoSuchElementException ex) {
        LOGGER.error("Unable to complete transaction", ex);
        return ex.getMessage();
    }
}