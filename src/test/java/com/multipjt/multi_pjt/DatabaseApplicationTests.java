package com.multipjt.multi_pjt;


import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DatabaseApplicationTests {
 
    // Given: 테스트를 위한 사전 조건 설정 (자동 주입된 SqlSessionFactory)
   @Autowired
    private SqlSessionFactory factory;

    @Test
    @DisplayName("00 : SqlSessionFactory 빈 주입 성공 테스트")
    public void givenSqlSessionFactory_whenInjected_thenFactoryShouldNotBeNull() {
       
        // When: factory 객체가 정상적으로 생성되었는지 확인
        String factoryInfo = factory.toString();

        // Then: factory가 null이 아니며, 정보가 출력되는지 검증
        System.out.println("debug >>>>>>> factory");
        System.out.println(factoryInfo);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>");

        // Assert: factory가 null이 아님을 검증
        assertThat(factory).isNotNull();
    }
}
