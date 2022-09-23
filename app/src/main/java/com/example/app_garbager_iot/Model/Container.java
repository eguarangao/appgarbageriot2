package com.example.app_garbager_iot.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Container {
    private int id;

    private String nameContainer;

    private String address;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameContainer() {
        return nameContainer;
    }

    public void setNameContainer(String nameContainer) {
        this.nameContainer = nameContainer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
