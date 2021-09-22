package com.company;

public class Train {
    String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

class CargoTrain extends Train {
    private int tonnage;

    public int getTonnage() {
        return tonnage;
    }
    public void setTonnage(int tonnage) {
        this.tonnage = tonnage;
    }
    @Override
    public String toString() {
        return "Wagon: Name = " + this.name + "; Tonnage = " + this.tonnage;
    }

}

class PassengerTrain extends Train {
    private int passengers;

    public int getPassengers() {
        return passengers;
    }
    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
    @Override
    public String toString() {
        return "Wagon: Name = " + this.name + "; Passengers = " + this.passengers;
    }
}
