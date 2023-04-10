package com.example.restaurant.service;

import com.example.restaurant.naver.NaverClient;
import com.example.restaurant.naver.dto.SearchImageReq;
import com.example.restaurant.naver.dto.SearchLocalReq;
import com.example.restaurant.wishlist.dto.WishListDto;
import com.example.restaurant.wishlist.entity.WishListEntity;
import com.example.restaurant.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // @Autowired 해줌
public class WishListService {

    private final NaverClient naverClient;
    private final WishListRepository wishListRepository;

    // query 에 해당하는 정보를 검색해서 WishListDto 로 받기
    public WishListDto search(String query) {

        // 지역 검색
        // 1. 원하는 query에 해당하는 SearchLocalReq를 생성
        var searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);

        // 2. naverClient.searchLocal(SearchLocalReq searchLocalReq)을 이용해서
        // 1번에 해당하는 query 에 해당하는 json 형태의 searchLocalRes를 받는다.
        var searchLocalRes = naverClient.searchLocal(searchLocalReq);

        // 검색되는 데이터가 있을 때
        if (searchLocalRes.getTotal() > 0) {
            // searchLocalRes 에서 받아온 Body 중 Items의 정보를 가져온다.
            var localItem = searchLocalRes.getItems().stream().findFirst().get();
            var imageQuery = localItem.getTitle().replaceAll("<[^>]*>", "");
            var searchImageReq = new SearchImageReq();
            searchImageReq.setQuery(imageQuery);
            // 이미지 검색
            var searchImageRes = naverClient.searchImage(searchImageReq);

            if (searchImageRes.getTotal() > 0) {
                var imageItem = searchImageRes.getItems().stream().findFirst().get();
                // 결과를 리턴
                var result = new WishListDto();
                result.setTitle(localItem.getTitle());
                result.setCategory(localItem.getCategory());
                result.setAddress(localItem.getAddress());
                result.setRoadAddress(localItem.getRoadAddress());
                result.setHomePageLink(localItem.getLink());
                result.setImageLink(imageItem.getLink());
                return result;
            }
        }
        return new WishListDto();
    }

    public WishListDto add(WishListDto wishListDto) {
        // Dto -> Entity 로 변환
        var entity = dtoToEntity(wishListDto);

        var saveEntity = wishListRepository.save(entity);

        // WishListDto를 원하므로  Entity -> Dto로 변환
        return entityToDto(saveEntity);
    }

    private WishListEntity dtoToEntity(WishListDto wishListDto) {
        var entity = new WishListEntity();
        entity.setIndex(wishListDto.getIndex());
        entity.setTitle(wishListDto.getTitle());
        entity.setCategory(wishListDto.getCategory());
        entity.setAddress(wishListDto.getAddress());
        entity.setRoadAddress(wishListDto.getRoadAddress());
        entity.setHomePageLink(wishListDto.getHomePageLink());
        entity.setImageLink(wishListDto.getImageLink());
        entity.setVisit(wishListDto.isVisit());
        entity.setVisitCount(wishListDto.getVisitCount());
        entity.setLastVisitDate(wishListDto.getLastVisitDate());

        return entity;
    }

    private WishListDto entityToDto(WishListEntity wishListEntity) {
        var dto = new WishListDto();

        dto.setIndex(wishListEntity.getIndex());
        dto.setTitle(wishListEntity.getTitle());
        dto.setCategory(wishListEntity.getCategory());
        dto.setAddress(wishListEntity.getAddress());
        dto.setRoadAddress(wishListEntity.getRoadAddress());
        dto.setHomePageLink(wishListEntity.getHomePageLink());
        dto.setImageLink(wishListEntity.getImageLink());
        dto.setVisit(wishListEntity.isVisit());
        dto.setVisitCount(wishListEntity.getVisitCount());
        dto.setLastVisitDate(wishListEntity.getLastVisitDate());

        return dto;
    }

    public List<WishListDto> findAll() {
        return wishListRepository.findAll()
                .stream()
                .map(it -> entityToDto(it))
                .collect(Collectors.toList());
    }

    public void delete(int index) {
        wishListRepository.deleteById(index);
    }

    public void addVisit(int index) {
        var wishItem = wishListRepository.findById(index);

        if (wishItem.isPresent()) {
            var item = wishItem.get();
            item.setVisit(true);
            item.setVisitCount(item.getVisitCount()+1);
        }
    }
}
