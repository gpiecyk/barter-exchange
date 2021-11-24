package com.gp.barter.exchange.rest.controller;


import com.gp.barter.exchange.events.OnSendQuestionEvent;
import com.gp.barter.exchange.persistence.model.OfferData;
import com.gp.barter.exchange.persistence.model.UserData;
import com.gp.barter.exchange.persistence.model.WatchListData;
import com.gp.barter.exchange.persistence.service.OfferService;
import com.gp.barter.exchange.persistence.service.UserService;
import com.gp.barter.exchange.persistence.service.WatchListService;
import com.gp.barter.exchange.rest.dto.OfferDto;
import com.gp.barter.exchange.rest.dto.PictureDto;
import com.gp.barter.exchange.rest.exception.OfferIsAlreadyObserved;
import com.gp.barter.exchange.rest.exception.UserDoesNotExists;
import com.gp.barter.exchange.util.constants.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = "/offer")
public class OfferController {

    private final OfferService offerService;
    private final UserService userService;
    private final WatchListService watchListService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public OfferController(OfferService offerService, UserService userService, WatchListService watchListService, ApplicationEventPublisher eventPublisher) {
        this.offerService = offerService;
        this.userService = userService;
        this.watchListService = watchListService;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity createOffer(@RequestBody OfferData offer) {
        Optional<UserData> user = userService.getUserByEmail(offer.getUser().getEmail());
        if (!user.isPresent()) {
            throw new UserDoesNotExists();
        }
        if (UserStatus.INACTIVE.equals(user.get().getStatus())) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        offer.setUser(user.get());
        offer.getPictures().forEach(pictureData -> pictureData.setOffer(offer));
        offerService.create(offer);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public OfferDto getOfferById(@PathVariable("id") Long id) {
        return repackOfferToDto(offerService.getById(id));
    }

    @RequestMapping(value = "/find/paginated", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public List<OfferDto> findOffersPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {
        List<OfferData> offers = offerService.findPaginated(page, size);
        List<OfferDto> result = new LinkedList<>();
        offers.forEach(offerData -> result.add(repackOfferToDto(offerData)));
        return result;
    }

    @RequestMapping(value = "/find/search/paginated", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public List<OfferDto> findSearchOffersPaginated(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("titlePattern") String titlePattern) {
        List<OfferData> offers = offerService.findSearchPaginated(page, size, titlePattern);
        List<OfferDto> result = new LinkedList<>();
        offers.forEach(offerData -> result.add(repackOfferToDto(offerData)));
        return result;
    }

    @RequestMapping(value = "/find/search/titles", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public Set<String> findSearchOfferTitles(@RequestParam("titlePattern") String titlePattern) {
        return offerService.findSearchOfferTitles(titlePattern).stream().collect(Collectors.toSet());
    }

    @RequestMapping(value = "/find/all", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public List<OfferDto> findAll() {
        List<OfferData> offers = offerService.findAll();
        List<OfferDto> result = new LinkedList<>();
        offers.forEach(offerData -> result.add(repackOfferToDto(offerData)));
        return result;
    }

    @RequestMapping(value = "/find/email", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public List<OfferDto> findOffersByEmail(@RequestParam("email") String email) {
        Optional<UserData> user = userService.getUserByEmail(email);
        if (!user.isPresent()) {
            throw new UserDoesNotExists();
        }
        List<OfferData> offers = offerService.findByUserId(user.get().getId());
        List<OfferDto> result = new LinkedList<>();
        offers.forEach(offerData -> result.add(repackOfferToDto(offerData)));
        return result;
    }

    @RequestMapping(value = "/find/observed", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public List<OfferDto> findOffersObservedByUser(@RequestParam("userId") Long userId) {
        List<WatchListData> offersObservedByUser = watchListService.findOffersObservedByUser(userId);
        List<OfferDto> result = new LinkedList<>();
        offersObservedByUser.forEach(watchListData -> result.add(repackOfferToDto(watchListData.getOffer())));
        return result;
    }

    private OfferDto repackOfferToDto(OfferData offerData) {
        OfferDto offerDto = new OfferDto();
        offerDto.setId(offerData.getId());
        offerDto.setTitle(offerData.getTitle());
        offerDto.setDescription(offerData.getDescription());
        offerDto.setCategory(offerData.getCategory());
        offerDto.setEndDate(offerData.getEndDate());
        Set<PictureDto> urls = new HashSet<>();
        offerData.getPictures().forEach(pictureData -> {
            PictureDto picture = new PictureDto();
            picture.setUrl(pictureData.getPicture());
            urls.add(picture);
        });
        offerDto.setUrls(urls);
        offerDto.setEmail(offerData.getUser().getEmail());
        return offerDto;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public void deleteOffer(@RequestParam("id") Long id) {
        OfferData offer = offerService.getById(id);
        offerService.delete(offer);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public OfferDto updateOffer(@RequestBody OfferDto offer) {
        OfferData offerData = offerService.getById(offer.getId());
        repackOfferFromDto(offer, offerData);
        offerData = offerService.update(offerData);
        return repackOfferToDto(offerData);
    }

    private void repackOfferFromDto(OfferDto offer, OfferData offerData) {
        offerData.setTitle(offer.getTitle());
        offerData.setDescription(offer.getDescription());
        offerData.setEndDate(offer.getEndDate());
        offerData.setCategory(offer.getCategory());
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public Long getOffersCount() {
        return offerService.getOffersCount();
    }

    @RequestMapping(value = "/search/count", method = RequestMethod.GET)
    @PreAuthorize("permitAll")
    public Long getSearchOffersCount(@RequestParam("titlePattern") String titlePattern) {
        return offerService.getSearchOffersCount(titlePattern);
    }

    @RequestMapping(value = "/addToWatchList", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public void addToWatchList(@RequestParam("offerId") Long offerId, @RequestParam("userId") Long userId) {
        OfferData offer = offerService.getById(offerId);
        UserData user = userService.getById(userId);
        Objects.requireNonNull(offer);
        Objects.requireNonNull(user);
        Optional<WatchListData> observedOffer = watchListService.findObservedOffer(offerId, userId);
        if (observedOffer.isPresent()) {
            throw new OfferIsAlreadyObserved();
        }
        WatchListData watchListData = new WatchListData();
        watchListData.setOffer(offer);
        watchListData.setUser(user);
        watchListService.create(watchListData);
    }

    @RequestMapping(value = "/delete/watchList", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public void removeFromWatchList(@RequestParam("offerId") Long offerId, @RequestParam("userId") Long userId) {
        Optional<WatchListData> observedOffer = watchListService.findObservedOffer(offerId, userId);
        observedOffer.ifPresent(watchListService::delete);
    }

    @RequestMapping(value = "/isUserObservingOffer", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity isUserObservingOffer(@RequestParam("offerId") Long offerId, @RequestParam("userId") Long userId) {
        Optional<WatchListData> observedOffer = watchListService.findObservedOffer(offerId, userId);
        if (observedOffer.isPresent()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/sendQuestion", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll")
    public void sendQuestion(@RequestParam("senderEmail") String senderEmail, @RequestParam("receiverEmail") String receiverEmail,
                             @RequestParam("offerTitle") String offerTitle,
                             @RequestParam("offerId") String offerId, @RequestParam("message") String message,
                             HttpServletRequest request) {
        Optional<UserData> user = userService.getUserByEmail(receiverEmail);
        if (!user.isPresent()) {
            throw new UserDoesNotExists();
        }
        eventPublisher.publishEvent(new OnSendQuestionEvent(user.get(), senderEmail, offerTitle, message, getAppUrl(request), offerId));
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort()
                + "/app/app/#" + request.getContextPath();
    }
}
