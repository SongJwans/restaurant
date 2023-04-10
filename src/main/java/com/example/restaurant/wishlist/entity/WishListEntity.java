package com.example.restaurant.wishlist.entity;

import com.example.restaurant.db.MemoryDbEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishListEntity extends MemoryDbEntity {

    private String title;   // 가게 이름
    private String category;    // 카테고리
    private String address;   // 주소
    private String roadAddress; // 도로명주소
    private String homePageLink; // 홈페이지 주소
    private String imageLink;
    private boolean isVisit;
    private int visitCount;
    private LocalDateTime lastVisitDate;

}
