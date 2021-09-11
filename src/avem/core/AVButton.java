package avem.core;

import javafx.scene.control.Button;

public class AVButton extends Button {

    public AVButton(String label, AVReservation reservation) {
        super.setText(label);
        this.reservation = reservation;
    }

    public AVReservation getReservation() {
        return reservation;
    }

    public void setReservation(AVReservation reservation) {
        this.reservation = reservation;
    }

    private AVReservation reservation;

}
