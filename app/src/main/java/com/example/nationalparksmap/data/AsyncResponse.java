package com.example.nationalparksmap.data;

import com.example.nationalparksmap.model.Park;

import java.util.List;

public interface AsyncResponse {
    void processPark(List<Park> parks);
}
