package com.example.restaurant.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemoryDbEntity {
    // 모든 DB는 MemoryDbEntity 를 상속받는다.
    protected Integer index;
}
