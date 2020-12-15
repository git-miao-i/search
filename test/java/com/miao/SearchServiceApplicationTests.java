package com.miao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SearchServiceApplicationTests {

    @Test
    void testUser(){
        User user = new User();
        System.out.println(user);
    }
}
