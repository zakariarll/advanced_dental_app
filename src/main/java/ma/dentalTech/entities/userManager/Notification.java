package ma.dentalTech.entities.userManager;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    private Long idNotification;
    private String titre;
    private String message;
    private LocalDate date;
    private LocalTime time;
    private String type;
    private String priorite;

    private Long idUser;
}