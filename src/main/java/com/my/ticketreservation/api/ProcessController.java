package com.my.ticketreservation.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessController { /** 예매처리 서버 **/

    //폴링 성공시 예매 티케팅 화면
    @GetMapping("/ticketting")
    public String ticketMain(){

        return "티켓팅 입장 성공";
    }


}
