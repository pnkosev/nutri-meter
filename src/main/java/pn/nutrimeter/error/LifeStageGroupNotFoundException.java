package pn.nutrimeter.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such life stage group found!")
public class LifeStageGroupNotFoundException extends BaseRuntimeException {

    public LifeStageGroupNotFoundException(String message) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
