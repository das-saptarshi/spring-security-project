package com.saptarshi.das.demoservice.controllers;

import com.saptarshi.das.demoservice.annotations.IsAdmin;
import com.saptarshi.das.demoservice.annotations.IsUser;
import com.saptarshi.das.demoservice.requests.DataRequest;
import com.saptarshi.das.demoservice.responses.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/demo-service")
public class DataController {

    @IsUser
    @GetMapping("/data")
    public Response getData() {
        return Response.builder()
                .message("Successfully fetched all data.")
                .build();
    }

    @IsUser
    @GetMapping("/data/{id}")
    public Response getData(@PathVariable final String id) {
        return Response.builder()
                .message("Successfully fetched data related to id: " + id)
                .build();
    }

    @IsAdmin
    @PatchMapping("/data/{id}")
    public Response patchData(@PathVariable final String id, @RequestBody final DataRequest data) {
        return Response.builder()
                .message(String
                        .format("Successfully patched data related to id %s with data %s",
                                id,
                                data)
                )
                .build();
    }

    @IsAdmin
    @DeleteMapping("/data/{id}")
    public Response deleteData(@PathVariable final String id) {
        return Response.builder()
                .message("Successfully delete data related to id " + id)
                .build();
    }
}
