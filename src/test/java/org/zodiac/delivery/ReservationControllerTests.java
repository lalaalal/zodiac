package org.zodiac.delivery;

import org.junit.jupiter.api.Test;
import org.zodiac.delivery.controller.ReservationController;

/**
 * ReservationControllerTests
 */
public class ReservationControllerTests {
    @Test
    public void sendMessageTests() {
        try {
            ReservationController.sendMessage("01066259880", "hi");
        } catch(Exception e) {

        }
    }
    
}