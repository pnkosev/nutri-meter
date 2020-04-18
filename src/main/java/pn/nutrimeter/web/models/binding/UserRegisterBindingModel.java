package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import pn.nutrimeter.annotations.DateBeforeToday;
import pn.nutrimeter.annotations.UserEmail;
import pn.nutrimeter.annotations.UserPassword;
import pn.nutrimeter.data.models.enums.ActivityLevel;
import pn.nutrimeter.data.models.enums.AgeCategory;
import pn.nutrimeter.data.models.enums.Sex;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel {

    @NotEmpty(message = "This field is mandatory!")
    @Length(min = 2, max = 15, message = "Username should be between 2 and 15 symbols!")
    private String username;

    @UserEmail
    private String email;

    @UserPassword
    private String password;

    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @NotNull(message = "This field is mandatory!")
    @Range(min = 3, max = 200, message = "Weight must be between 3 and 200 kilos!")
    private Double weight;

    @NotNull(message = "This field is mandatory!")
    @Range(min = 50, max = 250, message = "Height must be between 50 and 250 cm!")
    private Double height;

    @DateBeforeToday
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;
}
