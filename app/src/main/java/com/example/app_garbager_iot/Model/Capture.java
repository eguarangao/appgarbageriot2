package com.example.app_garbager_iot.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Capture {
    String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
